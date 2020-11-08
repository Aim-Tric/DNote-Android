package top.devonte.note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.yuruiyin.richeditor.RichEditText;

import java.util.HashMap;
import java.util.Map;

import top.devonte.note.R;
import top.devonte.note.base.BaseActivity;
import top.devonte.note.bean.FileBean;
import top.devonte.note.presenter.FileDetailPresenter;
import top.devonte.note.view.IFileDetailView;

public class FileDetailActivity extends BaseActivity<FileDetailPresenter> implements IFileDetailView {

    private static final String TAG = "FileDetailActivity";
    private boolean editable = false;
    private TextView title;
    private TextView content;
    private EditText editTitle;
    private RichEditText editContent;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        id = intent.getLongExtra("ID", -1);
        editable = intent.getBooleanExtra("EDITABLE", false);
        title = findViewById(R.id.file_detail_title);
        content = findViewById(R.id.file_detail_content);
        presenter.initData(id);
    }

    @Override
    protected FileDetailPresenter initPresenter() {
        return new FileDetailPresenter(this);
    }

    @Override
    public void loadContent(FileBean bean) {
        runOnUiThread(() -> {
            if (editable) {
                setContentView(R.layout.activity_file_detial_edit);
                editTitle = findViewById(R.id.edit_title);
                editContent = findViewById(R.id.edit_content);
                editTitle.setText(bean.getTitle());
                if (bean.getBody() != null) {
                    editContent.setText(bean.getBody().replaceAll("\\[\\(br\\)]", "\n"));
                }
            } else {
                setContentView(R.layout.activity_file_detail);
                title = findViewById(R.id.file_detail_title);
                content = findViewById(R.id.file_detail_content);
                title.setText(bean.getTitle());
                if (bean.getBody() != null) {
                    content.setText(bean.getBody().replaceAll("\\[\\(br\\)]", "\n"));
                }
            }
        });
    }

    @Override
    public void updateComplete() {
        runOnUiThread(() -> {
            getIntent().putExtra("EDITABLE", false);
            recreate();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (editable) {
            menu.add(0, 0, 0, "保存")
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else {
            menu.add(0, 1, 0, "编辑")
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                String title = editTitle.getText().toString();
                String content = editContent.getText().toString();
                presenter.updateDetail(id, title, content);
                break;
            case 1:
                runOnUiThread(() -> {
                    getIntent().putExtra("EDITABLE", true);
                    recreate();
                });
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
