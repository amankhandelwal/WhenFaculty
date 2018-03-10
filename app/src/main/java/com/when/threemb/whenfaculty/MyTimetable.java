package com.when.threemb.whenfaculty;

/**
 * Created by Freeware Sys on 10/15/2016.
 */

//NO NGROK

public class MyTimetable {

    private String Ppr_Code, Room_No,Day;
    private int Start_Time, End_Time,Group,Sem;

    public MyTimetable(String ppr_Code, String room_No, int start_Time, int end_Time ,int sem, int group,String day) {
        Ppr_Code = ppr_Code;
        Room_No = room_No;
        Start_Time = start_Time;
        End_Time = end_Time;
        Sem=sem;
        Group = group;
        Day = day;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public int getEnd_Time() {
        return End_Time;
    }

    public void setEnd_Time(int end_Time) {
        End_Time = end_Time;
    }

    public int getGroup() {
        return Group;
    }

    public void setGroup(int group) {
        Group = group;
    }

    public String getPpr_Code() {
        return Ppr_Code;
    }

    public void setPpr_Code(String ppr_Code) {
        Ppr_Code = ppr_Code;
    }

    public String getRoom_No() {
        return Room_No;
    }

    public void setRoom_No(String room_No) {
        Room_No = room_No;
    }

    public int getSem() {
        return Sem;
    }

    public void setSem(int sem) {
        Sem = sem;
    }

    public int getStart_Time() {
        return Start_Time;
    }

    public void setStart_Time(int start_Time) {
        Start_Time = start_Time;
    }
}
