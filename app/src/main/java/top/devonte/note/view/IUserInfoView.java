package top.devonte.note.view;

import java.util.Map;

import top.devonte.note.base.IPresenter;

public interface IUserInfoView extends IPresenter.IView {

    void logout();

    void freshAnalyticData(Map<String, Object> data);

}
