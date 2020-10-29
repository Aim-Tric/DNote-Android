package top.devonte.note.activity.ui.folder;

import android.widget.AbsListView;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import top.devonte.note.activity.ui.file.FileListAdapter;
import top.devonte.note.base.BaseCallBack;
import top.devonte.note.base.InfinityList;
import top.devonte.note.bean.FileBean;
import top.devonte.note.net.FileModel;

public class FolderListOnScrollerListener implements AbsListView.OnScrollListener {

    private InfinityList fileList;
    private FolderListAdapter fileListAdapter;
    private boolean loading;
    private int totalItemCount;
    private int page = 1;

    public FolderListOnScrollerListener(InfinityList fileList, FolderListAdapter fileListAdapter) {
        this.fileList = fileList;
        this.fileListAdapter = fileListAdapter;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int lastVisibleIndex = view.getLastVisiblePosition();
        if (!loading && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex == totalItemCount - 1) {
            fileList.loading();
            loading = true;
            FileModel.getInstance().getFolders(page, new BaseCallBack(fileList.getContext()) {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    super.onFailure(call, e);
                    fileList.getRootView().post(() -> fileList.loadComplete());
                    if (page > 1) {
                        page--;
                    }
                    loading = false;
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String jsonStr = response.body().string();
                    List<FileBean> resultBean = JSON.parseArray(jsonStr, FileBean.class);
                    fileList.getRootView().post(() -> {
                        fileListAdapter.update(resultBean);
                        fileList.loadComplete();
                    });
                    loading = false;
                }
            });
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }
}
