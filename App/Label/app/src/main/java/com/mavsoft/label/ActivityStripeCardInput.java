package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.mavsoft.label.Conf.LabelCore;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Helpers.IntentHelper;
import com.mavsoft.label.Helpers.ToolHelper;
import com.mavsoft.label.Models.CheckoutOrderResponse;
import com.mavsoft.label.Models.StoreBasket;
import com.mavsoft.label.Models.StoreOrder.StoreOrder;
import com.mavsoft.label.Models.StoreOrderResponse.StoreOrderResponse;
import com.mavsoft.label.Models.StripeCreateOrderDict;
import com.mavsoft.label.Models.StripeCreateOrderPost;
import com.mavsoft.label.Models.UserBilling;
import com.mavsoft.label.Networking.LabelAPI;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class ActivityStripeCardInput extends AppCompatActivity {

    StoreOrder storeOrder;
    String total = "";
    StoreOrderResponse storeOrderResponse;
    List<StoreBasket> storeBaskets;
    UserBilling userBilling;

    String a;
    int keyDel;

    ArrayList<String> listOfPattern = new ArrayList<String>();

    @OnClick(R.id.ibDismiss)
    public void dismissView() {
        finish();
    }

    @OnClick(R.id.btnPay)
    public void payTapped() {

        if (etCardHolderName.getText().equals("")) {
            ToolHelper.showToast(getString(R.string.please_check_the_card_name));
            return;
        }

        Boolean hasMatchedCard = false;
        String ccNum = etCardNumber.getText().toString().replaceAll("\\s+","");
        for(String p:listOfPattern){
            if(ccNum.matches(p)){
                hasMatchedCard = true;
                break;
            }
        }

        if (!hasMatchedCard) {
            ToolHelper.showToast(getString(R.string.please_check_the_card_number));
            return;
        }

        if (etMonth.getText().toString().equals("") || etYear.getText().toString().equals("")) {
            ToolHelper.showToast(getString(R.string.please_check_the_card_month_year));
            return;
        }

        Integer ccExpiryMonth = Integer.valueOf(etMonth.getText().toString());
        Integer ccExpiryYear = Integer.valueOf(etYear.getText().toString());
        String ccCVC = etCVV.getText().toString();

        Card card = new Card(ccNum, ccExpiryMonth, ccExpiryYear, ccCVC);
        if (!card.validateCard()) {
            ToolHelper.showToast(getString(R.string.please_check_the_card));
            return;
        }

        getPayButtonDisabled();

        btnPay.setText(getText(R.string.processing));

        Stripe stripe = new Stripe(this, LabelCore.stripePublicKey);
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {

                        StripeCreateOrderPost stripeCreateOrderPost = new StripeCreateOrderPost();
                        StripeCreateOrderDict stripeCreateOrderDict = new StripeCreateOrderDict();

                        Double doubleTotal = Double.parseDouble(total) * 100;

                        Integer totalStripe = doubleTotal.intValue();
                        String basketDesc = ToolHelper.getStripeDescForBasket(storeBaskets);

                        stripeCreateOrderDict.setAmount(totalStripe);
                        stripeCreateOrderDict.setDescription(basketDesc);
                        stripeCreateOrderDict.setEmail(storeOrder.getBilling().getEmail());
                        stripeCreateOrderDict.setToken(token.getId());

                        stripeCreateOrderPost.setDict(stripeCreateOrderDict);

                        LabelAPI.Factory.getInstance().checkoutStripe(stripeCreateOrderPost).enqueue(new Callback<CheckoutOrderResponse>() {
                            @Override
                            public void onResponse(Call<CheckoutOrderResponse> call, retrofit2.Response<CheckoutOrderResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().getStatus().equals("205")) {

                                        Gson gson = new Gson();
                                        apiCreateOrder(gson.toJson(storeOrder));

                                    } else {
                                        ToolHelper.showToast(getString(R.string.oops_something_went_wrong));
                                        getPayButtonEnabled();
                                    }
                                } else {
                                    ToolHelper.showToast(getString(R.string.oops_something_went_wrong));
                                    getPayButtonEnabled();
                                }
                            }

                            @Override
                            public void onFailure(Call<CheckoutOrderResponse> call, Throwable t) {
                                ToolHelper.networkError(t);
                                getPayButtonEnabled();
                            }
                        });
                    }
                    public void onError(Exception error) {
                        ToolHelper.showToast(getString(R.string.oops_something_went_wrong));
                        getPayButtonEnabled();
                    }
                }
        );
    }

    public void getPayButtonDisabled() {
        btnPay.setEnabled(false);
        btnPay.setBackgroundColor(getColor(R.color.darkGrey));
        btnPay.setText(getText(R.string.processing));
    }

    public void getPayButtonEnabled() {
        btnPay.setEnabled(true);
        btnPay.setBackgroundColor(getColor(R.color.labelPayButtonPrimary));
        btnPay.setText(getText(R.string.pay));
    }

    @BindView(R.id.tvTotal)
    TextView tvTotal;

    @BindView(R.id.etCardHolderName)
    EditText etCardHolderName;
    @BindView(R.id.etCardNumber) EditText etCardNumber;
    @BindView(R.id.etMonth) EditText etMonth;
    @BindView(R.id.etYear) EditText etYear;
    @BindView(R.id.etCVV) EditText etCVV;
    @BindView(R.id.btnPay)
    Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_card_input);
        ButterKnife.bind(this);

        storeOrder = (StoreOrder) IntentHelper.getObjectForKey("storeOrder");
        total = (String) IntentHelper.getObjectForKey("total");
        storeBaskets = AWCore.getBasket();
        userBilling = new UserBilling();
        userBilling = (UserBilling) Paper.book().read(AWCore.PREF_USER_BILLING);

        tvTotal.setText("Total: " + AWCore.strToPrice(total));

        etCardHolderName.requestFocus();
        if(etCardHolderName.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        // CARD REGEX
        String ptVisa = "^(4[0-9]{6,}$)";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "(^5[1-5][0-9]{5,}$)";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^(3[47][0-9]{5,}$)";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "(^3(?:0[0-5]|[68][0-9])[0-9]{4,}$)";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "(^6(?:011|5[0-9]{2})[0-9]{3,}$)";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);

        // LISTENER
        etCardNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean flag = true;
                String eachBlock[] = etCardNumber.getText().toString().split(" ");
                for (int i = 0; i < eachBlock.length; i++) {
                    if (eachBlock[i].length() > 4) {
                        flag = false;
                    }
                }
                if (flag) {

                    etCardNumber.setOnKeyListener(new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {

                            if (keyCode == KeyEvent.KEYCODE_DEL)
                                keyDel = 1;
                            return false;
                        }
                    });

                    if (keyDel == 0) {

                        if (((etCardNumber.getText().length() + 1) % 5) == 0) {

                            if (etCardNumber.getText().toString().split(" ").length <= 3) {
                                etCardNumber.setText(etCardNumber.getText() + " ");
                                etCardNumber.setSelection(etCardNumber.getText().length());
                            }
                        }
                        a = etCardNumber.getText().toString();
                    } else {
                        a = etCardNumber.getText().toString();
                        keyDel = 0;
                    }

                } else {
                    etCardNumber.setText(a);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    // API
    private void apiCreateOrder(String data) {

        LabelAPI.Factory.getInstance().createOrder(data).enqueue(new Callback<StoreOrderResponse>() {
            @Override
            public void onResponse(Call<StoreOrderResponse> call, retrofit2.Response<StoreOrderResponse> response) {

                if (Paper.book().getAllKeys().contains("Orders")) {
                    List<Integer> orders = Paper.book().read("Orders");
                    orders.add(response.body().getId());
                    Paper.book().write("Orders", orders);
                } else {
                    List<Integer> orders = new ArrayList<>();
                    orders.add(response.body().getId());
                    Paper.book().write("Orders", orders);
                }

                storeOrderResponse = response.body();
                Paper.book().write("UserOrderBasket",storeBaskets);

                viewOrder();
            }

            @Override
            public void onFailure(Call<StoreOrderResponse> call, Throwable t) {
                btnPay.setEnabled(true);
                orderError();
                getPayButtonEnabled();
            }
        });
    }

    public void orderError() {
        ToolHelper.showToast(getString(R.string.error_with_payment_please_contact_support));
        btnPay.setEnabled(true);
    }

    public void viewOrder() {
        Intent i = new Intent(this, ActivityOrderConfirmed.class);
        i.putExtra("OrderId", storeOrderResponse.getId());
        i.putExtra("OrderBuyerName", storeOrder.getBilling().getFirstName() + " " + storeOrder.getBilling().getLastName());
        i.putExtra("OrderBuyerAddress", userBilling.getAddress().createAddress());

        AWCore.clearBasket();
        startActivity(i);
    }

}
