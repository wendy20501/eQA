package com.project.icube.eqa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditActionActivity extends AppCompatActivity {
    private Context context;
    private TaskMgr taskMgr;
    private DatePicker dateStartPicker, dateEndPicker;
    private EditText ed_Desc, ed_Owner;
    private TaskMgr.Task curTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        taskMgr = TaskMgr.getInstance(this);
        setContentView(R.layout.activity_edit_action);
        initUI();
    }

    private void initUI() {
        TextView txtTitle = (TextView) findViewById(R.id.toolbar_user);
        txtTitle.setText("Create New Action");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        final int iTaskNo = bundle.getInt(DataColumns.TASK_NO);
        curTask = taskMgr.getTask(iTaskNo);

        TextView txtTaskDesc = (TextView) findViewById(R.id.task_desc);
        txtTaskDesc.setText(curTask.getDesc());

        TextView txtTaskTime = (TextView) findViewById(R.id.task_time);
        txtTaskTime.setText(curTask.getDadded() + " to " + curTask.getEtd());

        /* action desc & owner */
        ed_Desc = (EditText) findViewById(R.id.input_action_desc);
        ed_Owner = (EditText) findViewById(R.id.input_action_owner);

        /* date picker */
        dateStartPicker = (DatePicker) findViewById(R.id.dp_act_start);
        dateEndPicker = (DatePicker) findViewById(R.id.dp_act_end);

        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iStartYear = dateStartPicker.getYear();
                int iStartMonth = dateStartPicker.getMonth() + 1;
                int iStartDay = dateStartPicker.getDayOfMonth();

                int iEndYear = dateEndPicker.getYear();
                int iEndMonth = dateEndPicker.getMonth() + 1;
                int iEndDay = dateEndPicker.getDayOfMonth();

                String strStart = getDate(iStartYear, iStartMonth, iStartDay);
                String strEnd = getDate(iEndYear, iEndMonth, iEndDay);

                Calendar today = Calendar.getInstance();
                SimpleDateFormat MyDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strToday = MyDateFormat.format(today.getTime());

                Calendar start = Calendar.getInstance();
                start.set(iStartYear, iStartMonth, iStartDay);

                Calendar end = Calendar.getInstance();
                end.set(iEndYear, iEndMonth, iEndDay);

                String strDesc = ed_Desc.getText().toString();
                String strOwner = ed_Owner.getText().toString();
                if (strDesc.matches("")) {
                    Toast.makeText(context, "Please enter task description!", Toast.LENGTH_SHORT).show();
                } else if (strOwner.matches("")) {
                    Toast.makeText(context, "Please enter task owner!", Toast.LENGTH_SHORT).show();
                } else if (end.getTime().before(start.getTime())) {
                    Toast.makeText(context, "Please enter reasonable end date!", Toast.LENGTH_SHORT).show();
                } else {
                    TaskMgr.Action newAction = new TaskMgr.Action(curTask.getNo(), strDesc, strOwner, strEnd,
                            getDatePeriod(today.getTime(), start.getTime()), strToday, strToday, strToday,
                            strToday, curTask.getCateg(), curTask.getType());
                    taskMgr.insertAction(newAction);
                    taskMgr.UpdateActionCount(iTaskNo);
                    close();
                }
            }
        });

        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void close() {
        this.finish();
    }

    private int getDatePeriod(Date start, Date end) {
        return (int) ((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
    }

    private String getDate(int year, int month, int day) {
        String result = String.valueOf(year) + "-";
        if (month < 10)
            result += "0";
        result += String.valueOf(month) + "-";
        if (day < 10)
            result += "0";
        result += String.valueOf(day);
        return result;
    }
}
