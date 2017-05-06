package com.project.icube.eqa;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {


    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        MyCalendarView calendar = (MyCalendarView) v.findViewById(R.id.calendar_view);
        calendar.updateCalendar();
        return v;
    }

}


class MyCalendarView extends LinearLayout {
    private static final int DAYS_COUNT = 42;
    private static final String DATE_FORMAT = "MMM yyyy";
    private String dateFormat;
    private Calendar currentDate = Calendar.getInstance();

    // internal components
    private Context context;
    private ImageView btnPrev, btnNext, btnPrevYear, btnNextYear;
    private TextView txtDate;
    private GridView grid;

    public MyCalendarView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MyCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_layout, this);

        btnPrev = (ImageView) findViewById(R.id.calendar_prev_btn);
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        btnNext = (ImageView) findViewById(R.id.calendar_next_btn);
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        btnPrevYear = (ImageView) findViewById(R.id.calendar_prev_year_btn);
        btnPrevYear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.YEAR, -1);
                updateCalendar();
            }
        });

        btnNextYear = (ImageView) findViewById(R.id.calendar_next_year_btn);
        btnNextYear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.YEAR, 1);
                updateCalendar();
            }
        });

        txtDate = (TextView) findViewById(R.id.calendar_date_display);
        grid = (GridView) findViewById(R.id.calendar_grid);

        updateCalendar();
    }

    public void updateCalendar()
    {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar)currentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells (42 days calendar as per our business logic)
        while (cells.size() < DAYS_COUNT)
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(context, cells));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        txtDate.setText(sdf.format(currentDate.getTime()));
    }
}

class CalendarAdapter extends ArrayAdapter<Date> {
    private LayoutInflater inflater;
    private Context context;

    public CalendarAdapter(@NonNull Context context, ArrayList<Date> days) {
        super(context, R.layout.calendar_layout, days);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date date = getItem(position);
        int day = date.getDate();
        int month = date.getMonth();
        int year = date.getYear();

        Date today = new Date();

        if (convertView == null)
            convertView = inflater.inflate(R.layout.calendar_date, parent, false);

        ((TextView)convertView).setTypeface(null, Typeface.NORMAL);
        ((TextView)convertView).setTextColor(Color.BLACK);

        if (year == today.getYear() && month == today.getMonth() && day == today.getDay()) {
            ((TextView)convertView).setTypeface(null, Typeface.BOLD);
            ((TextView)convertView).setTextColor(context.getResources().getColor(R.color.icubeRed));
        } else if (date.before(today)) {
            ((TextView)convertView).setTextColor(context.getResources().getColor(R.color.greyed_out));
        }

        // set text
        ((TextView)convertView).setText(String.valueOf(date.getDate()));

        return convertView;
    }
}