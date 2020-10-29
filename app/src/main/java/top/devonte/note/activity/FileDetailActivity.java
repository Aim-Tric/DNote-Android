package top.devonte.note.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import top.devonte.note.R;
import top.devonte.note.base.BaseCallBack;
import top.devonte.note.bean.FileBean;
import top.devonte.note.net.FileModel;

public class FileDetailActivity extends AppCompatActivity {

    private static final String TAG = "FileDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        Intent intent = getIntent();
        long id = intent.getLongExtra("ID", -1);
        TextView title = findViewById(R.id.file_detail_title);
        TextView content = findViewById(R.id.file_detail_content);
        FileModel.getInstance().getFile(id, new BaseCallBack(this) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonStr = response.body().string();
                Log.d(TAG, jsonStr);
                FileBean bean = JSON.parseObject(jsonStr, FileBean.class);
                title.setText(bean.getTitle());
                content.setText(bean.getBody());
            }
        });
    }
}
