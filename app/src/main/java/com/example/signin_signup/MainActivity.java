package com.example.signin_signup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button logout_btn;
    FirebaseAuth mAuth;
    FirebaseUser user;

    EditText phone_field, sms_field, sms_msg_field, wa_msg_field, email_field, email_sub_field, email_msg_field;
    Button call_btn, sms_btn, wa_send_btn, email_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Edit Text
        phone_field = findViewById(R.id.phone_field);
        sms_field = findViewById(R.id.sms_field);
        sms_msg_field = findViewById(R.id.sms_msg_field);
        wa_msg_field = findViewById(R.id.wa_msg_field);
        email_field = findViewById(R.id.email_field);
        email_sub_field = findViewById(R.id.email_sub_field);
        email_msg_field = findViewById(R.id.email_msg_field);

        //Buttons
        call_btn = findViewById(R.id.call_btn);
        sms_btn = findViewById(R.id.sms_btn);
        wa_send_btn = findViewById(R.id.wa_send_btn);
        email_btn = findViewById(R.id.email_btn);
        logout_btn = findViewById(R.id.logout_btn);
        //Firebase instance
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

//To Send data to diff app
//                Intent shareText=Intent.createChooser(intent,null);
//                startActivity(shareText);


        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Signing Out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(), sign_in.class);
                startActivity(i);
                finish();
            }
        });

        //SMS SEND
        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = sms_field.getText().toString();
                String msg = sms_msg_field.getText().toString();
                send_sms(num, msg);
            }

            private void send_sms(String num, String msg) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("smsto:" + num));
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                startActivity(intent);

                sms_field.setText("");
                sms_msg_field.setText("");
            }
        });

//For Whatsapp MSg
        wa_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = wa_msg_field.getText().toString();
                whatsappSMS(msg);
            }

            @SuppressLint("QueryPermissionsNeeded")
            private void whatsappSMS(String msg) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.gbwhatsapp");
                intent.putExtra(Intent.EXTRA_TEXT, msg);
//             Intent sendtoanother=new Intent(getApplicationContext(),temp.class);
//             sendtoanother.putExtra("msg",msg);

//                 Checking whether Whatsapp is installed or not
                if (intent.resolveActivity(getPackageManager()) == null) {
                    Toast.makeText(getApplicationContext(), "Please install whatsapp first.", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(intent);
//                    startActivity(sendtoanother);
                    wa_msg_field.setText("");
                }

            }
        });

        //Email Send
        email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to = email_field.getText().toString();
                String sub = email_sub_field.getText().toString();
                String msg = email_msg_field.getText().toString();
                sendEmail(to, sub, msg);
            }

            @SuppressLint("QueryPermissionsNeeded")
            private void sendEmail(String to, String sub, String msg) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, sub);
                email.putExtra(Intent.EXTRA_TEXT, msg);


                //need this to prompts email client only
                email.setType("message/rfc822");
                email.setPackage("com.google.android.gm");

                if (email.resolveActivity(getPackageManager()) == null) {
                    Toast.makeText(getApplicationContext(), "Please Install Gmail first.", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(email);
                    email_field.setText("");
                    email_sub_field.setText("");
                    email_msg_field.setText("");
                }
            }
        });


        //Calling System
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phn_call();
            }
        });
    }

    public void phn_call() {

        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);
            } else {
                String phone_value = phone_field.getText().toString();
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + phone_value));
                startActivity(i);

                phone_field.setText("");
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String phone_value = phone_field.getText().toString();
        if (grantResults[0] == 101) {
            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:" + phone_value));
            startActivity(i);

            phone_field.setText("");
        }

    }
}