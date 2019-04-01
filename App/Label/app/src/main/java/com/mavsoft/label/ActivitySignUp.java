package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
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

public class ActivitySignUp extends AppCompatActivity {

    // UI ACTIONS
    @BindView(R.id.etEmail) EditText etEmail;

    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.ivAppIcon) ImageView ivAppIcon;

    @OnClick(R.id.btnDismissView)
    public void dismissView(ImageButton imageButton) {
        finish();
    }

    @OnClick(R.id.btnSignUp)
    public void viewSignUp(Button button) {
        Intent i = new Intent(this, ActivityAccountGettingStarted.class);
        i.putExtra("email", etEmail.getText().toString());
        i.putExtra("password", etPassword.getText().toString());
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_signup);
        ButterKnife.bind(this);

        ivAppIcon.setImageDrawable(AWCore.getAppIcon());
    }

}
