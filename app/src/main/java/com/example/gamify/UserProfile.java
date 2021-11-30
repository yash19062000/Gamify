package com.example.gamify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private CircleImageView image;
    private TextView name, username;
    private TextView user_games, user_preference, user_dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(fAuth.getCurrentUser().getUid());
        image = (CircleImageView) findViewById(R.id.img_profile);
        name = (TextView) findViewById(R.id.title);
        username = (TextView) findViewById(R.id.username);
        user_games = (TextView) findViewById(R.id.user_games);
        user_preference = (TextView) findViewById(R.id.user_preference);
        user_dob = (TextView) findViewById(R.id.user_dob);


        StorageReference img_ref = storageReference.child("Users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        img_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(image);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("Full Name").getValue().toString());
                username.setText(snapshot.child("Username").getValue().toString());
                user_games.setText(snapshot.child("Games").getValue().toString());
                user_preference.setText(snapshot.child("Preference").getValue().toString());
                user_dob.setText(snapshot.child("DOB").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnav2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.search:
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                        break;
                    case R.id.chat:
                        Intent i1 = new Intent(getApplicationContext(),ChatList.class);
                        startActivity(i1);
                        break;
                    case R.id.profile:
                        break;
                }
                return false;
            }
        });
    }
}