package top.devonte.note.presenter;

import android.os.Build;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import top.devonte.note.base.BaseCallBack;
import top.devonte.note.base.IPresenter;
import top.devonte.note.bean.FileBean;
import top.devonte.note.bean.ResultBean;
import top.devonte.note.constant.ApiConstants;
import top.devonte.note.util.HttpUtils;
import top.devonte.note.view.IFileListView;

public class FilePresenter implements IPresenter {

    private IFileListView view;
    private int currentFolder = 0;
    private LinkedList<Integer> history;

    public FilePresenter(IFileListView fileListView) {
        this.view = fileListView;
        history = new LinkedList<>();
        history.add(0);
    }

    public void initData() {
        loadData(currentFolder);
    }

    public void loadData(int folderId) {
        HttpUtils.get(ApiConstants.GET_FILES_API + "/" + folderId, new BaseCallBack<IView>(view) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                String jsonStr = response.body().string();
                ResultBean result = JSON.parseObject(jsonStr, ResultBean.class);
                if (result.getCode() == 10000) {
                    List<FileBean> resultBean = ((JSONArray) result.getData()).toJavaList(FileBean.class);
                    LinkedList<FileBean> newResultBean = new LinkedList<>();
                    if (folderId > 0) {
                        if (history.size() > 0) {
                            if (folderId == history.getLast()) {
                                history.pollLast();
                            } else if (currentFolder != 0) {
                                history.addLast(currentFolder);
                            }
                        }
                        FileBean goBack = new FileBean();
                        goBack.setTitle("返回上一级");
                        goBack.setFoldered(true);
                        goBack.setId(history.getLast());
                        newResultBean.add(goBack);
                        newResultBean.addAll(resultBean);
                        resultBean = newResultBean;
                    }
                    currentFolder = folderId;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        resultBean.sort((o1, o2) -> {
                            if ("返回上一级".equals(o1.getTitle())) {
                                return -1;
                            }
                            if ("返回上一级".equals(o2.getTitle())) {
                                return 1;
                            }
                            if (o1.isFoldered()) {
                                return -1;
                            }
                            if (o2.isFoldered()) {
                                return 1;
                            }
                            return 0;
                        });
                    }
                    view.flushList(resultBean);
                    view.loadComplete();
                } else {
                    view.toast(result.getMsg());
                }
            }
        });
    }

    public void rename(long id, String newName) {
        Map<String, String> datas = new HashMap<>();
        datas.put("id", String.valueOf(id));
        datas.put("title", newName);
        datas.put("topping", "false");
        HttpUtils.put(ApiConstants.RESTFUL_FILE_API, JSON.toJSONString(datas), new BaseCallBack<IView>(view) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                ResultBean result = JSON.parseObject(json, ResultBean.class);
                loadData(currentFolder);
                view.toast(result.getMsg());
            }
        });
    }

    public void addFile(boolean foldered) {
        Date now = new Date();
        FileBean bean = new FileBean();
        bean.setTitle(foldered ? "未命名文件夹" : "未命名笔记");
        bean.setFolderId(currentFolder);
        bean.setFoldered(foldered);
        bean.setType(1);
        bean.setCreated(now);
        bean.setUpdated(now);
        HttpUtils.post(ApiConstants.RESTFUL_FILE_API, JSON.toJSONString(bean), new BaseCallBack<IView>(view) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                ResultBean result = JSON.parseObject(json, ResultBean.class);
                FileBean file = ((JSONObject) result.getData()).toJavaObject(FileBean.class);
                view.flushList(file);
                view.toast("未命名文件创建成功");
            }
        });
    }

    public void deleteFile(long id) {
        HttpUtils.delete(ApiConstants.RESTFUL_FILE_API + "/" + id, new BaseCallBack<IView>(view) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                ResultBean result = JSON.parseObject(json, ResultBean.class);
                view.deleteItem(id);
                view.toast(result.getMsg());
            }
        });
    }

}
