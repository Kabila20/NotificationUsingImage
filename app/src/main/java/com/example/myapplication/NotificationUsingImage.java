package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class NotificationUsingImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showNotificationImg();

            }
        });

    }
    @SuppressLint("staticFieldLeak")
    private void showNotificationImg()
    {
        new AsyncTask<String, Void, Bitmap>()
        {
            @Override
            protected Bitmap doInBackground(String... strings) {
                InputStream inputStream;
                try{
                    URL url = new URL(strings [0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    inputStream =connection.getInputStream();
                    return BitmapFactory.decodeStream(inputStream);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                showNotification(bitmap);
            }
        }.execute("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQctXVLMqc908wZly9RQA3q8navcqPA4dVRenrxNe4b&s");
    }
    private void showNotification(Bitmap bitmap)
    {
        int notificationid = new Random().nextInt(100);
        String channelid = "notifi_1";

        NotificationManager notificationManager = (NotificationManager)  getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, SecondActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),channelid

        );
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_24);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Its just an notification demo");
        builder.setContentTitle("Image notifi");
         builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelid) == null)
            {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelid,
                        "Notifi_Name_1",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("it willl notifiy the user");
                notificationChannel.enableVibration(true);
                notificationChannel.enableLights(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Notification notification = builder.build();
        if (notification != null)
        {
            notificationManager.notify(notificationid,notification);
        }
    }
}