package top.devonte.note.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import top.devonte.note.R;
import top.devonte.note.bean.ButtonBean;

/**
 * 配置界面Activity类
 */
public class SettingActivity extends AppCompatActivity {

    private static final int CHECKBOX = 0;

    private static final String TAG = "SettingActivity";
    private List<ButtonBean> buttons;
    private ListView listView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置");
        setContentView(R.layout.activity_setting);
        listView = findViewById(R.id.setting_items);
        initSettingButtons();
        adapter = new ListAdapter();
        listView.setAdapter(adapter);
    }

    /**
     * 新增配置按键在此添加按钮名称、类型及回调事件
     */
    private void initSettingButtons() {
        buttons = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("NOTE", MODE_PRIVATE);

        ButtonBean autoOpen = new ButtonBean("下载完毕自动打开", CHECKBOX, v -> {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            CheckBox checkBox = (CheckBox) v;
            boolean checked = checkBox.isChecked();
            edit.putBoolean("autoOpen", checked);
            edit.apply();
        });



        buttons.add(autoOpen);

    }

    /**
     * 目前只有checkbox类型，有待完善
     */
    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return buttons.size();
        }

        @Override
        public Object getItem(int position) {
            return buttons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_setting_checkbox_item, null);
                viewHolder = new ViewHolder();
                viewHolder.layout = convertView.findViewById(R.id.setting_item_layout);
                viewHolder.label = convertView.findViewById(R.id.setting_item_label);
                viewHolder.fn = convertView.findViewById(R.id.setting_item_fn);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ButtonBean buttonBean = buttons.get(position);
            viewHolder.label.setText(buttonBean.getName());
            View view = getView(buttonBean.getType());
            if (view != null) {
                view.setOnClickListener(v -> buttonBean.getCallBack().callback(v));
                viewHolder.layout.removeView(viewHolder.fn);
                viewHolder.layout.addView(view);
            }
            return convertView;
        }

        private View getView(Integer type) {
            View view = null;
            switch (type) {
                case CHECKBOX:
                    view = new CheckBox(SettingActivity.this);
                default:
                    break;
            }
            return view;
        }

        private class ViewHolder {
            GridLayout layout;
            TextView label;
            View fn;
        }
    }


}
