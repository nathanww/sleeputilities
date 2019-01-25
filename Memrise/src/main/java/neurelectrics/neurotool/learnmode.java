package neurelectrics.neurotool;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class learnmode extends WearableActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learnmode);
        vibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sharedPref =  PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();
        editor.putInt("runservice",1); //tell service to go to sleep mode
        editor.commit();


        Intent msgIntent = new Intent(getApplicationContext(), vibe.class);
        startService(msgIntent);
       final ToggleButton learnButton=(ToggleButton) findViewById(R.id.toggleButton);
       learnButton.setChecked(true);
        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (learnButton.isChecked()) {
                    editor.putInt("runservice",1); //tell service to go to learn mode
                    editor.commit();
                    Intent msgIntent = new Intent(getApplicationContext(), vibe.class);
                    startService(msgIntent);
                }
                else {
                    editor.putInt("runservice",3); //tell service to go to stop
                    editor.commit();
                    vibrator.cancel();
                }
            }
        });
    }





}