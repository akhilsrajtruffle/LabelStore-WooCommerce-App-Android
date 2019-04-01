package com.mavsoft.label.Helpers;


import android.graphics.drawable.Drawable;
import android.util.Log;
import com.mavsoft.label.Conf.LabelCore;
import com.mavsoft.label.Models.StoreBasket;
import com.mavsoft.label.Models.StoreItemWithVariation.Attribute_;
import com.mavsoft.label.Models.StoreItemWithVariation.Variation;
import com.mavsoft.label.Models.StoreShippingLine;
import com.mavsoft.label.Utils.LabelApplication;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

/**
 * Created by Anthony Gordon on 11/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class AWCore {

    /**
     * Private variables
     */
    public static final String PREF_BASKET = "basket";
    public static final String PREF_USER_BILLING = "UserBilling";
    public static final String LOG_TAG = "AWCORE_TAG";
    public static final String PREF_TOKEN = "TOKEN";
    public static final String PREF_APP_ID = "APP_ID";
    public static final String PREF_WP_NONCE = "WP_USER_NONCE";
    public static final String PREF_WP_USER_ID = "WP_USER_ID";
    public static final String PREF_WP_SIGNIN = "WP_SIGNIN";

    private AWCore() {
    }

    public static String workoutVAT(String price) {
        double amount = Double.parseDouble(price);
        double tax = Double.parseDouble(LabelCore.taxAmount);
        return String.valueOf((amount * tax) / 100);
    }

    public static String strToPrice(String price) {

        double amount = 0;

        if (price == null || price.equals("")) {
            amount = 0;
        } else {
            if (price == null) {
                price = "0";
            }
            amount = Double.parseDouble(price);
        }

        Locale locale = new Locale(LabelCore.localeLanguage,LabelCore.localeCountry);

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(amount);
    }

    public static String woSubtotal(List<StoreBasket> storeBaskets) {

        double qtyPrice = 0;

        for (int i = 0; i < storeBaskets.size(); i++) {
            StoreBasket storeBasket = storeBaskets.get(i);
            qtyPrice = Double.parseDouble(woStoreBasketTotal(storeBasket)) + qtyPrice;
        }

        return String.valueOf(qtyPrice);
    }

    public static String woTotal(List<StoreBasket> storeBaskets, StoreShippingLine storeShippingLine) {

        double qtyPrice = 0;

        for (int i = 0; i < storeBaskets.size(); i++) {
            StoreBasket storeBasket = storeBaskets.get(i);
            qtyPrice = Double.parseDouble(woStoreBasketTotal(storeBasket)) + qtyPrice;
        }

        if (storeShippingLine != null) {
            qtyPrice = (qtyPrice + Double.parseDouble(storeShippingLine.getTotal()));
        }

        return String.valueOf(qtyPrice);
    }

    public static String woStoreBasketTotal(StoreBasket storeBasket) {

        double str;
        String price = storeBasket.getStoreItem().getPrice();
        if (price == null) {
            price = "0";
        }


        if (storeBasket.getQty() > 1) {
            str = (((double)storeBasket.getQty()) * Double.parseDouble(price));
        } else {
            str = (((double)storeBasket.getQty()) * Double.parseDouble(price));
        }

        return String.valueOf(str);
    }


    public static void addToBasket(StoreBasket item) {

        Paper.init(LabelApplication.getAppContext());

        List<StoreBasket> mObject;

        if (Paper.book().getAllKeys().contains(PREF_BASKET)) {
            mObject = Paper.book().read(PREF_BASKET);
        } else {
            mObject = new ArrayList<>();
        }

        if (mObject == null) {
            mObject = new ArrayList<>();
        }

        for (int i = 0; i < mObject.size(); i++) {
            if (mObject.get(i).getStoreItem().getId().equals(item.getStoreItem().getId())) {
                Log.d(LOG_TAG, item.getStoreItem().getName() + " already exists in the basket.");
                return;
            }
        }

        mObject.add(item);

        Paper.book().write(PREF_BASKET, mObject);

        ToolHelper.LabelPrint(item.getStoreItem().getName() + " Added to basket");
    }


    public static List<StoreBasket> getBasket() {
        ToolHelper.LabelPrint("* Getting Basket *");
        Paper.init(LabelApplication.getAppContext());
        List<StoreBasket> mObject;

        List<String> allKeys = Paper.book().getAllKeys();
        if (allKeys.contains(PREF_BASKET)) {
            ToolHelper.LabelPrint("* FOUND BASKET *");
            mObject = Paper.book().read(PREF_BASKET);
        } else {
            ToolHelper.LabelPrint("* NOT RESULTS IN BASKET *");
            mObject = new ArrayList<>();
        }

        return mObject;
    }

    public static void clearBasket() {
        ToolHelper.LabelPrint("* Clearing Basket *");
        Paper.init(LabelApplication.getAppContext());

        Paper.book().delete(PREF_BASKET);
        ToolHelper.LabelPrint("* Basket Cleared *");
    }

    public static String getVariationDescriptionForId(Variation storeItemVariation) {

        String tmpString = "";

        for (Attribute_ attr : storeItemVariation.getAttributes()) {
            tmpString = attr.getName() + ": " + attr.getOption() + ", " + tmpString;
        }

        if (tmpString.endsWith(", ")) {
            tmpString = tmpString.substring(0, tmpString.length() - 2);
        }

        return tmpString;
    }

    public static void setToken(String token) {
        ToolHelper.LabelPrint("* Setting Woosignal token *");
        Paper.init(LabelApplication.getAppContext());

        Paper.book().write(PREF_TOKEN, token);
    }

    public static String getToken() {
        Paper.init(LabelApplication.getAppContext());
        return Paper.book().read(PREF_TOKEN);
    }

    public static void setAppID(String appId) {
        ToolHelper.LabelPrint("* Setting Woosignal AppID *");
        Paper.init(LabelApplication.getAppContext());

        Paper.book().write(PREF_APP_ID, appId);
    }

    public static String getAppID() {
        Paper.init(LabelApplication.getAppContext());
        return Paper.book().read(PREF_APP_ID);
    }

    public static void setWpNonce(String nonce) {
        Paper.init(LabelApplication.getAppContext());
        Paper.book().write(PREF_WP_NONCE,nonce);
    }

    public static String getWpNonce() {
        Paper.init(LabelApplication.getAppContext());
        return Paper.book().read(PREF_WP_NONCE);
    }

    public static void setUserId(String userId) {
        Paper.init(LabelApplication.getAppContext());
        Paper.book().write(PREF_WP_USER_ID,userId);
    }

    public static String getUserId() {
        Paper.init(LabelApplication.getAppContext());
        return Paper.book().read(PREF_WP_USER_ID);
    }

    public static void logoutUser() {
        Paper.init(LabelApplication.getAppContext());
        Paper.book().write(PREF_WP_SIGNIN,"");
        Paper.book().write(PREF_WP_USER_ID,"");
    }

    public static void loginUser(String userId) {
        Paper.init(LabelApplication.getAppContext());
        setUserId(userId);
        Paper.book().write(PREF_WP_SIGNIN,1);
    }

    public static Drawable getAppIcon() {
        return ToolHelper.getDrawableForName(LabelCore.appIcon);
    }
}
