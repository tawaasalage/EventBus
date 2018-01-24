package com.apextech.eventbuslibrarytutorial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apextech.eventbuslibrarytutorial.Events.Events;
import com.apextech.eventbuslibrarytutorial.Events.GlobalBus;
import com.apextech.eventbuslibrarytutorial.Events.ShowNotificationJob;
import com.apextech.eventbuslibrarytutorial.Shedulers.MyJobService;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    JobScheduler jobScheduler;
    private static final int MYJOBID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);


        addFragment();

        //ShowNotificationJob.schedulePeriodic();
        startJob();
    }

    public void connectToScale(View view)
    {


    }

    public void startJob()
    {
        PendingIntent pendingIntent;
        Intent alarmIntent = new Intent(MainActivity.this, MyJobService.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 5000;
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
    }

    public void cancelAll()
    {
        List<JobInfo> allPendingJobs = jobScheduler.getAllPendingJobs();
        String s = "";
        for(JobInfo j : allPendingJobs){
            int jId = j.getId();
            jobScheduler.cancel(jId);
            s += "jobScheduler.cancel(" + jId + " )";
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cancelAll();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register this fragment to listen to event.
        GlobalBus.getBus().register(this);
    }

    private void addFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new UserFragment())
                .commit();
    }

    public void sendMessageToFragment(View view) {
        EditText etMessage = (EditText) findViewById(R.id.activityData);
        Events.ActivityFragmentMessage activityFragmentMessageEvent =new Events.ActivityFragmentMessage(String.valueOf(etMessage.getText()));

        GlobalBus.getBus().post(activityFragmentMessageEvent);
    }

    @Subscribe
    public void getMessage(Events.FragmentActivityMessage fragmentActivityMessage) {
        TextView messageView = (TextView) findViewById(R.id.message);
        messageView.setText(getString(R.string.message_received) + " " + fragmentActivityMessage.getMessage());

        Toast.makeText(getApplicationContext(),getString(R.string.message_main_activity) + " " + fragmentActivityMessage.getMessage(),Toast.LENGTH_SHORT).show();
    }


    @Subscribe
    public void getMessage(Events.ServiceMessage serviceMessage) {
        TextView messageView = (TextView) findViewById(R.id.lblServiceCallback);
        messageView.setText(serviceMessage.getMessage());

        Toast.makeText(getApplicationContext(),serviceMessage.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }
}
