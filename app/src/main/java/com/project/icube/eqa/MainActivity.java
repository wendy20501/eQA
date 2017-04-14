package com.project.icube.eqa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private TaskMgr taskMgr;
    HashMap<String, List<String>> mapTypes;
    List<String> lstCategs;
    String strUserName;
    public static final String CATEG_NAME_LIST = "ListOfCategoryName";
    public static final String CATEG_NAME = "strTaskCategName";
    public static final String TYPE_NAME = "strTaskTypeName";
    public static final String TASK_NO = "intTaskNo";
    public static final String TASK_DESC = "strTaskDescription";
    public static final String TASK_STATUS = "strTaskStatus";
    public static final String TASK_ETD = "strTaskETD";
    public static final String TASK_DLEFT = "intTaskDayLeft";
    public static final String TASK_DEND = "strTaskDayEnd";
    public static final String USER_NAME = "strUserName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        this.context = this;
        taskMgr = TaskMgr.getInstance(this);
        /*
        if (taskMgr.GetTaskCount() == 0) {
            taskMgr.init();
        } else {
            taskMgr.deleteAllTasks();
            taskMgr.deleteAllActions();
        }*/
        //taskMgr.sample();

        initUI();
    }

    private void initUI() {
        strUserName = "Wendy Wang";
        TextView txtUser = (TextView) findViewById(R.id.toolbar_user);
        txtUser.setText(strUserName);

        lstCategs = new ArrayList<String>();
        mapTypes = new HashMap<String, List<String>>();
        List<TaskMgr.Categ> categs = taskMgr.getCategories();
        for (int i = 0; i < categs.size(); i++) {
            TaskMgr.Categ current = categs.get(i);
            mapTypes.put(current.getName(), current.getTypes());
            lstCategs.add(current.getName());
        }

        CategAdapter adapter = new CategAdapter(this, lstCategs, mapTypes);
        final ExpandableListView categList = (ExpandableListView) findViewById(R.id.category_lst);

        categList.setAdapter(adapter);
        categList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    categList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        categList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(CATEG_NAME, lstCategs.get(groupPosition));
                bundle.putString(TYPE_NAME, mapTypes.get(lstCategs.get(groupPosition)).get(childPosition));
                bundle.putString(USER_NAME, strUserName);

                Intent intent = new Intent(context, TaskMenuActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }
        });

        /*
        ConnectionClass connection = new ConnectionClass();
        Connection conn = connection.CONN();
        if (conn == null) {
            Toast.makeText(this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Connect successfully!", Toast.LENGTH_SHORT).show();
        }
        */
    }

    private class CategAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<String> listDataHeader;
        private HashMap<String, List<String>> mapDataChild;
        private int lastExpandedGroupPosition = -1;

        public CategAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> mapDataChild) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.mapDataChild = mapDataChild;
        }

        @Override
        public int getGroupCount() {
            return this.listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.mapDataChild.get(this.listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.listDataHeader.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.mapDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String strCategText = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.categ_list_header, null);
            }

            TextView categ_name = (TextView) convertView.findViewById(R.id.categ_name);
            categ_name.setText(strCategText);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String strChildText = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.categ_list_content, null);
            }

            TextView type_name = (TextView) convertView.findViewById(R.id.type_name);
            type_name.setText(strChildText);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ConnectionClass {

        String ip = "localhost";
        String classs = "net.sourceforge.jtds.jdbc.Driver";
        String db = "cmdev";
        String un = "root";
        String password = "1234";

        @SuppressLint("NewApi")
        public Connection CONN() {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection conn = null;
            String ConnURL = null;
            try {

                Class.forName(classs);
                ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                        + "databaseName=" + db + ";user=" + un + ";password="
                        + password + ";";
                conn = DriverManager.getConnection(ConnURL);
            } catch (SQLException se) {
                Log.e("ERRO", se.getMessage());
            } catch (ClassNotFoundException e) {
                Log.e("ERRO", e.getMessage());
            } catch (Exception e) {
                Log.e("ERRO", e.getMessage());
            }
            return conn;
        }

    }

    private static int STATUS_CREAT = 0;
    private static int STATUS_START = 1;
    private class Task {
        String category;
        String type;
        String description;
        Date etd;
        int status;

        public Task(String catg, String type, String desc, Date etd) {
            this.category = catg;
            this.type = type;
            this.description = desc;
            this.etd = etd;
            this.status = STATUS_CREAT;
        }
    }
}
