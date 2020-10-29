package top.devonte.note.activity.ui.file;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import top.devonte.note.R;
import top.devonte.note.activity.FileDetailActivity;
import top.devonte.note.base.BaseCallBack;
import top.devonte.note.base.InfinityList;
import top.devonte.note.bean.FileBean;
import top.devonte.note.net.FileModel;

public class FileFragment extends Fragment {

    private static final String TAG = "FileFragment";

    private FileListAdapter adapter;

    private InfinityList filesWidget;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_file, container, false);

        init(root, container);

        startup(root);

        return root;
    }

    private void init(View root, ViewGroup container) {
        filesWidget = root.findViewById(R.id.home_files);

        adapter = new FileListAdapter(new ArrayList<>());
        filesWidget.setAdapter(adapter);
        filesWidget.setOnScrollListener(new FileListOnScrollerListener(filesWidget, adapter));
        filesWidget.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), FileDetailActivity.class);
            intent.putExtra("ID", id);
            startActivity(intent);
        });
    }


    private void startup(View root) {
        FileModel.getInstance().getFiles(1, new BaseCallBack(getContext()) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonStr = response.body().string();
                List<FileBean> resultBean = JSON.parseArray(jsonStr, FileBean.class);
                root.post(() -> {
                    adapter.update(resultBean);
                });
            }
        });
    }

}