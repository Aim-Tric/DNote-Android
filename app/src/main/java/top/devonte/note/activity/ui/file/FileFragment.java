package top.devonte.note.activity.ui.file;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import top.devonte.note.R;
import top.devonte.note.activity.FileDetailActivity;
import top.devonte.note.activity.MainActivity;
import top.devonte.note.activity.MoveFileActivity;
import top.devonte.note.base.BaseFragment;
import top.devonte.note.component.InfinityList;
import top.devonte.note.bean.FileBean;
import top.devonte.note.constant.ApiConstants;
import top.devonte.note.constant.Constants;
import top.devonte.note.presenter.FilePresenter;
import top.devonte.note.service.DownloadService;
import top.devonte.note.util.HttpUtils;
import top.devonte.note.view.IFileListView;

public class FileFragment extends BaseFragment<FilePresenter> implements IFileListView {

    private static final String TAG = "FileFragment";
    private static final int MENU_ID_ADD = 0;
    private static final int MENU_ID_EDIT = 1;
    private static final int MENU_ID_DELETE = 2;
    private static final int MENU_ID_RENAME = 3;
    private static final int MENU_ID_DOWNLOAD = 4;
    private static final int MENU_ID_MOVE = 5;

    private FileListAdapter adapter;
    private InfinityList filesWidget;
    private boolean loading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_file, container, false);

        init(root, container);

        presenter.initData();

        return root;
    }

    @Override
    protected FilePresenter initPresenter() {
        return new FilePresenter(this);
    }

    private void init(View root, ViewGroup container) {
        filesWidget = root.findViewById(R.id.home_files);
        adapter = new FileListAdapter(new ArrayList<>());
        filesWidget.setAdapter(adapter);
        filesWidget.setOnItemClickListener((parent, view, position, id) -> {
            int layoutId = view.getId();
            switch (layoutId) {
                case R.id.fragment_file_item:
                    intentToFileDetail(id, false);
                    break;
                case R.id.fragment_folder_item:
                    presenter.loadData((int) id);
                    break;
            }
        });
        filesWidget.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            int id = info.targetView.getId();
            if (id == R.id.fragment_file_item) {
                menu.add(0, MENU_ID_ADD, 0, "新建文档");
                menu.add(0, MENU_ID_EDIT, 0, "编辑");
                menu.add(0, MENU_ID_MOVE, 0, "移动到");
                menu.add(0, MENU_ID_DOWNLOAD, 0, "下载");
            }
            menu.add(0, MENU_ID_DELETE, 0, "删除");
            menu.add(0, MENU_ID_RENAME, 0, "重命名");
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.file_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.file_list_menu_add_file:
                presenter.addFile(false);
                break;
            case R.id.file_list_menu_add_folder:
                presenter.addFile(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        long itemId = info.id;
        switch (item.getItemId()) {
            case MENU_ID_ADD:
                presenter.addFile(false);
                break;
            case MENU_ID_EDIT:
                intentToFileDetail(itemId, true);
                break;
            case MENU_ID_DELETE:
                presenter.deleteFile(itemId);
                break;
            case MENU_ID_RENAME:
                showRenameDialog(itemId);
                break;
            case MENU_ID_DOWNLOAD:
                download(itemId);
                break;
            case MENU_ID_MOVE:
                moveFile(itemId);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void loading() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            loading = true;
            filesWidget.loading();
        });
    }

    @Override
    public void loadComplete() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            loading = false;
            filesWidget.loadComplete();
        });
    }

    @Override
    public void loadFailure() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            loading = false;
        });
    }

    @Override
    public void flushList(List<FileBean> fileBeans) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            adapter.update(fileBeans);
        });
    }

    @Override
    public void flushList(FileBean fileBeans) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            adapter.add(fileBeans);
        });
    }

    @Override
    public void deleteItem(long id) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            adapter.delete(id);
        });
    }

    @Override
    public void downloadComplete(File f) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(getActivity());
            normalDialog.setTitle("提示");
            normalDialog.setMessage("文件已经下载完成，是否查看?");
            normalDialog.setPositiveButton("打开", (dialog, which) -> {
                Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW);
                String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(f).toString());
                String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                myIntent.setDataAndType(Uri.fromFile(f), mimetype);
                Objects.requireNonNull(getActivity()).startActivity(myIntent);
            });
            normalDialog.setNegativeButton("不打开", (dialog, which) -> dialog.cancel());
            normalDialog.show();
        });
    }

    private void intentToFileDetail(long id, boolean editable) {
        Intent intent = new Intent(getContext(), FileDetailActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("EDITABLE", editable);
        startActivity(intent);
    }

    private void showRenameDialog(long id) {
        final EditText editText = new EditText(getActivity());
        editText.setText(adapter.get(id).getTitle());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        inputDialog.setTitle("重命名").setView(editText);
        inputDialog.setPositiveButton("确定",
                (dialog, which) -> {
                    String newName = editText.getText().toString();
                    presenter.rename(id, newName);
                }).show();
    }

    private void download(long id) {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        // 验证是否许可权限
        for (String str : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Objects.requireNonNull(getActivity()).checkSelfPermission(str)
                        != PackageManager.PERMISSION_GRANTED) {
                    getActivity().requestPermissions(permissions, 101);
                    break;
                } else {
                    FileBean fileInfo = adapter.get(id);
                    String downloadUrl = ApiConstants.DOWNLOAD_FILE_API + "/" + id;

                    Intent downloadIntent = new Intent(getActivity(), DownloadService.class);
                    downloadIntent.putExtra("url", downloadUrl);
                    downloadIntent.putExtra("fileName", fileInfo.getTitle());

                    List<Cookie> cookies = HttpUtils.client.cookieJar()
                            .loadForRequest(HttpUrl.parse(downloadUrl));
                    for (Cookie c : cookies) {
                        if ("D_NOTE".equals(c.name())) {
                            downloadIntent.putExtra("cookie", c.toString());
                            break;
                        }
                    }
                    toast("开始下载文件");
                    getActivity().startService(downloadIntent);
                }
            }
        }
    }

    private void moveFile(long itemId) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            Intent intent = new Intent(getActivity(), MoveFileActivity.class);
            intent.putExtra("id", itemId);
            intent.putExtra("folderId", presenter.getCurrentFolder());
            startActivityForResult(intent, 0);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0) {
            presenter.initData();
        }
    }
}