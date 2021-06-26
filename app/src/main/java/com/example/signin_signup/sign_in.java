package com.example.signin_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sign_in extends AppCompatActivity {

    EditText email_inputfield,pass_inputfield;
    Button login_btn;
    private FirebaseAuth mAuth;

    TextView signup_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email_inputfield=findViewById(R.id.username_inputfield);
        pass_inputfield=findViewById(R.id.password_inputfield);

        login_btn=findViewById(R.id.login_btn);

        //Firebase instance
        mAuth = FirebaseAuth.getInstance();

        signup_text=findViewById(R.id.signup_text);
        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),sign_up.class);
                startActivity(i);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


    }

    private void loginUser() {
        String email=email_inputfield.getText().toString();
        String pass=pass_inputfield.getText().toString();

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if(!pass.isEmpty()){
               mAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                   @Override
                   public void onSuccess(AuthResult authResult) {
                       Toast.makeText(sign_in.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                       Intent tohome=new Intent(getApplicationContext(),MainActivity.class);
                       startActivity(tohome);
                       finish();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(sign_in.this, "Login Failed", Toast.LENGTH_SHORT).show();
                   }
               });
            }else {
                pass_inputfield.setError("Empty Field Not Allowed");
            }
        }else if(email.isEmpty()) {
            email_inputfield.setError("Empty Field Not Allowed");
        }else {
            email_inputfield.setError("Please Enter Correct Email");
        }
    }
}