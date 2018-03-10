package com.when.threemb.whenfaculty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
//NO NGROK
public class DatabaseContents extends AppCompatActivity {
    TextView dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_contents);
        dbc=(TextView)findViewById(R.id.table1);
        String s="Strtym PprCode Roll Attend Total Sem";
        int i;
        DatabaseHandler dbh=new DatabaseHandler(DatabaseContents.this);
        ArrayList<AttendanceObject> listitems=dbh.getAllStudentDisplay();
        for(i=0;i<listitems.size();i++)
        {
            AttendanceObject obj=listitems.get(i);
            //String ppr_code , int roll, String name ,int attend, int total,int sem
            s+="\n"+obj.getPpr_code()+" "+obj.getRoll()+" "+obj.getAttend()+" "+obj.getTotal()+" "+obj.getSem();
        }
        dbc.setText(s);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
