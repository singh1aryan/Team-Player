package com.hackumass.med.boltaction;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackumass.med.boltaction.Soccer.Ground;
import com.hackumass.med.boltaction.Soccer.GroundList;
import com.hackumass.med.boltaction.Soccer.Player;
import com.hackumass.med.boltaction.Soccer.PlayerList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SoccerActivity extends AppCompatActivity {

    public static final String APPLICATION_ID = "YXC33U49BA";
    public static final String API_KEY = "cd9ced8765fbe3de3cef8fb2b56d9ca7";
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

//        Searcher searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME);
//        InstantSearch helper = new InstantSearchHelper(this, searcher);
//        helper.search();

        Client client = new Client(APPLICATION_ID, API_KEY);
        final Index index = client.getIndex("grounds");
        List<JSONObject> array = new ArrayList<JSONObject>();
        try {
            array.add(
                    new JSONObject().put("firstname", "Hockey Field").put("lastname", "Barninger")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array.add(
                    new JSONObject().put("firstname", "Open Court").put("lastname", "Speach")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array.add(
                    new JSONObject().put("firstname", "Mullins Center").put("lastname", "Speach")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        index.addObjectsAsync(new JSONArray(array), null);

        EditText search = findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Query query = new Query(editable.toString())
                        .setAttributesToRetrieve("firstname")
                        .setHitsPerPage(50);
                index.searchAsync(query, new CompletionHandler() {
                    @Override
                    public void requestCompleted(JSONObject content, AlgoliaException error) {
                        // [...]
                        try {
                            JSONArray hits = content.getJSONArray("hits");
                            List<Ground> list = new ArrayList<>();
                            for(int i=0;i<hits.length();i++){
                                JSONObject jsonObject = hits.getJSONObject(i);
                                String firstName = jsonObject.getString("firstname");
                                for(int j=0;j<grounds.size();j++){
                                    if(grounds.get(i).name.equals(firstName)){
                                        list.add(grounds.get(i));
                                    }
                                }
                                ArrayAdapter<Ground> arrayAdapter = new ArrayAdapter<Ground>(
                                        getApplicationContext(),R.layout.ground_layout,list);
                                listViewGrounds.setAdapter(arrayAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//
//            SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
//            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
//            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    Query query1 = new Query(query)
//                            .setAttributesToRetrieve("name")
//                            .setHitsPerPage(50);
//                    index.searchAsync(query1, new CompletionHandler() {
//                        @Override
//                        public void requestCompleted(JSONObject content, AlgoliaException error) {
//                            // [...]
//                            try {
//                                JSONArray hits = content.getJSONArray("hits");
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    });
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    return false;
//                }
//            });
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            startActivity(new Intent(SoccerActivity.this,SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
