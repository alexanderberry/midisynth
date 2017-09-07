package com.example.berry.midibackend.synth;

import android.media.midi.MidiDeviceService;
import android.media.midi.MidiDeviceStatus;
import android.media.midi.MidiReceiver;

/**
 * Created by per6 on 8/31/17.
 */

public class SynthDeviceService extends MidiDeviceService {
    private static final String TAG = "SynthDeviceService";
    private SynthEngine mSynthEngine = new SynthEngine();
    private boolean synthStarted = false;

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public void onDestroy(){
        mSynthEngine.stop();
        super.onDestroy();
    }

    @Override
    // Declares receivers associated with input ports
    public MidiReceiver[] onGetInputPortReceivers(){
        return new MidiReceiver[] {mSynthEngine};
    }

    @Override
    /**
     * Called when clients connect or disconnect.
     * Used only to turn on synth when needed.
     */
    public void onDeviceStatusChanged(MidiDeviceStatus status){
        if(status.isInputPortOpen(0) && !synthStarted){
            mSynthEngine.start();
            synthStarted = true;
        }
        else if(!status.isInputPortOpen(0) && synthStarted){
            mSynthEngine.stop();
            synthStarted = false;
        }
    }
}
