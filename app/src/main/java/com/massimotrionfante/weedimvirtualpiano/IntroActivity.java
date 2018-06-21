package com.massimotrionfante.weedimvirtualpiano;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

// Intro Screen Activity!
public class IntroActivity extends AppCompatActivity {

    private Button start;    // 3 buttons of our View
    private Button settings;
    private Button exit;
    private String weedimServer="http://192.168.1.5:5000"; // Default server for Weedim (from my local pc lol)

    @Override
    public void onCreate(Bundle savedinstanceState)
    {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.intro_screen);
        start = findViewById(R.id.start); // Find the buttons
        settings = findViewById(R.id.set);
        exit = findViewById(R.id.exit);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        // Start the app when you press START
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextapp = new Intent(IntroActivity.this, MainActivity.class);
                nextapp.putExtra("weedimURL",weedimServer); // Pass the default URL as an extra, to the main activity.
                startActivity(nextapp);
            }
        });

        // Go to settings view if you press SETTINGS
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSettings = new Intent(IntroActivity.this, SettingsActivity.class);
                startActivity(goSettings);
            }
        });

        // Exit from the app if you press EXIT
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0); // This is apparently the clean way to terminate an app.
            }
        });

    }

}
