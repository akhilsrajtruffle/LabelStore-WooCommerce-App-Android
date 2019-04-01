package com.mavsoft.label;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mavsoft.label.Conf.LabelCore;
import com.mavsoft.label.Helpers.AWCore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class ActivityAbout extends AppCompatActivity {

    // UI
    @BindView(R.id.ivAppLogo) ImageView appLogo;
    @BindView(R.id.tvAppVersion) TextView appVersion;
    @BindView(R.id.tvStoreTitle) TextView storeTitle;
    @BindView(R.id.tvOrderConfirmedSupportEmail) TextView tvSupportEmail;

    @OnClick(R.id.btnViewTerms)
    public void viewTerms(Button button) {

        // OPENS APP TERMS URL
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(LabelCore.urlTerms));
    }

    @OnClick(R.id.btnViewPrivacy)
    public void viewPrivacy(Button button) {

        // OPENS APP PRIVACY URL
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(LabelCore.urlPrivacy));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setupUI();
    }

    // SETUP UI
    void setupUI() {
        appVersion.setText( getString(R.string.about_version, Integer.toString(BuildConfig.VERSION_CODE)));
        storeTitle.setText(LabelCore.storeName);
        tvSupportEmail.setText(LabelCore.storeEmail);
        appLogo.setImageDrawable(AWCore.getAppIcon());
    }
}
