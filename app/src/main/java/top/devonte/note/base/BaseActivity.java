package top.devonte.note.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity<T extends IPresenter> extends AppCompatActivity implements IPresenter.IView {

    protected T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();

    }

    @Override
    public void toast(String msg) {
        runOnUiThread(() -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });
    }

    protected abstract T initPresenter();
}
