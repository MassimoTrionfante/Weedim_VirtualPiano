package com.massimotrionfante.weedimvirtualpiano;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.billthefarmer.mididriver.MidiDriver;

import java.lang.reflect.Array;
import java.util.Stack;


public class MainActivity extends AppCompatActivity implements MidiDriver.OnMidiStartListener {

    // Data structures for Midi to work
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
    private ImageView lowerOctave;
    private ImageView upperOctave;
    private TextView ottavaVisual;
    private ImageView lowerLength;
    private ImageView upperLength;
    private TextView lengthVisual;
    private TextView onOffToggle;
    private ImageView playButton;
    private ImageView undo;
    private ImageView rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        ottava = 4;
        lunghezza = 4;

        notes = new Stack<Integer>();
        delays = new Stack<Integer>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Put all onClickListeners here, to serve the basic piano functionality
        noteC.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); }});
        noteCm.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); }});
        noteD.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); }});
        noteDm.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); }});
        noteE.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); }});
        noteF.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); }});
        noteFm.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); }});
        noteG.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); }});
        noteGm.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); }});
        noteA.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); }});
        noteAm.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); }});
        noteB.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { determinaNota((Button)v); }});

        // Play recorded song on play press
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<notes.size();i++)
                {
                    playNote(notes.get(i).byteValue());
                    try {
                        Thread.sleep( (32 / delays.get(i).intValue()) * 64);
                    } catch (Exception e){}
                }
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
                }
            }
        });

        // Push a rest when clicking on the black box
        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes.push(0);
                delays.push(lunghezza);
            }
        });

        //Lower octave by 1
        lowerOctave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ottava>1) {
                    ottava--;
                    ottavaVisual.setText(Integer.toString(ottava));
                }
            }
        });

        //Upper octave by 1
        upperOctave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ottava<7) {
                    ottava++;
                    ottavaVisual.setText(Integer.toString(ottava));
                }
            }
        });

        //Lower note lenght
        lowerLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lunghezza!=16) {
                    lunghezza *= 2;
                    lengthVisual.setText(Integer.toString(lunghezza));
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
                }
                else
                {
                    onOffToggle.setText("OFF");
                }
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
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        midiDriver.stop();
    }

    @Override
    public void onMidiStart() {

        // Beginning jingle (55 57 59 60 with 0.1 second delay)
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

    private void playNote(byte nota){
        byte[] musica = new byte[3];
        musica[0] = (byte) 0x90; // 0x90 -> note ON
        musica[1] = nota;
        musica[2] = (byte) 127; // velocity al massimo (127)
        // Play a note!
        midiDriver.write(musica);
    }

    private void determinaNota(Button v)
    {
        String ottavaStr = v.getText().toString();
        int nota=0;

        // Process note pitch...
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

        // Process major notes...
        if (ottavaStr.length()==2) // Process major notes
        {
            nota += 1;
        }

        playNote((byte)nota);

        //If it's recording, then store note value and delay in global arrays
        if (onOffToggle.getText().equals("ON"))
        {
            notes.push(nota);
            delays.push(lunghezza);
        }

    }

}
