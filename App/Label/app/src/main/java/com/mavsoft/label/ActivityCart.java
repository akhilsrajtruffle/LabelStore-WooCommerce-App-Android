package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Helpers.ToolHelper;
import com.mavsoft.label.Models.StoreBasket;

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

public class ActivityCart extends AppCompatActivity {

    public List<StoreBasket> storeBasket = new ArrayList<>();
    RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;

    @BindView(R.id.tvCartTotal) TextView tvTotal;
    @BindView(R.id.ivEmptyCart) ImageView ivEmptyCart;
    @BindView(R.id.tvCartSubtotal) TextView tvSubtotal;

    @OnClick(R.id.btnCartCheckout)
    public void checkout(Button button) {

        if (storeBasket.size() == 0) {
            emptyCheckoutError();
            return;
        }

        Intent i = new Intent(this,ActivityOrderConfirmation.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        storeBasket = AWCore.getBasket();

        if (storeBasket.size() == 0) {
            emptyCart();
            tvSubtotal.setText(AWCore.strToPrice("0"));
            return;
        }

        ivEmptyCart.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.CartProductsRV);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvTotal.setText(getString(R.string.total_with, AWCore.strToPrice(AWCore.woTotal(storeBasket,null))));

        reloadAdapter();
    }

    public void reloadAdapter() {
        adapter = new CartProductAdapter(storeBasket, this);

        if (storeBasket.size() == 0) {
            ivEmptyCart.setVisibility(View.VISIBLE);
        }
        recyclerView.setAdapter(adapter);

        tvTotal.setText(getString(R.string.total_with, AWCore.strToPrice(AWCore.woTotal(storeBasket,null))));
        tvSubtotal.setText(AWCore.strToPrice(AWCore.woTotal(storeBasket,null)));
    }

    public void emptyCart() {
        ToolHelper.showToast(getString(R.string.basket_empty));
        tvTotal.setText(getString(R.string.total_with, AWCore.strToPrice("0")));
        ivEmptyCart.setVisibility(View.VISIBLE);
    }

    public void emptyCheckoutError() {
        ToolHelper.showToast(getString(R.string.you_cannot_advance_with_an_empty_basket));
    }
}
