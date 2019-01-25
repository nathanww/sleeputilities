package neurelectrics.neurotool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPref.edit();
        String arousalMessage="";
        TextView stats=(TextView)findViewById(R.id.statustext);
        if (sharedPref.getBoolean("shamMode",false)) {
            stats.setText("Last night was a sham session");
        }
         else {
            if (sharedPref.getInt("arousals", 0) > 12) {
                arousalMessage = "It looks like the vibrations kept you up a bit. You may want to try a lower intensity.";
            }

            stats.setText("LAST NIGHT\nRan for " + (sharedPref.getInt("stimMinutes", 0) * 10) / 60 + " minutes\n" + arousalMessage);
        }
        Button sleepMode=(Button) findViewById(R.id.sleep);
        sleepMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, setupVibe.class);
                MainActivity.this.startActivity(myIntent);
                finish();
            }
        });

        Button learnMode=(Button) findViewById(R.id.learn);
        learnMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, learnmode.class);
                MainActivity.this.startActivity(myIntent);

            }
        });
        // Enables Always-on
        //setAmbientEnabled();
    }
}
