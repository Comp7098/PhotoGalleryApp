package ca.bcit.c7098.photogalleryapp.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.bcit.c7098.photogalleryapp.data.AppDatabase;
import ca.bcit.c7098.photogalleryapp.data.Photo;
import ca.bcit.c7098.photogalleryapp.data.PhotoDao;

public class PhotoViewModel extends AndroidViewModel {

    private MutableLiveData<List<Photo>> photos;

    public PhotoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return null;
    }

}
