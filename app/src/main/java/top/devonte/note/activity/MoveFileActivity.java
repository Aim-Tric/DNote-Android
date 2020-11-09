package top.devonte.note.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.devonte.note.R;
import top.devonte.note.bean.FileBean;
import top.devonte.note.bean.ResultBean;
import top.devonte.note.constant.ApiConstants;
import top.devonte.note.constant.Constants;
import top.devonte.note.util.HttpUtils;

/**
 * 移动文档位置的Activity类
 */
public class MoveFileActivity extends AppCompatActivity {

    private ListView listView;
    private Button folder;
    private Button confirm;

    private int currentFolder = 0;
    private LinkedList<Integer> history;
    private List<FileBean> folders;
    private ListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("移动文件");
        setContentView(R.layout.activity_move_file);
        history = new LinkedList<>();
        folders = new ArrayList<>();
        listView = findViewById(R.id.move_file_folder_list);
        folder = findViewById(R.id.move_file_selected_path);
        confirm = findViewById(R.id.move_file_confirm);
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);
        currentFolder = intent.getIntExtra("folderId", 0);
        history.add(0);
        if (id == -1) {
            Toast.makeText(this, "invalid params", Toast.LENGTH_SHORT).show();
        }
        loadData(0);
        adapter = new ListAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id1) -> {
            FileBean f = folders.get(position);
            folder.setText(String.format("当前位置: %s", f.getTitle()));
            loadData(f.getId());
        });
        confirm.setOnClickListener(v -> move(id));
    }

    private void loadData(int folderId) {
        HttpUtils.get(ApiConstants.GET_FOLDERS_API + "/" + folderId, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> Toast.makeText(MoveFileActivity.this,
                        Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show());
            }

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
                    }
                    currentFolder = folderId;
                    newResultBean.addAll(resultBean);
                    folders = newResultBean;
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                } else {
                    toast(result.getMsg());
                }
            }
        });
    }

    private void move(long id) {
        Map<String, Object> datas = new HashMap<>();
        datas.put("id", id);
        datas.put("folderId", currentFolder);
        datas.put("topping", false);
        HttpUtils.put(ApiConstants.RESTFUL_FILE_API, JSON.toJSONString(datas), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(()
                        -> Toast.makeText(MoveFileActivity.this,
                        Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonStr = response.body().string();
                ResultBean result = JSON.parseObject(jsonStr, ResultBean.class);
                toast(result.getMsg());
                if (result.getCode() == 10000) {
                    finishActivity(1);
                }
                finish();
            }
        });
    }

    private void toast(String msg) {
        runOnUiThread(() -> Toast.makeText(MoveFileActivity.this,
                msg, Toast.LENGTH_SHORT).show());
    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return folders.size();
        }

        @Override
        public Object getItem(int position) {
            return folders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return folders.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FileBean fileBean = folders.get(position);
            FolderViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_folder_item, null);
                viewHolder = new FolderViewHolder();
                viewHolder.title = convertView.findViewById(R.id.folder_item_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (FolderViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(fileBean.getTitle());
            return convertView;
        }

        private class FolderViewHolder {
            private TextView title;
        }
    }
}
