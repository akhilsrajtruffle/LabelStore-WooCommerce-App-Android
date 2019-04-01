package com.mavsoft.label.Conf;
import com.mavsoft.label.Models.StoreShippingLine;
import com.paypal.android.sdk.payments.PayPalConfiguration;

/**
 * Created by Anthony Gordon on 27/08/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

/*
 Developer Notes

 HERE YOU CAN CONFIG DETAILS ACROSS THE APP
 SUPPORT EMAIL - support@woosignal.com
 VERSION - 1.3
 */

/* ! CONFIGURE YOUR STORE HERE ! */

public class LabelCore {

    private LabelCore() {
    }

    /* ! CONNECT TO WOOSIGNAL ! */
    // Visit https://woosignal.com and generate an appKey to connect your store, follow the documentation for more information
    public static final String appKey = "your app key";
    public static final String wcUrl = "http://yourdomain.com/"; // BASE URL FOR THE SITE e.g. http://mysite.com/

    public static final String storeName = "LABEL STORE"; // - STORE NAME
    public static final String storeEmail = "e.g. support@woosignal.com"; // - STORE EMAIL

    public static final String urlTerms = "http://yourdoamin.com/terms";
    public static final String urlPrivacy = "http://yourdoamin.com/privacy";

    //
    /*<! ------ APP CURRENCY LOCALE ------!>*/

    // REF - https://woosignal.com/docs/list/iso-369-1
    public static final String localeLanguage = "en";

    // REF - https://woosignal.com/docs/ios/app-country-codes-list-iso-3166-1
    public static final String localeCountry = "GB";

    /*<! ------ APP ICON ------!>*/

    public static final String appIcon = "labelicon";

    /*<! ------ CURRENCY ------!>*/

    public static final String currency = "GBP";

    /*<! ------ TAX ------!>*/

    public static final String taxAmount = "0.00";

    /*<! ------ PAYMENT PROVIDERS ENABLED ------!>*/

    public static final Boolean useStripe = true;
    public static final Boolean usePayPal = false;
    public static final Boolean useCashOnDelivery = false;


    /*<! ------ STRIPE ------!>*/
    /*
     CONNECT STRIPE (OPTIONAL)
     Accept Stripe payments within the app from customers
     - Help setting up Stripe?
     * REF LINK - https://woosignal.com/docs/android/label-store#payments-stripe
     * STRIPE DOCS - https://stripe.com/docs/dashboard#api-keys
     */
    public static final String stripePublicKey = "your stripe publishable";


    /*<! ------ PAYPAL ------!>*/
    /*
     CONNECT PAYPAL (OPTIONAL)
     Accept PayPal payments within the app from customers
     - Help setting up PayPal?
     * REF LINK - https://woosignal.com/docs/android/label-store#payments-paypal
     */
    // ENVIRONMENT_NO_NETWORK - Mock testing
    // ENVIRONMENT_SANDBOX - Sandbox
    // ENVIRONMENT_PRODUCTION - Production

    public static final String paypalEnvironment = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    public static final String paypalClientID = "client id";
    public static final String currencySymbol = "GBP";

    /**
     * Create your shipping methods here for WooCommerce.
     */
    public static StoreShippingLine[] shippingLines = {

            new StoreShippingLine("simple_fee","Simple Shipping Fee","2.99"),

            new StoreShippingLine("free_shipping","Free Shipping","0.00")
    };

    /**
     * Supported countries (Add ISO for the countries you need)
     */
    public static String[] supportedCountryISO = {
            "GBR",
            "USA",
            "CHN",
            "DEU",
            "ESP",
            "CAN"
    };

    /*<! ------ POSTCODE REGEX ------!>*/
    // E.G. ^[A-Za-z]{1,2}[0-9][0-9A-Za-z]?\s?[0-9][A-Za-z]{2}$ (e.g. UK postcode)
    public static final String regexPostcode = ".*";

    /*<! ------ LOGIN ------!>*/
    /*
     * Enable login/registration in the app, you must have the following plugins installed on WordPress for this to work: Label plugin, JSON API, JSON API Auth, JSON API User
     - Help setting up Login?
     * REF LINK - https://woosignal.com/docs/android/label-store#feature-login
     */
    public static final Boolean useLabelLogin = false;


    /*<! ------ DEBUGGER ENABLED ------!>*/

    public static final Boolean labelDebug = true;
}


