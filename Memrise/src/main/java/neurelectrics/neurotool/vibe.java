package neurelectrics.neurotool;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;


public class vibe extends IntentService implements SensorEventListener {
    PowerManager powerMgr;
    SensorManager hub;
    Sensor accelerometer;
    float oldx=0;
    float oldy=0;
    float oldz=0;
    Vibrator vibrator;
    boolean vibrationOn=false;
    long vibeStart=0;
    int arousals=0;
    long BACKDOWN_PERIOD=5*60000;
    //long BACKDOWN_PERIOD=0;
    int totalStims=0;
    long initialStart=0;
    long INITIAL_DELAY=40*60000;
    //long INITIAL_DELAY=0;
    long MAX_STIMS=60*6;
    long MAX_STIM_TIME=180*60000;
    boolean keepRunning=true;
    int vibePower=40;
    boolean learnMode=false;
    boolean sham=false;
    long sleepStart=0;
    public vibe() {
        super("vibe");
    }

    void startVibration() {
        if (vibrator != null) {
            vibrator.cancel();
        }
        vibrationOn=true;
        long[] vibrationPattern = {vibePower, 1200};
        int[] amp={255,0};
        VibrationEffect vibe=VibrationEffect.createWaveform(vibrationPattern,amp,0);
        vibrator.vibrate(vibe);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("vibe","Running");
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();
        vibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        startVibration();
        final SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sham=sharedPref.getBoolean("shamMode",false);
        final SharedPreferences.Editor editor = sharedPref.edit();
        if (sharedPref.getInt("runservice",1)==2) { //only do this if we are configured for sleep mode rather than learning mode
            vibeStart = System.currentTimeMillis() + INITIAL_DELAY;
            sleepStart = System.currentTimeMillis();
            hub = (SensorManager) getSystemService(SENSOR_SERVICE);
            accelerometer = hub.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            hub.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
            vibePower=sharedPref.getInt("userIntensity",40);
            if (sham) { //sham works just by making the power 0
                vibePower=0;
            }
        }
        else {
            learnMode=true;
            vibePower=60;
        }




        while (keepRunning) {
            try {
                Thread.sleep(10000);
            }
            catch (Exception e) {

            }
            if (vibrationOn) {
                totalStims++;
                if (!learnMode) {
                    editor.putInt("stimMinutes", totalStims);
                    editor.commit();
                }
            }
            if ((System.currentTimeMillis() >= vibeStart && totalStims < MAX_STIMS && System.currentTimeMillis() <= sleepStart+MAX_STIM_TIME) || learnMode) {
                startVibration();
            }
            else if (totalStims >= MAX_STIMS || System.currentTimeMillis() > sleepStart+MAX_STIM_TIME) {
                vibrator.cancel();
                vibrationOn=false;
            }
            if (sharedPref.getInt("runservice",0) >= 3) { //main activity has asked this to shut down.
                keepRunning=false;
                vibrator.cancel();
            }
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (Math.abs(event.values[0]-oldx) > 0.4 || Math.abs(event.values[1]-oldy) > 0.4 || Math.abs(event.values[2]-oldz) > 0.4) {
            if (vibrationOn) {
                arousals++;
                final SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(this);
                final SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("arousals",arousals);
                editor.commit();
                if (!sham) {
                    vibePower = vibePower - 15;
                    if (vibePower < 15) {
                        vibePower = 15;
                    }
                }
            }
            if (vibrator != null) {
                vibrator.cancel();
                vibrationOn = false;
                vibeStart = System.currentTimeMillis() + BACKDOWN_PERIOD;
            }

        }
        oldx=event.values[0];
        oldy=event.values[1];
        oldz=event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
