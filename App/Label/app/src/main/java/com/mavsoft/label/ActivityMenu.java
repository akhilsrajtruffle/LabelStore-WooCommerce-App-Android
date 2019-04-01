package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mavsoft.label.Conf.LabelCore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class ActivityMenu extends AppCompatActivity {

    @BindView(R.id.containerAccount)
    ConstraintLayout containerAccount;

    // UI ACTIONS
    @OnClick(R.id.btnViewBasket)
    public void viewBasket(Button button) {
        Intent intent = new Intent(this, ActivityCart.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnViewOrders)
    public void viewOrders(Button button) {
        Intent intent = new Intent(this, ActivityOrders.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnViewAbout)
    public void viewAbout(Button button) {
        Intent intent = new Intent(this, ActivityAbout.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnViewAccount)
    public void viewAccount(Button button) {
        Intent intent = new Intent(this, ActivityAccount.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        if (!LabelCore.useLabelLogin) {
            containerAccount.setVisibility(View.GONE);
        }
    }
}
