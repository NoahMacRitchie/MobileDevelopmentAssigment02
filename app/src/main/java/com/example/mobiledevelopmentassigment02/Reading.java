package com.example.mobiledevelopmentassigment02;

public class Reading {
    String readingId;
    int systolicNum;
    int diastolicNum;
    String familyMember;

    public Reading() {}

    public Reading(String readingId, int systolicNum,
                   int diastolicNum, String familyMember) {
        this.readingId = readingId;
        this.systolicNum = systolicNum;
        this.diastolicNum = diastolicNum;
        this.familyMember = familyMember;
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
}
