package com.project.icube.eqa;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity {
    private TaskMgr taskMgr;
    private AlertDialog.Builder adCateg, adType;
    private List<String> lstCategs;
    private HashMap<String, List<String>> mapTypes;
    private TextView txtCateg, txtType;
    private int iCateg, iType;
    private DatePicker dateStartPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

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

        /* date picker */
        dateStartPicker = (DatePicker) findViewById(R.id.dp_strat);


    }

    private void updateCategType() {
        String newCateg = lstCategs.get(iCateg);
        txtCateg.setText(newCateg);
        txtType.setText(mapTypes.get(newCateg).get(iType));
    }

}
