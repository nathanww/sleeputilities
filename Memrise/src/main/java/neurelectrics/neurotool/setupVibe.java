package neurelectrics.neurotool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class setupVibe extends WearableActivity {

    Vibrator vibrator;

    boolean vibrationOn=false;

    void startVibration(int power) {
        if (vibrator != null) {
            vibrator.cancel();
        }
        vibrationOn=true;
        long[] vibrationPattern = {power, 1200};
        int[] amp={255,0};
        VibrationEffect vibe=VibrationEffect.createWaveform(vibrationPattern,amp,0);
        vibrator.vibrate(vibe);
    }


    @Override
    protected void onUserLeaveHint() {
        vibrator.cancel();
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        vibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        startVibration(40);

        final SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPref.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_vibe);
        final SeekBar setting =findViewById(R.id.seekBar3);
        setting.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                editor.putInt("userIntensity",progress);
                editor.commit();
                startVibration(progress);

            }
        });
        Button startSleep=(Button) findViewById(R.id.startbut);
        startSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox enableSham=(CheckBox)findViewById(R.id.checkBox4) ;
                editor.putBoolean("shamMode",false);
                editor.commit();
                if (enableSham.isChecked()) {
                    Random rand=new Random();
                    if (rand.nextInt(10) > 5) {
                        editor.putBoolean("shamMode",true);
                        editor.commit();
                    }
                }
                Intent myIntent = new Intent(setupVibe.this, sleepmode.class);
                setupVibe.this.startActivity(myIntent);

            }
        });
    }
}
