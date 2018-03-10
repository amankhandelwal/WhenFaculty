package com.when.threemb.whenfaculty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

//NGROK present
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //TODO change URL
    String string1 ="https://34fb35b1.ngrok.io/website";
    private String REGISTER_URL =string1+"/apiloginfac.php";
    //TODO change URL
    String urli = string1+"/apitimetablefac.php";
    String tokenx="";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";


    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button loginButton;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    boolean googleok=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        FirebaseAuth.getInstance().signOut();

        TextView tx1 = (TextView)findViewById(R.id.textView11);
        TextView tx2 = (TextView)findViewById(R.id.textView22);
        TextView tx3 = (TextView)findViewById(R.id.textView33);
        /*TextView tx4 = (TextView)findViewById(R.id.textView3);
        TextView tx5 = (TextView)findViewById(R.id.textView2);*/

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/FFF_Tusj.ttf");

        tx1.setTypeface(custom_font);
        tx2.setTypeface(custom_font);
        tx3.setTypeface(custom_font);
       /* tx4.setTypeface(custom_font);
        tx5.setTypeface(custom_font);*/


        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        GoogleSignInFunction();
    }
    //On button clicked
    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (username.length()!=0 && password.length()!=0) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(LoginActivity.this, response+"Sent successfully !", Toast.LENGTH_LONG).show();
                            //request initial/name/dept
                            Toast.makeText(LoginActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                            if(response.equalsIgnoreCase("error"))
                            {
                                Toast.makeText(LoginActivity.this,"Incorrect Credentials", Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                            }
                            else
                                parseJson(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "FAILED " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(KEY_USERNAME, username);
                    params.put(KEY_PASSWORD, password);
                    params.put("token",tokenx);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else
        {
            Toast.makeText(this,"Invalid Username/Password",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void parseJson(String response) {

        String dept,name,initials;
        try {
            JSONObject k = new JSONObject(response);
            JSONObject j=k.optJSONObject("keyvalue");
            name=j.optString("Fac_Name");
            dept=j.optString("Fac_Dept");
            initials=j.optString("Fac_Initials");
            Toast.makeText(this, "cred:"+name+" "+dept, Toast.LENGTH_SHORT).show();

            sp=getSharedPreferences("Userinfo",MODE_PRIVATE);
            editor=sp.edit();
            editor.putString("Name",name);
            editor.putString("Dept",dept);
            editor.putString("Initials",initials);
            editor.putInt("Status",1);
            editor.apply();
            retrieveData(initials);



        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void loginClick(View v) {
        Toast.makeText(this, "Signing into Google Account", Toast.LENGTH_SHORT).show();

        signIn();
        /*if(googleok) {

        }
        else
        {
            Toast.makeText(this, "Google Auth Failed", Toast.LENGTH_SHORT).show();
        }*/
    }


    public void retrieveData(String initials){
        urli+="?Fac_Initials="+initials;//to get timetable for that faculty
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //TODO update as required
        //Toast.makeText(LoginActivity.this, "initals"+initials+"\nurl"+urli, Toast.LENGTH_LONG).show();



        final JsonObjectRequest kor=new JsonObjectRequest(Request.Method.GET, urli, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject ob) {

                        //Toast.makeText(LoginActivity.this, "fetching response", Toast.LENGTH_SHORT).show();
                        if(ob!=null){
                            //TODO parse json data
                            //Toast.makeText(LoginActivity.this,"OUR response:"+ ob.toString(), Toast.LENGTH_SHORT).show();
                            //ppr_code,start_time,end_time,room_no,day
                            try {
                                JSONObject monday = ob.optJSONObject("Monday");
                                JSONObject tuesday = ob.optJSONObject("Tuesday");
                                JSONObject wednesday = ob.optJSONObject("Wednesday");
                                JSONObject thursday = ob.optJSONObject("Thursday");
                                JSONObject friday = ob.optJSONObject("Friday");
                                int i;
                                String Ppr_Code, Room_No,dow;
                                int Start_Time, End_Time,Ppr_Sem,Group_No;
                                DatabaseHandler object=new DatabaseHandler(LoginActivity.this);

                                //For Monday
                                JSONArray APpr_Code=monday.optJSONArray("Ppr_Code");
                                JSONArray ARoom_No=monday.optJSONArray("Room_No");
                                JSONArray AStart_Time=monday.optJSONArray("Start_Time");
                                JSONArray AEnd_Time=monday.optJSONArray("End_Time");
                                JSONArray APpr_Sem=monday.optJSONArray("Ppr_Sem");
                                JSONArray AGroup_No=monday.optJSONArray("Group_No");


                                //For Monday
                                for (i = 0; i < APpr_Code.length(); i++) {
                                    Ppr_Code = APpr_Code.optString(i);
                                    Room_No = ARoom_No.optString(i);
                                    Start_Time = AStart_Time.optInt(i);
                                    End_Time = AEnd_Time.optInt(i);
                                    Ppr_Sem = APpr_Sem.optInt(i);
                                    Group_No=AGroup_No.optInt(i);
                                    dow="Monday";
                                    MyTimetable obj=new MyTimetable(Ppr_Code,Room_No,Start_Time,End_Time,Ppr_Sem,Group_No,dow);
                                    object.addPeriod(obj);
                                }
                                //For Tuesday
                                 APpr_Code=tuesday.optJSONArray("Ppr_Code");
                                 ARoom_No=tuesday.optJSONArray("Room_No");
                                 AStart_Time=tuesday.optJSONArray("Start_Time");
                                 AEnd_Time=tuesday.optJSONArray("End_Time");
                                 APpr_Sem=tuesday.optJSONArray("Ppr_Sem");
                                 AGroup_No=tuesday.optJSONArray("Group_No");


                                //For Tuesday
                                for (i = 0; i < APpr_Code.length(); i++) {
                                    Ppr_Code = APpr_Code.optString(i);
                                    Room_No = ARoom_No.optString(i);
                                    Start_Time = AStart_Time.optInt(i);
                                    End_Time = AEnd_Time.optInt(i);
                                    Ppr_Sem = APpr_Sem.optInt(i);
                                    Group_No=AGroup_No.optInt(i);
                                    dow="Tuesday";
                                    //Toast.makeText(LoginActivity.this,"inside tuesday:"+ Ppr_Code+Room_No+Group_No+dow, Toast.LENGTH_SHORT).show();
                                    MyTimetable obj=new MyTimetable(Ppr_Code,Room_No,Start_Time,End_Time,Ppr_Sem,Group_No,dow);
                                    object.addPeriod(obj);
                                }

                                //For Wednesday
                                APpr_Code=wednesday.optJSONArray("Ppr_Code");
                                ARoom_No=wednesday.optJSONArray("Room_No");
                                AStart_Time=wednesday.optJSONArray("Start_Time");
                                AEnd_Time=wednesday.optJSONArray("End_Time");
                                APpr_Sem=wednesday.optJSONArray("Ppr_Sem");
                                AGroup_No=wednesday.optJSONArray("Group_No");
                                for (i = 0; i < APpr_Code.length(); i++) {
                                    Ppr_Code = APpr_Code.optString(i);
                                    Room_No = ARoom_No.optString(i);
                                    Start_Time = AStart_Time.optInt(i);
                                    End_Time = AEnd_Time.optInt(i);
                                    Ppr_Sem = APpr_Sem.optInt(i);
                                    Group_No=AGroup_No.optInt(i);
                                    dow="Wednesday";
                                    MyTimetable obj=new MyTimetable(Ppr_Code,Room_No,Start_Time,End_Time,Ppr_Sem,Group_No,dow);
                                    object.addPeriod(obj);
                                }
                                //For Thursday
                                APpr_Code=thursday.optJSONArray("Ppr_Code");
                                ARoom_No=thursday.optJSONArray("Room_No");
                                AStart_Time=thursday.optJSONArray("Start_Time");
                                AEnd_Time=thursday.optJSONArray("End_Time");
                                APpr_Sem=thursday.optJSONArray("Ppr_Sem");
                                AGroup_No=thursday.optJSONArray("Group_No");

                                for (i = 0; i < APpr_Code.length(); i++) {
                                    Ppr_Code = APpr_Code.optString(i);
                                    Room_No = ARoom_No.optString(i);
                                    Start_Time = AStart_Time.optInt(i);
                                    End_Time = AEnd_Time.optInt(i);
                                    Ppr_Sem = APpr_Sem.optInt(i);
                                    Group_No=AGroup_No.optInt(i);
                                    dow="Thursday";
                                    MyTimetable obj=new MyTimetable(Ppr_Code,Room_No,Start_Time,End_Time,Ppr_Sem,Group_No,dow);
                                    object.addPeriod(obj);
                                }
                                //For Friday
                                APpr_Code=friday.optJSONArray("Ppr_Code");
                                ARoom_No=friday.optJSONArray("Room_No");
                                AStart_Time=friday.optJSONArray("Start_Time");
                                AEnd_Time=friday.optJSONArray("End_Time");
                                APpr_Sem=friday.optJSONArray("Ppr_Sem");
                                AGroup_No=friday.optJSONArray("Group_No");
                                for (i = 0; i < APpr_Code.length(); i++) {
                                    Ppr_Code = APpr_Code.optString(i);
                                    Room_No = ARoom_No.optString(i);
                                    Start_Time = AStart_Time.optInt(i);
                                    End_Time = AEnd_Time.optInt(i);
                                    Ppr_Sem = APpr_Sem.optInt(i);
                                    Group_No=AGroup_No.optInt(i);
                                    dow="Friday";
                                    MyTimetable obj=new MyTimetable(Ppr_Code,Room_No,Start_Time,End_Time,Ppr_Sem,Group_No,dow);
                                    object.addPeriod(obj);
                                }

                                Intent TimeTable=new Intent(LoginActivity.this,TimeTable.class);
                                startActivity(TimeTable);
                                finish();
                                //TODO Copy paste for other days
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }


                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley","Error");
                        Toast.makeText(LoginActivity.this, "No data recieved", Toast.LENGTH_SHORT).show();
                        sp=getSharedPreferences("Userinfo",MODE_PRIVATE);
                        editor=sp.edit();
                        editor.putBoolean("Status",false);
                        editor.apply();
                    }
                }
        );


        //TODO add second request

        requestQueue.add(kor);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //finish();
        //TODO finish this activity from somewhere
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!=null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void GoogleSignInFunction()
    {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    Toast.makeText(LoginActivity.this,"Signed in as : "+ user.getUid(), Toast.LENGTH_SHORT).show();
                    sp=getSharedPreferences("Userinfo",MODE_PRIVATE);
                    editor=sp.edit();
                    editor.putString("Token",user.getUid());
                    editor.apply();
                    tokenx=user.getUid();
//                    googleok=true;
                    Toast.makeText(LoginActivity.this, "Please Wait! logging you in...", Toast.LENGTH_LONG).show();
                    registerUser();

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Signed out ", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();




    }

    /*public void signer(View view)
    {
        Toast.makeText(this, "Signing into Google Account", Toast.LENGTH_SHORT).show();

        signIn();
    }*/


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, "FAILED TO SIGN IN", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();

                        }
                        // ...
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "ERROR Logging in", Toast.LENGTH_SHORT).show();
    }

}

