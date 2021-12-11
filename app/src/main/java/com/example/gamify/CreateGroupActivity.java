package com.example.gamify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

public class CreateGroupActivity extends AppCompatActivity {

    private GroupManager manager;
    private EditText name;
    private EditText game;
    private Button create;
    private Spinner preference;
    private String selected_pref;
    private EditText descEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        preference = (Spinner) findViewById(R.id.pref);
        String[] items = new String[]{"Online", "Offline"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        preference.setAdapter(adapter);
        preference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_pref = items[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    public void createGroupRoutine(View view) {
        name = (EditText) findViewById(R.id.group_name);
        game = (EditText) findViewById(R.id.game_edit);
        create = (Button) findViewById(R.id.button_confirm);
        descEditText = (EditText) findViewById(R.id.description);

        manager = new GroupManager();

        String groupName = name.getText().toString();
        String groupGame = game.getText().toString();
        String description = descEditText.getText().toString();


        Log.i("Group Name: " , groupName);
        Log.i("Game: ", groupGame);

        Bundle extras = getIntent().getExtras();
        manager.createGroup(groupName, groupGame, extras.getString("username"), selected_pref,
                description);
        Intent intent = new Intent(CreateGroupActivity.this, MainActivity.class);
        intent.putExtra("groupName", groupName);
        intent.putExtra("description", description);
        intent.putExtra("preference", selected_pref);
        startActivity(intent);

    }
}