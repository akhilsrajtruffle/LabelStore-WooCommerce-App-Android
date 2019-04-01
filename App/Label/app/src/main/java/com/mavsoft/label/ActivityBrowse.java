package com.mavsoft.label;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Helpers.ToolHelper;
import com.mavsoft.label.Models.StoreItem;
import com.mavsoft.label.Networking.LabelAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

public class ActivityBrowse extends AppCompatActivity {

    // VARS
    public List<StoreItem> storeItems;
    public int categoryId;
    public String search;
    public Boolean hasSearched = false;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    // UI
    @BindView(R.id.tvCategoryTitle) TextView tvCategoryTitle;
    @BindView(R.id.BrowseProductsRV) RecyclerView browseProductsRV;
    @BindView(R.id.tvCartTotal) TextView tvCartTotal;
    @BindView(R.id.cl_loader_view) ConstraintLayout cl_loader_view;

    @OnClick(R.id.btnSortOptions)
    public void sortOptions() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] categoryNames = {getString(R.string.sort),getString(R.string.low_to_high),getString(R.string.high_to_low)};

        builder.setTitle(getString(R.string.categories))
                .setItems(categoryNames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (which != 0) {

                            switch (which) {
                                case 1:
                                    apiGetProdsHighLow();
                                    break;
                                case 2:
                                    apiGetProdsLowHigh();
                                    break;
                                default:
                                    finish();
                                    break;
                            }
                        }
                    }
                });
        builder.show();
    }

    @OnClick(R.id.btnViewCart)
    public void viewCart() {

        Intent i = new Intent(this, ActivityCart.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        ButterKnife.bind(this);

        recyclerView = (RecyclerView) findViewById(R.id.BrowseProductsRV);
        recyclerView.setHasFixedSize(true);

        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));


        if (getIntent().getStringExtra("type").equals("")) {
            if (getIntent().getStringExtra("categoryName").equals(null)) {
                finish();
            }

            String categoryName = getIntent().getStringExtra("categoryName");

            categoryId = getIntent().getIntExtra("categoryId",0);
            tvCategoryTitle.setText(ToolHelper.htmlToString(categoryName));

            apiGetProds();
        } else {
            hasSearched = true;
            search = getIntent().getStringExtra("search");
            apiGetSearchedProducts();
        }

        cl_loader_view.setVisibility(View.GONE);
    }

    // API
    // RETURNS STORES ITEMS ORDERED HIGH TO LOW
    private void apiGetProdsHighLow() {

        Collections.sort(storeItems, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                StoreItem p1 = (StoreItem) o1;
                StoreItem p2 = (StoreItem) o2;
                return String.valueOf(p2.getPrice()).compareTo(p1.getPrice());
            }
        });
        reloadAdapter();
    }

    // RETURNS STORES ITEMS ORDERED LOW TO HIGH
    private void apiGetProdsLowHigh() {
        Collections.sort(storeItems, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                StoreItem p1 = (StoreItem) o1;
                StoreItem p2 = (StoreItem) o2;
                return String.valueOf(p1.getPrice()).compareTo(p2   .getPrice());
            }
        });
        reloadAdapter();
    }

    private void apiGetProds() {

        LabelAPI.Factory.getInstance().categoryProducts(categoryId).enqueue(new Callback<StoreItem[]>() {
            @Override
            public void onResponse(Call<StoreItem[]> call, Response<StoreItem[]> response) {
                List<StoreItem> arrItems = new ArrayList<StoreItem>();
                for (int i=0;i < response.body().length; i++)
                {
                    StoreItem item = response.body()[i];
                    if (item != null) {
                        if (arrItems.size() != 0) {
                            Boolean found = false;
                            for (int ii = 0; ii < arrItems.size(); ii++) {
                                if (arrItems.get(ii).getName().equals(item.getName())) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                arrItems.add(item);
                            }
                        } else {
                            arrItems.add(item);
                        }
                    }
                }

                storeItems = arrItems;
                reloadAdapter();
            }

            @Override
            public void onFailure(Call<StoreItem[]> call, Throwable t) {
                ToolHelper.networkError(t);
            }
        });
    }

    public void apiGetSearchedProducts() {

        tvCategoryTitle.setText(getString(R.string.search_prod, search.toUpperCase()));

        LabelAPI.Factory.getInstance().searchProducts(search).enqueue(new Callback<StoreItem[]>() {
            @Override
            public void onResponse(Call<StoreItem[]> call, Response<StoreItem[]> response) {

                List<StoreItem> arrItems = new ArrayList<StoreItem>();
                for (int i=0;i < response.body().length; i++)
                {
                    StoreItem item = response.body()[i];
                    if (!item.equals(null)) {
                        arrItems.add(item);
                    }
                }

                storeItems = arrItems;
                reloadAdapter();
            }

            @Override
            public void onFailure(Call<StoreItem[]> call, Throwable t) {
                ToolHelper.networkError(t);
            }
        });
    }

    // RELOADS ADAPTER
    public void reloadAdapter() {
        adapter = new HomeProductAdapter(storeItems,this);
        recyclerView.setAdapter(adapter);
    }

    // UPDATE UI BASKET COUNT
    public void updateBasket() {
        String countBasket = Integer.toString(AWCore.getBasket().size());
        tvCartTotal.setText(countBasket);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBasket();
    }

}
