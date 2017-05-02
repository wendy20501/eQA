package com.project.icube.eqa;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yiwenwang on 2016/10/3.
 */
public class TaskMgr {
        private ContentResolver mResolver;
    private static TaskMgr instance = null;
    private static int task_no_count = 0;
    private static int action_no_count = 0;

    public static final Uri TASK_URI = Uri.parse("content://" + DataColumns.AUTHORITY + "/" + DataColumns.TASK_TABLE_NAME);
    public static final Uri ACTION_URI = Uri.parse("content://" + DataColumns.AUTHORITY + "/" + DataColumns.ACTION_TABLE_NAME);

    public static final String STATUS_START = "0";
    public static final String STATUS_URGENT = "1";
    public static final String STATUS_DONE = "2";

    public TaskMgr(Context context) {
        mResolver = context.getContentResolver();
    }

    public static TaskMgr getInstance(Context context) {
        if (instance == null)
            instance = new TaskMgr(context);
        return instance;
    }

    public Task insertTask(Task newTask) {
        ContentValues cv = new ContentValues();
        cv.put(DataColumns.TASK_NO, newTask.getNo());
        cv.put(DataColumns.TASK_DESCRIPTION, newTask.getDesc());
        cv.put(DataColumns.TASK_CATEGORY, newTask.getCateg());
        cv.put(DataColumns.TASK_TYPE, newTask.getType());
        cv.put(DataColumns.TASK_STATUS, newTask.getStatus());
        cv.put(DataColumns.TASK_OBJECT, newTask.getObject());
        cv.put(DataColumns.TASK_ERP, newTask.getErp());
        cv.put(DataColumns.TASK_PRIORITY, newTask.getPriority());
        cv.put(DataColumns.TASK_NUM_DOC, newTask.getNumdoc());
        cv.put(DataColumns.TASK_NUM_ACT, newTask.getNumact());
        cv.put(DataColumns.TASK_ETD, newTask.getEtd());
        cv.put(DataColumns.TASK_DAY_LEFT, newTask.getDleft());
        cv.put(DataColumns.TASK_DAY_END, newTask.getDend());
        cv.put(DataColumns.TASK_ALERT, newTask.getAlert());
        cv.put(DataColumns.TASK_NOTE, newTask.getTknote());
        cv.put(DataColumns.TASK_ISSUE, newTask.getTkissue());
        cv.put(DataColumns.TASK_DADDED, newTask.getDadded());
        cv.put(DataColumns.TASK_UADDED, newTask.getUadded());
        cv.put(DataColumns.TASK_DEDITED, newTask.getDedited());
        cv.put(DataColumns.TASK_UEDITED, newTask.getUedited());
        cv.put(DataColumns.TASK_ALERT_DAYS, newTask.getAlertdays());

        mResolver.insert(TASK_URI, cv);
        return newTask;
    }

    public void deleteTask(int iTaskNo) {
        String where = DataColumns.TASK_NO + "=" + iTaskNo;
        mResolver.delete(ACTION_URI, where, null);
        mResolver.delete(TASK_URI, where, null);
    }

    public void deleteAction(int iActionNo) {
        String where = DataColumns.ACTION_NO + "=" + iActionNo;
        mResolver.delete(ACTION_URI, where, null);
    }

    public Action insertAction(Action newAction) {
        ContentValues cv = new ContentValues();
        cv.put(DataColumns.TASK_NO, newAction.getTaskNo());
        cv.put(DataColumns.ACTION_NO, newAction.getNo());
        cv.put(DataColumns.ACTION_DESC, newAction.getDesc());
        cv.put(DataColumns.ACTION_OWNER, newAction.getOwner());
        cv.put(DataColumns.ACTION_STATUS, newAction.getStatus());
        cv.put(DataColumns.ACTION_ETD, newAction.getEtd());
        cv.put(DataColumns.ACTION_DAY_LATE, newAction.getDlate());
        cv.put(DataColumns.ACTION_ATD, newAction.getAtd());
        cv.put(DataColumns.ACTION_DAY_WORK, newAction.getDwork());
        cv.put(DataColumns.ACTION_NOTE, newAction.getNote());
        cv.put(DataColumns.ACTION_ISSUE, newAction.getIssue());
        cv.put(DataColumns.ACTION_DADDED, newAction.getDadded());
        cv.put(DataColumns.ACTION_UADDED, newAction.getUadded());
        cv.put(DataColumns.ACTION_DEDITED, newAction.getDedited());
        cv.put(DataColumns.ACTION_UEDITED, newAction.getUedited());
        cv.put(DataColumns.ACTION_CATEGORY_ID, newAction.getCategory_id());
        cv.put(DataColumns.ACTION_TYPE_ID, newAction.getType_id());

        //long id = db.InsertDB(ActionEntry.ACTION_TABLE_NAME, null, cv);
        mResolver.insert(ACTION_URI, cv);
        //newAction.setId(id);
        return newAction;
    }

    public void deleteAllTasks() {
        //db.DeleteDB(TaskEntry.TASK_TABLE_NAME, null, null);
        mResolver.delete(TASK_URI, null, null);
    }

    public void deleteAllActions() {
        //db.DeleteDB(ActionEntry.ACTION_TABLE_NAME, null, null);
        mResolver.delete(ACTION_URI, null, null);
    }

    public int getStatusColor(String no) {
        switch (no) {
            case STATUS_START:
                return R.color.task_start;
            case STATUS_URGENT:
                return R.color.task_urgent;
            case STATUS_DONE:
                return R.color.task_done;
            default:
                return R.color.task_start;
        }
    }

    public Task getTask(int no) {
        String where = DataColumns.TASK_NO + "=" + no;
        //Cursor cursor = db.QueryDB(TaskEntry.TASK_TABLE_NAME, null, where, null, null, null, null);
        Cursor cursor = mResolver.query(TASK_URI, null, where, null, null);
        Task result = null;
        if (cursor.moveToFirst()) {
            result = new Task(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9),
                    cursor.getString(10), cursor.getInt(11), cursor.getString(12), cursor.getString(13), cursor.getString(14) ,
                    cursor.getString(15), cursor.getString(16), cursor.getString(17), cursor.getString(18), cursor.getString(19), cursor.getInt(20));
        }
        cursor.close();
        return result;
    }

    public List<Task> getTasks(String categName, String typeName) {
        List<Task> result = new ArrayList<Task>();
        String where = DataColumns.TASK_CATEGORY + " = '" + categName + "' AND " + DataColumns.TASK_TYPE + " = '" + typeName + "'";
        //Cursor cursor = db.QueryDB(TaskEntry.TASK_TABLE_NAME, null, where, null, null, null, null);
        Cursor cursor = mResolver.query(TASK_URI, null, where, null, null);
        cursor.moveToFirst();
        int rows = cursor.getCount();
        for (int i = 0; i < rows; i++) {
            result.add(new Task(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9),
                    cursor.getString(10), cursor.getInt(11), cursor.getString(12), cursor.getString(13), cursor.getString(14) ,
                    cursor.getString(15), cursor.getString(16), cursor.getString(17), cursor.getString(18), cursor.getString(19), cursor.getInt(20)));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public List<Action> getActions(int taskNo) {
        List<Action> result = new ArrayList<Action>();
        String where = DataColumns.TASK_NO + " = '" + taskNo + "'";
        //Cursor cursor = db.QueryDB(ActionEntry.ACTION_TABLE_NAME, null, where, null, null, null, null);
        Cursor cursor = mResolver.query(ACTION_URI, null, where, null, null);
        cursor.moveToFirst();
        int rows = cursor.getCount();
        for (int i = 0; i < rows; i++) {
            String strActionNo = cursor.getString(1);
            action_no_count = Math.max(Integer.parseInt(strActionNo), action_no_count);
            result.add(new Action(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getInt(6), cursor.getString(7), cursor.getInt(8), cursor.getString(9), cursor.getString(10),
                    cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15),
                    cursor.getString(16)));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public List<String> getTaskDescs(String categName, String typeName) {
        List<String> result = new ArrayList<String>();
        String where = DataColumns.TASK_CATEGORY + " = '" + categName + "' AND " + DataColumns.TASK_TYPE + " = '" + typeName + "'";
        //Cursor cursor = db.QueryDB(TaskEntry.TASK_TABLE_NAME, null, where, null, null, null, null);
        Cursor cursor = mResolver.query(TASK_URI, null, where, null, null);
        cursor.moveToFirst();
        int rows = cursor.getCount();
        for (int i = 0; i < rows; i++) {
            result.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public List<String> getActionDescs(int TaskNo) {
        List<String> result = new ArrayList<String>();
        String where = DataColumns.TASK_NO + " = '" + TaskNo + "'";
        //Cursor cursor = db.QueryDB(ActionEntry.ACTION_TABLE_NAME, null, where, null, null, null, null);
        Cursor cursor = mResolver.query(ACTION_URI, null, where, null, null);
        cursor.moveToFirst();
        int rows = cursor.getCount();
        for (int i = 0; i < rows; i++) {
            result.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public List<Categ> getCategories() {
        List<String> categ_names = new ArrayList<String>();
        List<Categ> result = new ArrayList<Categ>();

        String[] cols = new String[4];
        cols[0] = DataColumns.TASK_CATEGORY;
        cols[1] = DataColumns.TASK_TYPE;
        cols[2] = DataColumns.TASK_NO;
        cols[3] = DataColumns.TASK_DESCRIPTION;

        //Cursor cursor = db.QueryDB(TaskEntry.TASK_TABLE_NAME, cols, null, null, null, null, null);
        Cursor cursor = mResolver.query(TASK_URI, cols, null, null, null);
        int rows = cursor.getCount();
        cursor.moveToFirst();
        for (int i = 0; i < rows; i++) {
            String newCateg = cursor.getString(0);
            String newType = cursor.getString(1);
            String newTaskNo = cursor.getString(2);
            String newTaskDesc = cursor.getString(3);
            int index = categ_names.indexOf(newCateg);
            task_no_count = Math.max(Integer.parseInt(newTaskNo), task_no_count);
            if (index != -1) {
                result.get(index).addTask(newType, new Task(Integer.parseInt(newTaskNo), newTaskDesc));
            } else {
                Categ temp = new Categ(newCateg);
                temp.addTask(newType, new Task(Integer.parseInt(newTaskNo), newTaskDesc));
                result.add(temp);
                categ_names.add(newCateg);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public void UpdateActionStatus(Action action, String status) {
        ContentValues value = new ContentValues();
        value.put(DataColumns.ACTION_STATUS, status);

        String selection = DataColumns.ACTION_NO + "=" + String.valueOf(action.no);

        //db.UpdateDB(ActionEntry.ACTION_TABLE_NAME, value, selection, null);
        mResolver.update(ACTION_URI, value, selection, null);
    }

    public void sample() {
        Task task1 = new Task(1, "eQA Project - Implement a task reminder system.", "work", "project", "0", "obj", "erp", "1", "0", "0", "2016-10-07", 1, "2016-10-07", "0", "eQa", "no issue", "2016-10-05", "2016-10-05", "2016-10-05", "2016-10-05", 2);
        Task task2 = new Task(2, "My personal website", "work", "personal", "0", "obj", "erp", "1", "0", "0", "2016-10-01", 1, "2016-11-01", "0", "eQa", "no issue", "2016-10-05", "2016-10-05", "2016-10-05", "2016-10-05", 2);
        Task task3 = new Task(3, "Leetcode coding practice", "work", "personal", "0", "obj", "erp", "1", "0", "0", "2016-10-07", 1, "2016-12-07", "0", "eQa", "no issue", "2016-10-05", "2016-10-05", "2016-10-05", "2016-10-05", 2);
        Task task4 = new Task(4, "Book ticket", "other", "travel", "0", "obj", "erp", "1", "0", "0", "2016-10-09", 1, "2016-10-20", "0", "eQa", "no issue", "2016-10-05", "2016-10-05", "2016-10-05", "2016-10-05", 2);
        Task task5 = new Task(5, "Buy milk and eggs", "daily", "food", "0", "obj", "erp", "1", "0", "0", "2016-10-23", 1, "2016-10-24", "0", "eQa", "no issue", "2016-10-05", "2016-10-05", "2016-10-05", "2016-10-05", 2);

        insertTask(task1);
        insertTask(task2);
        insertTask(task3);
        insertTask(task4);
        insertTask(task5);

        Action action1 = new Action(1, 1, "UML design", "Wendy", "0", "2016-10-13", 3, "2016-10-16", 5, "", "", "2016-10-07", "2016-10-07", "2016-10-07", "2016-10-07", "work", "project");
        Action action2 = new Action(1, 2, "class design", "Wendy", "0", "2016-10-18", 0, "2016-10-18", 2, "", "", "2016-10-07", "2016-10-07", "2016-10-07", "2016-10-07", "work", "project");
        Action action3 = new Action(1, 3, "database connection", "Wendy", "0", "2016-10-20", 2, "2016-10-22", 4, "", "", "2016-10-07", "2016-10-07", "2016-10-07", "2016-10-07", "work", "project");
        Action action4 = new Action(1, 4, "main activity design", "Wendy", "0", "2016-10-30", 0, "2016-10-30", 8, "", "", "2016-10-07", "2016-10-07", "2016-10-07", "2016-10-07", "work", "project");
        Action action5 = new Action(1, 5, "main activity testing", "Wendy", "0", "2016-11-4", 1, "2016-11-5", 6, "", "", "2016-10-07", "2016-10-07", "2016-10-07", "2016-10-07", "work", "project");
        Action action6 = new Action(1, 6, "task menu activity design", "Wendy", "0", "2016-10-13", 3, "2016-10-16", 5, "", "", "2016-10-07", "2016-10-07", "2016-10-07", "2016-10-07", "work", "project");
        Action action7 = new Action(1, 7, "task menu activity testing", "Wendy", "0", "2016-10-18", 0, "2016-10-18", 2, "", "", "2016-10-07", "2016-10-07", "2016-10-07", "2016-10-07", "work", "project");
        Action action8 = new Action(1, 8, "action activity design", "Wendy", "0", "2016-10-20", 2, "2016-10-22", 4, "", "", "2016-10-07", "2016-10-07", "2016-10-07", "2016-10-07", "work", "project");
        Action action9 = new Action(1, 9, "action activity testing", "Wendy", "0", "2016-10-30", 0, "2016-10-30", 8, "", "", "2016-10-07", "2016-10-07", "2016-10-07", "2016-10-07", "work", "project");
        Action action10 = new Action(1, 10, "unit testing", "Wendy", "0", "2016-11-4", 1, "2016-11-5", 6, "", "", "2016-10-07", "2016-10-07", "2016-10-07", "2016-10-07", "work", "project");

        insertAction(action1);
        insertAction(action2);
        insertAction(action3);
        insertAction(action4);
        insertAction(action5);
        insertAction(action6);
        insertAction(action7);
        insertAction(action8);
        insertAction(action9);
        insertAction(action10);
    }

    public class Categ {
        private String name;
        private Map<String, List<Task>> taskMap;
        private List<String> types;

        public Categ(String categ_name) {
            this.name = categ_name;
            this.taskMap = new HashMap<String, List<Task>>();
            this.types = new ArrayList<String>();
        }

        public void addTask(String type, Task newTask) {
            if (!taskMap.containsKey(type)) {
                taskMap.put(type, new ArrayList<Task>());
                types.add(type);
            }
            taskMap.get(type).add(newTask);
        }

        public String getName() {
            return name;
        }

        public List<Task> getTasks(String type) {
            return taskMap.get(type);
        }

        public List<String> getTypes() {
            return types;
        }

        public String getType(int pos) {
            return types.get(pos);
        }
    }

    public static class Task {
        private long id;
        private int no;
        private String desc;
        private String categ;
        private String type;
        private String status;
        private String object;
        private String erp;
        private String priority;
        private String numdoc;
        private String numact;
        private String etd;
        private int dleft;
        private String dend;
        private String alert;
        private String tknote;
        private String tkissue;
        private String dadded;
        private String uadded;
        private String dedited;
        private String uedited;
        private int alertdays;
        private int action_no_count;

        public Task(int no, String desc) {
            this.no = no;
            this.desc = desc;
            this.action_no_count = 0;
        }

        //add new task
        public Task(String desc, String categ, String type, String etd, int dleft, String dend,
                    String dadded, String uadded, String dedited, String uedited) {
            this.no = task_no_count + 1;
            task_no_count++;
            this.desc = desc;
            this.categ = categ;
            this.type = type;
            this.status = STATUS_START;
            this.object = "obj";
            this.erp = "erp";
            this.priority = "1";
            this.numdoc = "0";
            this.numact = "0";
            this.etd = etd;
            this.dleft = dleft;
            this.dend = dend;
            this.alert = "1";
            this.tknote = "";
            this.tkissue = "no issue";
            this.dadded = dadded;
            this.uadded = uadded;
            this.dedited = dedited;
            this.uedited = uedited;
            this.alertdays = 2;
            this.action_no_count = 0;
        }

        public Task(int no, String desc, String categ, String type, String status,
                    String object, String erp, String priority, String numdoc, String numact,
                    String etd, int dleft, String dend, String alert, String tknote,
                    String tkissue, String dadded, String uadded, String dedited,
                    String uedited, int alertdays) {
            this.no = no;
            this.desc = desc;
            this.categ = categ;
            this.type = type;
            this.status = status;
            this.object = object;
            this.erp = erp;
            this.priority = priority;
            this.numdoc = numdoc;
            this.numact = numact;
            this.etd = etd;
            this.dleft = dleft;
            this.dend = dend;
            this.alert = alert;
            this.tknote = tknote;
            this.tkissue = tkissue;
            this.dadded = dadded;
            this.uadded = uadded;
            this.dedited = dedited;
            this.uedited = uedited;
            this.alertdays = alertdays;
            this.action_no_count = 0;
        }

        public int addAction() {
            return ++this.action_no_count;
        }

        /*public void setId(long id) {
            this.id = id;
        }*/

        public int getNo() {
            return no;
        }

        public String getDesc() {
            return desc;
        }

        public String getCateg() {
            return categ;
        }

        public String getType() {
            return type;
        }

        public String getStatus() {
            return status;
        }

        public String getObject() {
            return object;
        }

        public String getErp() {
            return erp;
        }

        public String getPriority() {
            return priority;
        }

        public String getNumdoc() {
            return numdoc;
        }

        public String getNumact() {
            return numact;
        }

        public String getEtd() {
            return etd;
        }

        public int getDleft() {
            return dleft;
        }

        public String getDend() {
            return dend;
        }

        public String getAlert() {
            return alert;
        }

        public String getTknote() {
            return tknote;
        }

        public String getTkissue() {
            return tkissue;
        }

        public String getDadded() {
            return dadded;
        }

        public String getUadded() {
            return uadded;
        }

        public String getDedited() {
            return dedited;
        }

        public String getUedited() {
            return uedited;
        }

        public int getAlertdays() {
            return alertdays;
        }
    }

    public static class Action {
        private long id;
        private int task_no;
        private int no;
        private String desc;
        private String owner;
        private String status;
        private String etd;
        private int dlate;
        private String atd;
        private int dwork;
        private String note;
        private String issue;
        private String dadded;
        private String uadded;
        private String dedited;
        private String uedited;
        private String category_id;
        private String type_id;

        public Action(int task_no, int act_no, String act_desc) {

            this.task_no = task_no;
            this.no = act_no;
            this.desc = act_desc;
        }

        /* add new action */
        public Action(int task_no, String act_desc, String owner, String etd, int dwork,
                      String dadded, String uadded, String dedited, String uedited, String category_id, String type_id) {
            this.task_no = task_no;
            this.no = action_no_count + 1;
            action_no_count++;
            this.desc = act_desc;
            this.owner = owner;
            this.status = STATUS_START;
            this.etd = etd;
            this.dlate = 0;
            this.atd = "";
            this.dwork = dwork;
            this.note = "";
            this.issue = "no issue";
            this.dadded = dadded;
            this.uadded = uadded;
            this.dedited = dedited;
            this.uedited = uedited;
            this.category_id = category_id;
            this.type_id = type_id;
        }

        public Action(int task_no, int act_no, String act_desc, String owner, String status, String etd, int dlate,
                      String atd, int dwork, String note, String issue, String dadded, String uadded, String dedited,
                      String uedited, String category_id, String type_id) {
            this.task_no = task_no;
            this.no = act_no;
            this.desc = act_desc;
            this.owner = owner;
            this.status = status;
            this.etd = etd; // estimate date
            this.dlate = dlate; // date late
            this.atd = atd;
            this.dwork = dwork; // date from start
            this.note = note;
            this.issue = issue;
            this.dadded = dadded;
            this.uadded = uadded;
            this.dedited = dedited;
            this.uedited = uedited;
            this.category_id = category_id;
            this.type_id = type_id;
        }
/*
        public void setId(long id) {
            this.id = id;
        }
*/
        public int getTaskNo() {
            return task_no;
        }

        public int getNo() {
            return no;
        }

        public String getDesc() {
            return desc;
        }

        public String getOwner() {
            return owner;
        }

        public String getStatus() {
            return status;
        }

        public String getEtd() {
            return etd;
        }

        public int getDlate() {
            return dlate;
        }

        public String getAtd() {
            return atd;
        }

        public int getDwork() {
            return dwork;
        }

        public String getNote() {
            return note;
        }

        public String getIssue() {
            return issue;
        }

        public String getDadded() {
            return dadded;
        }

        public String getUadded() {
            return uadded;
        }

        public String getDedited() {
            return dedited;
        }

        public String getUedited() {
            return uedited;
        }

        public String getCategory_id() {
            return category_id;
        }

        public String getType_id() {
            return type_id;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
