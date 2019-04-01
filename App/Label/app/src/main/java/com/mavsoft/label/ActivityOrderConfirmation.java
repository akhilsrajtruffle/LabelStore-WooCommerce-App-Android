package com.mavsoft.label;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.mavsoft.label.Conf.LabelCore;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Helpers.IntentHelper;
import com.mavsoft.label.Helpers.ToolHelper;
import com.mavsoft.label.Models.PaymentMethodType;
import com.mavsoft.label.Models.StoreBasket;
import com.mavsoft.label.Models.StoreOrder.Billing;
import com.mavsoft.label.Models.StoreOrder.LineItem;
import com.mavsoft.label.Models.StoreOrder.Shipping;
import com.mavsoft.label.Models.StoreOrder.ShippingLine;
import com.mavsoft.label.Models.StoreOrder.StoreOrder;
import com.mavsoft.label.Models.StoreOrderResponse.StoreOrderResponse;
import com.mavsoft.label.Models.StoreShippingLine;
import com.mavsoft.label.Models.UserBilling;
import com.mavsoft.label.Networking.LabelAPI;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class ActivityOrderConfirmation extends AppCompatActivity {

    // VARS
    List<StoreBasket> storeBaskets;
    UserBilling userBilling;
    StoreOrderResponse storeOrderResponse;
    StoreShippingLine activeShippingMethod;

    PaymentMethodType paymentMethodTypeOption;

    // UI
    @BindView(R.id.etFirstName)EditText etFirstName;
    @BindView(R.id.etLastName) EditText etLastName;
    @BindView(R.id.etPhoneNumber)EditText etPhoneNumber;
    @BindView(R.id.etEmailAddress)EditText etEmailAddress;
    @BindView(R.id.tvSubtotal)TextView tvSubtotal;
    @BindView(R.id.switchRememberDetails)Switch rememberDetails;
    @BindView(R.id.tvOrderTotal) TextView tvTotal;
    @BindView(R.id.tvTax) TextView tvTax;
    @BindView(R.id.tvShippingAddress) TextView tvShippingAddress;
    @BindView(R.id.tvShippingOption) TextView tvShippingOption;
    @BindView(R.id.tvChangeShippingMethod) TextView tvChangeShippingMethod;
    @BindView(R.id.tvSelectShippingText) TextView tvSelectShippingText;
    @BindView(R.id.btnSelectShipping) Button btnSelectShipping;
    @BindView(R.id.iv_payment_method) ImageView ivPaymentMethod;
    @BindView(R.id.btnContinueToPayment) Button btnContinueToPayment;
    @BindView(R.id.tvPaymentMethodTitle) TextView tvPaymentMethodTitle;
    @BindView(R.id.tvChoosePaymentMethod) TextView tvChoosePaymentMethod;

    @OnClick(R.id.btnSelectShipping) public void selectShipping() {

        final List<String> arrayCategoryNames = new ArrayList<>();

        String[] myArray = new String[arrayCategoryNames.size()];

        for (int i = 0; i < LabelCore.shippingLines.length; i++) {
                arrayCategoryNames.add(LabelCore.shippingLines[i].getMethod_title() + ": " + AWCore.strToPrice(LabelCore.shippingLines[i].getTotal()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_shipping))
                .setItems(arrayCategoryNames.toArray(myArray), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tvSelectShippingText.setText(LabelCore.shippingLines[which].getMethod_title());
                        tvChangeShippingMethod.setText(getString(R.string.change));
                        tvShippingOption.setText(getString(R.string.shipping__, AWCore.strToPrice(LabelCore.shippingLines[which].getTotal())));
                        activeShippingMethod = LabelCore.shippingLines[which];
                        tvTotal.setText(getString(R.string.total_with, workoutStrTotal()));
                    }
                });
        builder.show();
    }

    @OnClick(R.id.btnSelectPaymentMethod) public void selectPaymentMethod() {

        final List<PaymentMethodType> arrayPaymentMethods = new ArrayList<>();

        if (LabelCore.usePayPal) {
            PaymentMethodType paymentMethodTypePayPal = new PaymentMethodType();
            paymentMethodTypePayPal.setIcon("paypal");
            paymentMethodTypePayPal.setId("1");
            paymentMethodTypePayPal.setName("PayPal");

            arrayPaymentMethods.add(paymentMethodTypePayPal);
        }
        if (LabelCore.useCashOnDelivery) {
            PaymentMethodType paymentMethodTypeCOD = new PaymentMethodType();
            paymentMethodTypeCOD.setIcon("cash_on_delivery_cod");
            paymentMethodTypeCOD.setId("2");
            paymentMethodTypeCOD.setName("Cash on delivery");

            arrayPaymentMethods.add(paymentMethodTypeCOD);
        }
        if (LabelCore.useStripe) {
            PaymentMethodType paymentMethodTypeStripe = new PaymentMethodType();
            paymentMethodTypeStripe.setIcon("stripe_option");
            paymentMethodTypeStripe.setId("3");
            paymentMethodTypeStripe.setName("Credit/Debit Card");

            arrayPaymentMethods.add(paymentMethodTypeStripe);
        }

        List<String> arrayPaymentMethodsStr = new ArrayList<>();
        for (int i = 0; i < arrayPaymentMethods.size(); i++) {
            arrayPaymentMethodsStr.add(arrayPaymentMethods.get(i).getName());
        }

        String[] myArray = new String[arrayPaymentMethodsStr.size()];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_payment_method))
                .setItems(arrayPaymentMethodsStr.toArray(myArray), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        paymentMethodTypeOption = arrayPaymentMethods.get(which);
                        ivPaymentMethod.setImageDrawable(ToolHelper.getDrawableForName(paymentMethodTypeOption.getIcon()));
                        tvPaymentMethodTitle.setText(paymentMethodTypeOption.getName());
                        tvChoosePaymentMethod.setText("Change");
                    }
                });
        builder.show();
    }

    public String workoutStrTotal() {
        double vat = 0;
        vat = Double.parseDouble(AWCore.workoutVAT(AWCore.woSubtotal(storeBaskets)));
        return AWCore.strToPrice(String.valueOf(Double.valueOf(AWCore.woTotal(storeBaskets, activeShippingMethod)) + vat));
    }

    // PAYPAL CONF
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(LabelCore.paypalEnvironment)
            .clientId(LabelCore.paypalClientID);

    private static final int REQUEST_CODE_PAYMENT = 1;

    @OnClick(R.id.btnShippingAddress)
    public void shippingAddressTapped(Button button) {

        setDetails();

        Intent i = new Intent(this, ActivityAddShippingAddress.class);
        startActivity(i);
    }

    // SETS USER BILLING OBJECT DETAILS FROM VIEW
    public void setDetails() {
        userBilling.setEmailAddress(etEmailAddress.getText().toString());
        userBilling.setFirstName(etFirstName.getText().toString());
        userBilling.setLastName(etLastName.getText().toString());
        userBilling.setPhone(etPhoneNumber.getText().toString());

        Paper.book().write(AWCore.PREF_USER_BILLING, userBilling);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        ButterKnife.bind(this);

        userBilling = new UserBilling();

        storeBaskets = AWCore.getBasket();

        updateUI();

        tvShippingOption.setText(getText(R.string.shipping_not_set_selected));

        tvSubtotal.setText(getString(R.string.subtotal__,AWCore.strToPrice(AWCore.woSubtotal(storeBaskets))));
        tvTotal.setText(getString(R.string.total_with, workoutStrTotal()));
        tvTax.setText(getString(R.string.vat__, AWCore.strToPrice(AWCore.workoutVAT(AWCore.woSubtotal(storeBaskets)))));
    }

    public void updateUI() {
        if (Paper.book().getAllKeys().contains(AWCore.PREF_USER_BILLING)) {
            userBilling = new UserBilling();
            userBilling = (UserBilling) Paper.book().read(AWCore.PREF_USER_BILLING);

            etFirstName.setText(userBilling.getFirstName());
            etLastName.setText(userBilling.getLastName());
            etEmailAddress.setText(userBilling.getEmailAddress());
            etPhoneNumber.setText(userBilling.getPhone());
            tvShippingAddress.setText(userBilling.getAddress().createAddress());
        } else {
            UserBilling tmpData = new UserBilling();
            Paper.book().write(AWCore.PREF_USER_BILLING,tmpData);
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    // VALIDATE DETAILS
    public Boolean validateDetails() {

        String regexName = "^[a-zA-Z ,.'-]+$";

        if (!RegexUtils.isMatch(regexName,etFirstName.getText().toString())) {
            alertMessage(getString(R.string.please_check_the_first_name_field));
            return false;
        }

        if (!RegexUtils.isMatch(regexName,etLastName.getText().toString())) {
            alertMessage(getString(R.string.please_check_the_last_name_field));
            return false;
        }

        if (!RegexUtils.isEmail(etEmailAddress.getText().toString())) {
            alertMessage(getString(R.string.please_check_the_email_field));
            return false;
        }

        if (userBilling.getAddress().street.length() < 1) {
            alertMessage(getString(R.string.please_check_the_shipping_address));
            return false;
        }

        return true;
    }

    public void alertMessage(String regexError) {
        CharSequence text = getString(R.string.oops__to_continue, regexError);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    @OnClick(R.id.btnContinueToPayment)
    public void onBuyPressed(View pressed) {

        if (!validateDetails()) {
            return;
        }

        if (paymentMethodTypeOption == null) {
            ToolHelper.showToast(getString(R.string.please_select_a_payment_method_to_continue));
            return;
        }

        if (activeShippingMethod == null) {
            ToolHelper.showToast(getString(R.string.please_select_a_shipping_method_to_continue));
            return;
        }

        setDetails();

        if (paymentMethodTypeOption.getId().equals("1")) {
            btnContinueToPayment.setEnabled(false);

            PayPalPayment thingToBuy = getPaypalIntent(PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent = new Intent(ActivityOrderConfirmation.this, PaymentActivity.class);

            // send the same configuration for restart resiliency
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        } else if (paymentMethodTypeOption.getId().equals("2")) {
            cashOnDelivery();
        } else if (paymentMethodTypeOption.getId().equals("3")) {
            Intent i = new Intent(this, ActivityStripeCardInput.class);
            IntentHelper.addObjectForKey(createStoreOrderObject(), "storeOrder");

            BigDecimal subtotal = new BigDecimal(AWCore.woSubtotal(storeBaskets));
            BigDecimal shipping = new BigDecimal(activeShippingMethod.getTotal());
            BigDecimal tax = new BigDecimal(AWCore.workoutVAT(AWCore.woSubtotal(storeBaskets)));

            BigDecimal amount = subtotal.add(shipping).add(tax);

            IntentHelper.addObjectForKey(amount.toString(), "total");
            startActivity(i);
        }
    }

    private PayPalPayment getPaypalIntent(String paymentIntent) {

        BigDecimal subtotal = new BigDecimal(AWCore.woTotal(storeBaskets, null));
        BigDecimal shipping = new BigDecimal(activeShippingMethod.getTotal());
        BigDecimal tax = new BigDecimal(AWCore.workoutVAT(AWCore.woSubtotal(storeBaskets)));

        BigDecimal amount = subtotal.add(shipping).add(tax);
        PayPalPayment payment = new PayPalPayment(amount, LabelCore.currencySymbol, "Total", paymentIntent);

        payment.custom(getString(R.string.payment_to, LabelCore.storeName));
        payment.payeeEmail(userBilling.getEmailAddress());

        return payment;
    }

    public void createOrderDict() {

        StoreOrder storeOrder = new StoreOrder();

        storeOrder.setSetPaid(true);
        storeOrder.setCurrency(LabelCore.currency);

            List<ShippingLine> shippingLines = new ArrayList<>();
            ShippingLine shippingLine = new ShippingLine();
            shippingLine.setMethodId(activeShippingMethod.getMethod_id());
            shippingLine.setMethodTitle(activeShippingMethod.getMethod_title());
            shippingLine.setTotal(activeShippingMethod.getTotal());
            shippingLines.add(shippingLine);

            storeOrder.setShippingLines(shippingLines);

        storeOrder.setPaymentMethodTitle("Android - " + paymentMethodTypeOption.getName());
        storeOrder.setPaymentMethod(paymentMethodTypeOption.getName());

        if (Paper.book().getAllKeys().contains(AWCore.PREF_USER_BILLING)) {
            userBilling = Paper.book().read(AWCore.PREF_USER_BILLING);
        }

        // BILLING
        Billing billing = new Billing();
        billing.setFirstName(userBilling.getFirstName());
        billing.setLastName(userBilling.getLastName());
        billing.setAddress1(userBilling.getAddress().getStreet());
        billing.setCity(userBilling.getAddress().getCity());
        billing.setEmail(userBilling.getEmailAddress());
        billing.setState(userBilling.getAddress().getCounty());
        billing.setPostcode(userBilling.getAddress().getPostcode());
        billing.setCountry(userBilling.getAddress().getCountry());
        billing.setPhone(userBilling.getPhone());

        storeOrder.setBilling(billing);

        List<LineItem> lineItem = new ArrayList<>();

        for (int i = 0; i < storeBaskets.size(); i++) {

            LineItem tmpLineItem = new LineItem();

            if (storeBaskets.get(i).getVariationID() != -1) {
                tmpLineItem.setProductId(storeBaskets.get(i).getStoreItem().getId());
                tmpLineItem.setQuantity(storeBaskets.get(i).getQty());
                tmpLineItem.setVariationId(storeBaskets.get(i).getVariationID());

                lineItem.add(tmpLineItem);
            } else {
                tmpLineItem.setProductId(storeBaskets.get(i).getStoreItem().getId());
                tmpLineItem.setQuantity(storeBaskets.get(i).getQty());

                lineItem.add(tmpLineItem);
            }

            storeOrder.setLineItems(lineItem);
        }

        Shipping shipping = new Shipping();
        shipping.setFirstName(userBilling.getFirstName());
        shipping.setLastName(userBilling.getLastName());
        shipping.setAddress1(userBilling.getAddress().getStreet());
        shipping.setAddress2("");
        shipping.setState(userBilling.getAddress().getCounty());
        shipping.setPostcode(userBilling.getAddress().getPostcode());
        shipping.setCity(userBilling.getAddress().getCity());
        shipping.setCountry(userBilling.getAddress().getCountry());

        storeOrder.setShipping(shipping);

        Gson gson = new Gson();
        apiCreateOrder(gson.toJson(storeOrder));
    }

    public StoreOrder createStoreOrderObject() {
        StoreOrder storeOrder = new StoreOrder();

        storeOrder.setSetPaid(true);
        storeOrder.setCurrency(LabelCore.currency);

        List<ShippingLine> shippingLines = new ArrayList<>();
        ShippingLine shippingLine = new ShippingLine();
        shippingLine.setMethodId(activeShippingMethod.getMethod_id());
        shippingLine.setMethodTitle(activeShippingMethod.getMethod_title());
        shippingLine.setTotal(activeShippingMethod.getTotal());
        shippingLines.add(shippingLine);

        storeOrder.setShippingLines(shippingLines);

        storeOrder.setPaymentMethodTitle("Android - " + paymentMethodTypeOption.getName());
        storeOrder.setPaymentMethod(paymentMethodTypeOption.getName());

        if (Paper.book().getAllKeys().contains(AWCore.PREF_USER_BILLING)) {
            userBilling = Paper.book().read(AWCore.PREF_USER_BILLING);
        }

        // BILLING
        Billing billing = new Billing();
        billing.setFirstName(userBilling.getFirstName());
        billing.setLastName(userBilling.getLastName());
        billing.setAddress1(userBilling.getAddress().getStreet());
        billing.setCity(userBilling.getAddress().getCity());
        billing.setEmail(userBilling.getEmailAddress());
        billing.setState(userBilling.getAddress().getCounty());
        billing.setPostcode(userBilling.getAddress().getPostcode());
        billing.setCountry(userBilling.getAddress().getCountry());
        billing.setPhone(userBilling.getPhone());

        storeOrder.setBilling(billing);

        List<LineItem> lineItem = new ArrayList<>();

        for (int i = 0; i < storeBaskets.size(); i++) {

            LineItem tmpLineItem = new LineItem();

            if (storeBaskets.get(i).getVariationID() != -1) {
                tmpLineItem.setProductId(storeBaskets.get(i).getStoreItem().getId());
                tmpLineItem.setQuantity(storeBaskets.get(i).getQty());
                tmpLineItem.setVariationId(storeBaskets.get(i).getVariationID());

                lineItem.add(tmpLineItem);
            } else {
                tmpLineItem.setProductId(storeBaskets.get(i).getStoreItem().getId());
                tmpLineItem.setQuantity(storeBaskets.get(i).getQty());

                lineItem.add(tmpLineItem);
            }

            storeOrder.setLineItems(lineItem);
        }

        Shipping shipping = new Shipping();
        shipping.setFirstName(userBilling.getFirstName());
        shipping.setLastName(userBilling.getLastName());
        shipping.setAddress1(userBilling.getAddress().getStreet());
        shipping.setAddress2("");
        shipping.setState(userBilling.getAddress().getCounty());
        shipping.setPostcode(userBilling.getAddress().getPostcode());
        shipping.setCity(userBilling.getAddress().getCity());
        shipping.setCountry(userBilling.getAddress().getCountry());

        storeOrder.setShipping(shipping);

        if (AWCore.getUserId() != null && !AWCore.getUserId().equals("")) {
            storeOrder.setCustomer_id(AWCore.getUserId());
        }

        return storeOrder;
    }

    public void cashOnDelivery() {
        setPayButtonDisabled();
        createOrderDict();
    }

    public void setPayButtonDisabled() {
        btnContinueToPayment.setEnabled(false);
        btnContinueToPayment.setBackgroundColor(getColor(R.color.darkGrey));
        btnContinueToPayment.setText(getText(R.string.processing));
    }

    public void setPayButtonEnabled() {
        btnContinueToPayment.setEnabled(false);
        btnContinueToPayment.setBackground(getDrawable(R.drawable.button_corner_radius));
        btnContinueToPayment.setText(getText(R.string.processing));
    }

    // API
    private void apiCreateOrder(String data) {

        LabelAPI.Factory.getInstance().createOrder(data).enqueue(new Callback<StoreOrderResponse>() {
            @Override
            public void onResponse(Call<StoreOrderResponse> call, Response<StoreOrderResponse> response) {

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
                orderError();
            }
        });
    }

    public void viewOrder() {
        Intent i = new Intent(this, ActivityOrderConfirmed.class);
        i.putExtra("OrderId", storeOrderResponse.getId());
        i.putExtra("OrderBuyerName", etFirstName.getText().toString() + " " + etLastName.getText().toString());
        i.putExtra("OrderBuyerAddress", userBilling.getAddress().createAddress());

        AWCore.clearBasket();

        if (!rememberDetails.isChecked()) {
            if (Paper.book().getAllKeys().contains(AWCore.PREF_USER_BILLING)) {
                Paper.book().delete(AWCore.PREF_USER_BILLING);
            }
        }

        startActivity(i);
    }

    public void orderError() {
        ToolHelper.showToast(getString(R.string.error_with_payment_please_contact_support));
        setPayButtonEnabled();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    ToolHelper.LabelPrint(confirm.toJSONObject().toString(4));

                    createOrderDict();

                } catch (JSONException e) {
                    orderError();
                    ToolHelper.LabelPrint(getString(R.string.payment_failed) + e.getLocalizedMessage());
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            ToolHelper.LabelPrint("Payment cancelled");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            ToolHelper.LabelPrint("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            ToastUtils.showShort(getString(R.string.payment_not_accepted_please_try_again));
        }
    }
}
