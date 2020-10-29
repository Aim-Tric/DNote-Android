package top.devonte.note.activity.ui.folder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import top.devonte.note.R;
import top.devonte.note.bean.FileBean;

public class FolderListAdapter extends BaseAdapter {

    private List<FileBean> fileBeans;

    public FolderListAdapter(List<FileBean> fileBeans) {
        this.fileBeans = fileBeans;
    }

    @Override
    public int getCount() {
        return fileBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return fileBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return fileBeans.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_folder_item, null);
            viewHolder = new FileViewHolder();
            viewHolder.title = convertView.findViewById(R.id.folder_item_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FileViewHolder) convertView.getTag();
        }

        FileBean fileBean = fileBeans.get(position);
        viewHolder.title.setText(fileBean.getTitle());
        return convertView;
    }

    public void update(List<FileBean> beans) {
        this.fileBeans.addAll(beans);
        notifyDataSetChanged();
    }

    private class FileViewHolder {
        private TextView title;
    }
}
