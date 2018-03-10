package com.when.threemb.whenfaculty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


//NO NGROK
public class MainActivity extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(MainActivity.this, "Main Activty", Toast.LENGTH_SHORT).show();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int dow = (calendar.get(Calendar.DAY_OF_WEEK)) - 1;
        tv=(TextView)findViewById(R.id.helloworld);
        SharedPreferences sp = getSharedPreferences("Userinfo", MODE_PRIVATE);
        /*SharedPreferences dow;
        SharedPreferences.Editor doweditor;
        dow=getSharedPreferences("Day",MODE_PRIVATE);
        doweditor=dow.edit();
        doweditor.putString("Today","Sunday");
        doweditor.apply();*/

        int status = sp.getInt("Status", 0);
        if (dow >= 1 && dow < 6) {
            if (status == 0 && laaDo()) { //Never Logged In

                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
            }
        /*else if(status==1 && laaDo())
        {
            DatabaseHandler db=new DatabaseHandler(this);
            StudentListActivity stu=new StudentListActivity();
            String arr[]=db.sabBhejo();
            for(int i=0;i<arr.length;i++)
            {
                db.get
                stu.uploadDataToServer()
            }
        }*/
            else if (status == 1)//Logged in once
            {
                Intent TimeTable = new Intent(this, TimeTable.class);
                startActivity(TimeTable);
            } else
                Toast.makeText(this, "Try again Later", Toast.LENGTH_SHORT).show();

        }
        else
        {
            tv.setText("HOLIDAY !!!");
        }
    }

    //TODO KAHA KAHA SE ONCLICK CALL AKRNA HAI
    //TODO KAL SE DIMAAG LAGAO
    //TODO BYE

    public Boolean laaDo(/*View view*/)//Check internet connection
    {

        //Toast.makeText(MainActivity.this, "Please Wait !!", Toast.LENGTH_SHORT).show();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            return true;

        } else {
            // display error
            Toast.makeText(MainActivity.this, "NO INTERNET CONNECTION !!", Toast.LENGTH_SHORT).show();
            tv.setText("No Internet Connection");
            return false;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
