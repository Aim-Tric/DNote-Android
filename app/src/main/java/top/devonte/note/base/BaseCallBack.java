package top.devonte.note.base;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

public abstract class BaseCallBack implements Callback {

    private Context context;

    public BaseCallBack(Context context) {
        this.context = context;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.d("BASE", e.getMessage());
        Looper.prepare();
        Toast.makeText(context,
                e.getMessage(),
                Toast.LENGTH_LONG)
                .show();
        Looper.loop();
    }
}
