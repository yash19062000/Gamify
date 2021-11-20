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

public class Login extends AppCompatActivity {

    EditText lEmail, lPassword;
    Button lBtn;
    TextView lText;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lEmail = findViewById(R.id.logEmail);
        lPassword = findViewById(R.id.logPassword);
        lBtn = findViewById(R.id.logBtn);
        lText = findViewById(R.id.logText);

        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        lBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                String email = lEmail.getText().toString().trim();
                String password = lPassword.getText().toString().trim();

                if(email==null || email.length()==0){
                    lEmail.setError("Please enter a valid email email");
                    return;
                }

                if(password==null || password.length()==0){
                    lPassword.setError("Please enter a valid password");
                    return;
                }

                if(password.length()<=5){
                    lPassword.setError("Minimum length of the password is 6 characters");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Login.this, "Error: "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });

        lText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

    }
}