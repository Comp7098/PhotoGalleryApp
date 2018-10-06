package ca.bcit.c7098.photogalleryapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PhotoDao {

    @Query("SELECT * FROM photo")
    LiveData<List<Photo>> getAll();

    @Query("SELECT * FROM photo WHERE date BETWEEN :startDate AND :endDate")
    LiveData<List<Photo>> getPhotosBetweenDates(Date startDate, Date endDate);

    @Query("SELECT * FROM photo WHERE photo.caption LIKE :caption")
    LiveData<List<Photo>> getPhotosByCaption(String caption);

    @Insert
    void insert(Photo photo);

    @Update(onConflict = REPLACE)
    void update(Photo[] photo);

    @Update(onConflict = REPLACE)
    void update(Photo photo);

    @Delete
    void delete(Photo photo);

    @Query("DELETE FROM photo")
    void deleteAll();

}
