package com.when.threemb.whenfaculty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//NGROK PRESENT

public class TimeTable extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String string1="https://34fb35b1.ngrok.io/website";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* StudentListActivity objectb=new StudentListActivity();
                DatabaseHandler dbd=new DatabaseHandler(TimeTable.this);
                dbd.addStudent("INFO3102",1454035,"Rahul Agarwal",0,0,5);
                for(int i=0;i<periods.size();i++)
                {

                    objectb.uploadDataToServer(periods.get(i).getPpr_Code(),periods.get(i).getGroup());
                }*/ //TODO Manually update to server....uncomment and check
                Snackbar.make(view, "DATA UPLOADED SERVER", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int dow = (calendar.get(Calendar.DAY_OF_WEEK)) - 1;
        String days="Monday";

        switch(dow)
        {
            case 1:days="Monday";
                break;
            case 2:days="Tuesday";
                break;
            case 3:days="Wednesday";
                break;
            case 4:days="Thursday";
                break;
            case 5:days="Friday";
                break;
            default:
                Toast.makeText(TimeTable.this, "HOLIDAY", Toast.LENGTH_SHORT).show();
                finish();
        }

        firstFunction(days);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.time_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           /* sp=getSharedPreferences("Userinfo",MODE_PRIVATE);
            editor=sp.edit();
            editor.putString("Name","");
            editor.putString("Dept","");
            editor.putString("Initials","");
            editor.putInt("Status",0);
            editor.apply();
            DatabaseHandler db = new DatabaseHandler(TimeTable.this);
            db.dropthebeatbaby();
            FirebaseAuth.getInstance().signOut();
            finish();*/
            Toast.makeText(this, "Hobe Naaa !!", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String days="Sunday";

        if (id == R.id.monday) {
            days="Monday";
            Intent jj =new Intent(TimeTable.this,AnyDay.class);
            jj.putExtra("dow",days);
            startActivity(jj);

        } else if (id == R.id.tuesday) {
            days="Tuesday";
            Intent jj =new Intent(TimeTable.this,AnyDay.class);
            jj.putExtra("dow",days);
            startActivity(jj);

        } else if (id == R.id.wednesday) {
            days="Wednesday";
            Intent jj =new Intent(TimeTable.this,AnyDay.class);
            jj.putExtra("dow",days);
            startActivity(jj);

        } else if (id == R.id.thursday) {
            days="Thursday";
            Intent jj =new Intent(TimeTable.this,AnyDay.class);
            jj.putExtra("dow",days);
            startActivity(jj);

        } else if (id == R.id.friday) {
            days="Friday";
            Intent jj =new Intent(TimeTable.this,AnyDay.class);
            jj.putExtra("dow",days);
            startActivity(jj);

        } else if (id == R.id.nav_send) {
            Intent i =new Intent(TimeTable.this,DatabaseContents.class);
            startActivity(i);

        }else if (id == R.id.nav_share) {

            Intent i =new Intent(TimeTable.this,AboutUs.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    TimetableAdapter adapter;
    ListView timetableListView;
    ArrayList<MyTimetable> periods;

    public void firstFunction(String days)
    {
        String dow;
        sp=getSharedPreferences("Day",MODE_PRIVATE);
        dow=sp.getString("Today","Sunday");


        DatabaseHandler obj=new DatabaseHandler(this);
        /*final ArrayList<MyTimetable> */periods=obj.getAllPeriods(days);// get that day's timetable

        timetableListView = (ListView) findViewById(R.id.list);
        adapter = new TimetableAdapter(this, 0, periods);
        timetableListView.setAdapter(adapter);
        //TODO add shared preference
        if(days.equals(dow)==false) {
            editor=sp.edit();
            editor.putString("Today",days);
            editor.apply();
            //Toast.makeText(this, "period.size"+periods.size(), Toast.LENGTH_SHORT).show();
            for (int i = 0; i < periods.size(); i++) {
                MyTimetable timeob = periods.get(i);
                //request m ppr_code bhejo group and sem
                //MainActivity object5 = new MainActivity();
                if (laaDo())
                    volley(timeob.getPpr_Code(), timeob.getGroup(), timeob.getSem(),timeob.getStart_Time()); //Student DAtabase...name and attendance
            }
        }

        timetableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyTimetable ohh=periods.get(i);
                //TODO chande subject to sem


                String ppr_code=ohh.getPpr_Code();
                int start_time=ohh.getStart_Time();
                int end_time=ohh.getEnd_Time();
                int grp_no=ohh.getGroup();
                int total;
                total=end_time-start_time;
                if(total<=95)//TODO make provision for ED and WORKSHOP
                    total=1;
                 else if (total>95 && total<=190)
                    total=2;
                else
                    total=3;


               /* DatabaseHandler dc=new DatabaseHandler(TimeTable.this);

                dc.updateAttendance(ppr_code,1454035,"Rohit",69,76,5);
                ArrayList<AttendanceObject> jaan=dc.getAllStudent(ppr_code);
                for (int z=0;z<jaan.size();z++)
                    Toast.makeText(TimeTable.this,"Roll"+jaan.get(z).getRoll()+"Attend"+jaan.get(z).getAttend()+"Total"+jaan.get(z).getTotal(), Toast.LENGTH_SHORT).show();
                */
                Intent stu_list=new Intent(TimeTable.this,StudentListActivity.class);
                stu_list.putExtra("ppr_code",ppr_code);
                stu_list.putExtra("Group_No",grp_no);
                stu_list.putExtra("total",total);
                stu_list.putExtra("StartTime",start_time);
                startActivity(stu_list);
                //TODO show from the database the name & roll no of student in the papercode


            }
        });

    }
    public void volley(final String ppr, final int gr, final int semm, final int startTime){
        final DatabaseHandler dbhandler=new DatabaseHandler(TimeTable.this);
        dbhandler.deleteEntry(ppr,startTime);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //TODO change get url pass group and sem
        String urli=string1+"/apilist.php";//?ROLL=
        urli+="?Ppr_Code="+ppr+"&Group_No="+gr+"&Ppr_Sem="+semm;

        //Toast.makeText(this, "VOLLEY CALLED", Toast.LENGTH_SHORT).show();


        final JsonObjectRequest kor=new JsonObjectRequest(Request.Method.GET, urli, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject ob) {

                        if(ob!=null){
                            JSONArray roll= ob.optJSONArray("Stud_Roll_No");
                            JSONArray stu_name= ob.optJSONArray("Stud_Name");
                            JSONArray attend= ob.optJSONArray("Stud_Attend");
                            int total= ob.optInt("Stud_Total");
                            //Toast.makeText(TimeTable.this, "Server Total:"+total, Toast.LENGTH_SHORT).show();
                            //TODO add total
//                            StudentListActivity objectx=new StudentListActivity();
//                            objectx.uploadDataToServer(ppr,gr);
                            //DatabaseHandler dbhandler=new DatabaseHandler(TimeTable.this);
                            for(int i=0;i<roll.length() && i<stu_name.length();i++)
                            {
                                /*if(false)//TODO add status condition
                                    dbhandler.updateAttendance(subject.optString(i),attend.optInt(i),10*//*total.optInt(i)*//*);
                                else*/
                                dbhandler.addStudent(ppr,roll.optInt(i),stu_name.optString(i),
                                        0,0,semm,startTime);
                                if(i==3)
                                    Toast.makeText(TimeTable.this, "Sem@3="+semm, Toast.LENGTH_SHORT).show();
                            }




                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley","Error");
                        Toast.makeText(TimeTable.this, "No data recieved", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(kor);
    }
    public Boolean laaDo(/*View view*/)//Check internet connection
    {

        //Toast.makeText(MainActivity.this, "Please Wait !!", Toast.LENGTH_SHORT).show();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;

        } else {
            // display error
            Toast.makeText(TimeTable.this, "NO INTERNET CONNECTION !!", Toast.LENGTH_SHORT).show();
            return false;

        }

    }

}
