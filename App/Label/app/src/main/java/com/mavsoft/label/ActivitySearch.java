package com.mavsoft.label;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class ActivitySearch extends AppCompatActivity {

    @BindView(R.id.etSearch) EditText etSearch;

    @OnClick(R.id.btnDismissView)
    public void dismissView() {
        finish();
    }

    @OnClick(R.id.btnSearch)
    public void searchClicked() {

        Intent i = new Intent(this, ActivityBrowse.class);
        i.putExtra("type","search");
        i.putExtra("search",etSearch.getText().toString());
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

    }
}
