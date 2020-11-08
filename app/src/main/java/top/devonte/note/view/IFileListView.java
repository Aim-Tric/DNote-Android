package top.devonte.note.view;

import java.io.File;
import java.util.List;

import top.devonte.note.base.IPresenter;
import top.devonte.note.bean.FileBean;

public interface IFileListView extends IPresenter.IView {

    void loading();

    void loadComplete();

    void loadFailure();

    void flushList(List<FileBean> fileBeans);

    void flushList(FileBean fileBean);

    void deleteItem(long id);

    void downloadComplete(File f);

}
