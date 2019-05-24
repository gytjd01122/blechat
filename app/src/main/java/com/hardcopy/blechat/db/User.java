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

    public User(@NonNull String name, @NonNull Integer age, @NonNull Double weight, @NonNull Double height) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name" , typeAffinity = 2) // SQLite: TEXT Type
    private String name;

    @NonNull
    @ColumnInfo(name = "age" , typeAffinity = 3) // SQLite: INTEGER Type
    private Integer age;

    @NonNull
    @ColumnInfo(name = "weight" , typeAffinity = 4) // SQLite: Real Type
    private Double weight;

    @NonNull
    @ColumnInfo(name = "height" , typeAffinity = 4) // SQLite: Real Type
    private Double height;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Integer getAge() {
        return age;
    }

    public void setAge(@NonNull Integer age) {
        this.age = age;
    }

    @NonNull
    public Double getWeight() {
        return weight;
    }

    public void setWeight(@NonNull Double weight) {
        this.weight = weight;
    }

    @NonNull
    public Double getHeight() {
        return height;
    }

    public void setHeight(@NonNull Double height) {
        this.height = height;
    }
}
