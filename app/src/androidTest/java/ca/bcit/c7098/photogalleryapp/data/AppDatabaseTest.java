package ca.bcit.c7098.photogalleryapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import ca.bcit.c7098.photogalleryapp.TestUtils;

import static org.junit.Assert.*;

public class AppDatabaseTest {

    private PhotoDao photoDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        photoDao = db.photoDao();
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        db.clearAllTables();
        db.close();
    }

    // Insertion tests
    @Test
    public void insertPhoto() {
        Photo p = TestUtils.createPhoto();
        photoDao.insert(p);
    }

    // End insertion tests

    // Update tests
    @Test
    public void updatePhotoCaption() {
        Photo old = new Photo();
        old.setCaption("Test");
        photoDao.insert(old);
        old.setCaption("New");
        photoDao.update(old);
        assertEquals("New", old.getCaption());
    }

    // End update tests

    // Searching
    @Test
    public void searchByDate_returnsCorrectResult() {
        Calendar c = Calendar.getInstance();
        Date start = new Date(c.getTimeInMillis());
        c.add(Calendar.MONTH, 1);
        Date end = new Date(c.getTimeInMillis());
        LiveData<List<Photo>> photosByDate = photoDao.getPhotosBetweenDates(start, end);

    }
}