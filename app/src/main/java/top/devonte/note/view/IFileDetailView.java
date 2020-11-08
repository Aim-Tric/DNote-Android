package top.devonte.note.view;

import top.devonte.note.base.IPresenter;
import top.devonte.note.bean.FileBean;

public interface IFileDetailView extends IPresenter.IView {

    void loadContent(FileBean file);

    void updateComplete();

}
