package hpiz.reaction.com.reaction;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chris on 5/11/2017.
 */


public class ColorSpinnerAdapter extends BaseAdapter {
    ArrayList<String> colors;
    Context context;

    public ColorSpinnerAdapter(Context context, boolean top) {
        this.context = context;
        colors = new ArrayList<String>();
        String retrieve[] = null;
        if (top) {
            retrieve = context.getResources().getStringArray(R.array.topColors);
        } else {
            retrieve = context.getResources().getStringArray(R.array.bottomColors);
        }
        for (String re : retrieve) {
            colors.add(re);
        }
    }

    @Override
    public int getCount() {
        return colors.size();
    }

    @Override
    public Object getItem(int arg0) {
        return colors.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        TextView txv = (TextView) view.findViewById(android.R.id.text1);
        txv.setBackgroundColor(Color.parseColor(colors.get(pos)));
        txv.setTextColor(Color.parseColor(colors.get(pos)));
        txv.setTextSize(85);
        txv.setText(" ");
        return view;
    }

}
