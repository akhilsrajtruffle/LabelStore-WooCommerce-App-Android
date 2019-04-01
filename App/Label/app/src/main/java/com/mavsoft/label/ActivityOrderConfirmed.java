package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mavsoft.label.Conf.LabelCore;
import com.mavsoft.label.Models.StoreBasket;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class ActivityOrderConfirmed extends AppCompatActivity {

    // VARS
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    private List<StoreBasket> storeBasket;

    // UI
@BindView(R.id.tvOrderConfirmedRef) TextView tvRef;
    @BindView(R.id.tvOrderConfirmedBuyerName) TextView tvBuyerName;
    @BindView(R.id.tvOrderConfirmedBuyerAddress) TextView tvBuyerAddress;
    @BindView(R.id.tvOrderConfirmedSupportEmail) TextView tvSupportEmail;

@OnClick(R.id.btnOrderConfirmedBack)
    public void BackTapped() {
    dismissView();
    }

    @Override
    public void onBackPressed() {
        dismissView();
    }

    private void dismissView() {
        Intent i = new Intent(this, ActivityHome.class);
        i.putExtra("didCompleteOrder",true);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);
        ButterKnife.bind(this);

        recyclerView = (RecyclerView) findViewById(R.id.OrdersPlacedRV);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (Paper.book().getAllKeys().contains("UserOrderBasket")) {
            storeBasket = Paper.book().read("UserOrderBasket");
        }

        String buyersName = getIntent().getStringExtra("OrderBuyerName");
        String buyersAddress = getIntent().getStringExtra("OrderBuyerAddress");

        adapter = new OrderItemsAdapter(storeBasket, this);

        recyclerView.setAdapter(adapter);

        tvRef.setText(getString(R.string.order_ref__, String.valueOf(getIntent().getIntExtra("OrderId",0))));
        tvBuyerName.setText(buyersName);
        tvBuyerAddress.setText(buyersAddress);
        tvSupportEmail.setText(LabelCore.storeEmail);
    }

}
