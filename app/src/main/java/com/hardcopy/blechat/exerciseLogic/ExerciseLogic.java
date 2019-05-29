package com.hardcopy.blechat.exerciseLogic;

import com.hardcopy.blechat.db.AppDatabase;

public class ExerciseLogic {

    private BIKE_METS_STRENGTH metsStrength;
    private AppDatabase db;
    private String date;
    private String userName;

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

    public ExerciseLogic(AppDatabase db){
        metsStrength = BIKE_METS_STRENGTH.LIGHT;
        this.db = db;

    }

    public ExerciseLogic(BIKE_METS_STRENGTH metsStrength , AppDatabase db){
        this.metsStrength = metsStrength;
        this.db = db;
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

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getUserName(){
        return  userName;
    }

    public  void setUserName(String userName){
        this.userName =userName;
    }

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

    private  Double getUserWeight(String userName){
        return  db.userDao().getWeightByName(userName).doubleValue();
    }

    public Double getKcal(){
        checkEmptyError(EMPTY_TYPE.USERNAME);
        checkEmptyError(EMPTY_TYPE.DATE);

        return (metsStrength.getValue() * (3.5 * getUserWeight(userName) * getSumTime(date)) * 5) / 1000;
    }

    public Double getKcal(String userName){
        checkEmptyError(EMPTY_TYPE.DATE);

        return (metsStrength.getValue() * (3.5 * getUserWeight(userName) * getSumTime(date)) * 5) / 1000;
    }

    public Double getKcal(String date , String userName){
        return (metsStrength.getValue() * (3.5 * getUserWeight(userName) * getSumTime(date)) * 5) / 1000;
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
