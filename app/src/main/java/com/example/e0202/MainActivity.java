package com.example.e0202;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    EditText  message_text;
    Button send_btn;
    Button voice_input_btn;
    String message;
    boolean isPaused = false;
    static boolean onResumeCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to Hide the action bar in main page
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


        //Decalration of UI variables
        message_text = (EditText) findViewById(R.id.etMessage);
        send_btn = (Button)findViewById(R.id.bsSend);
        voice_input_btn = (Button)findViewById(R.id.bviVoice);

        //Definition of Send Button
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contact_details = new Intent(getApplicationContext(), contactDetails.class);
                String message = message_text.getText().toString();

                contact_details.putExtra("message", message);
                if (message.length() !=0 && !message.matches(" *")){
                    startActivity(contact_details);
                }
            }
        });

        //Definition of Voice Input button
        voice_input_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                try {
                    startActivityForResult(intent, 10);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), "I think your device doesn't supported voice input.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //to catch the result of voice input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10) {
                if (resultCode == RESULT_OK && data != null) {
                    isPaused = true;
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    message_text.setText(result.get(0).toLowerCase());
                }
        }
    }

    //when the move the focus from the app
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences shared_pref = getSharedPreferences("SharedPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_pref.edit().clear();
        editor.putString("text", message_text.getText().toString());
        editor.apply();
    }

    //when we move the focus to the app
    @Override
    protected void onResume() {
        super.onResume();
        if(!isPaused){
            if(onResumeCalled) {
                SharedPreferences shared_pref = getSharedPreferences("SharedPref", MODE_PRIVATE);
                String s1 = shared_pref.getString("text", "");
                message_text.setText(s1);
            }
            else{
                onResumeCalled = true;
            }
        }
    }

}