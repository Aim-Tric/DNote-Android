package top.devonte.note.util;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import top.devonte.note.constant.ApiConstants;

public class HttpUtils {

    private static final String TAG = "HttpUtils";

    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1000 * 10, TimeUnit.SECONDS)
            .cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                    cookieStore.put(Objects.requireNonNull(HttpUrl.parse(ApiConstants.LOGIN_API)).host(),
                            cookies);
                    for (Cookie cookie : cookies) {
                        System.out.println("cookie Name:" + cookie.name());
                        System.out.println("cookie Path:" + cookie.path());
                    }
                }

                @NotNull
                @Override
                public List<Cookie> loadForRequest(@NotNull HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(
                            Objects.requireNonNull(HttpUrl.parse(ApiConstants.LOGIN_API))
                                    .host());
                    if (cookies == null) {
                        Log.d(TAG, "未加载到Cookie");
                    }
                    return cookies != null ? cookies : new ArrayList<>();
                }
            }).build();

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    public static void post(String url, Map<String, String> formBodyMap, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(buildBody(formBodyMap))
                .build();
        client.newCall(request)
                .enqueue(callback);
    }

    public static void post(String url, String json, Callback callback) {
        RequestBody requestBody = RequestBody.create(json, MEDIA_TYPE_JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request)
                .enqueue(callback);
    }

    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void put(String url, String json, Callback callback) {
        Request request = new Request.Builder()
                .put(FormBody.create(json, MEDIA_TYPE_JSON))
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void delete(String url, Callback callback) {
        Request request = new Request.Builder()
                .delete()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    private static FormBody buildBody(Map<String, String> formBodyMap) {
        FormBody.Builder builder = new FormBody.Builder();
        if (formBodyMap != null) {
            for (Map.Entry<String, String> entry : formBodyMap.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

}
