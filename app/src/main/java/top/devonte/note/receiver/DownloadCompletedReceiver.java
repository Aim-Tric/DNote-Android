package top.devonte.note.receiver;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.File;

public class DownloadCompletedReceiver extends BroadcastReceiver {

    private Context ctx;

    public DownloadCompletedReceiver(Context context) {
        this.ctx = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences("NOTE", Context.MODE_PRIVATE);
        String fileName = intent.getStringExtra("fileName");
        if (sharedPreferences.contains("autoOpen")
                && sharedPreferences.getBoolean("autoOpen", false)) {
            open(fileName);
        } else {
            askFoOpen(fileName);
        }
    }

    private void askFoOpen(String fileName) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this.ctx);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("文件已经下载完成，是否查看?");
        normalDialog.setPositiveButton("打开", (dialog, which) -> open(fileName));
        normalDialog.setNegativeButton("不打开", (dialog, which) -> dialog.cancel());
        normalDialog.show();
    }

    private void open(String fileName) {
        Intent newIntent = new Intent();
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this.ctx, "top.devonte.note.fileprovider",
                    new File(Environment.getExternalStorageDirectory()
                            + File.separator + "Download" + File.separator + fileName));
        } else {
            uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                    + File.separator + "Download" + File.separator + fileName));
        }
        newIntent.setAction(Intent.ACTION_GET_CONTENT);
        newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        newIntent.setDataAndType(uri,
                "application/msword");
        this.ctx.startActivity(newIntent);
    }
}
