package ca.bcit.c7098.photogalleryapp.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

@Database(entities = {Photo.class}, version = 4)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "app-db";

    private static AppDatabase instance;

    static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                    .addCallback(startDb)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract PhotoDao photoDao();

    private static RoomDatabase.Callback startDb = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new OpenDbAsyncTask(instance).execute();
        }
    };

    private static class OpenDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private PhotoDao dao;

        OpenDbAsyncTask(AppDatabase db) {
            dao = db.photoDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
          //  dao.deleteAll();
            return null;
        }
    }


}
