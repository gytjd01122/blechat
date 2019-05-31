package com.hardcopy.blechat.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Room USER Entity 클래스로 테이블을 정의함. <p>
 *
 * @author KIM HYO SEONG
 * @see UserDao
 * @see AppDatabase
 */
@Entity
public class User {

    public User(String name, String weight, String height) {
        this.name = name;
        this.weight = weight;
        this.height = height;
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "weight")
    private String weight;

    @NonNull
    @ColumnInfo(name = "height")
    private String height;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(@NonNull String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(@NonNull String height) {
        this.height = height;
    }
}
