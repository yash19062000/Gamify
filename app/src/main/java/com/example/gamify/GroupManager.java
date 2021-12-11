package com.example.gamify;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupManager {

    private DatabaseReference mDatabase;
    private DatabaseReference usersRef;

    public GroupManager() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }


    public void createGroup(String groupName, String game, String user, String preference, String description) {
        ArrayList<String> users = new ArrayList<>();
        users.add(user);
        mDatabase.child("Groups").child(groupName).child("Game").setValue(game);
        mDatabase.child("Groups").child(groupName).child("Preference").setValue(preference);
        mDatabase.child("Groups").child(groupName).child("Description").setValue(description);
        usersRef = mDatabase.child("Groups").child("Users");
        /*
        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> users = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String user = snapshot.getValue(String.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("JOIN FALED ", error.getMessage());
            }
        };
        usersRef.addListenerForSingleValueEvent(valueEventListener);
//        joinGroup(game, user);

         */
    }

    public void joinGroup(String groupName, String userID) {

        String index = mDatabase.child("Groups").child(groupName).child("Users").push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put(index, userID);
        mDatabase.child("Groups").child(groupName).child("Users").updateChildren(map);

    }


}
