package com.apextech.eventbuslibrarytutorial.Shedulers;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import com.apextech.eventbuslibrarytutorial.Events.Events;
import com.apextech.eventbuslibrarytutorial.Events.GlobalBus;

import java.io.File;
import java.util.List;

//Require API Level 21
public class MyJobService extends BroadcastReceiver {

    public MyJobService() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Events.ServiceMessage serviceMessage =new Events.ServiceMessage("Service Message");
        GlobalBus.getBus().post(serviceMessage);

    }


}