package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Helpers.ToolHelper;
import com.mavsoft.label.Models.WpGetUserPost.WpGetUserPost;
import com.mavsoft.label.Models.WpGetUserPostData.WpGetUserPostData;
import com.mavsoft.label.Models.WpGetUserResponse.WpGetUserResponse;
import com.mavsoft.label.Networking.WpAPI;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anthony Gordon on 24/07/2018.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class ActivityAccountDetail extends AppCompatActivity {

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    // UI ACTIONS
    @OnClick(R.id.btnSettings)
    public void viewSettings(Button button) {
        Intent intent = new Intent(this, ActivitySettings.class);
        startActivity(intent);
    }

    @OnClick(R.id.ibDismiss)
    public void viewDismiss(ImageButton button) {
        Intent i = new Intent(this, ActivityHome.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        ButterKnife.bind(this);

        getProfile();
    }

    public void getProfile() {
        WpGetUserPost wpGetUser = new WpGetUserPost();
        WpGetUserPostData wpGetUserPostData = new WpGetUserPostData();
        wpGetUserPostData.setUser_id(AWCore.getUserId());
        wpGetUser.setData(wpGetUserPostData);

        String request=new Gson().toJson(wpGetUser);

        Map<String,String> params = new HashMap<String, String>();
        params.put("data", request);

        WpAPI.Factory.getInstance().wpGetUser(wpGetUser).enqueue(new Callback<WpGetUserResponse>() {
            @Override
            public void onResponse(Call<WpGetUserResponse> call, Response<WpGetUserResponse> response) {
                if (response.body().getStatus().equals("205")) {
                    tvName.setText(response.body().getResult().getFirst_name());
                    tvEmail.setText(response.body().getResult().getEmail());
                } else {
                    ToolHelper.showToast(getString(R.string.oops_something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<WpGetUserResponse> call, Throwable t) {
                ToolHelper.networkError(t);
            }
        });
    }
}
