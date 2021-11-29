package com.example.gamify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateGroupActivity extends AppCompatActivity {

    private GroupManager manager;
    private EditText name;
    private EditText game;
    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

    }

    public void createGroupRoutine(View view) {
        name = (EditText) findViewById(R.id.name);
        game = (EditText) findViewById(R.id.game);
        create = (Button) findViewById(R.id.create);
        manager = new GroupManager();

        String groupName = name.getText().toString();
        String groupGame = game.getText().toString();

        Log.i("Group Name: " , groupName);
        Log.i("Game: ", groupGame);

        Bundle extras = getIntent().getExtras();
        manager.createGroup(groupName, groupGame, extras.getString("username"));
        Intent intent = new Intent(CreateGroupActivity.this, MainActivity.class);
        startActivity(intent);

    }
}