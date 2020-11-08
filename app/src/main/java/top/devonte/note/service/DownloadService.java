package top.devonte.note.service;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class DownloadService extends IntentService {

    private String TAG = "DownloadService";
    public static final String BROADCAST_ACTION =
            "top.devonte.note.broadcast.ACTION";
    public static final String EXTENDED_DATA_STATUS =
            "top.devonte.note.download.STATUS";

    private LocalBroadcastManager mLocalBroadcastManager;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String url = intent.getStringExtra("url");
        String cookie = intent.getStringExtra("cookie");
        String fileName = intent.getStringExtra("fileName");

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, fileName + ".doc");
        request.addRequestHeader("Cookie", cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(fileName);
        request.setAllowedOverRoaming(false);
        long requestId = downloadManager.enqueue(request);

        Intent localIntent = new Intent(BROADCAST_ACTION);
        localIntent.putExtra("fileName", fileName + ".doc");
        localIntent.putExtra(EXTENDED_DATA_STATUS, requestId);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(requestId);
        try {
            boolean isGoging = true;
            while (isGoging) {
                Cursor cursor = downloadManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (status) {
                        case DownloadManager.STATUS_SUCCESSFUL:
                            isGoging = false;
                            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
                            mLocalBroadcastManager.sendBroadcast(localIntent);
                            break;
                    }
                }

                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
