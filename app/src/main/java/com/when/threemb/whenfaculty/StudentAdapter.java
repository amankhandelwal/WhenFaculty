package com.when.threemb.whenfaculty;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 8/21/2016.
 */

//NO NGROK
public class StudentAdapter extends ArrayAdapter<AttendanceObject> {
    TextView attendanceView,subjectView,teacherView,startTimeView,endTimeView,roomNoView;
    int res;


    public StudentAdapter(Context context, int resource, List<AttendanceObject> objects) {
        super(context, resource, objects);
        res=resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View listItemView=convertView;
        if(listItemView==null)
        {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.attendance_list_item,parent,false);
        }
        AttendanceObject currentDay=getItem(position);

        attendanceView=(TextView)listItemView.findViewById(R.id.attend_magnitude);
        int attend,total,percent;
        attend=currentDay.getAttend();
        total=currentDay.getTotal();
        if(total==0)
            percent=0;
        else
            percent=(attend*100)/total;


        attendanceView.setText(Integer.toString(attend));

        roomNoView=(TextView)listItemView.findViewById(R.id.Roll_No);
        roomNoView.setText(Integer.toString(currentDay.getRoll()));

        subjectView=(TextView)listItemView.findViewById(R.id.Stud_Name);
        subjectView.setText(currentDay.getName());
        //TODO remove face initial from here and timetable_list_view

        GradientDrawable attendanceCircle = (GradientDrawable) attendanceView.getBackground();

        // Get the appropriate background color based on the current MyTimetableObject attendance
        int attendanceColor = getAttendanceColor(Integer.toString(attend));

        // Set the color on the attendance circle
        attendanceCircle.setColor(attendanceColor);



        return listItemView;
    }


    private int getAttendanceColor(String s) {
        int magnitude=Integer.parseInt(s);
        int magnitudeColorResourceId;
        int magnitudeFloor = (int)(magnitude/10);
        switch (magnitude) {
            case 1:
                magnitudeColorResourceId = R.color.green;
                break;
            case 0:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.green;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

}

