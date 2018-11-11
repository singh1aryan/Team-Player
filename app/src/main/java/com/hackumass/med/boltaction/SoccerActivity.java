package com.hackumass.med.boltaction;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackumass.med.boltaction.Soccer.Ground;
import com.hackumass.med.boltaction.Soccer.GroundList;
import com.hackumass.med.boltaction.Soccer.Player;
import com.hackumass.med.boltaction.Soccer.PlayerList;

import java.util.ArrayList;
import java.util.List;

public class SoccerActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference("message");

    //Button button;
    private DatabaseReference groundsDatabase;

    List<Ground> grounds;
    ListView listViewGrounds;
    FloatingActionButton button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer);
        getSupportActionBar().setTitle("Grounds");

        listViewGrounds = findViewById(R.id.listViewPlayers);
        grounds = new ArrayList<>();

        // Ground players would be the name of that
        groundsDatabase = FirebaseDatabase.getInstance().getReference("Grounds");
//        groundsDatabase = playersDatabase.child("Soccer_fields");

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlayer();
            }
        });

        listViewGrounds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Ground ground = grounds.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), GroundActivity.class);

                //putting artist name and id to intent
                intent.putExtra("id", ground.id);
                intent.putExtra("name", ground.name);

                //starting the activity with intent
                startActivity(intent);
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
//        Ground ground = new Ground("BU field","2pm");
//        groundsDatabase.child(id).setValue(ground);

        String id = groundsDatabase.push().getKey();
        Ground ground = new Ground("Ground2","2 PM","14",id);
        groundsDatabase.child(id).setValue(ground);
        Toast.makeText(this, "Ground added added", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        groundsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                grounds.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Ground ground = postSnapshot.getValue(Ground.class);
                    //adding artist to the list
                    grounds.add(ground);
                }

                //creating adapter
                GroundList artistAdapter = new GroundList(SoccerActivity.this, grounds);
                //attaching adapter to the listview
                listViewGrounds.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
