package top.devonte.note.base;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

public abstract class BaseCallBack<T extends IPresenter.IView> implements Callback {

    private T view;

    public BaseCallBack() {}

    public BaseCallBack(T view) {
        this.view = view;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        view.toast(e.getMessage());
    }
}
