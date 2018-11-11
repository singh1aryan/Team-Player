package com.hackumass.med.boltaction;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackumass.med.boltaction.Soccer.Player;
import com.hackumass.med.boltaction.Soccer.PlayerList;

import java.util.ArrayList;
import java.util.List;

public class GroundActivity extends AppCompatActivity {

    // soccer to here, we can see the players at each ground
    FloatingActionButton buttonAddTrack;

    ListView playersList;

    DatabaseReference playersDatabase;

    List<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground);

        Intent intent = getIntent();

        /*
        * this line is important
        * this time we are not getting the reference of a direct node
        * but inside the node track we are creating a new child with the artist id
        * and inside that node we will store all the tracks with unique ids
        * */
        String id = intent.getStringExtra("id");
        if(id!=null) {
            playersDatabase = FirebaseDatabase.getInstance().getReference("players").
                    child(intent.getStringExtra("id"));
        }
        getSupportActionBar().setTitle(intent.getStringExtra("name"));
        buttonAddTrack =  findViewById(R.id.buttonAddTrack);
        playersList = (ListView) findViewById(R.id.playersLV);

        players = new ArrayList<>();

        buttonAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTrack();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        playersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                players.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Player track = postSnapshot.getValue(Player.class);
                    players.add(track);
                }
                PlayerList trackListAdapter = new PlayerList(GroundActivity.this, players);
                playersList.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveTrack() {

            String id  = playersDatabase.push().getKey();
            Player player = new Player("Ayush", "19", "4",id);
        playersDatabase.child(id).setValue(player);
            Toast.makeText(this, "Player Added", Toast.LENGTH_LONG).show();

    }
}