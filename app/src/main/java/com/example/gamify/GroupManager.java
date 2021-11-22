package com.example.gamify;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupManager {

    private DatabaseReference mDatabase;

    public GroupManager() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void createGroup(String groupName, String game, String user) {
        ArrayList<String> users = new ArrayList<>();
        users.add(user);
        mDatabase.child("Groups").child(groupName).child("Game").setValue(game);
        mDatabase.child("Groups").child(groupName).child("Users").setValue(users);
    }
}
