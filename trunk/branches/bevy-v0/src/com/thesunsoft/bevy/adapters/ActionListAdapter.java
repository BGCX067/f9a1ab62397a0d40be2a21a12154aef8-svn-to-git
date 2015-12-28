package com.thesunsoft.bevy.adapters;

import java.util.List;

import com.thesunsoft.bevy.R;
import com.thesunsoft.bevy.elements.BaseListElement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ActionListAdapter extends ArrayAdapter<BaseListElement> {
    private List<BaseListElement> listElements;
    private Context context;
    public ActionListAdapter(Context context, int resourceId, List<BaseListElement> listElements) {
        super(context, resourceId, listElements);
        this.context = context;
        this.listElements = listElements;
        for (int i = 0; i < listElements.size(); i++) {
            listElements.get(i).setAdapter(this);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cell_listitem, null);
        }

        BaseListElement listElement = listElements.get(position);
        if (listElement != null) {
            view.setOnClickListener(listElement.getOnClickListener());
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            TextView text1 = (TextView) view.findViewById(R.id.text1);
            TextView text2 = (TextView) view.findViewById(R.id.text2);
            if (icon != null) {
                icon.setImageDrawable(listElement.getIcon());
            }
            if (text1 != null) {
                text1.setText(listElement.getText1());
            }
            if (text2 != null) {
                text2.setText(listElement.getText2());
            }
        }
        return view;
    }
}
