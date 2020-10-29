package top.devonte.note.net;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import top.devonte.note.constant.ApiConstants;
import top.devonte.note.bean.UserBean;
import top.devonte.note.util.HttpUtils;

public class UserModel {

    private static final UserModel instance = new UserModel();

    private UserModel() {
    }

    public static UserModel getInstance() {
        return instance;
    }

    public void login(String username, String password, Callback callback) {
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        HttpUtils.post(ApiConstants.LOGIN_API, data, callback);
    }

    public void register(UserBean userBean, Callback callback) {
        String jsonStr = JSON.toJSONString(userBean);
        HttpUtils.post(ApiConstants.REGISTER_API, jsonStr, callback);
    }

    public void getUserInfo() {

    }
}
