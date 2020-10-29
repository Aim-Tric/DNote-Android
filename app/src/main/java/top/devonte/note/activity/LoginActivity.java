package top.devonte.note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import kotlin.Pair;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Response;
import top.devonte.note.R;
import top.devonte.note.base.BaseCallBack;
import top.devonte.note.bean.ResultBean;
import top.devonte.note.constant.ApiConstants;
import top.devonte.note.net.UserModel;

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
        Log.d(TAG, "init LoginActivity");
        usernameInput = findViewById(R.id.login_username);
        passwordInput = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_login_button);
        registerButton = findViewById(R.id.login_register_button);
        forgetPasswordButton = findViewById(R.id.login_forget_button);

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            UserModel.getInstance().login(username, password, new BaseCallBack(LoginActivity.this) {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String jsonStr = Objects.requireNonNull(response.body()).string();
                    ResultBean resultBean = JSON.parseObject(jsonStr, ResultBean.class);
                    runOnUiThread(() -> {
                        if (resultBean.getStatusCodeValue() == 200) {
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    resultBean.getBody().toString(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }
            });
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        forgetPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
            startActivity(intent);
        });
        Log.d(TAG, "init LoginActivity success!");
    }
}
