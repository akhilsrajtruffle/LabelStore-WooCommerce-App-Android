package com.mavsoft.label.Networking;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public interface LabelRequest {

    String api_base_url = "https://woosignal.com/";
    String api_version = "api/v1/";

    String api_base = LabelRequest.api_base_url + LabelRequest.api_version;

    String authorize = "authorize";
    String products = "products";
    String new_order = "order";
    String get_cats = "categories";
    String get_prodcat = "category/products";
    String get_ords = "customer/orders";
    String get_prodsearchall = "products/search";
    String get_variations_for_id = "product/android/variations";
    String payment_stripe = "payment/stripe";

    // LABEL API

    String get_user_details = "wp-json/label/v1/gdetails/";

    String wp_login = "wp/login";
    String wp_register = "wp/register";
    String get_user_nonce = "wp/nonce";

}
