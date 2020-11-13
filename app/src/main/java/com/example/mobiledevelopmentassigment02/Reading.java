package com.example.mobiledevelopmentassigment02;

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


