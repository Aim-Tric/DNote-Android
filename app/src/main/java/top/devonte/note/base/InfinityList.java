package top.devonte.note.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import top.devonte.note.R;

public class InfinityList extends ListView {

    private View footView;

    public InfinityList(Context context) {
        super(context);
        init(context);
    }

    public InfinityList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        footView = LayoutInflater.from(context)
                .inflate(R.layout.fragment_loading, null);
    }

    public void loadComplete() {
        removeFooterView(footView);
    }

    public void loading() {
        addFooterView(footView);
    }

    @Override
    protected void handleDataChanged() {
        super.handleDataChanged();
        loadComplete();
    }

}
