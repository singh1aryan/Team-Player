package com.hackumass.med.boltaction.Soccer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hackumass.med.boltaction.R;

import java.util.List;

/**
 * Created by Aryan Singh on 11/10/2018.
 */

public class GroundList extends ArrayAdapter<Ground> {
    private Activity context;
    List<Ground> grounds;

    public GroundList(Activity context, List<Ground> grounds) {
        super(context, R.layout.ground_layout, grounds);
        this.context = context;
        this.grounds = grounds;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.ground_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewTime = (TextView) listViewItem.findViewById(R.id.textViewGenre);
        TextView textViewCap = (TextView) listViewItem.findViewById(R.id.textViewGenre1);

        Ground ground = grounds.get(position);
        textViewName.setText(ground.name);
        textViewTime.setText(textViewTime.getText() + " " + ground.time);
        textViewCap.setText(textViewCap.getText() + " " + ground.capacity);

        return listViewItem;
    }
}