package com.example.workertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    private TextView textViewCounter1, textViewCounter2;
    private WorkManager workManager;
    private OneTimeWorkRequest counter1Request, counter2Request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCounter1 = findViewById(R.id.textViewCounter1);
        textViewCounter2 = findViewById(R.id.textViewCounter2);
        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonStop = findViewById(R.id.buttonStop);

        workManager = WorkManager.getInstance(this);

        buttonStart.setOnClickListener(v -> startCounters());
        buttonStop.setOnClickListener(v -> stopCounters());

        registerReceivers();
    }

    private void startCounters() {
        Data counter1Data = new Data.Builder().putString(CounterWorker.KEY_COUNTER_NAME, "COUNTER_1").build();
        Data counter2Data = new Data.Builder().putString(CounterWorker.KEY_COUNTER_NAME, "COUNTER_2").build();

        counter1Request = new OneTimeWorkRequest.Builder(CounterWorker.class).setInputData(counter1Data).build();
        counter2Request = new OneTimeWorkRequest.Builder(CounterWorker.class).setInputData(counter2Data).build();

        workManager.enqueue(counter1Request);
        workManager.enqueue(counter2Request);
    }

    private void stopCounters() {
        workManager.cancelWorkById(counter1Request.getId());
        workManager.cancelWorkById(counter2Request.getId());
    }

    private void registerReceivers() {
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int value = intent.getIntExtra("value", 0);
                textViewCounter1.setText("Counter 1: " + value);
            }
        }, new IntentFilter("COUNTER_1_UPDATE"));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int value = intent.getIntExtra("value", 0);
                textViewCounter2.setText("Counter 2: " + value);
            }
        }, new IntentFilter("COUNTER_2_UPDATE"));
    }
}
