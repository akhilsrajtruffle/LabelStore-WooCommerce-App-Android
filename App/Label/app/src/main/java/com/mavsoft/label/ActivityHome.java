package com.mavsoft.label;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.mavsoft.label.Conf.LabelCore;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Helpers.ToolHelper;
import com.mavsoft.label.Models.Authorize;
import com.mavsoft.label.Models.StoreCategory.StoreCategory;
import com.mavsoft.label.Models.StoreItem;
import com.mavsoft.label.Models.WpUserNonceResponse.WpUserNonceResponse;
import com.mavsoft.label.Networking.LabelAPI;
import com.mavsoft.label.Utils.DispatchGroup;
import com.mavsoft.label.Utils.LabelApplication;

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

public class ActivityHome extends AppCompatActivity {

    List<StoreItem> storeItems;
    StoreCategory[] storeCategories;
    List<StoreCategory> storeCat;
    Boolean hasTappedCategories = false;
    DispatchGroup dispatchGroup;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @BindView(R.id.ivAppIcon) ImageView ivAppIcon;
    @BindView(R.id.tvCartTotal) TextView tvCartTotal;
    @BindView(R.id.tvWooSignalMsg) TextView tvWooSignalMsg;
    @BindView(R.id.cl_loader_view) ConstraintLayout cl_loader_view;

    @OnClick(R.id.btnViewAllCategories)
    public void viewCategories() {

        if (!hasTappedCategories) {
            hasTappedCategories = true;
            openCategories();
        }
    }

    public void openCategories() {

        storeCat = new ArrayList<>();

      if (storeCategories.length == 0) {
          return;
      }

      final List<StoreCategory> tmpStoreCategories = new ArrayList<>();

      for (int i = 0; i < storeCategories.length; i++) {
          if (storeCategories[i].getParent() == 0) {
              tmpStoreCategories.add(storeCategories[i]);
          }
      }

        if (storeCategories.length > 0) {

            // PARENT CATEGORIES
            for (int i = 0; i < storeCategories.length; i++) {
                if (storeCategories[i].getParent() == 0) {
                    storeCat.add(storeCategories[i]);
                }
            }

            final List<String> arrayCategoryNames = new ArrayList<>();

            String[] myArray = new String[arrayCategoryNames.size()];


            for (int i = 0; i < storeCat.size(); i++) {
                if (storeCat.get(i).getParent() == 0) {
                    arrayCategoryNames.add(ToolHelper.htmlToString(storeCat.get(i).getName()));
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.categories))
                    .setItems(arrayCategoryNames.toArray(myArray), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent i = new Intent(LabelApplication.getAppContext(), ActivityBrowse.class);
                            i.putExtra("categoryName",tmpStoreCategories.get(which).getName());
                            i.putExtra("categoryId", tmpStoreCategories.get(which).getId());
                            i.putExtra("type","");
                            startActivity(i);
                        }
                    });
            builder.show();

            hasTappedCategories = false;
        }
    }

    @Override
    public void onBackPressed() {
    }

    @OnClick(R.id.btnMenu)
    public void viewMenu() {
        Intent i = new Intent(this, ActivityMenu.class);
        startActivity(i);
    }

    @OnClick(R.id.btnViewCart)
    public void viewCart() {
        Intent i = new Intent(this, ActivityCart.class);
        startActivity(i);
    }

    @OnClick(R.id.btnSearch)
    public void viewSearch() {
        Intent i = new Intent(this,ActivitySearch.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        cl_loader_view.setVisibility(View.VISIBLE);

        // RECYCLER VIEW SETUP
        recyclerView = (RecyclerView) findViewById(R.id.HomeProductsRV);
        recyclerView.setHasFixedSize(true);

        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        // CLEARS ACTIVITIES IF WE COMPLETE AN ORDER
        int shouldClearActivities = getIntent().getIntExtra("didCompleteOrder",0);
        if (shouldClearActivities == 1) {
            clearActivities();
        }

        if (LabelCore.appKey.equals("your app key") || LabelCore.appKey.equals("")) {
            ToolHelper.showToast("You need to app an AppKey from WooSignal first");
            tvWooSignalMsg.setVisibility(View.VISIBLE);
            return;
        } else {
            tvWooSignalMsg.setVisibility(View.GONE);
        }

        dispatchGroup = new DispatchGroup();
        String appID = AWCore.getAppID();
        if (appID != null && !appID.equals(LabelCore.appKey)) {
            AWCore.setAppID("");
            appID = "";
        }

        if (appID == null || appID.equals("")) {
            dispatchGroup.enter();
            getAppKey();
        }

        updateBasket();

        dispatchGroup.notify(new Runnable() {
            @Override
            public void run() {
                apiGetProds();
                apiGetCategories();
                if (LabelCore.useLabelLogin) {
                    getWpNonce();
                }
            }
        });

        ivAppIcon.setImageDrawable(AWCore.getAppIcon());
    }

    public void getWpNonce() {
        LabelAPI.Factory.getInstance().wpUserNonce().enqueue(new Callback<WpUserNonceResponse>() {
            @Override
            public void onResponse(Call<WpUserNonceResponse> call, Response<WpUserNonceResponse> response) {
                    AWCore.setWpNonce(response.body().getResult().getToken());
            }

            @Override
            public void onFailure(Call<WpUserNonceResponse> call, Throwable t) {
                ToolHelper.networkError(t);
            }
        });
    }

    public void getAppKey() {
        // GET ACCESS TOKEN
        LabelAPI.Factory.getInstance().accessToken(LabelCore.appKey).enqueue(new Callback<Authorize>() {
            @Override
            public void onResponse(Call<Authorize> call, Response<Authorize> response) {
                AWCore.setToken(response.body().getResult().getToken());
                dispatchGroup.leave();
            }

            @Override
            public void onFailure(Call<Authorize> call, Throwable t) {
                ToolHelper.networkError(t);
            }
        });
    }

    // API
    private void apiGetProds() {
        LabelAPI.Factory.getInstance().get_products().enqueue(new Callback<StoreItem[]>() {
            @Override
            public void onResponse(Call<StoreItem[]> call, Response<StoreItem[]> response) {
                if (response.code() == 404) {
                    ToolHelper.LabelPrint("Invalid url inside LabelCore");
                    ToolHelper.showToast(getString(R.string.oops_something_went_wrong));
                    return;
                }

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
                cl_loader_view.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StoreItem[]> call, Throwable t) {
                ToolHelper.networkError(t);
            }
        });
    }

    private void apiGetCategories() {

        LabelAPI.Factory.getInstance().categories().enqueue(new Callback<StoreCategory[]>() {
            @Override
            public void onResponse(Call<StoreCategory[]> call, Response<StoreCategory[]> response) {
                storeCategories = response.body();
            }

            @Override
            public void onFailure(Call<StoreCategory[]> call, Throwable t) {
                ToolHelper.networkError(t);
            }
        });

    }

    public void reloadAdapter() {
        adapter = new HomeProductAdapter(storeItems,this);
        recyclerView.setAdapter(adapter);
    }


    public void updateBasket() {
        String countBasket = Integer.toString(AWCore.getBasket().size());
        tvCartTotal.setText(countBasket);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateBasket();
    }

    public void clearActivities() {
        ActivityUtils.finishAllActivities();
    }
}
