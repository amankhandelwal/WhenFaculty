package com.when.threemb.whenfaculty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//NGROK PRESENT
public class StudentListActivity extends AppCompatActivity {
    ListView studentListView;
    StudentAdapter adapter;
    ArrayList<AttendanceObject> studList;
    int arr[]=new int[100];
    String s="";
    int grp_no,totalint,startTime;
    DatabaseHandler obj=new DatabaseHandler(this);
    String string1="https://34fb35b1.ngrok.io/website";
    StringRequest stringRequest;

    private static final int RC_PLAY_SERVICES = 123;
    private GcmNetworkManager mGcmNetworkManager;
    public static final String TAG_TASK_ONEOFF="upload";


     String Ggirri="";
     String Sem="";
     String Total="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        mGcmNetworkManager = GcmNetworkManager.getInstance(this);

        // BACK BUTTON
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        final int group;
        Bundle kuchbhi=getIntent().getExtras();
        if(kuchbhi==null) {
            Toast.makeText(this, "No data recieved from timetable", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            s = kuchbhi.getString("ppr_code");
            grp_no=kuchbhi.getInt("Group_No",0);
            totalint=kuchbhi.getInt("total",0);
            startTime=kuchbhi.getInt("StartTime",0);
            //Toast.makeText(this, "Total milla"+totalint, Toast.LENGTH_LONG).show();
        }
        studList=obj.getAllStudent(s,startTime);
        studentListView = (ListView) findViewById(R.id.stud_list);
        adapter = new StudentAdapter(this, 0, studList);
        if(studList.isEmpty() || studList.size()==0)
            finish();
        studentListView.setAdapter(adapter);
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*i=(int)l;*/
//                Toast.makeText(StudentListActivity.this, "L="+i, Toast.LENGTH_SHORT).show();
                arr[i]=arr[i]+1;//TODO multiple period attendance
                arr[i]=arr[i]%2;

                studList.get(i).setAttend(arr[i]*totalint);
                adapter.notifyDataSetChanged();
                /*adapter = new StudentAdapter(StudentListActivity.this, 0, studList);
                studentListView.setAdapter(adapter);*/

            }
        });

        checkPlayServicesAvailable();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Toast.makeText(StudentListActivity.this, "Upload button", Toast.LENGTH_SHORT).show();
                uploadDataToServer(s,grp_no);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }


    public void updateClick(View view){
        /*int totalek=0,attendek=0,j;
        for(j=0;j<studList.size();j++)
        {   //TODO do this upload in main activity
            AttendanceObject object1=studList.get(j);
            totalek=(object1.getTotal());
            attendek=object1.getAttend();
            *//*if(arr[j]==1)
            attendek+=totalint-1;*//*

            if(attendek>0)
            Toast.makeText(this, "IN DB Total="+totalek+"  Attend="+attendek+" for "+object1.getName(), Toast.LENGTH_SHORT).show();
            //obh.updateAttendance(s,object1.getRoll(),object1.getName(),attendek,totalek+totalint,object1.getSem(),startTime);
        }*/
        //Toast.makeText(this, "J="+j, Toast.LENGTH_SHORT).show();

        //MainActivity object4=new MainActivity();
        /*if(laaDo()==false)//connected to web
        {
            DatabaseHandler obh=new DatabaseHandler(this);
            for(int j=0;j<studList.size();j++){
                AttendanceObject obj=studList.get(j);
                obh.updateAttendance(s,obj.getRoll(),obj.getName(),obj.getAttend(),totalint,obj.getSem(),startTime);
            }


        }*/
        //else{
            uploadDataToServer(s,grp_no);
        //}

        finish();

    }
    private void checkPlayServicesAvailable() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int resultCode = availability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (availability.isUserResolvableError(resultCode)) {
                // Show dialog to resolve the error.
                availability.getErrorDialog(this, resultCode, RC_PLAY_SERVICES).show();
            } else {
                // Unresolvable error
                Toast.makeText(this, "Google Play Services error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void uploadDataToServer(String sub, final int group_no) {

        JSONArray Roll=new JSONArray();
        JSONArray Attend=new JSONArray();
        JSONObject jsonstr=new JSONObject();
        //studList.clear();
        //ArrayList<AttendanceObject> attendance=db.getAllStudent(s);
        int isem=0,itotal=0,i;
        if(studList.size()>0) {

            for (i = 0; i < studList.size(); i++) {
                Roll.put(studList.get(i).getRoll());
                Attend.put(studList.get(i).getAttend());
                isem=studList.get(i).getSem();
                itotal=studList.get(i).getTotal();
            }
            Toast.makeText(this,"sem="+isem, Toast.LENGTH_SHORT).show();

            try {
                jsonstr.put("Stud_Roll", Roll);
                jsonstr.put("Stud_Attend", Attend);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, jsonstr.toString(), Toast.LENGTH_LONG).show();

            final String REGISTER_URL = string1+"/apiupdate.php"; //TODO check url

            Ggirri = jsonstr.toString();
            Sem = Integer.toString(isem);
            Total = Integer.toString(itotal);
            Toast.makeText(StudentListActivity.this, "total sent=" + totalint+" Subject="+s+" Sem="+Sem, Toast.LENGTH_SHORT).show();

            stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //TODO change button color
                            Toast.makeText(StudentListActivity.this, "Uploaded succesfully", Toast.LENGTH_SHORT).show();
                            /*
                                $ Not using the database directly uploading
                             */
                            DatabaseHandler db = new DatabaseHandler(StudentListActivity.this);
                            db.deleteEntry(s,startTime);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(StudentListActivity.this, "NO internet connection. Scheduling for later upload!" + error.toString(), Toast.LENGTH_LONG).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("JSON",Ggirri);
                            bundle.putString("Stud_Sem",Sem);
                            bundle.putString("Group_No",Integer.toString(grp_no));
                            bundle.putString("Ppr_Code",s);
                            bundle.putString("Total",Integer.toString(totalint));
                            bundle.putInt("startTime",startTime);
            /*
                *Staring the JOB SCHEDULING
             */
                            Task task = new OneoffTask.Builder()
                                    .setService(CustomService.class)
                                    .setExecutionWindow(0, 30)
                                    .setTag(TAG_TASK_ONEOFF+startTime)
                                    .setUpdateCurrent(false)
                                    .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                                    .setRequiresCharging(false)
                                    .setExtras(bundle)
                                    .build();

                            mGcmNetworkManager.schedule(task);

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("JSON",Ggirri);
                    params.put("Stud_Sem",Sem);
                    params.put("Group_No",Integer.toString(group_no));
                    params.put("Ppr_Code",s);
                    params.put("Total",Integer.toString(totalint));
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }else
            Toast.makeText(this, "attendance null", Toast.LENGTH_SHORT).show();

    }
    public Boolean laaDo(/*View view*/)//Check internet connection
    {

        //Toast.makeText(MainActivity.this, "Please Wait !!", Toast.LENGTH_SHORT).show();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            //
            return true;

        } else {
            // display error
            Toast.makeText(StudentListActivity.this, "NO INTERNET CONNECTION !!", Toast.LENGTH_SHORT).show();
            return false;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        studList.clear();
        finish();
    }



}

