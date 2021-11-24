package com.example.gamify;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupManager {

    private DatabaseReference mDatabase;

    public GroupManager() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void createGroup(String groupName, String game, String user) {
        ArrayList<String> users = new ArrayList<>();
        users.add(user);
        mDatabase.child("Groups").child(groupName).child("Game").setValue(game);
        joinGroup(groupName, user);
    }

    public void joinGroup(String groupName, String user) {
        String index = mDatabase.child("Groups").child(groupName).child("Users").push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put(index, user);
        mDatabase.child("Groups").child(groupName).child("Users").updateChildren(map);
    }


}
