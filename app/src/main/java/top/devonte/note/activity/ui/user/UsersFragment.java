package top.devonte.note.activity.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

import top.devonte.note.R;
import top.devonte.note.activity.LoginActivity;
import top.devonte.note.activity.SettingActivity;
import top.devonte.note.base.BaseFragment;
import top.devonte.note.bean.UserBean;
import top.devonte.note.presenter.UsersPresenter;
import top.devonte.note.view.IUserInfoView;

public class UsersFragment extends BaseFragment<UsersPresenter> implements IUserInfoView {

    private static final String TAG = "UsersFragment";

    private ImageView avatarWidget;
    private TextView usernameWidget;
    private TextView joinTimeWidget;
    private TextView fileCountWidget;
    private TextView folderCountWidget;
    private ListView btnsWidget;

    private String[] list = new String[]{
            "设置", "退出登录"
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_mine, container, false);

        init(root, container);

        return root;
    }

    @Override
    protected UsersPresenter initPresenter() {
        return new UsersPresenter(this);
    }

    private void init(View root, ViewGroup container) {
        CallBack[] callBacks = getCallBacks();
        avatarWidget = root.findViewById(R.id.home_user_avatar);
        usernameWidget = root.findViewById(R.id.home_username);
        joinTimeWidget = root.findViewById(R.id.home_join_time);
        fileCountWidget = root.findViewById(R.id.analytic_file_count);
        folderCountWidget = root.findViewById(R.id.analytic_folder_count);
        btnsWidget = root.findViewById(R.id.user_setting);

        btnsWidget.setAdapter(new ArrayAdapter<>(root.getContext(), R.layout.fragment_user_btn, list));

        btnsWidget.setOnItemClickListener((parent, view, position, id) -> {
            callBacks[position].call(view);
        });

        SharedPreferences noteuser = getActivity().getSharedPreferences("NOTE", Context.MODE_PRIVATE);
        if(noteuser.contains("userInfo")) {
            String json = noteuser.getString("userInfo", "{}");
            UserBean userBean = JSONObject.parseObject(json, UserBean.class);
            usernameWidget.setText(userBean.getNickName());
        }

        presenter.initAnalyticData();
    }

    private CallBack[] getCallBacks() {
        return new CallBack[]{
                v -> getActivity().runOnUiThread(() -> {
                    Intent intent = new Intent(getContext(), SettingActivity.class);
                    startActivity(intent);
                }),
                v -> presenter.logout()
        };
    }

    @Override
    public void logout() {
        getActivity().finish();
    }

    @Override
    public void freshAnalyticData(Map<String, Object> data) {
        getActivity().runOnUiThread(() -> {
            joinTimeWidget.setText(String.format("已使用 %s 天", data.get("days").toString()));
            fileCountWidget.setText(data.get("files").toString());
            folderCountWidget.setText(data.get("folders").toString());
        });
    }


    private interface CallBack {
        void call(View v);
    }

}