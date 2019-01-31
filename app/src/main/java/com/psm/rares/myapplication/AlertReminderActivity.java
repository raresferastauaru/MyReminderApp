package com.psm.rares.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlertReminderActivity extends Activity {

    SQLiteDatabase db;
    String CurrentReminderTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=openOrCreateDatabase("ReminderDb", Context.MODE_PRIVATE, null);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(this, notification);
        r.play();

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("You have a reminder!");
        builder.setMessage(ReadCurrentReminder());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AlertReminderActivity.this.finish();
            }
        });
        builder.show();

        db.execSQL("DELETE FROM Reminders WHERE dateTime = '" + CurrentReminderTime + "'");

    }

    private String ReadCurrentReminder()
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();

        CurrentReminderTime = df.format(calendar.getTimeInMillis());

        System.out.println(CurrentReminderTime);

        Cursor c =db.rawQuery("SELECT * FROM Reminders WHERE dateTime = '" + CurrentReminderTime + "'", null);
        if(c.moveToNext())
            return c.getString(0);

        return "There was a problem retrieving the data!";
    }
}
