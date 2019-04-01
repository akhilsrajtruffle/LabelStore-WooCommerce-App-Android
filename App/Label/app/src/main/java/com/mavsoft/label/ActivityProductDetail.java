package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.EncodeUtils;
import com.bumptech.glide.Glide;;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Helpers.IntentHelper;
import com.mavsoft.label.Helpers.ToolHelper;
import com.mavsoft.label.Models.StoreBasket;
import com.mavsoft.label.Models.StoreItem;
import com.mavsoft.label.Models.StoreItemWithVariation.Image;
import com.mavsoft.label.Models.StoreItemWithVariation.StoreItemWithVariation;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class ActivityProductDetail extends AppCompatActivity {

    CarouselView carouselView;
    StoreItem storeItem;

    @BindView(R.id.tvProductName) TextView tvProductName;
    @BindView(R.id.tvProductDescription) TextView tvProductDescription;
    @BindView(R.id.tvProductPrice) TextView tvProductPrice;
    @BindView(R.id.cl_loader_view) ConstraintLayout cl_loader_view;
    @BindView(R.id.tvCartTotal) TextView tvCartTotal;


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

        // BASKET ITEM
        StoreBasket item = new StoreBasket();
        StoreItemWithVariation storeItemWithVariation = new StoreItemWithVariation();
        storeItemWithVariation.setName(storeItem.getName());

        List<Image> tmpImages = new ArrayList<>();
        for(int i=0;  i < storeItem.getImages().size(); i++) {
            Image tmpImg = new Image();
            tmpImg.setImageFromStoreItemSimple(storeItem.getImages().get(i));
            tmpImages.add(tmpImg);
        }

        storeItemWithVariation.setImages(tmpImages);
        storeItemWithVariation.setPrice(storeItem.getPrice());
        storeItemWithVariation.setDescription(storeItem.getDescription());
        storeItemWithVariation.setId(storeItem.getId());
        storeItemWithVariation.setInStock(storeItem.getInStock());
        storeItemWithVariation.setPurchasable(storeItem.getPurchasable());
        storeItemWithVariation.setRegularPrice(storeItem.getRegularPrice());
        storeItemWithVariation.setShippingClass(storeItem.getShippingClass());
        storeItemWithVariation.setStatus(storeItem.getStatus());
        storeItemWithVariation.setStockQuantity(storeItem.getStockQuantity());
        storeItemWithVariation.setType(storeItem.getType());
        storeItemWithVariation.setSku(storeItem.getSku());
        storeItemWithVariation.setShippingTaxable(storeItem.getShippingTaxable());
        storeItemWithVariation.setShippingClassId(storeItem.getShippingClassId());

        item.setStoreItem(storeItemWithVariation);
        item.setQty(1);

        if (!storeItem.getInStock()) {
            ToolHelper.showToast(getString(R.string.sorry_this_item_is_out_of_stock));
            return;
        }

        AWCore.addToBasket(item);
        ToolHelper.showToast(getString(R.string.added_to_basket));
        updateBasket();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);

        StoreItem obj = (StoreItem) IntentHelper.getObjectForKey("storeItem");

        cl_loader_view.setVisibility(View.VISIBLE);
        if (obj != null) {
            storeItem = obj;

            this.tvProductName.setText(obj.getName());
            this.tvProductPrice.setText(AWCore.strToPrice(obj.getPrice()));
            this.tvProductDescription.setText(EncodeUtils.htmlDecode(obj.getDescription()));
            this.tvProductDescription.setMovementMethod(new ScrollingMovementMethod());

            carouselView = findViewById(R.id.carouselView);

            if (obj.getImages().size() == 0) {
                carouselView.setPageCount(1);
            } else {
                carouselView.setPageCount(obj.getImages().size());
            }
            carouselView.setImageListener(imageListener);

            cl_loader_view.setVisibility(View.GONE);
        }

        String countBasket = Integer.toString(AWCore.getBasket().size());

        tvCartTotal.setText(countBasket);

        updateBasket();
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            if (storeItem.getImages().size() == 0) {

            } else {
                imageView.setAdjustViewBounds(true);
                Glide
                        .with(getApplicationContext())
                        .load(storeItem.getImages().get(position).getSrc().replace("https","http"))
                        .into(imageView);
            }
        }
    };

    public void updateBasket() {
        String countBasket = Integer.toString(AWCore.getBasket().size());

        tvCartTotal.setText(countBasket);
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
