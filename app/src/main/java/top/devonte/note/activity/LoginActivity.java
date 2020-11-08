package top.devonte.note.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.devonte.note.R;
import top.devonte.note.bean.ResultBean;
import top.devonte.note.bean.UserBean;
import top.devonte.note.constant.ApiConstants;
import top.devonte.note.util.HttpUtils;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button registerButton;
    private Button forgetPasswordButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        usernameInput = findViewById(R.id.login_username);
        passwordInput = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_login_button);
        registerButton = findViewById(R.id.login_register_button);
        forgetPasswordButton = findViewById(R.id.login_forget_button);

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            Map<String, String> data = new HashMap<>();
            data.put("username", username);
            data.put("password", password);
            HttpUtils.post(ApiConstants.LOGIN_API, data, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String jsonStr = Objects.requireNonNull(response.body()).string();
                    ResultBean resultBean = JSON.parseObject(jsonStr, ResultBean.class);
                    HttpUtils.get(ApiConstants.AUTH_INFO_API, new Callback() {

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String json = response.body().string();
                            SharedPreferences noteuser = getSharedPreferences("NOTE", MODE_PRIVATE);
                            SharedPreferences.Editor edit = noteuser.edit();
                            edit.putString("userInfo", json);
                            edit.apply();
                        }

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            runOnUiThread(() -> {
                                Toast.makeText(LoginActivity.this,
                                        e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                    runOnUiThread(() -> {
                        if (resultBean.getStatusCodeValue() == 200) {
                            intent(MainActivity.class);
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    resultBean.getBody().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        });

        registerButton.setOnClickListener(v -> {
            intent(RegisterActivity.class);
        });

        forgetPasswordButton.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "暂不支持此功能", Toast.LENGTH_LONG).show();
        });
    }

    private void intent(Class target) {
        runOnUiThread(() -> {
            Intent intent = new Intent(LoginActivity.this, target);
            startActivity(intent);
        });
    }

    private void checkLoginStatus() {
        SharedPreferences noteShared = getSharedPreferences("NOTE", Context.MODE_PRIVATE);
        Map<String, ?> all = noteShared.getAll();
        // TODO 如果存在cookie，则访问测试接口
        // TODO 如果过时，进行登录，否则，直接进入MainActivity

    }
}
