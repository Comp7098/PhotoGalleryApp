package ca.bcit.c7098.photogalleryapp.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utilities {
    /**
     * @return A full sized image file of the photo that was taken
     */
    public static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return imageFile;
    }

    public static void longToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    public static double round(double d, int places) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static boolean isEditTextEmpty(EditText e) {
        return e.getText().toString().isEmpty();
    }
}
