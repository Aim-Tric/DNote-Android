package top.devonte.note.activity.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import top.devonte.note.R;
import top.devonte.note.activity.SettingActivity;

public class UsersFragment extends Fragment {

    private static final String TAG = "UsersFragment";

    private UsersViewModel usersViewModel;

    private ImageView avatarWidget;
    private TextView usernameWidget;
    private TextView joinTimeWidget;
    private ListView btnsWidget;

    private String[] list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mine, container, false);

        init(root, container);

        return root;
    }

    private void init(View root, ViewGroup container) {
        usersViewModel =
                ViewModelProviders.of(this).get(UsersViewModel.class);

        list = new String[]{
                "清空文档", "设置"
        };

        CallBack[] callBacks = new CallBack[]{
                v -> {

                },
                v -> {
                    Intent intent = new Intent(getContext(), SettingActivity.class);
                    startActivity(intent);
                }
        };

        avatarWidget = root.findViewById(R.id.home_user_avatar);
        usernameWidget = root.findViewById(R.id.home_username);
        joinTimeWidget = root.findViewById(R.id.home_join_time);
        btnsWidget = root.findViewById(R.id.user_setting);

        btnsWidget.setAdapter(new UsersBtnAdapter(getContext(), list));

        btnsWidget.setOnItemClickListener((parent, view, position, id) -> {
            callBacks[position].call(view);
        });
    }

    private void startup() {

    }

    private interface CallBack {
        void call(View v);
    }

    private class UsersBtnAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private String[] list;

        public UsersBtnAdapter(Context context, String[] list) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public Object getItem(int position) {
            return list[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.fragment_user_btn, null);
                viewHolder = new ViewHolder();
                viewHolder.text = convertView.findViewById(R.id.user_setting_btn);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.text.setText(list[position]);

            return convertView;
        }
    }

    private class ViewHolder {
        TextView text;
    }
}