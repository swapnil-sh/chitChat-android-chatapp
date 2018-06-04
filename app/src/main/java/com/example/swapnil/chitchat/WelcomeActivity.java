package com.example.swapnil.chitchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Thread thread = new Thread()
        {

            @Override
            public void run() {

                try
                {
                       sleep(4000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                finally
                {


                    Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(mainIntent);

                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }
}
