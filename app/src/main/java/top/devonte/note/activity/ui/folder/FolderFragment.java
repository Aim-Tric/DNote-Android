package top.devonte.note.activity.ui.folder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import top.devonte.note.R;
import top.devonte.note.base.BaseCallBack;
import top.devonte.note.base.InfinityList;
import top.devonte.note.bean.FileBean;
import top.devonte.note.net.FileModel;

public class FolderFragment extends Fragment {

    private static final String TAG = "FolderFragment";

    private FolderListAdapter adapter;
    private InfinityList folderList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_folder, container, false);
        folderList = root.findViewById(R.id.folder_list);

        adapter = new FolderListAdapter(new ArrayList<>());
        folderList.setAdapter(adapter);
        folderList.setOnScrollListener(new FolderListOnScrollerListener(folderList, adapter));

        FileModel.getInstance().getFolders(1, new BaseCallBack(getContext()) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonStr = response.body().string();
                List<FileBean> resultBean = JSON.parseArray(jsonStr, FileBean.class);
                root.post(() -> {
                    adapter.update(resultBean);
                });
            }
        });

        return root;
    }
}