package com.example.mobiledevelopmentassigment02;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

public class Reading {
    String readingId;
    int systolicNum;
    int diastolicNum;
    String familyMember;
    String currentDate;
    String currentTime;
    String condition;


    public Reading() {}

    public Reading(String readingId, int systolicNum,
                   int diastolicNum, String familyMember,
                   String currentDate, String currentTime, String condition) {
        this.readingId = readingId;
        this.systolicNum = systolicNum;
        this.diastolicNum = diastolicNum;
        this.familyMember = familyMember;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.condition = condition;
    }

    public static String calcCondition(int systolic, int diastolic) {
        String condition = "";
        if(systolic < 120 && diastolic < 80){
            condition = "Normal";
        } else if((systolic <= 129 && systolic >= 120)
                && (diastolic < 80)){
            condition = "Elevated";
        } else if((systolic <= 139 && systolic >= 130)){
            condition = "High Blood Pressure (Stage 1)";
        } else if((diastolic <= 89 && diastolic >= 80)){
            condition = "High Blood Pressure (Stage 1)";
        } else if ((systolic >= 140 && systolic <= 180)){
            condition = "High Blood Pressure (Stage 2)";
        } else if (diastolic >= 90 && diastolic <= 120){
            condition = "High Blood Pressure (Stage 2)";
        } else if (systolic > 180 || diastolic > 120){
            condition = "Hypertensive Crisis";
        } else if (systolic > 180 && diastolic > 120){
            condition = "Hypertensive Crisis";
        }else {
            condition = "Unknown";
        }
        return condition;
    }

    public String getReadingId() { return readingId; }

    public void setReadingId(String readingId) {
        this.readingId = readingId;
    }

    public int getSystolicNum() {return systolicNum;}

    public void setSystolicNum(int systolicNum) {
        this.systolicNum = systolicNum;
    }

    public int getDiastolicNum() {return diastolicNum;}

    public void setDiastolicNum(int diastolicNum) {
        this.diastolicNum = diastolicNum;
    }

    public String getFamilyMember() {return familyMember;}

    public void setFamilyMember(String familyMember) {
        this.familyMember = familyMember;
    }

    public String getCurrentDate() { return currentDate; }

    public Boolean isCurMonth(){
        Calendar cal = Calendar.getInstance();
        DateFormatSymbols dfs =  new DateFormatSymbols();
        String[] months = dfs.getMonths();
        int number = cal.get(Calendar.MONTH);
        String month = "";
        if(number <= 11){
            month = months[number];
        }
        String[] parsedReadingDate = currentDate.split(" ");
        if(parsedReadingDate.length < 1) return false;
        String readingMonth = parsedReadingDate[0];
        return readingMonth.equals(month);
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() { return currentTime; }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCondition() { return condition; }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}


