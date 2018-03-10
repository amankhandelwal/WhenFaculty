package com.when.threemb.whenfaculty;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AnyDay extends AppCompatActivity {
    TimetableAdapter adapter;
    ListView timetableListView;
    ArrayList<MyTimetable> periods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_any_day);
        String days="Sunday";

        // BACK BUTTON
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle kuchbhi=getIntent().getExtras();
        if(kuchbhi==null) {
            Toast.makeText(this, "No data recieved from timetable", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            days = kuchbhi.getString("dow");
            ab.setTitle(days);
        }
        DatabaseHandler obj=new DatabaseHandler(this);
        periods=obj.getAllPeriods(days);// get that day's timetable

        timetableListView = (ListView) findViewById(R.id.list);
        adapter = new TimetableAdapter(this, 0, periods);
        timetableListView.setAdapter(adapter);
    }
}
