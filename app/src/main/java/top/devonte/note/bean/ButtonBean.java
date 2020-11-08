package top.devonte.note.bean;

import android.view.View;

public class ButtonBean {

    private String name;
    private Integer type;
    private CallBack callBack;

    public ButtonBean(String name, CallBack callBack) {
        this.name = name;
        this.callBack = callBack;
    }

    public ButtonBean(String name, Integer type, CallBack callBack) {
        this.name = name;
        this.type = type;
        this.callBack = callBack;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public interface CallBack {
        void callback(View view);
    }
}
