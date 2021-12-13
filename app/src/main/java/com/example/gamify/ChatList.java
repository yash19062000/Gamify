package com.example.gamify;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChatList extends AppCompatActivity {

    private ListView groupsListView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listOfGroups = new ArrayList<>();
    private DatabaseReference groupRef;
    private Button createGroupButton;
    private FirebaseAuth firebaseAuth;
    private String currentUserID;
    private Toolbar toolbar;
    private HashMap<String,String> hm = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");


        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();

        initFields();
        displayGroups();

        groupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currGroupName = parent.getItemAtPosition(position).toString();
                Intent groupChatIntent = new Intent(getApplicationContext(),GroupChat.class);
                groupChatIntent.putExtra("name",currGroupName);
                groupChatIntent.putExtra("key", hm.get(currGroupName));
                startActivity(groupChatIntent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnav1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.search:
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                        break;
                    case R.id.chat:
                        break;
                    case R.id.profile:
                        Intent i1 = new Intent(getApplicationContext(),UserProfile.class);
                        startActivity(i1);
                        break;
                }
                return false;
            }
        });
    }

    private void initFields() {
        toolbar = (Toolbar)findViewById(R.id.chat_list_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Chats");
        groupsListView = (ListView) findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfGroups);
        groupsListView.setAdapter(arrayAdapter);
    }

    private void displayGroups() {
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<>();
                Iterator it = snapshot.getChildren().iterator();


                while(it.hasNext()) {
//                    set.add(((DataSnapshot) it.next()).getKey());
                    DataSnapshot ds = (DataSnapshot) it.next();
                    String grpName = (String)ds.getKey();
                    Iterator it1 = ds.getChildren().iterator();
                    Iterator it2 = null;
                    while(it1.hasNext()){
                        it2 = ((DataSnapshot) it1.next()).getChildren().iterator();
                    }
                    boolean ifPresentInGroup = false;
                    while(it2.hasNext()){
                        DataSnapshot d1 = (DataSnapshot)it2.next();
                        String uKey = (String) d1.getValue();
                        if(uKey.compareTo(currentUserID)==0){
                            hm.put(grpName, (String) d1.getKey());
                            ifPresentInGroup=true;
                            break;
                        }
                    }
                    if(ifPresentInGroup){
                        set.add(ds.getKey());
                    }
                }
                listOfGroups.clear();
                listOfGroups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}