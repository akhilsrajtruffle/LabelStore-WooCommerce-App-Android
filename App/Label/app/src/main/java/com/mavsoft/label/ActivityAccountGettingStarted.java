package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.gson.Gson;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Helpers.ToolHelper;
import com.mavsoft.label.Models.WpRegisterPost.WpRegisterPost;
import com.mavsoft.label.Models.WpRegisterPostData.WpRegisterPostData;
import com.mavsoft.label.Models.WpRegisterResponse.WpRegisterResponse;
import com.mavsoft.label.Networking.LabelAPI;

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

public class ActivityAccountGettingStarted extends AppCompatActivity {

    public String email = "";
    public String password = "";

    @BindView(R.id.etFirstName)
    EditText etFirstName;

    @BindView(R.id.etLastName)
    EditText etLastName;

    @OnClick(R.id.btnDismissView)
    public void dismissView() {
        finish();
    }

    @OnClick(R.id.btnNext)
    public void nextTapped() {

        WpRegisterPost wpRegisterPost = new WpRegisterPost();
        WpRegisterPostData wpRegisterPostData = new WpRegisterPostData();
        wpRegisterPostData.setEmail(email);
        wpRegisterPostData.setPassword(password);
        wpRegisterPostData.setFirst_name(etFirstName.getText().toString());
        wpRegisterPostData.setLast_name(etLastName.getText().toString());
        if (AWCore.getWpNonce() != null) {
            wpRegisterPostData.setNonce(AWCore.getWpNonce());
        }
        wpRegisterPostData.setUsername("label_" + ToolHelper.getRandomString(3) + "_" + etFirstName.getText().toString());

        wpRegisterPost.setData(wpRegisterPostData);
        LabelAPI.Factory.getInstance().wpRegister(wpRegisterPost).enqueue(new Callback<WpRegisterResponse>() {
            @Override
            public void onResponse(Call<WpRegisterResponse> call, Response<WpRegisterResponse> response) {
                if (response.body().getStatus().equals("205")) {
                    AWCore.loginUser(response.body().getResult().getUser_id().toString());
                    viewAccountOnboarding();
                } else {
                    ToolHelper.showToast(getString(R.string.oops_something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<WpRegisterResponse> call, Throwable t) {
                ToolHelper.networkError(t);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_getting_started);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
    }

    public void viewAccountOnboarding() {
        Intent i = new Intent(this,ActivityAccountOnboardingFinish.class);
        startActivity(i);
    }

}
