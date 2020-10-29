package top.devonte.note.net;

import okhttp3.Callback;
import top.devonte.note.constant.ApiConstants;
import top.devonte.note.util.HttpUtils;

public class FileModel {

    private static FileModel instance = new FileModel();

    private FileModel() {
    }

    public static FileModel getInstance() {
        return instance;
    }

    public void getFiles(int page, Callback callback) {
        HttpUtils.get(ApiConstants.GET_FILES_API + "/" + page, callback);
    }

    public void getFolders(int page, Callback callback) {
        HttpUtils.get(ApiConstants.GET_FOLDERS_API + "/" + page, callback);
    }

    public void getFile(long id, Callback callback) {
        HttpUtils.get(ApiConstants.GET_FILE_API + "/" + id, callback);
    }
}
