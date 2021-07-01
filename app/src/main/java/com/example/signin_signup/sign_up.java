package com.example.signin_signup;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class sign_up extends AppCompatActivity {

    EditText name_inputfield, phn_inputfield, email_inputfield, pass_inputfield;
    Button register_btn;
    TextView signin_text;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    String userID;
    FirebaseFirestore firebasedb;
    private FirebaseAuth mAuth;
    dbhelper db=new dbhelper(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Input Field
        name_inputfield = findViewById(R.id.name_inputfield);
        phn_inputfield = findViewById(R.id.phn_inputfield);
        email_inputfield = findViewById(R.id.email_inputfield);
        pass_inputfield = findViewById(R.id.pass_inputfield);

        radioGroup = findViewById(R.id.gender_radio);


        //Button
        register_btn = findViewById(R.id.register_btn);
        //TextView
        signin_text = findViewById(R.id.signin_text);
//
//        signin_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                startActivity(new Intent(getApplicationContext(), sign_in.class));
//            }
//        });

//Firebase instance
        mAuth = FirebaseAuth.getInstance();
//firebase Firestore
        firebasedb = FirebaseFirestore.getInstance();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });


    }

    private void createUser() {
        String name = name_inputfield.getText().toString();
        String phn = phn_inputfield.getText().toString();
        String email = email_inputfield.getText().toString();
        int selectedID=radioGroup.getCheckedRadioButtonId();
        selectedRadioButton=findViewById(selectedID);
        String selectedRadioButton_text= (String) selectedRadioButton.getText();
        String pass = pass_inputfield.getText().toString();





        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!pass.isEmpty()) {
                //Firebase Firestore
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(sign_up.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        //store data using userid
//                        userID= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        userID = email;
                        DocumentReference df = firebasedb.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("Full Name", name);
                        user.put("Number", phn);
                        user.put("Email", email);
                        user.put("Gender",selectedRadioButton_text);

                        df.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(sign_up.this, "User Profile Created", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(sign_up.this, "Error While Profile Creating" + e, Toast.LENGTH_SHORT).show();
                            }
                        });
//
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(sign_up.this, "Registration Error", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onFailure: " + e);
                    }
                });

                //Sqlite DB
                Boolean insert = db.insertuser(name, phn, email, selectedRadioButton_text, pass);
                if (insert) {
                    Toast.makeText(this, "Inserted Into SQLite", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error while inserting into SQLite", Toast.LENGTH_SHORT).show();
                }
            } else {
                pass_inputfield.setError("Empty Field Not Allowed");
            }
        } else if (email.isEmpty()) {
            email_inputfield.setError("Empty Field Not Allowed");
        } else {
            email_inputfield.setError("Please Enter Correct Email");
        }
    }
}