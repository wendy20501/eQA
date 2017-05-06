package com.project.icube.eqa;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategFragment extends Fragment {

    private TaskMgr taskMgr;
    private HashMap<String, List<String>> mapTypes;
    private List<String> lstCategs;
    private ExpandableListView categList;
    private CategAdapter adapter;
    private TaskContentObserver mTaskObserver;

    public CategFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskMgr = TaskMgr.getInstance(getContext());
        mTaskObserver = new TaskContentObserver(mHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categ, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                startActivity(intent);
            }
        });

        categList = (ExpandableListView) v.findViewById(R.id.category_lst);
        updateCategData();
        return v;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updateCategData();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getContext().getContentResolver().registerContentObserver(TaskMgr.TASK_URI, true, mTaskObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTaskObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(mTaskObserver);
            mTaskObserver = null;
        }
    }

    private void updateCategData() {
        lstCategs  = new ArrayList<String>();
        mapTypes = new HashMap<String, List<String>>();
        List<TaskMgr.Categ> categs = taskMgr.getCategories();
        for (int i = 0; i < categs.size(); i++) {
            TaskMgr.Categ current = categs.get(i);
            mapTypes.put(current.getName(), current.getTypes());
            lstCategs.add(current.getName());
        }
        adapter = new CategAdapter(getContext(), lstCategs, mapTypes);

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
                bundle.putString(DataColumns.TASK_CATEGORY, lstCategs.get(groupPosition));
                bundle.putString(DataColumns.TASK_TYPE, mapTypes.get(lstCategs.get(groupPosition)).get(childPosition));
                //bundle.putString(DataColumns.USER_NAME, strUserName);

                Intent intent = new Intent(getContext(), TaskMenuActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }
        });
    }

    public static class CategAdapter extends BaseExpandableListAdapter {
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
}
