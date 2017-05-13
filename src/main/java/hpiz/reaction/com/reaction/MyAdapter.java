package hpiz.reaction.com.reaction;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Chris on 5/13/2017.
 */
public class MyAdapter extends BaseAdapter implements View.OnTouchListener {
    private List<String> mItems;

    MyAdapter(List<String> items) {
        mItems = items;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder vh;

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            view.setOnTouchListener(this);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }

        vh.text.setText(mItems.get(position));

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ViewHolder vh = (ViewHolder) v.getTag();

        vh.lastTouchedX = event.getX();
        vh.lastTouchedY = event.getY();

        return false;
    }

    public static class ViewHolder {
        public TextView text;
        public float lastTouchedX;
        public float lastTouchedY;

        public ViewHolder(View v) {
            text = (TextView) v;
        }
    }
}

