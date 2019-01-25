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

public class sleepmode extends WearableActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepmode);
        sharedPref =  PreferenceManager.getDefaultSharedPreferences(this);
         editor = sharedPref.edit();
        editor.putInt("runservice",2); //tell service to go to sleep mode
        editor.putInt("arousals",0);
        editor.putInt("stimMinutes",0);
        editor.commit();


        Intent msgIntent = new Intent(getApplicationContext(), vibe.class);
        startService(msgIntent);

        Button stopButton=(Button) findViewById(R.id.endbutton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("runservice",3);
                editor.commit();
                Intent myIntent = new Intent(sleepmode.this, MainActivity.class);
                sleepmode.this.startActivity(myIntent);
            }
        });
    }

    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        editor.putInt("runservice",3);
        editor.commit();
        finish();
    }



}
