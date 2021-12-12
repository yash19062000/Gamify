package com.example.gamify;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChat extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton sendMessageBtn;
    private EditText userMessage;
    private ScrollView scrollView;
    private TextView displayMessage;
    private String groupName;
    private String currentUserID, currentUserName,currentDate, currentTime;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference UsersRef,GroupNameRef,GroupMessageKeyRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        groupName = getIntent().getExtras().get("name").toString();
        Toast.makeText(GroupChat.this,groupName,Toast.LENGTH_SHORT).show();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Messages");

        InitFields();
        GetUserInfo();

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveMessage();
                userMessage.setText("");
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    DisplayAllMessage(snapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    DisplayAllMessage(snapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DisplayAllMessage(DataSnapshot snapshot) {
        Iterator iterator = snapshot.getChildren().iterator();
        while(iterator.hasNext()){
            String uName = (String) ((DataSnapshot)iterator.next()).getValue();
            String date = (String) ((DataSnapshot)iterator.next()).getValue();
            String message = (String) ((DataSnapshot)iterator.next()).getValue();
            String time = (String) ((DataSnapshot)iterator.next()).getValue();

            displayMessage.append(uName+":\n"+message+"\n"+time+" "+date+"\n\n\n");
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    private void SaveMessage() {
        String input = userMessage.getText().toString();
        String messageKey = GroupNameRef.push().getKey();
        if(TextUtils.isEmpty(input)){
            Toast.makeText(GroupChat.this,"Message is Empty",Toast.LENGTH_SHORT).show();
        }
        else{
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currDateFormat.format(calendar.getTime());

            Calendar calendar1 = Calendar.getInstance();
            SimpleDateFormat currTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currTimeFormat.format(calendar1.getTime());

            HashMap<String,Object> groupMessage = new HashMap<>();
            GroupNameRef.updateChildren(groupMessage);


            GroupMessageKeyRef = GroupNameRef.child(messageKey);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("Username",currentUserName);
            messageInfoMap.put("message",input);
            messageInfoMap.put("date",currentDate);
            messageInfoMap.put("time",currentTime);

            GroupMessageKeyRef.updateChildren(messageInfoMap);

        }
    }

    private void GetUserInfo() {
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    currentUserName=snapshot.child("Username").getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void InitFields() {
        toolbar = (Toolbar)findViewById(R.id.chat_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(groupName);
        sendMessageBtn = (ImageButton) findViewById(R.id.sendButton);
        userMessage = (EditText) findViewById(R.id.group_message);
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        displayMessage = (TextView)findViewById(R.id.text_display);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.leave){
            FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Users").child(getIntent().getExtras().get("key").toString()).removeValue();
            startActivity(new Intent(getApplicationContext(),ChatList.class));
        }
        return super.onOptionsItemSelected(item);
    }
}