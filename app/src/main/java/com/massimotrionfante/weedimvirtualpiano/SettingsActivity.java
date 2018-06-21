package com.massimotrionfante.weedimvirtualpiano;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// Settings Activity incoming!
public class SettingsActivity extends AppCompatActivity {

    private String weedimLink; // the URL for Weedim's web services
    private Button start;  // various views used.
    private Button gotoTitle;
    private EditText input;
    private TextView errors;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);

        // Collect our views
        start = findViewById(R.id.startapp);
        input = findViewById(R.id.weedimlink);
        errors = findViewById(R.id.error);
        gotoTitle = findViewById(R.id.gototitle);

    }

    @Override
    public void onStart() {
        super.onStart();

        //If we click on the button, then let's attempt a start!
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weedimLink = input.getText().toString(); // Collect the inputted URL
                if (weedimLink.equals("")) {
                    errors.setText("Please, input a valid URL before continuing!"); // Ignore empty fields...
                } else {

                    CheckConnectivity check = new CheckConnectivity();
                    URL miaURL = null;

                    try
                    {
                        miaURL = new URL(weedimLink + "/check");  // Build the URL which checks if the service is running on the inputted link
                    } catch (Exception e){}
                    check.execute(miaURL);  //...and execute!
                }
            }
        });

        // If we click on the second button, bring user to titlescreen
        gotoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent titolo = new Intent(SettingsActivity.this, IntroActivity.class);
                titolo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // This flag is super useful: it makes sure older activities go away.
                startActivity(titolo);
            }
        });

    }

    // This class takes care of getting the result from the request.
    public class CheckConnectivity extends AsyncTask<URL, Void, String> {
        @Override
        public String doInBackground(URL... urls) {
            String result = null;
            URL miaURL = urls[0];
            try {
                // Connect
                HttpURLConnection connection = (HttpURLConnection) miaURL.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "text/plain");
                // Get the response, same way as we did in MainActivity
                InputStream stream = connection.getInputStream();
                StringBuffer sb = new StringBuffer();
                try {
                    int chr;
                    while ((chr = stream.read()) != -1) {
                        sb.append((char) chr);
                    }
                    result = sb.toString();
                    stream.close();
                } catch (Exception e) {}
                connection.disconnect();
            } catch (Exception e) {
                result = "E404";  // Give a bad result if we have connection issues.
            }

            return result;
        }

        protected void onPostExecute(String result)
        {
            if (result.equals("42")) // If result is 42 ;)
            {
                Intent goNext = new Intent(SettingsActivity.this, MainActivity.class);
                goNext.putExtra("weedimURL", weedimLink);
                startActivity(goNext); // ...then start the service!
            }
            else // Else, just print an error message.
            {
                errors.setText(R.string.cantconnect);
            }
        }

    }
}
