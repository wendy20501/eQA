package com.project.icube.eqa;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TaskMenuActivity extends AppCompatActivity {
    private TaskMgr taskMgr;
    private Context context;
    private List<String> lstTaskDescs;
    private List<TaskMgr.Task> lstTasks;
    private ListView taskList;
    private ArrayAdapter<TaskMgr.Task> adTask;
    private String strUser;
    private TextView txtDescValue;
    private AutoCompleteTextView txtSearch;
    private int index;
    private ImageButton imgBtn1;
    private ImageButton imgBtn2;
    private ImageButton imgNote;
    private ImageButton imgDoc;
    private AlertDialog.Builder adList, adDelete, adNote;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_menu);
        taskMgr = TaskMgr.getInstance(this);
        this.context = this;
        initUI();
        setDefault();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initUI() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        /* iCube title bar*/
        TextView txtUser = (TextView) findViewById(R.id.toolbar_user);
        strUser = bundle.getString(DataColumns.USER_NAME);
        txtUser.setText(strUser);

        /* Category and type */
        TextView txtCategType = (TextView) findViewById(R.id.content_value);
        String categ_name = bundle.getString(DataColumns.TASK_CATEGORY);
        String type_name = bundle.getString(DataColumns.TASK_TYPE);
        txtCategType.setText(categ_name + " / " + type_name);

        /* Task list contents */
        lstTasks = taskMgr.getTasks(categ_name, type_name);
        adTask = new TaskAdapter(context, R.id.taskList, R.layout.task_item, lstTasks);

        taskList = (ListView) findViewById(R.id.taskList);
        taskList.setAdapter(adTask);
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select(position);
            }
        });
        taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                select(position);
                adList.setTitle(lstTasks.get(index).getDesc());
                adList.show();
                return false;
            }
        });

        final String[] listItem = {"Add an action", "Delete"};
        adList = new AlertDialog.Builder(this);
        adList.setItems(listItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Bundle bundle = new Bundle();
                        bundle.putInt(DataColumns.TASK_NO, lstTasks.get(index).getNo());

                        Intent intent = new Intent(context, EditActionActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 1:
                        adDelete.show();
                        break;
                    default:
                        break;
                }
            }
        });

        adDelete = new AlertDialog.Builder(this);
        adDelete.setTitle("Delete task");
        adDelete.setMessage("1 task and all the related actions will be deleted.");
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

        /* search for the task */
        lstTaskDescs = taskMgr.getTaskDescs(categ_name, type_name);
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lstTaskDescs);

        txtSearch = (AutoCompleteTextView) findViewById(R.id.search_input);
        txtSearch.setThreshold(1);
        txtSearch.setAdapter(searchAdapter);

        ImageButton imgSearch = (ImageButton) findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = lstTaskDescs.indexOf(txtSearch.getText().toString());
                if (selected != -1) {
                    select(selected);
                }
                txtSearch.setText("");
            }
        });

        /* task description */
        txtDescValue = (TextView) findViewById(R.id.task_desc_value);

        imgBtn2 = (ImageButton) findViewById(R.id.imgBtn2);
        imgNote = (ImageButton) findViewById(R.id.imgNote);
        imgNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(TaskMenuActivity.this);
                View vNote = inflater.inflate(R.layout.note_item, null);
                final EditText edNote = (EditText) vNote.findViewById(R.id.ed_note);
                edNote.setText(taskMgr.getTask(lstTasks.get(index).getNo()).getTknote());

                adNote = new AlertDialog.Builder(TaskMenuActivity.this);
                adNote.setTitle("Note");
                adNote.setView(vNote);
                adNote.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taskMgr.UpdateTaskNote(lstTasks.get(index).getNo(), edNote.getText().toString());
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
        imgDoc = (ImageButton) findViewById(R.id.imgDoc);
    }

    private void setDefault() {
        index = 0;
        select(index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adTask.notifyDataSetChanged();
    }

    private void select(int position) {
        index = position;
        taskList.setItemChecked(index, true);
        taskList.requestFocus();
        hideKeyboard(TaskMenuActivity.this);
        txtDescValue.setText(lstTasks.get(index).getDesc());
    }

    private void delete(int position) {
        taskMgr.deleteTask(lstTasks.get(position).getNo());
        lstTasks.remove(position);
        adTask.notifyDataSetChanged();
        if (lstTasks.size() == 0)
            this.finish();
        else if (position < lstTasks.size())
            select(position);
        else
            select(position - 1);
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void open(int position) {
        if (taskMgr.getActions(lstTasks.get(position).getNo()).size() == 0) {
            Toast.makeText(context, "No related activities.", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(DataColumns.USER_NAME, strUser);
        bundle.putInt(DataColumns.TASK_NO, lstTasks.get(position).getNo());
        bundle.putString(DataColumns.TASK_DESCRIPTION, lstTasks.get(position).getDesc());

        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Task Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.project.icube.eqa/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Task Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.project.icube.eqa/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class TaskAdapter extends ArrayAdapter<TaskMgr.Task> {
        private List<TaskMgr.Task> lstTasks;
        private SimpleDateFormat MyDateFormat;
        private Calendar today;

        public TaskAdapter(Context context, int resource, int textViewResourceId, List<TaskMgr.Task> objects) {
            super(context, resource, textViewResourceId, objects);
            this.lstTasks = objects;
            this.today = Calendar.getInstance();
            this.MyDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TaskMgr.Task current = lstTasks.get(position);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.task_item, null);

            TextView taskDesc = (TextView) view.findViewById(R.id.task_desc);
            taskDesc.setText(current.getDesc());

            TextView taskEtd = (TextView) view.findViewById(R.id.task_etd);
            taskEtd.setText(current.getEtd());

            ImageView taskStatus = (ImageView) view.findViewById(R.id.task_status);
            Date Endtime = null;
            try {
                Endtime = MyDateFormat.parse(current.getEtd());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ImageView taskEnter = (ImageView) view.findViewById(R.id.task_enter);
            List<TaskMgr.Action> lstActions = taskMgr.getActions(lstTasks.get(position).getNo());
            if (lstActions.size() == 0) {
                taskEnter.setVisibility(View.INVISIBLE);
            } else if (IfDone(lstActions)){
                taskMgr.UpdateTaskStatus(current.getNo(), DataColumns.STATUS_END);
                current.setStatus(DataColumns.STATUS_END);
            }
            if (!current.getStatus().equals(DataColumns.STATUS_END) && IfUrgent(Endtime)) {
                taskMgr.UpdateTaskStatus(current.getNo(), DataColumns.STATUS_URGENT);
                current.setStatus(DataColumns.STATUS_URGENT);
            }
            taskStatus.setBackgroundColor(getResources().getColor(DataColumns.STATUS_COLOR[Integer.valueOf(current.getStatus())]));
            taskEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open(position);
                }
            });

            return view;
        }

        public boolean IfUrgent(Date deadline) {
            return deadline.getTime() - today.getTime().getTime() < DataColumns.URGENT_TIME ? true : false;
        }

        public boolean IfDone(List<TaskMgr.Action> lstActions) {
            for (int i = 0; i < lstActions.size(); i++) {
                if (!lstActions.get(i).getStatus().equals(DataColumns.STATUS_END))
                    return false;
            }
            return true;
        }
    }
}
