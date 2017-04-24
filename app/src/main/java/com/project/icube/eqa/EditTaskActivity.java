package com.project.icube.eqa;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.project.icube.eqa.TaskMgr.STATUS_START;

public class EditTaskActivity extends AppCompatActivity {
    private Context context;
    private TaskMgr taskMgr;
    private AlertDialog.Builder adCateg, adType;
    private List<String> lstCategs;
    private HashMap<String, List<String>> mapTypes;
    private TextView txtCateg, txtType;
    private int iCateg, iType;
    private EditText ed_Desc;
    private DatePicker dateStartPicker, dateEndPicker;
    private AutoCompleteTextView atCateg, atType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        context = this;
        taskMgr = TaskMgr.getInstance(this);
        initUI();
    }

    private void initUI() {
        TextView txtTitle = (TextView) findViewById(R.id.toolbar_user);
        txtTitle.setText("Create New Task");

        TaskMgr.Categ defultCateg = taskMgr.getCategories().get(0);
        txtCateg = (TextView) findViewById(R.id.categ_name);
        txtCateg.setText(defultCateg.getName());

        txtType = (TextView) findViewById(R.id.type_name);
        txtType.setText(defultCateg.getTypes().get(0));

        ed_Desc = (EditText) findViewById(R.id.input_task_desc);

        LinearLayout loCategType = (LinearLayout) findViewById(R.id.categ_type);
        loCategType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adCateg.show();
            }
        });

        /* initialize category and type alert dialog */
        lstCategs = new ArrayList<String>();
        mapTypes = new HashMap<String, List<String>>();
        List<TaskMgr.Categ> categs = taskMgr.getCategories();
        for (int i = 0; i < categs.size(); i++) {
            TaskMgr.Categ current = categs.get(i);
            mapTypes.put(current.getName(), current.getTypes());
            lstCategs.add(current.getName());
        }

        adType = new AlertDialog.Builder(this);
        adCateg = new AlertDialog.Builder(this);
        adCateg.setTitle("Choose category");
        adCateg.setItems(lstCategs.toArray(new CharSequence[lstCategs.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                iCateg = which;
                final List<String> lstTypes = mapTypes.get(lstCategs.get(which));
                adType.setTitle("Choose type under " + lstCategs.get(which));
                adType.setItems(lstTypes.toArray(new CharSequence[lstTypes.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iType = which;
                        updateCategType();
                    }
                });
                adType.show();
            }
        });

        ImageView imgAddCateg = (ImageView) findViewById(R.id.add_categ);
        imgAddCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater lf = LayoutInflater.from(context);
                final View vCategType = lf.inflate(R.layout.new_categ_type, null);

                ArrayAdapter<String> adCateg = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, lstCategs);
                atCateg = (AutoCompleteTextView) vCategType.findViewById(R.id.new_categ_name);
                atCateg.setThreshold(1);
                atCateg.setAdapter(adCateg);
                atCateg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            String str = atCateg.getText().toString();
                            if (lstCategs.contains(str)) {
                                ArrayAdapter<String> adType = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, mapTypes.get(str));
                                atType.setAdapter(adType);
                            }
                        }
                    }
                });

                atType = (AutoCompleteTextView) vCategType.findViewById(R.id.new_type_name);
                atType.setThreshold(1);


                new AlertDialog.Builder(context)
                        .setView(vCategType)
                        .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (atCateg.getText().toString().matches(""))
                                    Toast.makeText(context, "Please enter category.", Toast.LENGTH_SHORT).show();
                                else if (atType.getText().toString().matches(""))
                                    Toast.makeText(context, "Please enter type.", Toast.LENGTH_SHORT).show();
                                else {
                                    txtCateg.setText(atCateg.getText().toString());
                                    txtType.setText(atType.getText().toString());
                                }
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Category and type are not changed.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        /* date picker */
        dateStartPicker = (DatePicker) findViewById(R.id.dp_strat);
        dateEndPicker = (DatePicker) findViewById(R.id.dp_end);

        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iStartYear = dateStartPicker.getYear();
                int iStartMonth = dateStartPicker.getMonth();
                int iStartDay = dateStartPicker.getDayOfMonth();

                int iEndYear = dateEndPicker.getYear();
                int iEndMonth = dateEndPicker.getMonth();
                int iEndDay = dateEndPicker.getDayOfMonth();

                String strStart = getDate(iStartYear, iStartMonth, iStartDay);
                String strEnd = getDate(iEndYear, iEndMonth, iEndDay);

                Calendar today = Calendar.getInstance();

                Calendar start = Calendar.getInstance();
                start.set(iStartYear, iStartMonth, iStartDay);

                Calendar end = Calendar.getInstance();
                end.set(iEndYear, iEndMonth, iEndDay);

                String strDesc = ed_Desc.getText().toString();
                if (strDesc.matches("")) {
                    Toast.makeText(context, "Please enter task description!", Toast.LENGTH_SHORT).show();
                } else if (start.getTime().getTime() > end.getTime().getTime() || today.getTime().getTime() > end.getTime().getTime()) {
                    Toast.makeText(context, "Please enter reasonable end date!", Toast.LENGTH_SHORT).show();
                } else {
                    TaskMgr.Task newTask = new TaskMgr.Task(strDesc, txtCateg.getText().toString(), txtType.getText().toString(),
                            strEnd, getDayLeft(start.getTime(), end.getTime()), strEnd, strStart, strStart,
                            strStart, strStart);
                    taskMgr.insertTask(newTask);
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

    private int getDayLeft(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return (int) (diff / (60 * 60 * 1000 * 24));
    }

    private void updateCategType() {
        String newCateg = lstCategs.get(iCateg);
        txtCateg.setText(newCateg);
        txtType.setText(mapTypes.get(newCateg).get(iType));
    }

}
