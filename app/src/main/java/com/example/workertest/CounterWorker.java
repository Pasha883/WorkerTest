package com.example.workertest;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CounterWorker extends Worker {

    public static final String KEY_COUNTER_NAME = "COUNTER_NAME";

    public CounterWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String counterName = getInputData().getString(KEY_COUNTER_NAME);

        for (int i = 1; i <= 30; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return Result.failure();
            }

            sendCounterUpdate(counterName, i);
        }
        return Result.success();
    }

    private void sendCounterUpdate(String counterName, int value) {
        Intent intent = new Intent(counterName + "_UPDATE");
        intent.putExtra("value", value);
        getApplicationContext().sendBroadcast(intent);
    }
}
