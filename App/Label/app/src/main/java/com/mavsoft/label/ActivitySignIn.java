package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Helpers.ToolHelper;
import com.mavsoft.label.Models.WpLoginPost;
import com.mavsoft.label.Models.WpLoginPostData.WpLoginPostData;
import com.mavsoft.label.Models.WpLoginResponse;
import com.mavsoft.label.Models.WpLoginResponseResult.WpLoginResponseResult;
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

public class ActivitySignIn extends AppCompatActivity {

    @BindView(R.id.ivAppIcon)
    ImageView ivAppIcon;

    @BindView(R.id.etEmail) EditText etEmail;

    @BindView(R.id.etPassword) EditText etPassword;

    @OnClick(R.id.btnDismissView)
    public void dismissView(ImageButton imageButton) {
        finish();
    }

    @OnClick(R.id.btnSignIn)
    public void signIn(Button button) {
        WpLoginPost wpLoginPost = new WpLoginPost();
        WpLoginPostData wpLoginPostData = new WpLoginPostData();
        wpLoginPostData.setEmail(etEmail.getText().toString());
        wpLoginPostData.setPassword(etPassword.getText().toString());
        if (AWCore.getWpNonce() != null) {
            wpLoginPostData.setToken(AWCore.getWpNonce());
        }

        wpLoginPost.setWpLoginPostData(wpLoginPostData);

                LabelAPI.Factory.getInstance().wpLogin(wpLoginPost).enqueue(new Callback<WpLoginResponse>() {
                    @Override
                    public void onResponse(Call<WpLoginResponse> call, Response<WpLoginResponse> response) {
                        if (response.body().getStatus().equals("205")) {
                            WpLoginResponseResult result = response.body().getWpLoginResponseResult();

                            AWCore.loginUser(result.getUser().getId().toString());
                            presentAccount();

                        } else if (response.body().getStatus().equals("505")) {
                            ToolHelper.showToast(getString(R.string.invalid_details));
                        } else {
                            ToolHelper.showToast(getString(R.string.oops_something_went_wrong));
                        }
                    }

                    @Override
                    public void onFailure(Call<WpLoginResponse> call, Throwable t) {
                        ToolHelper.networkError(t);
                    }
                });
    }

    public void presentAccount() {
        Intent intent = new Intent(this, ActivityAccountDetail.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);
        ButterKnife.bind(this);

        ivAppIcon.setImageDrawable(AWCore.getAppIcon());
    }

}
