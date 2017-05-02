package com.project.icube.eqa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private Button btnDone;
    private ImageView imgNote;
    private AlertDialog.Builder adList, adDelete, adNote;

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
        strUser = bundle.getString(DataColumns.USER_NAME);
        txtUser.setText(strUser);

        /* task desc */
        TextView txtTaskDesc = (TextView) findViewById(R.id.task_desc_value);
        String strTaskDesc = bundle.getString(DataColumns.TASK_DESCRIPTION);
        txtTaskDesc.setText(strTaskDesc);

        /* Action list contents */
        intTaskNo = bundle.getInt(DataColumns.TASK_NO);
        lstActions = taskMgr.getActions(intTaskNo);

        adpAction = new ActionAdapter(context, R.id.actionList, lstActions);

        actionList = (ListView) findViewById(R.id.actionList);
        actionList.setAdapter(adpAction);
        actionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select(position);
            }
        });
        actionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                select(position);
                adList.setTitle(lstActionDescs.get(index));
                adList.show();
                return false;
            }
        });

        /* AlertDialog after long click */
        final String[] listItem = {"Delete"};
        adList = new AlertDialog.Builder(this);
        adList.setItems(listItem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        adDelete.show();
                        break;
                    default:
                        break;
                }
            }
        });

        /* AlertDialog after click delete */
        adDelete = new AlertDialog.Builder(this);
        adDelete.setTitle("Delete action");
        adDelete.setMessage("1 action will be deleted.");
        adDelete.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        adDelete.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(index);
            }
        });

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

        imgNote = (ImageView) findViewById(R.id.imgNote);
        imgNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(TaskActivity.this);
                View vNote = inflater.inflate(R.layout.note_item, null);
                final EditText edNote = (EditText) vNote.findViewById(R.id.ed_note);
                edNote.setText(taskMgr.getAction(intTaskNo, lstActions.get(index).getNo()).getNote());

                adNote = new AlertDialog.Builder(TaskActivity.this);
                adNote.setTitle("Note");
                adNote.setView(vNote);
                adNote.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taskMgr.UpdateActionNote(intTaskNo, lstActions.get(index).getNo(), edNote.getText().toString());
                    }
                });
                adNote.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

                adNote.show();
            }
        });
    }

    private void updateStatus() {
        TaskMgr.Action current = lstActions.get(index);
        taskMgr.UpdateActionStatus(current, TaskMgr.STATUS_DONE);
        current.setStatus(TaskMgr.STATUS_DONE);
        adpAction.notifyDataSetChanged();
    }

    private void setDefault() {
        index = 0;
        select(index);
    }

    private void select(int pos) {
        index = pos;
        actionList.setItemChecked(index, true);
        actionList.requestFocus();
        actionList.setSelection(pos);
        hideKeyboard(TaskActivity.this);
        txtActionDesc.setText(lstActions.get(pos).getDesc());
    }

    private void delete(int position) {
        taskMgr.deleteAction(intTaskNo, lstActions.get(position).getNo());
        lstActions.remove(position);
        adpAction.notifyDataSetChanged();
        if (lstActions.size() == 0)
            this.finish();
        else if (position < lstActions.size())
            select(position);
        else
            select(position - 1);
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

            ImageView imgStatus = (ImageView) view.findViewById(R.id.action_status);
            imgStatus.setBackgroundColor(getResources().getColor(taskMgr.getStatusColor(current.getStatus())));

            TextView txtDesc = (TextView) view.findViewById(R.id.action_desc);
            txtDesc.setText(current.getDesc());

            TextView txtOwner = (TextView) view.findViewById(R.id.action_owner);
            txtOwner.setText(current.getOwner());

            TextView txtTime = (TextView) view.findViewById(R.id.action_time);
            txtTime.setText(current.getEtd());

            return view;
        }
    }
}
