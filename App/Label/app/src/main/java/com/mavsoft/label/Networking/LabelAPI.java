package com.mavsoft.label.Networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Models.Authorize;
import com.mavsoft.label.Models.CheckoutOrderResponse;
import com.mavsoft.label.Models.StoreCategory.StoreCategory;
import com.mavsoft.label.Models.StoreItem;
import com.mavsoft.label.Models.StoreItemWithVariation.StoreItemWithVariation;
import com.mavsoft.label.Models.StoreOrderHistory.StoreOrderHistory;
import com.mavsoft.label.Models.StoreOrderResponse.StoreOrderResponse;
import com.mavsoft.label.Models.StripeCreateOrderPost;
import com.mavsoft.label.Models.WpLoginPost;
import com.mavsoft.label.Models.WpLoginResponse;
import com.mavsoft.label.Models.WpRegisterPost.WpRegisterPost;
import com.mavsoft.label.Models.WpRegisterResponse.WpRegisterResponse;
import com.mavsoft.label.Models.WpUserNonceResponse.WpUserNonceResponse;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public interface LabelAPI {

    // AUTHORIZE
    @POST(LabelRequest.authorize) @FormUrlEncoded Call<Authorize> accessToken(@Field("app") String appId);

    // BASE ROUTES
    @GET(LabelRequest.products) Call<StoreItem[]> get_products();

    @POST(LabelRequest.get_variations_for_id) @FormUrlEncoded Call<StoreItemWithVariation> variationsForProduct(@Field("product_id") String id);

    @GET(LabelRequest.get_cats) Call<StoreCategory[]> categories();

    @POST(LabelRequest.get_ords) @FormUrlEncoded Call<StoreOrderHistory[]> getOrders(@Field("orders[]") ArrayList<Integer> orders);

    @POST(LabelRequest.get_prodcat) @FormUrlEncoded Call<StoreItem[]> categoryProducts(@Field("id_cat") int id);

    @POST(LabelRequest.get_prodsearchall) @FormUrlEncoded Call<StoreItem[]> searchProducts(@Field("search") String search);

    @POST(LabelRequest.new_order) @FormUrlEncoded Call<StoreOrderResponse> createOrder(@Field("data") String data);

    @POST(LabelRequest.payment_stripe) Call<CheckoutOrderResponse> checkoutStripe(@Body StripeCreateOrderPost data);

    // WP ROUTES
    @POST(LabelRequest.wp_login) Call<WpLoginResponse> wpLogin(@Body WpLoginPost data);

    @POST(LabelRequest.wp_register) Call<WpRegisterResponse> wpRegister(@Body WpRegisterPost data);

    @POST(LabelRequest.get_user_nonce) Call<WpUserNonceResponse> wpUserNonce();

    class Factory {
        private static LabelAPI service;

        private static SSLSocketFactory getSSLSocketFactory() {
            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                return sslSocketFactory;
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                return null;
            }
        }

        public static LabelAPI getInstance() {
            if (service == null) {
                OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
                builder.readTimeout(30, TimeUnit.SECONDS);
                builder.connectTimeout(30, TimeUnit.SECONDS);
                builder.writeTimeout(30, TimeUnit.SECONDS);
                builder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();

                        if (AWCore.getToken() != null && !AWCore.getToken().equals("")) {
                            ongoing.addHeader("Accept", "application/json");
                            ongoing.addHeader("Content", "application/json");
                            ongoing.addHeader("Authorization", "Bearer " + AWCore.getToken());
                        }

                        return chain.proceed(ongoing.build());
                    }
                });
                builder.sslSocketFactory(getSSLSocketFactory());

                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create();

                Retrofit retrofit = new Retrofit.Builder().client(builder.build())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .baseUrl(LabelRequest.api_base)
                        .build();
                service = retrofit.create(LabelAPI.class);
                return service;
            } else {
                return service;
            }
        }
    }
}
