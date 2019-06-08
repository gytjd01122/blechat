package com.hardcopy.blechat.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;


/**
 * 데이터 베이스 관리용 클래스 <p>
 * 사용방법 : <p>
 * final AppDatabase db = AppDatabase.getAppDatabase(this);
 *
 * @author KIM HYO SEONG
 * @see GpsDao
 */
@Database(entities = {Gps.class}, version = 1 , exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    /**
     * 참고:
     * {@link com.hardcopy.blechat.db.GpsDao}
     * @return
     */
    public abstract GpsDao gpsDao();

    public static AppDatabase getAppDatabase(Context context) {

        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
