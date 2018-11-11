package com.hackumass.med.boltaction;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackumass.med.boltaction.Database.Contract;
import com.hackumass.med.boltaction.Database.MedOpenHelper;
import com.hackumass.med.boltaction.Soccer.Ground;
import com.hackumass.med.boltaction.Soccer.Player;
import com.hackumass.med.boltaction.Soccer.PlayerList;

import java.util.ArrayList;
import java.util.List;

public class GroundActivity extends AppCompatActivity {

    // soccer to here, we can see the players at each ground
    TextView buttonAddTrack,ground,back,like;

    ListView playersList;

    DatabaseReference playersDatabase;

    List<Player> players;
    String ground1, time1, cap1, sport1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);
        getSupportActionBar().hide();
        Intent intent = getIntent();

        /*
        * this line is important
        * this time we are not getting the reference of a direct node
        * but inside the node track we are creating a new child with the artist id
        * and inside that node we will store all the tracks with unique ids
        * */
        String id = intent.getStringExtra("id");

         ground1 = intent.getStringExtra("ground");
         time1 = intent.getStringExtra("time");
         cap1 = intent.getStringExtra("cap");

         like = findViewById(R.id.like);
         ground = findViewById(R.id.ground);
         back = findViewById(R.id.back);
         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });

         ground.setText(ground1);
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

        Intent i = new Intent(GroundActivity.this,IntroActivity.class);
        startActivityForResult(i,8);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 8){
//            String id  = playersDatabase.push().getKey();
//            String name = data.getStringExtra("name");
//            String age = data.getStringExtra("age");
//            String level = data.getStringExtra("level");

            // sql data for personal use, one one user
            // push the data into sql database
            // check if it is empty or not
            // if it is not empty them disable the click button
            // if wants to delete, then delete the sql data

            // sql will have the ground, and everything here

//            MedOpenHelper openHelper = MedOpenHelper.getInstance(getApplicationContext());
//            SQLiteDatabase database = openHelper.getWritableDatabase();
//
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(Contract.User.COLUMN_ID, id);
//            contentValues.put(Contract.User.COLUMN_GROUND, ground1);
//            contentValues.put(Contract.User.COLUMN_TIME, time1);
//            contentValues.put(Contract.User.COLUMN_SPORT, sport1);
//            Player player = new Player("Ayush", "19", "4",id);
//            playersDatabase.child(id).setValue(player);
//            Toast.makeText(this, "Player Added", Toast.LENGTH_LONG).show();

        }

    }

    public void share(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey ! Wanna go to the field for some soccer ? ");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void similar(View view){
        finish();
    }

    public void like(View view){
        Toast.makeText(getApplicationContext(),"Added to wishlist",Toast.LENGTH_LONG).show();
    }
}