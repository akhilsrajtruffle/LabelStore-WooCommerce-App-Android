package com.mavsoft.label;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.mavsoft.label.Helpers.ToolHelper;
import com.mavsoft.label.Models.StoreOrderHistory.StoreOrderHistory;
import com.mavsoft.label.Networking.LabelAPI;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
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

public class ActivityOrders extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    private StoreOrderHistory[] storeOrderHistory;

    @BindView(R.id.AVLoadingIndicatorView) AVLoadingIndicatorView activityLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);

        recyclerView = (RecyclerView) findViewById(R.id.OrderHistoryRV);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiGetOrders();
    }

    public void apiGetOrders() {

        if (Paper.book().getAllKeys().contains("Orders")) {
            ArrayList<Integer> orders = Paper.book().read("Orders");

            if (orders.size() != 0) {

                activityLoader.smoothToShow();

            LabelAPI.Factory.getInstance().getOrders(orders).enqueue(new Callback<StoreOrderHistory[]>() {
                @Override
                public void onResponse(Call<StoreOrderHistory[]> call, Response<StoreOrderHistory[]> response) {

                    activityLoader.hide();
                    storeOrderHistory = response.body();

                    if (storeOrderHistory.length == 0) {
                        noOrderToast();
                        return;
                    }
                    reloadAdapter();
                }

                @Override
                public void onFailure(Call<StoreOrderHistory[]> call, Throwable t) {
                    showError();
                }
            });
        } else {
                activityLoader.hide();
                noOrderToast();
        }
        } else {
            activityLoader.hide();
            storeOrderHistory = new StoreOrderHistory[0];
            reloadAdapter();
            noOrderToast();
        }
    }

    public void showError() {
        ToolHelper.showToast(getString(R.string.oops_something_went_wrong));
    }

    public void reloadAdapter() {
        adapter = new OrderHistoryAdapter(storeOrderHistory, this);
        recyclerView.setAdapter(adapter);
    }

    public void noOrderToast() {
        ToolHelper.showToast(getString(R.string.no_previous_orders_found));
    }

}
