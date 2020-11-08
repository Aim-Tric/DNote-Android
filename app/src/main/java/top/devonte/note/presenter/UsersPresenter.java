package top.devonte.note.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import top.devonte.note.base.BaseCallBack;
import top.devonte.note.base.IPresenter;
import top.devonte.note.constant.ApiConstants;
import top.devonte.note.model.IUserInfoModel;
import top.devonte.note.util.HttpUtils;
import top.devonte.note.view.IUserInfoView;

public class UsersPresenter implements IPresenter {

    private IUserInfoView view;
    private IUserInfoModel model;

    public UsersPresenter(IUserInfoView view) {
        this.view = view;
    }

    public void logout() {
        HttpUtils.get(ApiConstants.LOG_OUT_API, new BaseCallBack<IView>(view) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                view.logout();
            }
        });
    }

    public void initAnalyticData() {
        HttpUtils.get(ApiConstants.ANALYTIC_USER_INFO_API, new BaseCallBack<IView>(view) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(json);
                view.freshAnalyticData(jsonObject.getInnerMap());
            }
        });
    }
}
