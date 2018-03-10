package com.when.threemb.whenfaculty;

/**
 * Created by Freeware Sys on 10/16/2016.
 */
//NO NGROK
public class AttendanceObject {
    private String ppr_code,name;
    private int roll,attend,total,sem;

    public AttendanceObject(String ppr_code , int roll, String name ,int attend, int total,int sem) {
        this.attend = attend;
        this.name = name;
        this.ppr_code = ppr_code;
        this.roll = roll;
        this.total = total;
        this.sem=sem;
    }

    public int getSem() {
        return sem;
    }

    public void setSem(int sem) {
        this.sem = sem;
    }

    public int getAttend() {
        return attend;
    }

    public void setAttend(int attend) {
        this.attend = attend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPpr_code() {
        return ppr_code;
    }

    public void setPpr_code(String ppr_code) {
        this.ppr_code = ppr_code;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
