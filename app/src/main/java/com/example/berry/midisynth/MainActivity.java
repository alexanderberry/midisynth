package com.example.berry.midisynth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.berry.midibackend.MidiConstants;

import org.billthefarmer.mididriver.MidiDriver;

public class MainActivity extends AppCompatActivity implements MidiDriver.OnMidiStartListener, View.OnTouchListener, AdapterView.OnItemSelectedListener {
    private MidiDriver midiDriver;

    private byte[] event;
    private int[] config;
    /**
     * An appended S signifies a note is sharp.
     * Oct signifies the note in the upper octave of the scale.
     */
    private Button playC;
    private Button playCS;
    private Button playD;
    private Button playDS;
    private Button playE;
    private Button playF;
    private Button playFS;
    private Button playG;
    private Button playGS;
    private Button playA;
    private Button playAS;
    private Button playB;
    private Button playCOct;

    private Spinner spinnerInstruments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //wire the buttons
        playC = (Button) findViewById(R.id.playC);
        playC.setOnTouchListener(this);
        playC.setText("C");
        playCS = (Button) findViewById(R.id.playCS);
        playCS.setOnTouchListener(this);
        playCS.setText("C#");
        playD = (Button) findViewById(R.id.playD);
        playD.setOnTouchListener(this);
        playD.setText("D");
        playDS = (Button) findViewById(R.id.playDS);
        playDS.setOnTouchListener(this);
        playDS.setText("D#");
        playE = (Button) findViewById(R.id.playE);
        playE.setOnTouchListener(this);
        playE.setText("E");
        playF = (Button) findViewById(R.id.playF);
        playF.setOnTouchListener(this);
        playF.setText("F");
        playFS = (Button) findViewById(R.id.playFS);
        playFS.setOnTouchListener(this);
        playFS.setText("F#");
        playG = (Button) findViewById(R.id.playG);
        playG.setOnTouchListener(this);
        playG.setText("G");
        playGS = (Button) findViewById(R.id.playGS);
        playGS.setOnTouchListener(this);
        playGS.setText("G#");
        playA = (Button) findViewById(R.id.playA);
        playA.setOnTouchListener(this);
        playA.setText("A");
        playAS = (Button) findViewById(R.id.playAS);
        playAS.setOnTouchListener(this);
        playAS.setText("A#");
        playB = (Button) findViewById(R.id.playB);
        playB.setOnTouchListener(this);
        playB.setText("B");
        playCOct = (Button) findViewById(R.id.playCOct);
        playCOct.setOnTouchListener(this);
        playCOct.setText("C4");

        midiDriver = new MidiDriver();
        midiDriver.setOnMidiStartListener(this);
        //wire the spinner
        spinnerInstruments = (Spinner)findViewById(R.id.spinnerInstruments);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.instruments_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInstruments.setAdapter(adapter);
        spinnerInstruments.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        midiDriver.start();
        //Retrieve configuration
        config = midiDriver.config();
        //Log the details of config. For debugging purposes
        Log.d(this.getClass().getName(), "maxVoices: " + config[0]);
        Log.d(this.getClass().getName(), "numChannels: " + config[1]);
        Log.d(this.getClass().getName(), "sampleRate: " + config[2]);
        Log.d(this.getClass().getName(), "mixBufferSize: " + config[3]);
    }

    @Override
    protected void onPause(){
        super.onPause();
        midiDriver.stop();
    }

    @Override
    public void onMidiStart(){
        Log.d(this.getClass().getName(), "onMidiStart()");
    }

    private void playNote(int noteNumber){
        //Creates a new note ON message for C3 at max v on channel 1
        event = new byte[3];
        //awful hacky sending individual bytes over the channel
        event[0] = (byte) (MidiConstants.STATUS_NOTE_ON|0x00); //0x00 = channel 1
        event[1] = (byte) noteNumber ; //0x3C (dec. 60) = C3
        event[2] = (byte) 0x7F; //0x7F = max. velocity (127)

        //Sends data to MIDI driver
        midiDriver.write(event);
    }

    private void stopNote(int noteNumber){
        //Creates a new note OFF message for C3 at min v on channel 1
        event = new byte[3];
        //more of the awful hacky sending individual bytes over the channel
        event[0] = (byte) (MidiConstants.STATUS_NOTE_OFF|0x00); //0x80 = note OFF, 0x00 = channel 1
        event[1] = (byte) noteNumber; //0x3C = C3
        event[2] = (byte) 0x00; //0x00 = min. velocity (0)

        //Sends data to MIDI driver
        midiDriver.write(event);
    }

    private void selectInstrument(int instrument){
        //Creates a program change to select an instrument on channel 1
        event = new byte[2];
        //even more awful hacky sending individual bytes over the channel
        event[0] = (byte) (MidiConstants.STATUS_PROGRAM_CHANGE|0x00); //0x00 = channel 1
        event[1] = (byte) (instrument);

        //Send data to MIDI driver
        midiDriver.write(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        Log.d(this.getClass().getName(), "Motion event: " + event);
        int noteNumber;
        //set the note number based on which button is pressed
        switch(v.getId()) {
            case R.id.playC:
                noteNumber = 60;
                break;
            case R.id.playCS:
                noteNumber = 61;
                break;
            case R.id.playD:
                noteNumber = 62;
                break;
            case R.id.playDS:
                noteNumber = 63;
                break;
            case R.id.playE:
                noteNumber = 64;
                break;
            case R.id.playF:
                noteNumber = 65;
                break;
            case R.id.playFS:
                noteNumber = 66;
                break;
            case R.id.playG:
                noteNumber = 67;
                break;
            case R.id.playGS:
                noteNumber = 68;
                break;
            case R.id.playA:
                noteNumber = 69; //lol
                break;
            case R.id.playAS:
                noteNumber = 70;
                break;
            case R.id.playB:
                noteNumber = 71;
                break;
            case R.id.playCOct:
                noteNumber = 72;
                break;
            default:
                noteNumber = -1;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
            playNote(noteNumber);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
            stopNote(noteNumber);
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        selectInstrument(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
