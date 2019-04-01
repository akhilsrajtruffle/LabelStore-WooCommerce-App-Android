package com.mavsoft.label.Networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mavsoft.label.Conf.LabelCore;
import com.mavsoft.label.Models.WpGetUserPost.WpGetUserPost;
import com.mavsoft.label.Models.WpGetUserResponse.WpGetUserResponse;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
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
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.


public interface WpAPI {

    @Headers("Content-type: application/json")
    @POST(LabelRequest.get_user_details)
    Call<WpGetUserResponse> wpGetUser(@Body WpGetUserPost data);

    class Factory {
        private static WpAPI service;

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

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                return sslSocketFactory;
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                return null;
            }

        }

        public static WpAPI getInstance() {
            if (service == null) {
                OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
                builder.readTimeout(30, TimeUnit.SECONDS);
                builder.connectTimeout(30, TimeUnit.SECONDS);
                builder.writeTimeout(30, TimeUnit.SECONDS);
                builder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();

                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("Content", "application/json");

                        return chain.proceed(ongoing.build());
                    }
                });
                builder.sslSocketFactory(getSSLSocketFactory());

                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create();

                Retrofit retrofit = new Retrofit.Builder().client(builder.build())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .baseUrl(LabelCore.wcUrl)
                        .build();
                service = retrofit.create(WpAPI.class);
                return service;
            } else {
                return service;
            }
        }
    }
}
