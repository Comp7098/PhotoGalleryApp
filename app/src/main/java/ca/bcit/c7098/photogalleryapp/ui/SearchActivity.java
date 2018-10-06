package ca.bcit.c7098.photogalleryapp.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import ca.bcit.c7098.photogalleryapp.R;

public class SearchActivity extends AppCompatActivity {

    // TODO: Write tests for photo searching
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText start = findViewById(R.id.start_date);
        DateListener startDateListener = new DateListener(start, this.getApplicationContext());
        EditText end = findViewById(R.id.end_date);
        DateListener endDateListener = new DateListener(end, this.getApplicationContext());
    }

    private class DateListener implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
        private EditText mEditText;
        private Context mContext;
        DateListener(EditText editText, Context context) {
            mEditText = editText;
            mContext = context;
            mEditText.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Calendar current = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), this, current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DATE));
            datePicker.show();
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            Date date = new Date();
            date.setTime(c.getTimeInMillis());
            String string = new SimpleDateFormat("EEE, MMM d yyyy", Locale.CANADA).format(date);
            mEditText.setText(string);
        }
    }
}
