package top.devonte.note.activity.ui.file;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import top.devonte.note.R;
import top.devonte.note.bean.FileBean;

public class FileListAdapter extends BaseAdapter {

    private List<FileBean> fileBeans;
    private DateFormat dateFormat = SimpleDateFormat.getDateInstance();

    public FileListAdapter(List<FileBean> fileBeans) {
        this.fileBeans = fileBeans;
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
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
    public int getItemViewType(int position) {
        FileBean fileBean = fileBeans.get(position);
        if (fileBean.isFoldered()) {
            return R.id.fragment_folder_item;
        }
        return R.id.fragment_file_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileBean fileBean = fileBeans.get(position);
        int type = getItemViewType(position);
        if (convertView == null || convertView.getId() != type) {
            switch (type) {
                case R.id.fragment_file_item:
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_file_item, null);
                    FileViewHolder fileViewHolder = new FileViewHolder();
                    fileViewHolder.title = convertView.findViewById(R.id.file_item_title);
                    fileViewHolder.brief = convertView.findViewById(R.id.file_item_brief);
                    fileViewHolder.created = convertView.findViewById(R.id.file_item_created);
                    convertView.setTag(fileViewHolder);
                    break;
                case R.id.fragment_folder_item:
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_folder_item, null);
                    FolderViewHolder folderViewHolder = new FolderViewHolder();
                    folderViewHolder.title = convertView.findViewById(R.id.folder_item_title);
                    convertView.setTag(folderViewHolder);
                    break;
            }
        }
        switch (type) {
            case R.id.fragment_file_item:
                FileViewHolder fileViewHolder = (FileViewHolder) convertView.getTag();
                fileViewHolder.title.setText(fileBean.getTitle());
                if (fileBean.getBody() != null) {
                    fileViewHolder.brief.setText(fileBean.getBody()
                            .replaceAll("\\[\\(br\\)]", "\n"));
                }
                fileViewHolder.created.setText(dateFormat.format(fileBean.getCreated()));
                break;
            case R.id.fragment_folder_item:
                FolderViewHolder folderViewHolder = (FolderViewHolder) convertView.getTag();
                folderViewHolder.title.setText(fileBean.getTitle());
                break;
        }
        return convertView;
    }

    public void update(List<FileBean> beans) {
        this.fileBeans = beans;
        notifyDataSetChanged();
    }

    public void add(FileBean bean) {
        this.fileBeans.add(bean);
        notifyDataSetChanged();
    }

    public void delete(long id) {
        Iterator<FileBean> iterator = fileBeans.iterator();
        while (iterator.hasNext()) {
            FileBean next = iterator.next();
            if (next.getId() == id) {
                iterator.remove();
                notifyDataSetChanged();
            }
        }
    }

    public FileBean get(long id) {
        Iterator<FileBean> iterator = fileBeans.iterator();
        while (iterator.hasNext()) {
            FileBean next = iterator.next();
            if (next.getId() == id) {
                return next;
            }
        }
        return null;
    }

    private class FileViewHolder {
        private TextView title;
        private TextView brief;
        private TextView created;
    }

    private class FolderViewHolder {
        private TextView title;
    }
}
