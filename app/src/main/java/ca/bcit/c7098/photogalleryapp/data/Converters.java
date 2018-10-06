package ca.bcit.c7098.photogalleryapp.data;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Project: photogalleryapp
 * Date: 10/6/2018
 * Author: William Murphy
 * Description:
 */
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
    @TypeConverter
    public static String fromDate(Date date) {
        return date == null ? null : new SimpleDateFormat("EEE, MMM d yyyy", Locale.CANADA).format(date);
    }
}
