package com.hardcopy.blechat.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


/**
 * 데이터 베이스 관리용 클래스 <p>
 * 사용방법 : <p>
 * final AppDatabase db = AppDatabase.getAppDatabase(this);
 *
 * @author KIM HYO SEONG
 * @see UserDao
 * @see GpsDao
 */
@Database(entities = {User.class , Gps.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    /**
     * 참고:
     * {@link com.hardcopy.blechat.db.UserDao}
     * @return
     */
    public abstract UserDao userDao();

    /**
     * 참고:
     * {@link com.hardcopy.blechat.db.GpsDao}
     * @return
     */
    public abstract GpsDao gpsDao();

    public static AppDatabase getAppDatabase(Context context) {

        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
