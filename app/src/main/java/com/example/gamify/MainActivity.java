package com.example.gamify;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView groupsListView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listOfGroups = new ArrayList<>();
    private DatabaseReference groupRef;
    private Button createGroupButton;
    private GroupManager manager;
    private FirebaseAuth fAuth;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.chat_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search New Groups");



        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        manager = new GroupManager();
        fAuth = FirebaseAuth.getInstance();
        currentUserID = fAuth.getCurrentUser().getUid();
        initFields();
        displayGroups();
        createGroupButton = (Button) findViewById(R.id.createGroup);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateGroupActivity.class);
                intent.putExtra("username", fAuth.getCurrentUser().getUid());
                v.getContext().startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.search:
                        break;
                    case R.id.chat:
                        startActivity(new Intent(getApplicationContext(),ChatList.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),UserProfile.class));
                        break;
                }
                return false;
            }
        });

        EditText searchBar = findViewById(R.id.search_groups);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.this.arrayAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initFields() {
        groupsListView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfGroups);
        groupsListView.setAdapter(arrayAdapter);

        groupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String groupName = listOfGroups.get(position);
                /*
                final String[] description = {""};
                DatabaseReference descRef = groupRef.child(groupName).child("Description");
                descRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        description[0] = snapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Log.i("DESCRIPTION", "Group: " + groupName + " Description: " +
                        description[0]);

                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Join " + groupName + "? ");
                builder.setCancelable(true);

                builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manager.joinGroup(groupName, currentUserID);

                        dialog.cancel();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
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
                    Iterator it1 = ds.getChildren().iterator();
                    Iterator it2 = null;
                    while(it1.hasNext()){
                        it2 = ((DataSnapshot) it1.next()).getChildren().iterator();
                    }
                    boolean ifPresentInGroup = false;
                    while(it2.hasNext()){
                        String uKey = (String) ((DataSnapshot)it2.next()).getValue();
                        if(uKey.compareTo(currentUserID)==0){
                            ifPresentInGroup=true;
                            break;
                        }
                    }
                    if(!ifPresentInGroup){
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


    public void logout(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}