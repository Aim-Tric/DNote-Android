package top.devonte.note.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment<T extends IPresenter> extends Fragment implements IPresenter.IView {

    protected T presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter = initPresenter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void toast(String msg) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        });
    }

    protected abstract T initPresenter();

}
