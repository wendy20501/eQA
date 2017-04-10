package com.project.icube.eqa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class TaskActivity extends AppCompatActivity {
    private Context context;
    private TaskMgr taskMgr;
    private int intTaskNo;
    private String strUser;
    private ListView actionList;
    private ArrayAdapter<TaskMgr.Action> adpAction;
    private List<TaskMgr.Action> lstActions;
    private List<String> lstActionDescs;
    private AutoCompleteTextView txtSearch;
    private int index;
    private TextView txtActionDesc;
    private boolean ifEmpty;
    private Button btnDone;
    private boolean ifStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.activity_task);
        taskMgr = TaskMgr.getInstance(this);
        initUI();
        setDefault();
    }

    private void initUI() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        /* iCube title bar*/
        TextView txtUser = (TextView) findViewById(R.id.toolbar_user);
        strUser = bundle.getString(MainActivity.USER_NAME);
        txtUser.setText(strUser);

        /* Task no and desc */
        TextView txtTaskNo = (TextView) findViewById(R.id.task_no_value);
        intTaskNo = bundle.getInt(TaskMenuActivity.TASK_NO);
        txtTaskNo.setText(String.valueOf(intTaskNo));

        TextView txtTaskDesc = (TextView) findViewById(R.id.task_desc_value);
        String strTaskDesc = bundle.getString(TaskMenuActivity.TASK_DESC);
        txtTaskDesc.setText(strTaskDesc);

        /* Action list title */
        LinearLayout layout = (LinearLayout) findViewById(R.id.action_list_title);
        layout.setBackgroundColor(getResources().getColor(R.color.gray));

        TextView txtNoTitle = (TextView) findViewById(R.id.action_no);
        txtNoTitle.setText("#");

        final TextView txtDescTitle = (TextView) findViewById(R.id.action_desc);
        txtDescTitle.setText("Desc");

        TextView txtOwnerTitle = (TextView) findViewById(R.id.action_owner);
        txtOwnerTitle.setText("Owner");

        final TextView txtStatusTitle = (TextView) findViewById(R.id.action_status);
        txtStatusTitle.setText("Status");

        /* Action list contents */
        lstActions = taskMgr.getActions(intTaskNo);
        ifEmpty = lstActions.isEmpty();

        if (!ifEmpty) {
            adpAction = new ActionAdapter(context, R.id.actionList, lstActions);

            actionList = (ListView) findViewById(R.id.actionList);
            actionList.setAdapter(adpAction);
            actionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    select(position);
                }
            });
        }

        /* Search for the actions */
        lstActionDescs = taskMgr.getActionDescs(intTaskNo);
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lstActionDescs);

        txtSearch = (AutoCompleteTextView) findViewById(R.id.search_input);
        txtSearch.setThreshold(1);
        txtSearch.setAdapter(searchAdapter);

        ImageButton imgSearch = (ImageButton) findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iSelected = lstActionDescs.indexOf(txtSearch.getText().toString());
                if (iSelected != -1) {
                    select(iSelected);
                }
                txtSearch.setText("");
            }
        });

        /* Action description */
        txtActionDesc = (TextView) findViewById(R.id.action_desc_value);

        /* buttons */
        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });
    }

    private void updateStatus() {
        ifStart = true;
        TaskMgr.Action current = lstActions.get(index);
        String newStatus = taskMgr.nextStatus(current.getStatus());
        if (newStatus != null) {
            taskMgr.UpdateActionStatus(current, newStatus);
            current.setStatus(newStatus);
            adpAction.notifyDataSetChanged();
            for (int i = 0; i < lstActions.size(); i++) {
                if (!lstActions.get(i).getStatus().equals("2"))
                    return;
            }
            
        }
    }

    private void setDefault() {
        index = 0;
        if (!ifEmpty) {
            select(index);
        }
        ifStart = false;
    }

    private void select(int pos) {
        index = pos;
        actionList.setItemChecked(index, true);
        actionList.requestFocus();
        actionList.setSelection(pos);
        hideKeyboard(TaskActivity.this);
        txtActionDesc.setText(lstActions.get(pos).getDesc());
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private class ActionAdapter extends ArrayAdapter<TaskMgr.Action> {
        List<TaskMgr.Action> lstActions;

        public ActionAdapter(Context context, int resource, List<TaskMgr.Action> objects) {
            super(context, resource, objects);
            this.lstActions = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TaskMgr.Action current = lstActions.get(position);

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.action_item, null);

            TextView txtNo = (TextView) view.findViewById(R.id.action_no);
            txtNo.setText(String.valueOf(current.getNo()));

            TextView txtDesc = (TextView) view.findViewById(R.id.action_desc);
            txtDesc.setText(current.getDesc());

            TextView txtOwner = (TextView) view.findViewById(R.id.action_owner);
            txtOwner.setText(current.getOwner());

            TextView txtStatus = (TextView) view.findViewById(R.id.action_status);
            txtStatus.setText(taskMgr.getStatusDesc(current.getStatus()));

            return view;
        }
    }
}
