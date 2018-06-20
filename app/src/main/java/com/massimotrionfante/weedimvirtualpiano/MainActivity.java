package com.massimotrionfante.weedimvirtualpiano;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.billthefarmer.mididriver.MidiDriver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;


public class MainActivity extends AppCompatActivity implements MidiDriver.OnMidiStartListener {

    private String weedimURL; // This contains the URL of Weedim's web service

    // Data structures for Midi addon/plugin/etc to work
    private MidiDriver midiDriver;
    private Stack<Integer> notes;
    private Stack<Integer> delays;
    private int[] config;
    private int ottava;
    private int lunghezza;

    // Piano buttons
    private Button noteC;
    private Button noteCm;
    private Button noteD;
    private Button noteDm;
    private Button noteE;
    private Button noteF;
    private Button noteFm;
    private Button noteG;
    private Button noteGm;
    private Button noteA;
    private Button noteAm;
    private Button noteB;

    // Various Views
    private Button send;
    private ImageView lowerOctave;
    private ImageView upperOctave;
    private TextView ottavaVisual;
    private TextView outputMessage;
    private TextView sessionNum;
    private ImageView lowerLength;
    private ImageView upperLength;
    private TextView lengthVisual;
    private TextView onOffToggle;
    private ImageView playButton;
    private ImageView undo;
    private ImageView rest;
    private ImageView settings;
    private ImageView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Start the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the URL from the old intent (it will either contain the base URL, or the working URL inputted in the settings view)
        Intent oldapp = getIntent();
        weedimURL = oldapp.getStringExtra("weedimURL");

        // Start midi driver
        midiDriver = new MidiDriver();
        midiDriver.setOnMidiStartListener(this);

        // Find views
        noteC = findViewById(R.id.C);
        noteCm = findViewById(R.id.Cm);
        noteD = findViewById(R.id.D);
        noteDm = findViewById(R.id.Dm);
        noteE = findViewById(R.id.E);
        noteF = findViewById(R.id.F);
        noteFm = findViewById(R.id.Fm);
        noteG = findViewById(R.id.G);
        noteGm = findViewById(R.id.Gm);
        noteA = findViewById(R.id.A);
        noteAm = findViewById(R.id.Am);
        noteB = findViewById(R.id.B);
        send = findViewById(R.id.send);

        outputMessage = findViewById(R.id.outputMessage);
        sessionNum = findViewById(R.id.sessionNumber);

        lowerOctave = findViewById(R.id.lowOctave);
        upperOctave = findViewById(R.id.uppOctave);

        ottavaVisual = findViewById(R.id.oct);

        lowerLength = findViewById(R.id.lowLen);
        upperLength = findViewById(R.id.uppLen);
        lengthVisual = findViewById(R.id.length);
        onOffToggle = findViewById(R.id.recToggle);
        playButton = findViewById(R.id.play);
        undo = findViewById(R.id.undo);
        rest = findViewById(R.id.rest);

        settings = findViewById(R.id.gotosettings);
        title = findViewById(R.id.gototitlescreen);

        ottava = 4;
        lunghezza = 4;

        // These two stacks will save all our notes and delays, in case user records
        notes = new Stack<Integer>();
        delays = new Stack<Integer>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Send recorded music to Weedim server here
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendFunction invia = new SendFunction();
                URL miaURL = null;
                JSONArray mieNote = new JSONArray(notes);   // Make JSONArrays for our request
                JSONArray mieiDelay = new JSONArray(delays);

                try // Build the URL...
                {
                    miaURL = new URL(weedimURL + "/saveSession/" + mieNote + "/" + mieiDelay);
                }
                catch (Exception e) {}
                invia.execute(miaURL); // ...and execute it!

            }
        });

        // Put all onClickListeners here, to serve the basic piano functionality
        noteC.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); outputMessage.setText(""); }});
        noteCm.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); outputMessage.setText(""); }});
        noteD.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); outputMessage.setText(""); }});
        noteDm.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); outputMessage.setText(""); }});
        noteE.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); outputMessage.setText(""); }});
        noteF.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); outputMessage.setText(""); }});
        noteFm.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); outputMessage.setText(""); }});
        noteG.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); outputMessage.setText(""); }});
        noteGm.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); outputMessage.setText(""); }});
        noteA.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); outputMessage.setText(""); }});
        noteAm.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); outputMessage.setText(""); }});
        noteB.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); outputMessage.setText(""); }});

        // Go back to title screen View on the arrow press
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent titolo = new Intent(MainActivity.this, IntroActivity.class);
                titolo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // This flag is super useful: it makes sure older activities go away.
                startActivity(titolo);
            }
        });

        // Go to settings View on the fancy cog press
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mysettings = new Intent(MainActivity.this,SettingsActivity.class);
                mysettings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mysettings);
            }
        });

        // Play recorded song on play press
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtraThread myPlay = new ExtraThread(); // This could require more than 7 seconds.
                myPlay.run(); // Thus, we'll give this task to another thread.
            }
        });

        // Pop away last note inputted on undo button press
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notes.size()>0)
                {
                    notes.pop();
                    delays.pop();
                    outputMessage.setText("Deleted last recorded note.");
                    sessionNum.setText("");
                }
            }
        });

        // Push a rest when clicking on the red box
        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputMessage.setText("");
                if (onOffToggle.getText().equals("ON")) {
                    notes.push(0);
                    delays.push(lunghezza);
                    outputMessage.setText("Added new rest.");
                    sessionNum.setText("");
                }
            }
        });

        //Lower octave by 1
        //(I had to find some workarounds, to prevent unvalid notes to play)
        lowerOctave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ottava>2) {
                    ottava--;
                    ottavaVisual.setText(Integer.toString(ottava-1));
                    outputMessage.setText("");
                    sessionNum.setText("");
                }
            }
        });

        //Upper octave by 1
        upperOctave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ottava<7) {
                    ottava++;
                    ottavaVisual.setText(Integer.toString(ottava-1));
                    outputMessage.setText("");
                    sessionNum.setText("");
                }
            }
        });

        //Lower note lenght
        lowerLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lunghezza!=32) {
                    lunghezza *= 2;
                    lengthVisual.setText(Integer.toString(lunghezza));
                    outputMessage.setText("");
                    sessionNum.setText("");
                }
            }
        });

        //Upper note length
        upperLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lunghezza!=1)
                {
                    lunghezza /= 2;
                    lengthVisual.setText(Integer.toString(lunghezza));
                    outputMessage.setText("");
                    sessionNum.setText("");
                }
            }
        });

        //Enable recording an audio track
        onOffToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOffToggle.getText().equals("OFF"))
                {
                    onOffToggle.setText("ON");
                    outputMessage.setText("Notes recording enabled!");
                }
                else
                {
                    onOffToggle.setText("OFF");
                    outputMessage.setText("Notes recording disabled!");
                }
                sessionNum.setText("");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        midiDriver.start();

        config = midiDriver.config();

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        midiDriver.stop();
    }

    // This method will start as soon as the Midi Driver loads succesfully.
    // Since we want to pair web service with phone app, we'll let it play the very same jingle on succesful load!
    @Override
    public void onMidiStart() {

        // Beginning jingle (55 57 59 60 with 0.1 second delay)
        // (note that, unlike Midi.js, this plugin doesn't give you a delay input value, so I was forced to use Thread.sleep())
        try {
            Thread.sleep(1000);
            playNote((byte)67);
            Thread.sleep(100);
            playNote((byte)69);
            Thread.sleep(100);
            playNote((byte)71);
            Thread.sleep(100);
            playNote((byte)72);
            Thread.sleep(100);
        } catch (Exception e){}
    }

    // Base function that plays a note
    private void playNote(byte nota){
        byte[] musica = new byte[3];
        musica[0] = (byte) 0x90; // 0x90 -> note ON in channel 0
        musica[1] = nota;
        musica[2] = (byte) 127; // velocity at maximum (127)
        // Play a note!
        midiDriver.write(musica);
    }

    // This function plays a note depending on what's in its text field.
    // It's used for the Piano to work properly, and to play the correct notes.
    private void determinaNota(Button v)
    {
        String ottavaStr = v.getText().toString();
        int nota=0;

        // Process note pitch.
        // We process white notes first.
        switch(ottavaStr.charAt(0))
        {
            case 'C':
                nota= (ottava * 12) ;
                break;
            case 'D':
                nota= (ottava * 12) + 2;
                break;
            case 'E':
                nota= (ottava * 12) + 4;
                break;
            case 'F':
                nota= (ottava * 12) + 5;
                break;
            case 'G':
                nota= (ottava * 12) + 7;
                break;
            case 'A':
                nota= (ottava * 12) + 9;
                break;
            case 'B':
                nota= (ottava * 12) + 11;
        }

        // Process black notes.
        if (ottavaStr.length()==2)
        {
            nota += 1;
        }

        playNote((byte)nota); // Play the note, once we got which one it is.

        //If it's recording, then store note value and delay in global arrays
        if (onOffToggle.getText().equals("ON"))
        {
            notes.push(nota);
            delays.push(32/lunghezza);
        }

    }

    // This is the thread that will play notes.
    public class ExtraThread extends Thread
    {
        public void run()
        {
            for (int i=0;i<notes.size();i++)
            {
                playNote(notes.get(i).byteValue()); // Play note...
                try {
                    Thread.sleep( (delays.get(i).intValue()) * 64); //... then wait for its delay.
                } catch (Exception e){}
            }
            outputMessage.setText("");
            sessionNum.setText("");
        }
    }

    // This class takes care of sending data to Weedim's web service.
    public class SendFunction extends AsyncTask<URL,Void,String>
    {
        @Override
        protected String doInBackground(URL... urls) {
            if (notes.isEmpty())
            {
                return "NOP"; // Don't go trough it if we didn't record anything.
            }
            else {
                String result = null;
                URL miaURL = urls[0];
                try {
                    // Connect
                    HttpURLConnection connection = (HttpURLConnection) miaURL.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "text/plain");
                    // Get the session number, in an unusual way: getting it character after character.
                    InputStream stream = connection.getInputStream();
                    StringBuffer sb = new StringBuffer();
                    try {
                        int chr;
                        while ((chr = stream.read()) != -1) {
                            sb.append((char) chr);
                        }
                        result = sb.toString();
                        stream.close();
                    } catch (Exception e) {
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    result = "WTF"; // Return this result if we can't connect with server.
                }

                return result;
            }
        }
        protected void onPostExecute(String result)
        {
            if (result.equals("WTF")) // WTF -> Connectivity issues
            {
                outputMessage.setText("");
                sessionNum.setText("There are issues with connectivity...");
            }
            else if (result.equals("NOP")) // NOP -> We didn't record anything
            {
                outputMessage.setText("");
                sessionNum.setText("Your didn't record anything yet!");
            }
            else {
                outputMessage.setText("Record was sent with session number:"); // In none of above, record was sent!
                sessionNum.setText(result); // Give session number for user to use in the Weedim Web App.
            }
        }
    }


}
