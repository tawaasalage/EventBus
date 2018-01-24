package com.apextech.eventbuslibrarytutorial;

import android.app.Application;

import  com.apextech.eventbuslibrarytutorial.Events.*;

import com.evernote.android.job.JobManager;

public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new DemoJobCreator());
    }
}