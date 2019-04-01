package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.mavsoft.label.Helpers.AWCore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Anthony Gordon on 24/07/2018.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class ActivitySettings extends AppCompatActivity {

    @BindView(R.id.ivAppIcon) ImageView ivAppIcon;

    @OnClick(R.id.btnDismissView)
    public void dismissView() {
        finish();
    }

    @OnClick(R.id.btnAbout)
    public void btnAbout() {
        Intent i = new Intent(this, ActivityAbout.class);
        startActivity(i);
    }

    @OnClick(R.id.btnOrder)
    public void btnOrders() {
        Intent i = new Intent(this, ActivityOrders.class);
        startActivity(i);
    }

    @OnClick(R.id.btnLogout)
    public void logoutTapped() {

        AWCore.logoutUser();

        Intent i = new Intent(this,ActivityHome.class);
        startActivity(i);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        ButterKnife.bind(this);
    }

}
