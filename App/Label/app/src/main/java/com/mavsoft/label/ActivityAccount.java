package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
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

public class ActivityAccount extends AppCompatActivity {

    @BindView(R.id.ivAppIcon)
    ImageView ivAppIcon;

    // UI ACTIONS
    @OnClick(R.id.btnSignUp)
    public void viewSignUp(Button button) {
        Intent intent = new Intent(this, ActivitySignUp.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnSignIn)
    public void viewSignIn(Button button) {
        Intent intent = new Intent(this, ActivitySignIn.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnDismissView)
    public void dismissView(ImageButton imageButton) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_landing);
        ButterKnife.bind(this);

        ivAppIcon.setImageDrawable(AWCore.getAppIcon());

        if (AWCore.getUserId() != null && !AWCore.getUserId().equals("")) {
            presentAccountView();
        }
    }

    public void presentAccountView() {
        Intent i = new Intent(this, ActivityAccountDetail.class);
        startActivity(i);
    }
}
