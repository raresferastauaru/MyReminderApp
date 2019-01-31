package com.psm.rares.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddReminderActivity extends AppCompatActivity {


    SQLiteDatabase db;
    EditText titleEditor;
    DatePicker datePicker;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(onClickListener);

        titleEditor = (EditText) findViewById(R.id.editText9);
        datePicker = (DatePicker) findViewById(R.id.datePicker4);
        timePicker = (TimePicker) findViewById(R.id.timePicker4);

        db=openOrCreateDatabase("ReminderDb", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Reminders(title VARCHAR,dateTime VARCHAR);");
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {

            String sqlString = "INSERT INTO Reminders VALUES('" + titleEditor.getText() + "','" + GetDateTime() + "');";
            db.execSQL(sqlString);

            SetAlarm();

            AddReminderActivity.super.onBackPressed();

        }
    };

    private String GetDateTime()
    {
        return datePicker.getYear() + "-" + GetTimeValue(datePicker.getMonth() + 1) + "-" + GetTimeValue(datePicker.getDayOfMonth())
                + " " + GetTimeValue(timePicker.getCurrentHour()) + ":" + GetTimeValue(timePicker.getCurrentMinute());
    }

    private String GetTimeValue(int pickerValue)
    {
        if(pickerValue < 10)
            return "0" + Integer.toString(pickerValue);
        else
            return Integer.toString(pickerValue);
    }

    private void SetAlarm()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
        Long time = calendar.getTimeInMillis();
        Intent intentAlarm = new Intent(this, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
