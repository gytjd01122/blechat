package com.hardcopy.blechat.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Room GPS Entity 클래스로 테이블을 정의함. <p>
 *
 * @author KIM HYO SEONG
 * @see GpsDao
 * @see AppDatabase
 */
@Entity
public class Gps {

    public Gps(@NonNull String date, @NonNull Long time, @NonNull Double distance) {
        this.date = date;
        this.time = time;
        this.distance = distance;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @NonNull
    @ColumnInfo(name = "time")
    private Long time;

    @NonNull
    @ColumnInfo(name = "distance")
    private Double distance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
