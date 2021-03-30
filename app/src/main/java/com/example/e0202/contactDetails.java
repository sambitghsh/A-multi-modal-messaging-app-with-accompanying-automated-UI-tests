package com.example.e0202;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class contactDetails extends AppCompatActivity {

    String contact ;
    EditText contact_details;
    Button confirm_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        //Declaration of UI varaibales
        contact_details = (EditText)findViewById(R.id.etContact);
        confirm_btn = (Button)findViewById(R.id.bcConfirm);

        //Keeping the intent message from the first Activity
        Intent i = getIntent();
        String message = i.getStringExtra("message");



        //Definition of Confirm button
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipientList = contact_details.getText().toString();
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(recipientList).matches()) {
                    sendEmail(v, recipientList, message);
                }
                else if(validPhoneNo(recipientList)){
                    sendMessage(v, recipientList, message);
                }
                else{
                    contact_details.setError("Invalid contact details.");
                }
            }
        });
    }

    //Checking the Phone number is Valid or not
    public boolean validPhoneNo(String input){
        Pattern p = Pattern.compile("[0-9]{10}");
        Matcher m = p.matcher(input);
        return m.matches();
    }


    //Sending the Email
    public void sendEmail(View v, String recipientList, String message){
        Toast.makeText(getApplicationContext(), "Sending Email..", Toast.LENGTH_LONG).show();

        String subject = "Programming Assignment 1";
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientList});
        emailIntent.setType("message/rfc822");

        startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
    }


    //Sending the SMS
    public void sendMessage(View v, String recipientList, String message){
        Toast.makeText( getApplicationContext(),"Sending SMS..", Toast.LENGTH_LONG).show();

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"+recipientList));
        smsIntent.putExtra("sms_body", message);

        startActivity(Intent.createChooser(smsIntent, "Choose a phone client"));
    }

}
