package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mavsoft.label.Conf.LabelCore;
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

public class ActivityAccountOnboardingFinish extends AppCompatActivity {

    @BindView(R.id.ivAppIcon) ImageView ivAppIcon;
    @BindView(R.id.tvHeader) TextView tvHeader;

    @OnClick(R.id.btnContinue)
    public void nextTapped() {
       Intent i = new Intent(this, ActivityAccountDetail.class);
       startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_onboarding_finish);
        ButterKnife.bind(this);

        ivAppIcon.setImageDrawable(AWCore.getAppIcon());
        tvHeader.setText(getString(R.string.welcome_to, " " + LabelCore.storeName));
    }

}
