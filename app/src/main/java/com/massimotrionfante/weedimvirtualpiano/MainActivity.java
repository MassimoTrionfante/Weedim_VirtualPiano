package com.massimotrionfante.weedimvirtualpiano;

import android.annotation.SuppressLint;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private Button noteCex;
    private Button noteCmex;
    private Button noteDex;
    private Button noteDmex;
    private Button noteEex;
    private Button noteFex;
    private Button noteFmex;
    private Button noteGex;
    private Button noteGmex;
    private Button noteAex;
    private Button noteAmex;
    private Button noteBex;

    // Various Views
    private ImageView send;
    private ImageView lowerOctave;
    private ImageView upperOctave;
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
    private TextView highOctave;
    private TextView lowOctave;

    private Stack<Button> noteButtons;
    private Stack<Button> noteButtonsEx;

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
        noteCex = findViewById(R.id.Cex);
        noteCmex = findViewById(R.id.Cmex);
        noteDex = findViewById(R.id.Dex);
        noteDmex = findViewById(R.id.Dmex);
        noteEex = findViewById(R.id.Eex);
        noteFex = findViewById(R.id.Fex);
        noteFmex = findViewById(R.id.Fmex);
        noteGex = findViewById(R.id.Gex);
        noteGmex = findViewById(R.id.Gmex);
        noteAex = findViewById(R.id.Aex);
        noteAmex = findViewById(R.id.Amex);
        noteBex = findViewById(R.id.Bex);

        send = findViewById(R.id.send);

        outputMessage = findViewById(R.id.outputMessage);
        sessionNum = findViewById(R.id.sessionNumber);

        lowerOctave = findViewById(R.id.lowOctave);
        upperOctave = findViewById(R.id.uppOctave);

        lowerLength = findViewById(R.id.lowLen);
        upperLength = findViewById(R.id.uppLen);
        lengthVisual = findViewById(R.id.length);
        onOffToggle = findViewById(R.id.recToggle);
        playButton = findViewById(R.id.play);
        undo = findViewById(R.id.undo);
        rest = findViewById(R.id.rest);

        settings = findViewById(R.id.gotosettings);
        title = findViewById(R.id.gototitlescreen);

        highOctave = findViewById(R.id.octavehigh);
        lowOctave = findViewById(R.id.octavelow);

        ottava = 4;
        lunghezza = 4;

        // These two stacks will save all our notes and delays, in case user records
        notes = new Stack<Integer>();
        delays = new Stack<Integer>();
        noteButtons = new Stack<Button>(); // Comfy stack for button notes
        noteButtonsEx = new Stack<Button>();

        // Push all the buttons inside the structures (later you'll see why I'm doing this)
        noteButtons.push(noteC); noteButtons.push(noteCm); noteButtons.push(noteD);
        noteButtons.push(noteDm); noteButtons.push(noteE); noteButtons.push(noteF);
        noteButtons.push(noteFm); noteButtons.push(noteG); noteButtons.push(noteGm);
        noteButtons.push(noteA); noteButtons.push(noteAm); noteButtons.push(noteB);

        noteButtonsEx.push(noteCex); noteButtonsEx.push(noteCmex); noteButtonsEx.push(noteDex);
        noteButtonsEx.push(noteDmex); noteButtonsEx.push(noteEex); noteButtonsEx.push(noteFex);
        noteButtonsEx.push(noteFmex); noteButtonsEx.push(noteGex); noteButtonsEx.push(noteGmex);
        noteButtonsEx.push(noteAex); noteButtonsEx.push(noteAmex); noteButtonsEx.push(noteBex);

    }

    @SuppressLint("ClickableViewAccessibility")
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
        // Smartly made with an iterable for loop ;)
        // (that's why I created "noteButtons" btw)

        // First we process low notes (the red-ish ones)...
        for (Button b:noteButtons) {
            b.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:               // Tapping down the button: play the note
                            determinaNota((Button) v, false, false);
                            if (onOffToggle.getText().equals("ON"))  // Show messages for recorded note (if recording)
                            {
                                outputMessage.setText(R.string.note_recorded);
                                sessionNum.setText("");
                            }
                            else
                            {
                                if (!outputMessage.getText().equals("Executing song...")) {
                                    outputMessage.setText("");
                                }
                                sessionNum.setText("");
                            }
                            break;
                        case MotionEvent.ACTION_UP:                // Removing your finger from the button: stop the note
                            determinaNota((Button) v, true, false);
                            break;
                    }
                    return true;
                }
            });
        }
        // ...now we process high notes (the blue ones).
        for (Button b:noteButtonsEx) {
            b.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:               // Tapping down the button: play the note
                            determinaNota((Button) v, false, true);
                            if (onOffToggle.getText().equals("ON"))  // Show messages for recorded note (if recording)
                            {
                                outputMessage.setText(R.string.note_recorded);
                                sessionNum.setText("");
                            }
                            else
                            {
                                if (!outputMessage.getText().equals("Executing song...")) {
                                    outputMessage.setText("");
                                }
                                sessionNum.setText("");
                            }
                            break;
                        case MotionEvent.ACTION_UP:                // Removing your finger from the button: stop the note
                            determinaNota((Button) v, true, true);
                            break;
                    }
                    return true;
                }
            });
        }

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
                myPlay.execute(); // Thus, we'll give this task to an AsyncTask.
                outputMessage.setText(R.string.execsong);
                sessionNum.setText("");
            }
        });

        // Pop away last note inputted on undo button press
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notes.size()==0)
                {
                    outputMessage.setText(R.string.canterase);
                }
                else
                {
                    notes.pop();
                    delays.pop();
                    outputMessage.setText(R.string.dellastnote);
                }
                sessionNum.setText("");
            }
        });

        // Push a rest when clicking on the red box
        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputMessage.setText("");
                if (onOffToggle.getText().equals("ON")) {
                    notes.push(0);
                    delays.push(32/lunghezza);
                    outputMessage.setText(R.string.newrest);
                    sessionNum.setText("");
                }
            }
        });

        //Lower octave by 1
        //(I had to find some workarounds, to prevent invalid notes to play)
        lowerOctave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ottava>2) {
                    ottava--;
                    outputMessage.setText("");
                    sessionNum.setText("");
                    highOctave.setText("-- OCTAVE " + ottava + " --");
                    int temp = ottava-1;
                    lowOctave.setText("-- OCTAVE " + temp + " --");
                }
            }
        });

        //Upper octave by 1
        upperOctave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ottava<6) {
                    ottava++;
                    outputMessage.setText("");
                    sessionNum.setText("");
                    highOctave.setText("-- OCTAVE " + ottava + " --");
                    int temp = ottava-1;
                    lowOctave.setText("-- OCTAVE " + temp + " --");
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
                    onOffToggle.setText(R.string.on);
                    outputMessage.setText(R.string.recenabled);
                }
                else
                {
                    onOffToggle.setText(R.string.off);
                    outputMessage.setText(R.string.recdisabled);
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
            Thread.sleep(100);
            playNote((byte)67);
            Thread.sleep(100);
            playNote((byte)69);
            Thread.sleep(100);
            playNote((byte)71);
            Thread.sleep(100);
            playNote((byte)72);
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

    // Base function that stops a note
    private void stopNote(byte nota){
        byte[] musica = new byte[3];
        musica[0] = (byte) 0x80; // 0x80 -> note OFF in channel 0
        musica[1] = nota;
        musica[2] = (byte) 127;
        // Stop the note!
        midiDriver.write(musica);
    }

    // This function plays a note depending on what's in its text field.
    // It's used for the Piano to work properly, and to play the correct notes.
    private void determinaNota(Button v, boolean isStop, boolean isHigh)
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

        if (isHigh) // If we pressed the higher notes (the ones in blue)
        {
            nota +=12;
        }

        if (isStop) // Check if we have to stop the note or not
        {
            stopNote((byte)nota);
        }
        else {
            playNote((byte) nota); // Play the note, once we got which one it is.
        }

        //If it's recording, then store note value and delay in global arrays
        if (onOffToggle.getText().equals("ON") && !isStop)
        {
            notes.push(nota);
            delays.push(32/lunghezza);
        }
    }

    // This is the AsyncTask that will play notes (so that UI won't freeze while playing notes).
    public class ExtraThread extends AsyncTask<Void, Void, String>
    {
        public String doInBackground(Void...args) {
            if (notes.size() > 0) {
                for (int i = 0; i < notes.size(); i++) {
                    playNote(notes.get(i).byteValue()); // Play note...
                    try {
                        Thread.sleep((delays.get(i).intValue()) * 64); //... then wait for its delay.
                    } catch (Exception e) {}
                }
            }
            return "OK";
        }
        public void onPostExecute(String result)
        {
            outputMessage.setText(R.string.execend);
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
                sessionNum.setText(R.string.connectissues);
            }
            else if (result.equals("NOP")) // NOP -> We didn't record anything
            {
                outputMessage.setText("");
                sessionNum.setText(R.string.didntrec);
            }
            else {
                outputMessage.setText(R.string.recsent); // In none of above, record was sent!
                sessionNum.setText(result); // Give session number for user to use in the Weedim Web App.
            }
        }
    }


}
