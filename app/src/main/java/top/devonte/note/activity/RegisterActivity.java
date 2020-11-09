package top.devonte.note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.devonte.note.R;
import top.devonte.note.bean.FileBean;
import top.devonte.note.bean.ResultBean;
import top.devonte.note.bean.UserBean;
import top.devonte.note.constant.ApiConstants;
import top.devonte.note.util.HttpUtils;

/**
 * 用户注册Activity类
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText checkPassEditText;
    private EditText phoneEditText;
    private EditText nicknameEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 初始化控件
        Button register = findViewById(R.id.register_register_button);
        usernameEditText = findViewById(R.id.register_username);
        passwordEditText = findViewById(R.id.register_password);
        checkPassEditText = findViewById(R.id.register_check_pass);
        phoneEditText = findViewById(R.id.register_phone);
        nicknameEditText = findViewById(R.id.register_nickname);

        register.setOnClickListener(v -> {
            // 完成注册事件
            if (checkForm()) {
                UserBean userBean = convertBean();
                String jsonStr = JSON.toJSONString(userBean);
                HttpUtils.post(ApiConstants.REGISTER_API, jsonStr, new Callback() {

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        toast(e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        ResultBean result = JSON.parseObject(response.body().string(), ResultBean.class);
                        toast(result.getMsg());
                        if (result.getCode() == 10000) {
                            runOnUiThread(() -> {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            });
                        }
                    }
                });
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean checkUsername() {
        String username = usernameEditText.getText().toString();
        if ("".equals(username)) {
            toast("用户名不能为空");
            return false;
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]+$");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            toast("用户名不能包含特殊字符");
            return false;
        }
        return true;
    }

    private boolean checkPassword() {
        String password = passwordEditText.getText().toString();
        if ("".equals(password)) {
            toast("密码不能为空");
            return false;
        }
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[\\^]{8,20}$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            toast("至少1个大写字母，1个小写字母和1个数字，长度不小于8位，并不大于20位");
            return false;
        }
        return true;
    }

    private boolean checkConfirmPass() {
        String password = passwordEditText.getText().toString();
        String checkPass = checkPassEditText.getText().toString();
        if ("".equals(checkPass)) {
            toast("请重复输入密码");
            return false;
        }
        if (!password.equals(checkPass)) {
            toast("确认密码与密码不一致");
            return false;
        }
        return true;
    }

    private boolean checkPhone() {
        String phone = phoneEditText.getText().toString();
        if ("".equals(phone)) {
            toast("手机号不能为空");
            return false;
        }

        return true;
    }

    private boolean checkNickName() {
        String nickName = nicknameEditText.getText().toString();
        if ("".equals(nickName)) {
            toast("昵称不能为空");
            return false;
        }
        return true;
    }

    /**
     * 检查所有的输入框是否满足要求
     *
     * @return 满足要求 true， 否则为 false
     */
    private boolean checkForm() {
        return checkUsername()
                && checkPassword()
                && checkConfirmPass()
                && checkPhone()
                && checkNickName();
    }

    /**
     * 将表单内的内容封装成 UserBean 对象
     *
     * @return 表单对应 UserBean 对象
     */
    private UserBean convertBean() {
        UserBean bean = new UserBean();
        bean.setUsername(usernameEditText.getText().toString());
        bean.setPassword(passwordEditText.getText().toString());
        bean.setPhone(phoneEditText.getText().toString());
        bean.setNickName(nicknameEditText.getText().toString());
        return bean;
    }

}
