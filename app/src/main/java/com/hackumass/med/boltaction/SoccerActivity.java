package com.hackumass.med.boltaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackumass.med.boltaction.Soccer.Player;
import com.hackumass.med.boltaction.Soccer.PlayerList;
import com.hackumass.med.boltaction.Soccer.SoccerGround;

import java.util.ArrayList;
import java.util.List;

public class SoccerActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference("message");

    Button button;
    private DatabaseReference playersDatabase;
    private DatabaseReference groundsDatabase;

    List<Player> players;
    ListView listViewPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer);

        listViewPlayers = findViewById(R.id.listViewPlayers);
        players = new ArrayList<>();

        // SoccerGround players would be the name of that
        playersDatabase = FirebaseDatabase.getInstance().getReference("Players");
//        groundsDatabase = playersDatabase.child("Soccer_fields");

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlayer();
            }
        });
    }

//    private void writeNewUser(String userId, String name, String email) {
//        User user = new User("Aryan", 19,5,"soccer");
//        mDatabase.child("ground").child(userId).setValue(user);
//
//        // for updating their values in the db
////        mDatabase.child("users").child(userId).child("username").setValue(name);
//    }

    public void addPlayer(){
//
//        String id = groundsDatabase.push().getKey();
//        SoccerGround ground = new SoccerGround("BU field","2pm");
//        groundsDatabase.child(id).setValue(ground);

        String id = playersDatabase.push().getKey();
        Player player = new Player("aryan","19","5");
        playersDatabase.child(id).setValue(player);
        Toast.makeText(this, "Player added", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        playersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                players.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Player player = postSnapshot.getValue(Player.class);
                    //adding artist to the list
                    players.add(player);
                }

                //creating adapter
                PlayerList artistAdapter = new PlayerList(SoccerActivity.this, players);
                //attaching adapter to the listview
                listViewPlayers.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
