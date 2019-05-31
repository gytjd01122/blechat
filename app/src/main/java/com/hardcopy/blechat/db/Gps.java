package com.hardcopy.blechat.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * Room GPS Entity 클래스로 테이블을 정의함. <p>
 *
 * @author KIM HYO SEONG
 * @see GpsDao
 * @see AppDatabase
 */
@Entity(primaryKeys = {"date","time"})
public class Gps {

    public Gps(@NonNull String date, @NonNull Long time, @NonNull Double distance) {
        this.date = date;
        this.time = time;
        this.distance = distance;
    }

    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @NonNull
    @ColumnInfo(name = "time")
    private Long time;

    @NonNull
    @ColumnInfo(name = "distance")
    private Double distance;

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public Long getTime() {
        return time;
    }

    public void setTime(@NonNull Long time) {
        this.time = time;
    }

    @NonNull
    public Double getDistance() {
        return distance;
    }

    public void setDistance(@NonNull Double distance) {
        this.distance = distance;
    }

}
