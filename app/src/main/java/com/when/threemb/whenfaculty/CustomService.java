package com.when.threemb.whenfaculty;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.when.threemb.whenfaculty.StudentListActivity.TAG_TASK_ONEOFF;

/*
 * Created by USER on 18-01-2017.
 */
public class CustomService extends GcmTaskService {
    String string1="https://34fb35b1.ngrok.io/website";
    String Ggirri="";
    String Sem="";
    String Total="";
    String Group_no="";
    String Ppr_code="";
    Integer startTime,i;
    NotificationManager mNotificationManager;
    List<TaskParams> taskItems = new ArrayList<>();

    @Override
    public int onRunTask(TaskParams taskParams) {
        Log.i(TAG, "onRunTask");
        switch (taskParams.getTag()) {
            case TAG_TASK_ONEOFF+"900":
            case TAG_TASK_ONEOFF+"955":
            case TAG_TASK_ONEOFF+"1050":
            case TAG_TASK_ONEOFF+"1225":
            case TAG_TASK_ONEOFF+"1320":
            case TAG_TASK_ONEOFF+"1415":
            case TAG_TASK_ONEOFF+"1510":
            case TAG_TASK_ONEOFF+"1605":
            case TAG_TASK_ONEOFF+"1700":
                taskItems.add(taskParams);
                // This is where useful work would go



                if( upload())
                    return GcmNetworkManager.RESULT_SUCCESS;

            default:
                return GcmNetworkManager.RESULT_FAILURE;
        }


    }
    public boolean upload(){
        for (int i=0;i<taskItems.size();i++){
            Bundle se=taskItems.get(i).getExtras();
            Ggirri=se.getString("JSON");
            Sem=se.getString("Stud_Sem");
            Group_no=se.getString("Group_No");
            Ppr_code=se.getString("Ppr_Code");
            Total=se.getString("Total");
            startTime=se.getInt("startTime");
            uploadToServer();
        }
        return true;
    }

    public boolean uploadToServer() {



            final String REGISTER_URL = string1+"/apiupdate.php"; //TODO check url

            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            DatabaseHandler db = new DatabaseHandler(CustomService.this);
                            db.deleteEntry(Ppr_code,startTime);
                            // Notify listeners (MainActivity) that task was completed successfully.
       /*                     Intent message=new Intent("completed");
                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(CustomService.this);
                            localBroadcastManager.sendBroadcast(message);*/
                            NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(CustomService.this);

                            mBuilder.setContentTitle("New Message");
                            mBuilder.setContentText("You've received new message.");
                            mBuilder.setTicker("New Message Alert!");

   /* Increase notification number every time a new notification arrives */

   /* Add Big View Specific Configuration */
                            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                            String[] events = new String[3];
                            events[0] = new String("This is first line....");
                            events[1] = new String("This is second line...");

                            // Sets a title for the Inbox style big view
                            inboxStyle.setBigContentTitle("Updated successfully:");

                            // Moves events into the big view
                            for (int i=0; i < events.length; i++) {
                                inboxStyle.addLine(events[i]);
                            }

                            mBuilder.setStyle(inboxStyle);

   /* Creates an explicit intent for an Activity in your app */
                            /*Intent resultIntent = new Intent(this, NotificationView.class);

                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                            stackBuilder.addParentStack(NotificationView.class);

   *//* Adds the Intent that starts the Activity to the top of the stack *//*
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

                            mBuilder.setContentIntent(resultPendingIntent);*/
                            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */
                            mNotificationManager.notify(0, mBuilder.build());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //TODO result false
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("JSON",Ggirri);
                    params.put("Stud_Sem",Sem);
                    params.put("Group_No",Group_no);
                    params.put("Ppr_Code",Ppr_code);
                    params.put("Total",Total);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        return true;
        }
    }

