package com.example.plantsmart;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import static com.example.plantsmart.RegisterActivity.SHARED_PREFS;

public class ExampleJobService extends Service {
    public Timer timer;
    public TimerTask timerTask;
    public static boolean started = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        startTimer();
        StartStatusAndTimerListener();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel serviceChannel = new NotificationChannel(
                    "serviceNotificationChannelId",
                    "Hidden Notification Service",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notifManager.createNotificationChannel(serviceChannel);

            Intent hidingIntent = new Intent(this, NotificationBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    1,
                    hidingIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            Notification notification = new Notification.Builder(this, "serviceNotificationChannelId")
                    .setContentTitle("System is stable")
                    .setContentText("Check out your plant")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .getNotification();
            startForeground(1, notification);
        } else {
            Notification.Builder builder = new Notification.Builder(this);
            this.startForeground(-1, builder.getNotification());
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void push() {
    }

    public void startNotificationListener(final String title, final String content) {
        //start's a new thread
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                //fetching notifications from server
                //if there is notifications then call this method
                not(title, content);
            }
        }).start();
    }

    String oldContent = "";

    public void StartStatusAndTimerListener() {
        FirebaseDatabase.getInstance().getReference().child("plants").child(loadData("Plant")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String content = "";
                if (snapshot.getValue() != null) {
                    if (snapshot.child("RealTimeVal").child("temperature").getValue() != null && snapshot.child("IdealValues").child("temperature").getValue() != null) {
                        if (getFloat(snapshot.child("RealTimeVal").child("temperature").getValue()) > getFloat(snapshot.child("IdealValues").child("temperature").getValue())) {
                            content += "-Temperature is higher than threshold temperature\n";
                        }
                    }

                    if (snapshot.child("RealTimeVal").child("moisture2").getValue() != null && snapshot.child("RealTimeVal").child("moisture1").getValue() != null && snapshot.child("IdealValues").child("moisture").getValue() != null) {
                        if (getFloat(snapshot.child("RealTimeVal").child("moisture1").getValue()) > getFloat(snapshot.child("IdealValues").child("moisture").getValue()) || getFloat(snapshot.child("RealTimeVal").child("moisture2").getValue()) > getFloat(snapshot.child("IdealValues").child("moisture").getValue())) {
                            content += "-The soil is dry\n";
                        }
                    }

                    if (snapshot.child("RealTimeVal").child("Tank 1").getValue() != null && snapshot.child("IdealValues").child("tank water level").getValue() != null) {
                        if (getFloat(snapshot.child("RealTimeVal").child("Tank 1").getValue()) < getFloat(snapshot.child("IdealValues").child("tank water level").getValue())) {
                            content += "-Water Tank is Low\n";
                        }
                    }

                    if (snapshot.child("RealTimeVal").child("rain").getValue()!=null) {
                        if (getFloat(snapshot.child("RealTimeVal").child("rain").getValue()) < 900) {
                            content += "-Rain is detected\n";
                        }
                    }
                }
                if (!content.isEmpty() && oldContent.compareTo(content) != 0) {
                    oldContent = content;
                    startNotificationListener("Warning!", content);
                } else if (content.isEmpty() && !oldContent.isEmpty()) {
                    oldContent = content;
                    startNotificationListener("System is stable", "Check out your plant");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        started = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void not(String title, String content) {
        Intent resultIntent = new Intent(this, RealTimeMonActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(RealTimeMonActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        ArrayList<String> cont = new ArrayList<>();
        int size = 4;
        String[] arr = content.split("\n", -1);
        for (int i = 0; i < size; i++) {
            try {
                cont.add(arr[i]);
            } catch (Exception ignored) {
                cont.add("");
            }
        }
        Notification notification = new Notification.Builder(this, "serviceNotificationChannelId")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setStyle(new Notification.InboxStyle()
                        .addLine(cont.get(0))
                        .addLine(cont.get(1))
                        .addLine(cont.get(2))
                        .addLine(cont.get(3))
                        .setSummaryText("click to view")).getNotification();
        startForeground(1, notification);
        //the n
    }

    public float getFloat(Object s) {
        try {
            return (float) Double.parseDouble(s.toString());
        } catch (Exception ignored) {
            return 0;
        }
    }

    public boolean isNumric(String string) {
        boolean numeric = true;
        try {
            Long num = Long.parseLong(string);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        return numeric;
    }

    public String loadData(String name) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences == null) {
            return "";
        }
        return sharedPreferences.getString(name, "");
    }

    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {


            }
        };
        timer.schedule(timerTask, 0, 2000);
    }
}