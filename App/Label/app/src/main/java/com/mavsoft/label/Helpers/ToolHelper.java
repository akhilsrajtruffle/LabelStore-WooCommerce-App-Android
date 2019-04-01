package com.mavsoft.label.Helpers;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.mavsoft.label.Conf.LabelCore;
import com.mavsoft.label.Models.StoreBasket;
import com.mavsoft.label.R;
import com.mavsoft.label.Utils.LabelApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Random;

/**
 * Created by Anthony Gordon on 11/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class ToolHelper {

    public ToolHelper() {
    }

    public static Drawable getDrawableForName(String name) {
        Resources resources = LabelApplication.getAppContext().getResources();
        final int resourceId = resources.getIdentifier(name, "drawable",
                LabelApplication.getAppContext().getPackageName());
        return resources.getDrawable(resourceId);
    }

    public static void showToast(String msg) {
        Toast.makeText(LabelApplication.getAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void networkError(Throwable t) {
        ToolHelper.LabelPrint(t.toString());
        Toast.makeText(LabelApplication.getAppContext(), Resources.getSystem().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
    }

    public static String htmlToString(String string) {
        return android.text.Html.fromHtml(string).toString();
    }

    public static void LabelPrint(String msg) {
        if (LabelCore.labelDebug) {
            Log.e("LABEL_PRINT", msg);
        }
    }

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    public static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static ArrayList<String> getStringNamesForCountries() {
        ArrayList<String> countries = new ArrayList<>();

        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            try {
                String iso = locale.getISO3Country();
                String code = locale.getCountry();
                String name = locale.getDisplayCountry();

                if (!"".equals(iso) && !"".equals(code) && !"".equals(name)) {

                    if (Arrays.asList(LabelCore.supportedCountryISO).contains(iso)) {
                        if (!countries.contains(name)) {
                            countries.add(name);
                        }
                    }

                }
            } catch (MissingResourceException ex) {
            }
        }

        return countries;
    }

    public static String getStripeDescForBasket(List<StoreBasket> storeBasket) {

            String result = "";

            if (storeBasket.size() != 0) {

                for (Integer i = 0; i < storeBasket.size(); i++) {
                    String decodeTitle = ToolHelper.htmlToString(storeBasket.get(i).getStoreItem().getName());

                    if ((i + 1) == storeBasket.size()) {
                        result = result + "x " + String.valueOf(storeBasket.get(i).getQty()) + " | " + decodeTitle;
                    } else {
                        result = result + "x " + String.valueOf(storeBasket.get(i).getQty()) + " | " + decodeTitle + ", ";
                    }
                }
            }
            return result;
    }
}
