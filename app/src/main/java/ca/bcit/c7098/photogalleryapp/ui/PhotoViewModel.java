package ca.bcit.c7098.photogalleryapp.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ca.bcit.c7098.photogalleryapp.data.Photo;
import ca.bcit.c7098.photogalleryapp.data.PhotoRepository;

public class PhotoViewModel extends AndroidViewModel {

    private LiveData<List<Photo>> data;
    private PhotoRepository photoRepo;

    public PhotoViewModel(Application application) {
        super(application);
        photoRepo = new PhotoRepository(application);
        data = photoRepo.getAllPhotos();
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return data;
    }

    public void insert(Photo photo) {
        photoRepo.insert(photo);
    }

    public void update(Photo photo) {
        photoRepo.update(photo);
    }

}
