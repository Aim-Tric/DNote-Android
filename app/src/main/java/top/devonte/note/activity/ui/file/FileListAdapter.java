package top.devonte.note.activity.ui.file;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import top.devonte.note.R;
import top.devonte.note.bean.FileBean;

public class FileListAdapter extends BaseAdapter {

    private List<FileBean> fileBeans;
    private DateFormat dateFormat = SimpleDateFormat.getDateInstance();

    public FileListAdapter(List<FileBean> fileBeans) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_file_item, null);
            viewHolder = new FileViewHolder();
            viewHolder.title = convertView.findViewById(R.id.file_item_title);
            viewHolder.brief = convertView.findViewById(R.id.file_item_brief);
            viewHolder.created = convertView.findViewById(R.id.file_item_created);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FileViewHolder) convertView.getTag();
        }

        FileBean fileBean = fileBeans.get(position);
        viewHolder.title.setText(fileBean.getTitle());
        viewHolder.brief.setText(fileBean.getBody());
        viewHolder.created.setText(dateFormat.format(fileBean.getCreated()));
        return convertView;
    }

    public void update(List<FileBean> beans) {
        this.fileBeans.addAll(beans);
        notifyDataSetChanged();
    }

    private class FileViewHolder {
        private TextView title;
        private TextView brief;
        private TextView created;
    }
}
