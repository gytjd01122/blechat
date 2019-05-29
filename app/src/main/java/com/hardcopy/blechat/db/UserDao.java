package com.hardcopy.blechat.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Room User 데이터 액세스 객체로 쿼리를 정의함. <p>
 * 사용방법 : <p>
 * final AppDatabase db = AppDatabase.getAppDatabase(this); <p>
 * db.UserDao.getAll();
 *
 * @author KIM HYO SEONG
 * @see User
 * @see AppDatabase
 */
@Dao
public interface UserDao {

    /**
     * User 테이블에 모든 형식을 입력합니다.
     * @param users (name:String , age:Integer , weight:String , height:String)
     */
    @Insert
    void insertAll(User... users);

    /**
     *  User 테이블중에 일치하는 일부를 삭제합니다.
     * @param user
     */
    @Delete
    void delete(User user);

    /**
     * User 테이블에서 모든 자료를 불러옵니다.
     * @return user:List<User>
     */
    @Query("SELECT * FROM user")
    List<User> getAll();

    /**
     * User 테이블에서 이름에 해당하는 모든 자료를 불러옵니다.
     * @param name 이름 type:String
     * @return user:List<User>
     */
    @Query("SELECT * FROM user WHERE name = :name")
    List<User> getAllByName(String name);

    /**
     * User 테이블에서 이름과 일치하는 나이를 불러옵니다.
     * @param name 이름 type:String
     * @return age type:Integer
     */
    @Query("SELECT age FROM user WHERE name = :name")
    Integer getAgeByName(String name);

    /**
     * User 테이블에서 이름과 일치하는 무게를 불러옵니다.
     * @param name 이름 type:String
     * @return weight type:Double
     */
    @Query("SELECT weight FROM user WHERE name = :name")
    Double getWeightByName(String name);

    /**
     * User 테이블에서 이름과 일치하는 키를 불러옵니다.
     * @param name 이름 type:String
     * @return height type:Double
     */
    @Query("SELECT height FROM user WHERE name = :name")
    Double getHeightByName(String name);



}
