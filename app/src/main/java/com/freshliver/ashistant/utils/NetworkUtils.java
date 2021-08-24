package com.freshliver.ashistant.utils;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class NetworkUtils {

    private NetworkUtils() {
    }


    public static Response simplePost(@NotNull String url, @Nullable Headers headers, @NotNull RequestBody body) throws IOException {

        // build https request instance
        final Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .headers((headers != null) ? headers : new Headers.Builder().build())
                .build();

        // build a client to send request
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        return client.newCall(request).execute();
    }
}
