package ca.bcit.c7098.photogalleryapp.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bumptech.glide.util.Util;

import ca.bcit.c7098.photogalleryapp.R;
import ca.bcit.c7098.photogalleryapp.common.Utilities;

public class SearchActivity extends AppCompatActivity {

    public static final String START_DATE = "START_DATE";
    public static final String TOP_LEFT = "TOP_LEFT";
    public static final String BOTTOM_RIGHT = "BOTTOM_RIGHT";
    public static final String END_DATE = "END_DATE";
    public static final String KEYWORD = "KEYWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText start = findViewById(R.id.start_date);
        final DateListener startDateListener = new DateListener(start);
        final EditText end = findViewById(R.id.end_date);
        final DateListener endDateListener = new DateListener(end);

        final EditText keyword = findViewById(R.id.keyword);
        final EditText topLeft = findViewById(R.id.top_left);
        final EditText bottomRight = findViewById(R.id.bottom_right);

        Button searchButton = findViewById(R.id.button_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utilities.isEditTextEmpty(start) && !Utilities.isEditTextEmpty(end)) {
                    Intent output = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putLong(START_DATE, startDateListener.getDateInMillis());
                    bundle.putLong(END_DATE, endDateListener.getDateInMillis());
                    output.putExtras(bundle);
                    setResult(RESULT_OK, output);
                    finish();
                }
                else if (!Utilities.isEditTextEmpty(keyword)) {
                    Intent output = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(KEYWORD, keyword.getText().toString());
                    output.putExtras(bundle);
                    setResult(RESULT_OK, output);
                    finish();
                } else if (!Utilities.isEditTextEmpty(topLeft) && !Utilities.isEditTextEmpty(bottomRight)) {
                    Intent output = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(TOP_LEFT, topLeft.getText().toString());
                    bundle.putString(BOTTOM_RIGHT, bottomRight.getText().toString());
                    output.putExtras(bundle);
                    setResult(RESULT_OK, output);
                    finish();
                }
            }
        });
    }

    private class DateListener implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
        private EditText mEditText;
        private long mDateInMillis;
        DateListener(EditText editText) {
            mEditText = editText;
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
            mDateInMillis = c.getTimeInMillis();
            String string = new SimpleDateFormat("EEE, MMM d yyyy", Locale.CANADA).format(date);
            mEditText.setText(string);
        }

        public long getDateInMillis() {
            return mDateInMillis;
        }
    }
}
