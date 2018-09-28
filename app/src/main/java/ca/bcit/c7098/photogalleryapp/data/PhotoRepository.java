package ca.bcit.c7098.photogalleryapp.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Update;
import android.os.AsyncTask;

import java.util.List;

/**
 * Project: photogalleryapp
 * Date: 9/27/2018
 * Author: William Murphy
 * Description:
 */
public class PhotoRepository {

    private PhotoDao photoDao;
    private LiveData<List<Photo>> allPhotos;

    public PhotoRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        photoDao = db.photoDao();
        allPhotos = photoDao.getAll();
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return allPhotos;
    }

    public void insert(Photo photo) {
        new InsertAsyncTask(photoDao).execute(photo);
    }

    public void update(Photo photo) {
        new UpdateAsyncTask(photoDao).execute(photo);
    }

    private static class UpdateAsyncTask extends AsyncTask<Photo, Void, Void> {
        private PhotoDao photoDao;

        UpdateAsyncTask(PhotoDao dao) {
            photoDao = dao;
        }
        @Override
        protected Void doInBackground(Photo... photos) {
            photoDao.update(photos[0]);
            return null;
        }
    }
    private static class InsertAsyncTask extends AsyncTask<Photo, Void, Void> {

        private PhotoDao asyncTaskDao;

        InsertAsyncTask(PhotoDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Photo... photos) {
            asyncTaskDao.insert(photos[0]);
            return null;
        }
    }
}
