package com.hardcopy.blechat.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Room GPS 데이터 액세스 객체로 쿼리를 정의함. <p>
 * 사용방법 : <p>
 * final AppDatabase db = AppDatabase.getAppDatabase(this); <p>
 * db.GpsDao.getAll();
 *
 * @author KIM HYO SEONG
 * @see AppDatabase
 * @see  Gps
 */
@Dao
public interface GpsDao {

    /**
     * GPS 테이블에 모든 형식을 입력합니다.
     * @param gps (date:String , time:Long , distance:Double )
     */
    @Insert
    void insertAll(Gps... gps);

    /**
     * GPS 테이블중에 일치하는 일부를 삭제합니다.
     * @param gps
     */
    @Delete
    void delete(Gps gps);

    /**
     * GPS 테이블에서 모든 자료를 불러옵니다.
     * @return gps:List<Gps>
     */
    @Query("SELECT * FROM gps")
    List<Gps> getAll();

    /**
     * Gps 테이블에서 날짜에 해당하는 모든 자료를 불러옵니다.
     * @param date 날짜 type:String
     * @return gps:List<Gps>
     */
    @Query("SELECT * FROM Gps WHERE date = :date")
      List<Gps> getAllByDate(String date);

    /**
     * Gps 테이블에서 날짜와 시간에 해당하는 모든 자료를 불러옵니다.
     * @param date 날짜 type:String
     * @param time 시간 type:Long
     * @return gps:List<Gps>
     */
    @Query("SELECT * FROM Gps WHERE date = :date AND time = :time" )
    List<Gps> getAllByDateAndTime(String date , Long time);
}
