package com.project.icube.eqa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class TaskMenuActivity extends AppCompatActivity {
    private TaskMgr taskMgr;
    private Context context;
    private List<String> lstTaskDescs;
    private List<TaskMgr.Task> lstTasks;
    private ListView taskList;
    private String strUser;
    private TextView txtDescValue;
    private AutoCompleteTextView txtSearch;
    private int index;
    private ImageButton imgGo;
    private ImageButton imgBtn2;
    private ImageButton imgNote;
    private ImageButton imgDoc;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public final static String USER_NAME = "strUserName";
    public final static String TASK_NO = "strTaskNo";
    public final static String TASK_DESC = "strTaskDesc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_menu);
        taskMgr = new TaskMgr(this);
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
        strUser = bundle.getString(MainActivity.USER_NAME);
        txtUser.setText(strUser);

        /* Category and type */
        TextView txtCategType = (TextView) findViewById(R.id.content_value);
        String categ_name = bundle.getString(MainActivity.CATEG_NAME);
        String type_name = bundle.getString(MainActivity.TYPE_NAME);
        txtCategType.setText(categ_name + " / " + type_name);

        /* Task list title */
        LinearLayout layout = (LinearLayout) findViewById(R.id.task_list_title);
        layout.setBackgroundColor(getResources().getColor(R.color.gray));

        TextView txtNoTitle = (TextView) findViewById(R.id.task_no);
        txtNoTitle.setText("#");

        TextView txtDescTitle = (TextView) findViewById(R.id.task_desc);
        txtDescTitle.setText("Desc");

        TextView txtETDTitle = (TextView) findViewById(R.id.task_etd);
        txtETDTitle.setText("ETD");

        TextView txtStatusTitle = (TextView) findViewById(R.id.task_status);
        txtStatusTitle.setText("Status");

        /* Task list contents */
        lstTasks = taskMgr.getTasks(categ_name, type_name);
        ArrayAdapter<TaskMgr.Task> adapter = new TaskAdapter(context, R.id.taskList, R.layout.task_item, lstTasks);

        taskList = (ListView) findViewById(R.id.taskList);
        taskList.setAdapter(adapter);
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select(position);
            }
        });
        taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                open(position);
                return true;
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

        /* buttons */
        imgGo = (ImageButton) findViewById(R.id.imgGo);
        imgGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(index);
            }
        });

        imgBtn2 = (ImageButton) findViewById(R.id.imgBtn2);
        imgNote = (ImageButton) findViewById(R.id.imgNote);
        imgDoc = (ImageButton) findViewById(R.id.imgDoc);
    }

    private void setDefault() {
        index = 0;
        select(index);
    }

    private void select(int position) {
        index = position;
        taskList.setItemChecked(index, true);
        taskList.requestFocus();
        hideKeyboard(TaskMenuActivity.this);
        txtDescValue.setText(lstTasks.get(index).getDesc());
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
        bundle.putString(USER_NAME, strUser);
        bundle.putInt(TASK_NO, lstTasks.get(position).getNo());
        bundle.putString(TASK_DESC, lstTasks.get(position).getDesc());

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

        public TaskAdapter(Context context, int resource, int textViewResourceId, List<TaskMgr.Task> objects) {
            super(context, resource, textViewResourceId, objects);
            this.lstTasks = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TaskMgr.Task current = lstTasks.get(position);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.task_item, null);
            TextView taskNo = (TextView) view.findViewById(R.id.task_no);
            taskNo.setText(String.valueOf(current.getNo()));
            TextView taskDesc = (TextView) view.findViewById(R.id.task_desc);
            taskDesc.setText(current.getDesc());
            TextView taskEtd = (TextView) view.findViewById(R.id.task_etd);
            taskEtd.setText(current.getEtd());
            TextView taskStatus = (TextView) view.findViewById(R.id.task_status);
            taskStatus.setText(taskMgr.getStatusDesc(current.getStatus()));
            return view;
        }
    }
}
