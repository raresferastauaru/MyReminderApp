package com.psm.rares.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonAdd = (Button) findViewById(R.id.button5);
        buttonAdd.setOnClickListener(onClickListenerAdd);

        Button buttonView = (Button) findViewById(R.id.button6);
        buttonView.setOnClickListener(onClickListenerView);

        Button buttonDelete = (Button) findViewById(R.id.button7);
        buttonDelete.setOnClickListener(onClickListenerDelete);

        db=openOrCreateDatabase("ReminderDb", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Reminders(title VARCHAR,dateTime VARCHAR);");
    }

    View.OnClickListener onClickListenerAdd = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context,AddReminderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    };

    View.OnClickListener onClickListenerView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            populateRemindersList();
        }
    };

    View.OnClickListener onClickListenerDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            db.execSQL("DELETE FROM Reminders");
            showMessage("Information", "All reminders have been deleted!");
        }
    };

    private void populateRemindersList()
    {
        Cursor c=db.rawQuery("SELECT * FROM Reminders", null);
        if(c.getCount()==0)
        {
            showMessage("Reminders", "No reminders to show");
            return;
        }
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
            buffer.append("Title: "+c.getString(0)+"\n");
            buffer.append("Date: "+c.getString(1)+"\n");
            buffer.append("\n");
        }
        showMessage("Reminders", buffer.toString());
    }

    private void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}