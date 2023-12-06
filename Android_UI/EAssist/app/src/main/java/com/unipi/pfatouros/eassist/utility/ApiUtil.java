package com.unipi.pfatouros.eassist.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtil {

    public static Retrofit getRetrofit() {

        // Return retrofit object (all requests require a jwt token, except sign in request)

        // Set lenient in order to accept malformed JSON
        // The response is an one-line string which contains the jwt
        Gson gson = new GsonBuilder().setLenient().create();

        // Return retrofit object without custom client
        return new retrofit2.Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static Retrofit getRetrofit(String token) {

        // Return retrofit object (all requests require a jwt token, except sign in request)

        // Create a client that intercepts the request and adds the authorization header
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader(Constants.HEADER_KEY, Constants.PREFIX + token)
                    .build();
            return chain.proceed(newRequest);
        }).build();

        // Return retrofit object with custom client
        return new retrofit2.Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
