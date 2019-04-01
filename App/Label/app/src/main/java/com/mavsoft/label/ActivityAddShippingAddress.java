package com.mavsoft.label;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.airsaid.pickerviewlibrary.OptionsPickerView;
import com.mavsoft.label.Conf.LabelCore;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Helpers.ToolHelper;
import com.mavsoft.label.Models.LabelAddress;
import com.mavsoft.label.Models.UserBilling;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class ActivityAddShippingAddress extends AppCompatActivity {

    ArrayList<String> labelCountryList;

    // UI
    @BindView(R.id.etAddAddressLine) EditText etAddressLine;
    @BindView(R.id.etAddCity) EditText etCity;
    @BindView(R.id.etAddCounty) EditText etCounty;
    @BindView(R.id.etAddPostcode) EditText etPostcode;
    @BindView(R.id.etAddCountry) TextView etCountry;

    @OnClick(R.id.btnUseShippingAddress)
    public void UseShippingAddress() {
        finishAddress();
    }


    @OnClick(R.id.btnCountry)
    public void showPickerCountry() {
        showPicker();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_add_shipping_address);
        ButterKnife.bind(this);

        if (Paper.book().getAllKeys().contains(AWCore.PREF_USER_BILLING)) {
            UserBilling billing = Paper.book().read(AWCore.PREF_USER_BILLING);
            LabelAddress address = billing.getAddress();

            etAddressLine.setText(address.getStreet());
            etCity.setText(address.getCity());
            etCounty.setText(address.getCounty());
            etPostcode.setText(address.getPostcode());
            if (!address.getCountry().equals("")) {
                etCountry.setText(address.getCountry());
            }
        }
    }

    public void showPicker() {
        OptionsPickerView<String> mOptionsPickerView = new OptionsPickerView<>(this);
        mOptionsPickerView.setCancelText(getString(R.string.cancel));
        mOptionsPickerView.setSubmitText(getString(R.string.done));

        labelCountryList = ToolHelper.getStringNamesForCountries();

        mOptionsPickerView.setPicker(labelCountryList);

        mOptionsPickerView.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int option1, int option2, int option3) {
                etCountry.setText(labelCountryList.get(option1));
            }
        });
        mOptionsPickerView.show();
    }

    // VALIDATE TEXT FIELDS
    public boolean validateFields() {
        if (etAddressLine.getText().toString().length() < 1) {
            alertMessage(" " + getString(R.string.address_line_field));
            return false;
        }

        if (etCity.getText().toString().length() < 1) {
            alertMessage(" " + getString(R.string.city_field));
            return false;
        }

        if (etCounty.getText().toString().length() < 1) {
            alertMessage(" " + getString(R.string.county_field));
            return false;
        }

        if (!etPostcode.getText().toString().matches(LabelCore.regexPostcode)) {
            alertMessage(" " + getString(R.string.postcode_field));
            return false;
        }

        if (etCountry.getText().equals(getString(R.string.select))) {
            alertMessage(" " + getString(R.string.country_field));
            return false;
        }

        return true;
    }

    // PRESENT TOAST MESSAGE
    public void alertMessage(String regexError) {
        ToolHelper.showToast(getString(R.string.oops_please_check_the__before_continuing, regexError));
    }

    // USE SHIPPING ADDRESS TAPPED
    public void finishAddress() {

        if (!validateFields()) { return; }

        LabelAddress address = new LabelAddress();

        String streetVal = etAddressLine.getText().toString();
        String cityVal = etCity.getText().toString();
        String countyVal = etCounty.getText().toString();
        String postcodeVal = etPostcode.getText().toString();
        String countryVal = etCountry.getText().toString();

        address.setStreet(streetVal);
        address.setCity(cityVal);
        address.setCounty(countyVal);
        address.setPostcode(postcodeVal);
        address.setCountry(countryVal);

        if (Paper.book().getAllKeys().contains(AWCore.PREF_USER_BILLING)) {

        UserBilling billing = Paper.book().read(AWCore.PREF_USER_BILLING);

        billing.setAddress(address);

        Paper.book().write(AWCore.PREF_USER_BILLING,billing);
        } else {
            UserBilling billing = new UserBilling();

            billing.setAddress(address);

            Paper.book().write(AWCore.PREF_USER_BILLING,billing);
        }
        finish();
    }

}
