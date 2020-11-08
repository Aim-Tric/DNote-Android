package top.devonte.note.presenter;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import top.devonte.note.base.BaseCallBack;
import top.devonte.note.base.IPresenter;
import top.devonte.note.bean.FileBean;
import top.devonte.note.constant.ApiConstants;
import top.devonte.note.util.HttpUtils;
import top.devonte.note.view.IFileDetailView;

public class FileDetailPresenter implements IPresenter {

    private IFileDetailView view;

    public FileDetailPresenter(IFileDetailView view) {
        this.view = view;
    }

    public void initData(long id) {
        HttpUtils.get(ApiConstants.RESTFUL_FILE_API + "/" + id, new BaseCallBack<IView>(view) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonStr = response.body().string();
                FileBean bean = JSON.parseObject(jsonStr, FileBean.class);
                view.loadContent(bean);
            }
        });
    }

    public void updateDetail(long id, String title, String content) {
        Map<String, Object> datas = new HashMap<>();
        datas.put("id", id);
        datas.put("title", title);
        datas.put("body", content);
        datas.put("topping", false);
        HttpUtils.put(ApiConstants.RESTFUL_FILE_API, JSON.toJSONString(datas), new BaseCallBack<IView>(view) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                view.toast(response.body().string());
                view.updateComplete();
            }
        });
    }
}
