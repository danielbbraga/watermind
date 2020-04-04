package com.example.watermind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_NOTIFY = "key_notify";
    private static final String KEY_INTERVAL = "key_interval";
    private static final String KEY_HOUR = "key_hour";
    private static final String KEY_MINUTE = "key_minute";

    private TimePicker timePicker;
    private Button btnNotify;
    private EditText editMinutes;

    private int interval;
    private int hour;
    private int minute;

    private boolean activated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = findViewById(R.id.time_picker);
        btnNotify = findViewById(R.id.button_notify);
        editMinutes = findViewById(R.id.edit_number_interval);

        final SharedPreferences storage = getSharedPreferences("storage", Context.MODE_PRIVATE);
        activated = storage.getBoolean(KEY_INTERVAL, false);

        if (activated){
            btnNotify.setText(R.string.pause);
            btnNotify.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray));
            editMinutes.setText(String.valueOf(storage.getInt(KEY_INTERVAL, 0)));
            timePicker.setCurrentHour(storage.getInt(KEY_HOUR, timePicker.getCurrentHour()));
            timePicker.setCurrentMinute(storage.getInt(KEY_MINUTE, timePicker.getCurrentMinute()));

        } else {
            btnNotify.setText(R.string.notify);
            btnNotify.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_blue_light));

        }

        timePicker.setIs24HourView(true);

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activated) {
                    String sInterval = editMinutes.getText().toString();

                    if (sInterval.isEmpty()) {
                        Toast.makeText(MainActivity.this, getString(R.string.textoIntervalo), Toast.LENGTH_LONG).show();
                        return;
                    }

                    interval = Integer.parseInt(sInterval);
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();

                    btnNotify.setText(R.string.pause);
                    btnNotify.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray));

                    SharedPreferences.Editor edit = storage.edit();
                    edit.putBoolean(KEY_NOTIFY, true);
                    edit.putInt(KEY_INTERVAL, interval);
                    edit.putInt(KEY_HOUR, hour);
                    edit.putInt(KEY_MINUTE, minute);
                    edit.apply();

                    activated = true;
                } else {

                    btnNotify.setText(R.string.notify);
                    btnNotify.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_blue_light));

                    SharedPreferences.Editor edit = storage.edit();
                    edit.putBoolean(KEY_NOTIFY, false);
                    edit.remove(KEY_INTERVAL);
                    edit.remove(KEY_HOUR);
                    edit.remove(KEY_MINUTE);
                    edit.apply();

                    activated = false;
                }


            }
        });



    }
}
