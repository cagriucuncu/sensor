package com.cagriucuncu.sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Switch switch1;
    SensorManager sensorManager;
    SensorEventListener sensorEventListener;
    Sensor sensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSensors();
        init_UI();


    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(sensorEvent.sensor.getStringType() + "->" + sensorEvent.sensor.getName() +"\n" );
                stringBuilder.append(sensorEvent.values[0] + "\n");
                stringBuilder.append(sensorEvent.timestamp);

                textView.setText(stringBuilder.toString());
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    private void init_UI() {
        textView = (TextView) findViewById(R.id.textView);
        switch1 = (Switch) findViewById(R.id.switch1);

        CompoundButton.OnCheckedChangeListener switch1listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked) {
                    sensorManager.unregisterListener(sensorEventListener);
                    List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Sensor sensor : sensorList) {
                        stringBuilder.append(sensor.getType() + " -> " + sensor.getName() + "\n");
                    }
                    textView.setText(stringBuilder.toString());
                } else {
                    sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                    textView.setText("Switch on to list sensors");
                }
            }
        };

        switch1.setOnCheckedChangeListener(switch1listener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }
}