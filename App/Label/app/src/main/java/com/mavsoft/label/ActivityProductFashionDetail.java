package com.mavsoft.label;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Helpers.IntentHelper;
import com.mavsoft.label.Helpers.ToolHelper;
import com.mavsoft.label.Models.StoreBasket;
import com.mavsoft.label.Models.StoreItem;
import com.mavsoft.label.Models.StoreItemWithVariation.StoreItemWithVariation;
import com.mavsoft.label.Models.StoreItemWithVariation.Variation;
import com.mavsoft.label.Networking.LabelAPI;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class ActivityProductFashionDetail extends AppCompatActivity {

    // VARS
    CarouselView carouselView;
    StoreItemWithVariation storeItem;
    String optionOne = "";
    String optionTwo = "";

    // USED TO CHECK WEATHER A ALERT IS OPEN FOR THE ATTRIBUTES
    // 1 = ATTRONE
    // 2 = ATTRTWO
    String openAlert = "";

    // UI
    @BindView(R.id.tvProductName) TextView tvProductName;
    @BindView(R.id.tvProductDescription) TextView tvProductDescription;
    @BindView(R.id.tvProductPrice) TextView tvProductPrice;
    @BindView(R.id.tvCartTotal) TextView tvCartTotal;
    @BindView(R.id.btnVariationOne) Button btnVariationOne;
    @BindView(R.id.btnVariationTwo) Button btnVariationTwo;
    @BindView(R.id.cl_loader_view) ConstraintLayout cl_loader_view;

    @OnClick(R.id.btnVariationOne)
    public void VariationOne(Button button) {

        List<String> attrOne = new ArrayList<>();

        for (int i = 0; i < storeItem.getAttributes().get(0).getOptions().size(); i++)
        {
            attrOne.add(StringUtils.upperFirstLetter(storeItem.getAttributes().get(0).getOptions().get(i)));
        }

        openAlert = "1";

        openAlert(StringUtils.upperFirstLetter(storeItem.getAttributes().get(0).getName()), attrOne);
    }

    @OnClick(R.id.btnVariationTwo)
    public void VariationTwo(Button button) {

        List<String> attrOne = new ArrayList<>();

        for (int i = 0; i < storeItem.getAttributes().get(1).getOptions().size(); i++)
        {
            attrOne.add(StringUtils.upperFirstLetter(storeItem.getAttributes().get(1).getOptions().get(i)));
        }

        openAlert = "2";
        openAlert(StringUtils.upperFirstLetter(storeItem.getAttributes().get(1).getName()), attrOne);
    }

    @OnClick(R.id.btnViewCart)
    public void viewCart(Button button) {
        Intent intent = new Intent(this, ActivityCart.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnViewMoreDescription)
    public void viewMoreDescription(Button button) {
    }

    @OnClick(R.id.btnAddToBasket)
    public void addToBasket(Button button) {

        if (!btnVariationOne.isEnabled() && !btnVariationTwo.isEnabled()) {

            // NO ATTRIBUTES FOUND IN WOOCOMMERCE
            ToolHelper.showToast(getString(R.string.oops_no_attributes_found_for_this_product));
            return;
        } else if (btnVariationOne.isEnabled() && !btnVariationTwo.isEnabled()) {

            // FIND VARIATION IN WOOCOMMERCE
            if (optionOne.equals("")) {
                ToolHelper.showToast( getString(R.string.please_add_a__, storeItem.getAttributes().get(0).getName()));
                return;
            }

            int variationId = getVariationId();

            if (variationId != 0) {

                for (Variation storeVariation : this.storeItem.getVariations()) {
                    if (storeVariation.getId() == variationId) {

                        if (!storeVariation.getInStock()) {
                            ToolHelper.showToast(getString(R.string.sorry_this_item_is_out_of_stock));
                            return;
                        }

                        // BASKET ITEM
                        StoreBasket item = new StoreBasket();
                        item.setStoreItem(storeItem);
                        item.getStoreItem().setPrice(storeVariation.getPrice());
                        item.setQty(1);
                        item.setVariationID(variationId);

                        ToolHelper.showToast(getString(R.string.added_to_basket));
                        AWCore.addToBasket(item);
                        updateBasket();
                        break;
                    }
                }
            }

            updateBasket();

        } else if (btnVariationOne.isEnabled() && btnVariationTwo.isEnabled()) {
            // FIND VARIATION IN WOOCOMMERCE

            if (optionOne.equals("")) {
                ToolHelper.showToast(getString(R.string.please_add_a__, storeItem.getAttributes().get(0).getName()));
                return;
            }

            if (optionTwo.equals("")) {
                ToolHelper.showToast(getString(R.string.please_add_a__, storeItem.getAttributes().get(1).getName()));
                return;
            }

            int variationId = getVariationId();

            if (variationId != 0) {

                for (Variation storeVariation : this.storeItem.getVariations()) {
                    if (storeVariation.getId() == variationId) {

                        if (!storeVariation.getInStock()) {
                            ToolHelper.showToast(getString(R.string.sorry_this_item_is_out_of_stock));
                            return;
                        }

                        // BASKET ITEM
                        StoreBasket item = new StoreBasket();
                        item.setStoreItem(storeItem);
                        item.getStoreItem().setPrice(storeVariation.getPrice());
                        item.setQty(1);
                        item.setVariationID(variationId);

                        AWCore.addToBasket(item);
                        updateBasket();
                        ToolHelper.showToast(getString(R.string.added_to_basket));
                        break;
                    }
                }
            }

            updateBasket();
        }
    }

    public int getVariationId() {
        int variationId = 0;

        if (btnVariationOne.isEnabled() && btnVariationTwo.isEnabled()) {

            Boolean attrOneFound = false;
            Boolean attrTwoFound = false;

            for (int i = 0; i < storeItem.getVariations().size(); i++) {

                for (int indx = 0; indx < storeItem.getVariations().get(i).getAttributes().size(); indx++) {

                    if (storeItem.getVariations().get(i).getAttributes().get(indx).getOption().equals(optionOne)) {
                        attrOneFound = true;
                    }

                    if (storeItem.getVariations().get(i).getAttributes().get(indx).getOption().equals(optionTwo)) {
                        attrTwoFound = true;
                    }
                }

                if (attrOneFound == true && attrTwoFound == true) {
                    return storeItem.getVariations().get(i).getId();
                } else {
                    attrOneFound = false;
                    attrTwoFound = false;
                }
            }
        } else if (btnVariationOne.isEnabled() && !btnVariationTwo.isEnabled()) {

            Boolean attrOneFound = false;

            for (int i = 0; i < storeItem.getVariations().size(); i++) {

                for (int indx = 0; indx < storeItem.getVariations().get(i).getAttributes().size(); indx++) {

                    if (storeItem.getVariations().get(i).getAttributes().get(indx).getOption().equals(optionOne)) {
                        attrOneFound = true;
                    }
                }

                if (attrOneFound == true) {
                    return storeItem.getVariations().get(i).getId();
                } else {
                    attrOneFound = false;
                }
            }
        }

        return variationId;
    }

    public Variation getStoreItemVariation() {

        int variationId = getVariationId();

        if (variationId != 0) {
            for (Variation storeVariation : this.storeItem.getVariations()) {
                if (storeVariation.getId() == variationId) {
                    return storeVariation;
                }
            }
        } else {
            return new Variation();
        }

        return new Variation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_fashion);
        ButterKnife.bind(this);

        StoreItem obj = (StoreItem) IntentHelper.getObjectForKey("storeItem");
        cl_loader_view.setVisibility(View.VISIBLE);
        carouselView = findViewById(R.id.carouselView);

        if (obj != null) {
            LabelAPI.Factory.getInstance().variationsForProduct(obj.getId().toString()).enqueue(new Callback<StoreItemWithVariation>() {
                @Override
                public void onResponse(Call<StoreItemWithVariation> call, Response<StoreItemWithVariation> response) {
                    cl_loader_view.setVisibility(View.GONE);
                    if (response.body() != null) {
                        setupView(response.body());
                    } else {
                        ToolHelper.showToast(getString(R.string.oops_something_went_wrong));
                    }
                }

                @Override
                public void onFailure(Call<StoreItemWithVariation> call, Throwable t) {
                    ToolHelper.networkError(t);
                }
            });
        }
    }

    public void setupView(StoreItemWithVariation obj) {
        storeItem = obj;

        this.tvProductName.setText(obj.getName());
        this.tvProductPrice.setText(AWCore.strToPrice(obj.getPrice()));
        this.tvProductDescription.setText(EncodeUtils.htmlDecode(obj.getDescription()));
        this.tvProductDescription.setMovementMethod(new ScrollingMovementMethod());

        carouselView.setImageListener(imageListener);

        if (obj.getImages().size() == 0) {
            carouselView.setPageCount(1);
        } else {
            carouselView.setPageCount(obj.getImages().size() - 1);
        }

        String countBasket = Integer.toString(AWCore.getBasket().size());
        tvCartTotal.setText(countBasket);

        btnVariationOne.setEnabled(false);
        btnVariationTwo.setEnabled(false);

        btnVariationOne.setTextColor(Color.GRAY);
        btnVariationTwo.setTextColor(Color.GRAY);

        if (storeItem.getAttributes().size() != 0) {

            for (int i = 0; i < storeItem.getAttributes().size(); i++) {
                if (i == 0) {

                    btnVariationOne.setEnabled(true);
                    btnVariationTwo.setVisibility(View.GONE);
                    btnVariationOne.setTextColor(ContextCompat.getColor(this, R.color.labelGreen));

                    String attrName = storeItem.getAttributes().get(i).getName();

                    btnVariationOne.setText(getString(R.string.select__, attrName));

                } else if (i == 1) {

                    btnVariationTwo.setEnabled(true);
                    btnVariationTwo.setVisibility(View.VISIBLE);
                    btnVariationTwo.setTextColor(ContextCompat.getColor(this, R.color.labelGreen));

                    String attrName = storeItem.getAttributes().get(i).getName();

                    btnVariationTwo.setText(getString(R.string.select__, attrName));

                }
            }
        }

        updateBasket();
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            if (storeItem.getImages().size() > 0) {
                imageView.setAdjustViewBounds(true);
                Glide
                        .with(getApplicationContext())
                        .load(storeItem.getImages().get(position).getSrc().replace("https","http"))
                        .into(imageView);
            }
        }
    };


    // UPDATE UI FOR BASKET
    public void updateBasket() {
        String countBasket = Integer.toString(AWCore.getBasket().size());

        tvCartTotal.setText(countBasket);
    }

    // PRESENT ALERT VIEW FOR ATTRIBUTE SELECTION
    public void openAlert(String title, final List<String> alertItems) {

        String[] items = new String[alertItems.size()];
        String[] item = alertItems.toArray(items);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setItems(item, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (openAlert.equals("1")) {

                            optionOne = storeItem.getAttributes().get(0).getOptions().get(which);
                            btnVariationOne.setText(storeItem.getAttributes().get(0).getName() + ": " + optionOne.toUpperCase());

                            if (btnVariationOne.isEnabled() && !btnVariationTwo.isEnabled()) {
                                if (!optionOne.equals("")) {

                                    Variation storeItemVariation = getStoreItemVariation();

                                    tvProductPrice.setText(AWCore.strToPrice(storeItemVariation.getPrice()));
                                }
                            } else if (btnVariationOne.isEnabled() && btnVariationTwo.isEnabled()) {

                                if (!optionOne.equals("") && !optionTwo.equals("")) {

                                    Variation storeItemVariation = getStoreItemVariation();
                                    tvProductPrice.setText(AWCore.strToPrice(storeItemVariation.getPrice()));
                                }
                            }

                        } else if (openAlert.equals("2")) {
                            optionTwo = storeItem.getAttributes().get(1).getOptions().get(which);
                            btnVariationTwo.setText(storeItem.getAttributes().get(1).getName() + ": " + optionTwo.toUpperCase());

                            if (btnVariationOne.isEnabled() && btnVariationTwo.isEnabled()) {
                                if (!optionOne.equals("") && !optionTwo.equals("")) {

                                    Variation storeItemVariation = getStoreItemVariation();
                                    tvProductPrice.setText(AWCore.strToPrice(storeItemVariation.getPrice()));
                                }
                            }
                        }
                    }
                });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBasket();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}

