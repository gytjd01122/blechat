package com.hardcopy.blechat.exerciseLogic;

import android.app.Activity;
import com.hardcopy.blechat.db.AppDatabase;
import com.hardcopy.blechat.setting.UserSetting;

public class ExerciseLogic {

    private BIKE_METS_STRENGTH metsStrength;
    private AppDatabase db;

    private  String date;
    private String userName;
    private Double userWeight;

    public enum BIKE_METS_STRENGTH {

        VERY_LIGHT(3) , LIGHT(5.5) , MEDIUM(7) , HEAVY(10.5) , VERY_HEAVY(12.5);

        double value;

        private BIKE_METS_STRENGTH(double value){
            this.value = value;
        }
        public double getValue(){
            return value;
        }
    }

    private  enum EMPTY_TYPE {
        DATE , USERNAME
    }

    public ExerciseLogic( Activity activity , AppDatabase db , String date){
        metsStrength = BIKE_METS_STRENGTH.LIGHT;
        this.db = db;

        UserSetting.init(activity);
        userName = getUserName();
        userWeight = getUserWeight();
        this.date = date;

    }

    public ExerciseLogic(Activity activity, BIKE_METS_STRENGTH metsStrength , AppDatabase db , String date){
        this.metsStrength = metsStrength;
        this.db = db;

        UserSetting.init(activity);

        userName = getUserName();
        userWeight = getUserWeight();
        this.date = date;
    }

    public BIKE_METS_STRENGTH getMetsStrength() {
        return metsStrength;
    }

    public void setMetsStrength(BIKE_METS_STRENGTH metsStrength){
        this.metsStrength = metsStrength;
    }

    private  void setDataBaseSoure(AppDatabase db){
        this.db = db;
    }

    private String getUserName(){
        return  UserSetting.getName();
    }

    private  Double getUserWeight(){ return UserSetting.getWeight().doubleValue(); }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public Long getSumTime(){
        checkEmptyError(EMPTY_TYPE.DATE);

        return db.gpsDao().getSumTimeByDate(date);
    }

    public Long getSumTime(String date){
        return db.gpsDao().getSumTimeByDate(date);
    }

    public Double getDistance(){
        checkEmptyError(EMPTY_TYPE.DATE);

        return db.gpsDao().getSumDistanceByDate(date);
    }

    public Double getDistance(String date){
        return db.gpsDao().getSumDistanceByDate(date);
    }

    public Double getKcal(){
        checkEmptyError(EMPTY_TYPE.USERNAME);
        checkEmptyError(EMPTY_TYPE.DATE);

        return (metsStrength.getValue() * (3.5 * getUserWeight() * (getSumTime(date)/60) * 5)) / 1000;
    }

    public Double getKcal(String date){
        return (metsStrength.getValue() * (3.5 * getUserWeight() * (getSumTime(date)/60) * 5)) / 1000;
    }

    private void checkEmptyError(EMPTY_TYPE error){

        switch (error) {
            case DATE:
                if(date.isEmpty()) { throw new NullPointerException("date를 입력하지 않았습니다."); }
                break;
            case USERNAME:
                if(userName.isEmpty()) { throw new NullPointerException("userName를 입력하지 않았습니다."); }
                break;
        }

    }

}
