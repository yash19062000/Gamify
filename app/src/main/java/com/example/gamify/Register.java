package com.example.gamify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {

    public EditText rUserName;
    private EditText rName, rEmail, rPassword;
    private Button rBtn;
    private TextView cText;
    private FirebaseAuth fAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rName = findViewById(R.id.registerName);
        rEmail = findViewById(R.id.registerEmail);
        rPassword = findViewById(R.id.registerPass);
        rUserName = findViewById(R.id.registerUName);
        rBtn = findViewById(R.id.registerBtn);
        cText = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        rBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                String email = rEmail.getText().toString().trim();
                String password = rPassword.getText().toString().trim();

                if(email==null || email.length()==0){
                    rEmail.setError("Please enter a valid email email");
                    return;
                }

                if(password==null || password.length()==0){
                    rPassword.setError("Please enter a valid password");
                    return;
                }

                if(password.length()<=5){
                    rPassword.setError("Minimum length of the password is 6 characters");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            mDatabase.child("Users").child(fAuth.getCurrentUser().getUid()).child("Full Name").setValue(rName.getText().toString());
                            mDatabase.child("Users").child(fAuth.getCurrentUser().getUid()).child("Username").setValue(rUserName.getText().toString());
                            startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                        }
                        else {
                            Toast.makeText(Register.this, "Error: "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        cText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}