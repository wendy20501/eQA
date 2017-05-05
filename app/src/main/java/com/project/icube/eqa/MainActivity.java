package com.project.icube.eqa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private String strUserName;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        strUserName = "Wendy Wang";
        TextView txtUser = (TextView) findViewById(R.id.toolbar_user);
        txtUser.setText(strUserName);

        inflater = LayoutInflater.from(this);
        View vTab1 = inflater.inflate(R.layout.categ_tab, null);
        ImageView imgTab1 = (ImageView) vTab1.findViewById(R.id.img);
        imgTab1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_calendar_24dp, null));

        View vTab2 = inflater.inflate(R.layout.categ_tab, null);
        ImageView imgTab2 = (ImageView) vTab2.findViewById(R.id.img);
        imgTab2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_list_24dp, null));

        FragmentTabHost mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.container);

        mTabHost.addTab(mTabHost.newTabSpec("One")
                        .setIndicator(vTab1)
                , CalenderFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("Two")
                        .setIndicator(vTab2)
                , CategFragment.class, null);

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
}
