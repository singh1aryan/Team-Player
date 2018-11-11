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

public class PlayerList extends ArrayAdapter<Player> {
    private Activity context;
    List<Player> players;

    public PlayerList(Activity context, List<Player> players) {
        super(context, R.layout.player_layout, players);
        this.context = context;
        this.players = players;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.player_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewGenre);
        TextView textViewGenre1 = (TextView) listViewItem.findViewById(R.id.textViewGenre1);

        Player player = players.get(position);
        textViewName.setText(player.name);
        textViewGenre.setText(textViewGenre.getText() + " " + player.age);
        textViewGenre1.setText(textViewGenre1.getText() + " " + player.level);

        return listViewItem;
    }
}
