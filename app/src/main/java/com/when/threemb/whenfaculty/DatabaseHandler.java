package com.when.threemb.whenfaculty;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freeware Sys on 10/15/2016.
 */
//NO NGROK

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "When";

    // Contacts table name
    private static final String TABLE_NAME = "Timetable";
    private static final String TABLE_NAME2 = "Attendance";
    // Contacts Table Columns names
    private static final String KEY_PPR_CODE = "ppr_code";
    private static final String KEY_ROOM_NO = "room";
    private static final String KEY_START_TIME = "starttime";
    private static final String KEY_END_TIME = "endtime";
    private static final String KEY_DAY = "day";
    private static final String KEY_ROLL = "roll";
    private static final String KEY_NAME = "name";
    private static final String KEY_ATTEND = "attend";
    private static final String KEY_TOTAL= "total";
    private static final String KEY_SEM = "sem";
    private static final String KEY_GROUP = "grp";
    private static final String KEY_PPR_SEM = "ppr_sem";
    private static final String KEY_ST="StartTime";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                +KEY_PPR_CODE + " TEXT,"
                +KEY_ROOM_NO + " TEXT,"
                +KEY_START_TIME +" INTEGER,"
                +KEY_END_TIME+" INTEGER,"
                +KEY_PPR_SEM+" TEXT,"
                +KEY_GROUP+" INTEGER,"
                +KEY_DAY+" TEXT"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " + TABLE_NAME2 + "("
                + KEY_PPR_CODE + " TEXT,"
                + KEY_ROLL + " INTEGER,"
                + KEY_NAME +" TEXT,"
                +KEY_ATTEND+" INTEGER,"
                +KEY_TOTAL+" INTEGER,"
                +KEY_SEM+" INTEGER,"
                +"StartTime"+" INTEGER"
                +")";
        db.execSQL(CREATE_ATTENDANCE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    void addPeriod(MyTimetable period) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PPR_CODE, period.getPpr_Code());
        values.put(KEY_ROOM_NO, period.getRoom_No());
        values.put(KEY_START_TIME, period.getStart_Time());
        values.put(KEY_END_TIME, period.getEnd_Time());
        values.put(KEY_PPR_SEM, period.getSem());
        values.put(KEY_GROUP, period.getGroup());
        values.put(KEY_DAY, period.getDay());
        // Inserting Row
        db.insert(TABLE_NAME,null,values);
        db.close(); // Closing database connection
    }
    /*MyTimetable viewPeriod(String ppr_code){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_PPR_CODE,
                         KEY_ROOM_NO, KEY_START_TIME , KEY_END_TIME , KEY_DAY,KEY_GROUP }, KEY_PPR_CODE + "=?",
                new String[] { ppr_code }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MyTimetable period = null;//= new MyTimetable(cursor.getString(0),cursor.getString(1), cursor.getString(2),Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));
        // return contact
        return period;
    }*/
    //Changing Timetable Object to MyTimetable
    public ArrayList<MyTimetable> getAllPeriods(String day) {
        ArrayList<MyTimetable> periodList = new ArrayList<MyTimetable>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME+ " WHERE "+KEY_DAY+" = \""+day+"\"";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst() && cursor!=null ) {
            do {
                MyTimetable to = new MyTimetable(cursor.getString(0),
                        cursor.getString(1),
                        Integer.parseInt( cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt( cursor.getString(5)),
                        cursor.getString(6));
                // Adding contact to list
                periodList.add(to);
            } while (cursor.moveToNext());
        }
        // return contact list
        return periodList;
    }




    void addStudent(String paper,int roll,String name,int attend,int total,int sem,int startTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PPR_CODE, paper);
        values.put(KEY_ROLL, roll);
        values.put(KEY_NAME, name); // Contact Name
        values.put(KEY_ATTEND, attend); // Contact Phone
        values.put(KEY_TOTAL, total);
        values.put(KEY_SEM,sem);
        values.put("StartTime",startTime);
        long newRowId=db.insert(TABLE_NAME2, null, values);
        db.close(); // Closing database connection
    }

    void updateAttendance(String paper,int roll,String name,int attend,int total,int sem,int startTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PPR_CODE, paper);
        values.put(KEY_ROLL, roll);
        values.put(KEY_NAME, name); // Contact Name
        values.put(KEY_ATTEND, attend); // Contact Phone
        values.put(KEY_TOTAL, total);
        values.put(KEY_SEM, sem);
        values.put("StartTime",startTime);

        long newRowId=db.update(TABLE_NAME2, values,KEY_ROLL+"=?",new String[]{Integer.toString(roll)});
        db.close(); // Closing database connection
    }
    public ArrayList<AttendanceObject> getAllStudent(String ppr_code,int startTime) {
        ArrayList<AttendanceObject> attendanceList = new ArrayList<AttendanceObject>();
        // Select All Query
        //TODO where ppr code =ppr_code
        //String selectQuery = "SELECT "+KEY_NAME+" AND "+KEY_ROLL + " FROM " + TABLE_NAME2;
        String selectQuery = "SELECT * FROM " + TABLE_NAME2+ " WHERE "+KEY_PPR_CODE+" = \""+ppr_code+"\" AND StartTime = "+startTime;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    AttendanceObject ao = new AttendanceObject(
                            cursor.getString(0),
                            Integer.parseInt(cursor.getString(1)),
                            cursor.getString(2),
                            Integer.parseInt(cursor.getString(3)),
                            Integer.parseInt(cursor.getString(4)),
                            Integer.parseInt(cursor.getString(5)));
                    // Adding contact to list
                    attendanceList.add(ao);
                } while (cursor.moveToNext());
            }
        }

        // return contact list
        return attendanceList;
    }




    public void deleteEntry(String Ppr_code,int startTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "DELETE FROM " + TABLE_NAME2+ " WHERE "+KEY_PPR_CODE+" = \""+Ppr_code+"\" AND StartTime = "+startTime;
        db.execSQL(selectQuery);
        db.close();
    }

    public String[] sabBhejo(){
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery="SELECT DISTINCT "+KEY_PPR_CODE+" FROM " +TABLE_NAME2;
        Cursor cursor=db.rawQuery(selectQuery,null);
        int i=0;
        String arr[]=new String[15];
        if(cursor!=null)
        {
            do{
                arr[i]=cursor.getString(0);
            }while(cursor.moveToNext());
        }
        return arr;
    }


    public ArrayList<AttendanceObject> getAllStudentDisplay() {
        ArrayList<AttendanceObject> attendanceList = new ArrayList<AttendanceObject>();
        // Select All Query
        //TODO where ppr code =ppr_code
        //String selectQuery = "SELECT "+KEY_NAME+" AND "+KEY_ROLL + " FROM " + TABLE_NAME2;
        String selectQuery = "SELECT * FROM " + TABLE_NAME2;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    int starttym=Integer.parseInt(cursor.getString(6));
                    AttendanceObject ao = new AttendanceObject(
                            starttym+" "+cursor.getString(0),
                            Integer.parseInt(cursor.getString(1)),
                            cursor.getString(2),
                            Integer.parseInt(cursor.getString(3)),
                            Integer.parseInt(cursor.getString(4)),
                            Integer.parseInt(cursor.getString(5)));

                    // Adding contact to list
                    attendanceList.add(ao);
                } while (cursor.moveToNext());
            }
        }

        // return contact list
        return attendanceList;
    }

    public void dropthebeatbaby()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("DELETE FROM " + TABLE_NAME2);

    }
}

