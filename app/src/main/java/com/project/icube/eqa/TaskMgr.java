package com.project.icube.eqa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Pack200;

/**
 * Created by yiwenwang on 2016/10/3.
 */
public class TaskMgr implements DataMgr{
    private static TaskMgr instance = null;
    private DBHelper db;
    private String DATABASE_NAME = "mydata.db";
    private int DATABASE_VERSION = 1;
    private static final String TASK_TABLE_NAME = "taskhd";

    private static final String TASK_NO = "tkno";
    private static final String TASK_DESCRIPTION = "tkdesc";
    private static final String TASK_CATEGORY = "category";
    private static final String TASK_TYPE = "type";
    private static final String TASK_STATUS = "status";
    private static final String TASK_OBJECT = "object";
    private static final String TASK_ERP = "erp";
    private static final String TASK_PRIORITY = "priority";
    private static final String TASK_NUM_DOC = "numdoc";
    private static final String TASK_NUM_ACT = "numact";
    private static final String TASK_ETD = "etd";
    private static final String TASK_DAY_LEFT = "dleft";
    private static final String TASK_DAY_END = "dend";
    private static final String TASK_ALERT = "alert";
    private static final String TASK_NOTE = "tknote";
    private static final String TASK_ISSUE = "tkissue";
    private static final String TASK_DADDED = "dadded";
    private static final String TASK_UADDED = "uadded";
    private static final String TASK_DEDITED = "dedited";
    private static final String TASK_UEDITED = "uadited";
    private static final String TASK_ALERT_DAYS = "alertdays";

    private static final String ACTION_TABLE_NAME = "taskaction";
    private static final String ACTION_NO = "actno";
    private static final String ACTION_DESC = "actdesc";
    private static final String ACTION_OWNER = "actowner";
    private static final String ACTION_STATUS = "actstatus";
    private static final String ACTION_ETD = "actetd";
    private static final String ACTION_DAY_LATE = "actdlate";
    private static final String ACTION_ATD = "actatd";
    private static final String ACTION_DAY_WORK = "actdwork";
    private static final String ACTION_NOTE = "actnote";
    private static final String ACTION_ISSUE = "actissue";
    private static final String ACTION_DADDED = "actdadded";
    private static final String ACTION_UADDED = "actuadded";
    private static final String ACTION_DEDITED = "actdedited";
    private static final String ACTION_UEDITED = "actuedited";
    private static final String ACTION_CATEGORY_ID = "actcategid";
    private static final String ACTION_TYPE_ID = "acttypeid";

    private static final String STATUS_CREATE = "st_create";
    private static final String STATUS_WORKING = "st_working";
    private static final String STATUS_END = "st_end";

    public TaskMgr(Context context) {
        db = SQLiteHelper.getInstance(context, this);
    }

    public static TaskMgr getInstance(Context context) {
        if (instance == null)
            instance = new TaskMgr(context);
        return instance;
    }

    public void init() {
        db.DropTable();
        db.CreateTable();
    }

    public Task insertTask(Task newTask) {
        ContentValues cv = new ContentValues();
        cv.put(TASK_NO, newTask.getNo());
        cv.put(TASK_DESCRIPTION, newTask.getDesc());
        cv.put(TASK_CATEGORY, newTask.getCateg());
        cv.put(TASK_TYPE, newTask.getType());
        cv.put(TASK_STATUS, newTask.getStatus());
        cv.put(TASK_OBJECT, newTask.getObject());
        cv.put(TASK_ERP, newTask.getErp());
        cv.put(TASK_PRIORITY, newTask.getPriority());
        cv.put(TASK_NUM_DOC, newTask.getNumdoc());
        cv.put(TASK_NUM_ACT, newTask.getNumact());
        cv.put(TASK_ETD, newTask.getEtd());
        cv.put(TASK_DAY_LEFT, newTask.getDleft());
        cv.put(TASK_DAY_END, newTask.getDend());
        cv.put(TASK_ALERT, newTask.getAlert());
        cv.put(TASK_NOTE, newTask.getTknote());
        cv.put(TASK_ISSUE, newTask.getTkissue());
        cv.put(TASK_DADDED, newTask.getDadded());
        cv.put(TASK_UADDED, newTask.getUadded());
        cv.put(TASK_DEDITED, newTask.getDedited());
        cv.put(TASK_UEDITED, newTask.getUedited());
        cv.put(TASK_ALERT_DAYS, newTask.getAlertdays());

        long id = db.InsertDB(TASK_TABLE_NAME, null, cv);
        newTask.setId(id);
        return newTask;
    }

    public Action insertAction(Action newAction) {
        ContentValues cv = new ContentValues();
        cv.put(TASK_NO, newAction.getTaskNo());
        cv.put(ACTION_NO, newAction.getNo());
        cv.put(ACTION_DESC, newAction.getDesc());
        cv.put(ACTION_OWNER, newAction.getOwner());
        cv.put(ACTION_STATUS, newAction.getStatus());
        cv.put(ACTION_ETD, newAction.getEtd());
        cv.put(ACTION_DAY_LATE, newAction.getDlate());
        cv.put(ACTION_ATD, newAction.getAtd());
        cv.put(ACTION_DAY_WORK, newAction.getDwork());
        cv.put(ACTION_NOTE, newAction.getNote());
        cv.put(ACTION_ISSUE, newAction.getIssue());
        cv.put(ACTION_DADDED, newAction.getDadded());
        cv.put(ACTION_UADDED, newAction.getUadded());
        cv.put(ACTION_DEDITED, newAction.getDedited());
        cv.put(ACTION_UEDITED, newAction.getUedited());
        cv.put(ACTION_CATEGORY_ID, newAction.getCategory_id());
        cv.put(ACTION_TYPE_ID, newAction.getType_id());

        long id = db.InsertDB(ACTION_TABLE_NAME, null, cv);
        newAction.setId(id);
        return newAction;
    }

    public void deleteAllTasks() {
        db.DeleteDB(TASK_TABLE_NAME, null, null);
    }

    public void deleteAllActions() {
        db.DeleteDB(ACTION_TABLE_NAME, null, null);
    }

    public String getStatusDesc(String no) {
        switch (no) {
            case "0":
                return "Create";
            case "1":
                return "Working";
            case "2":
                return "Done";
            default:
                return "None";
        }
    }

    public Task getTask(int no) {
        String where = TASK_NO + "=" + no;
        Cursor cursor = db.QueryDB(TASK_TABLE_NAME, null, where, null, null, null, null);
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
        String where = TASK_CATEGORY + " = '" + categName + "' AND " + TASK_TYPE + " = '" + typeName + "'";
        Cursor cursor = db.QueryDB(TASK_TABLE_NAME, null, where, null, null, null, null);
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
        String where = TASK_NO + " = '" + taskNo + "'";
        Cursor cursor = db.QueryDB(ACTION_TABLE_NAME, null, where, null, null, null, null);
        cursor.moveToFirst();
        int rows = cursor.getCount();
        for (int i = 0; i < rows; i++) {
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
        String where = TASK_CATEGORY + " = '" + categName + "' AND " + TASK_TYPE + " = '" + typeName + "'";
        Cursor cursor = db.QueryDB(TASK_TABLE_NAME, null, where, null, null, null, null);
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
        String where = TASK_NO + " = '" + TaskNo + "'";
        Cursor cursor = db.QueryDB(ACTION_TABLE_NAME, null, where, null, null, null, null);
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
        cols[0] = TASK_CATEGORY;
        cols[1] = TASK_TYPE;
        cols[2] = TASK_NO;
        cols[3] = TASK_DESCRIPTION;

        Cursor cursor = db.QueryDB(TASK_TABLE_NAME, cols, null, null, null, null, null);
        int rows = cursor.getCount();
        cursor.moveToFirst();
        for (int i = 0; i < rows; i++) {
            String newCateg = cursor.getString(0);
            String newType = cursor.getString(1);
            String newTaskNo = cursor.getString(2);
            String newTaskDesc = cursor.getString(3);
            int index = categ_names.indexOf(newCateg);
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

    public int GetTaskCount() {
        int result = 0;
        Cursor cursor = db.RawQuery("SELECT COUNT(*) FROM " + TASK_TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public void UpdateActionStatus(Action action, String status) {
        ContentValues value = new ContentValues();
        value.put(ACTION_STATUS, status);

        String selection = ACTION_NO + "=" + String.valueOf(action.no);

        db.UpdateDB(ACTION_TABLE_NAME, value, selection, null);
    }

    public String nextStatus(String status) {
        String newStatus = "0";
        switch (status) {
            case "0":
                newStatus = "1";
                break;
            case "1":
                newStatus = "2";
                break;
            default:
                return null;
        }
        return newStatus;
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

    @Override
    public String getDBName() {
        return DATABASE_NAME;
    }

    @Override
    public int getDBVersion() {
        return DATABASE_VERSION;
    }

    @Override
    public String getCreateTaskTableCmd() {
        return "CREATE TABLE " + TASK_TABLE_NAME + " (" +
                TASK_NO + " NVARCHAR(25), " +
                TASK_DESCRIPTION + " NVARCHAR(300), " +
                TASK_CATEGORY + " NVARCHAR(25), " +
                TASK_TYPE + " NVARCHAR(25), " +
                TASK_STATUS + " CHAR(1), " +
                TASK_OBJECT + " NVARCHAR(25), " +
                TASK_ERP + " NVARCHAR(12), " +
                TASK_PRIORITY + " NVARCHAR(3), " +
                TASK_NUM_DOC + " NVARCHAR(3), " +
                TASK_NUM_ACT + " NVARCHAR(3) ," +
                TASK_ETD + " DATETIME, " +
                TASK_DAY_LEFT + " INT, " +
                TASK_DAY_END + " DATETIME, " +
                TASK_ALERT + " BIT, " +
                TASK_NOTE + " TEXT, " +
                TASK_ISSUE + " TEXT, " +
                TASK_DADDED + " DATETIME, " +
                TASK_UADDED + " NVARCHAR(25), " +
                TASK_DEDITED + " DATETIME ," +
                TASK_UEDITED + " NVARCHAR(25), " +
                TASK_ALERT_DAYS + " INT)";
    }

    @Override
    public String getDeleteTaskTableCmd() {
        return "DROP TABLE IF EXISTS " + TASK_TABLE_NAME;
    }

    @Override
    public String getCreateActionTableCmd() {
        return "CREATE TABLE " + ACTION_TABLE_NAME + " (" +
                TASK_NO + " NVARCHAR(25), " +
                ACTION_NO + " NVARCHAR(25), " +
                ACTION_DESC + " NVARCHAR(300), " +
                ACTION_OWNER + " NVARCHAR(25), " +
                ACTION_STATUS + " CHAR(1)," +
                ACTION_ETD + " DATETIME, " +
                ACTION_DAY_LATE + " INT, " +
                ACTION_ATD + " DATETIME, " +
                ACTION_DAY_WORK + " INT, " +
                ACTION_NOTE + " TEXT, " +
                ACTION_ISSUE + " TEXT, " +
                ACTION_DADDED + " DATETIME, " +
                ACTION_UADDED + " NVARCHAR(25), " +
                ACTION_DEDITED + " DATETIME, " +
                ACTION_UEDITED + " NVARCHAR(25), " +
                ACTION_CATEGORY_ID + " NVARCHAR(25), " +
                ACTION_TYPE_ID + " NVARCHAR(25))";
    }

    @Override
    public String getDeleteActionTableCmd() {
        return "DROP TABLE IF EXISTS " + ACTION_TABLE_NAME;
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

    public class Task {
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

        public Task(int no, String desc) {
            this.no = no;
            this.desc = desc;
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
        }

        public void setId(long id) {
            this.id = id;
        }

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

    public class Action {
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

        public Action(int task_no, int act_no, String act_desc, String owner, String status, String etd, int dlate,
                      String atd, int dwork, String note, String issue, String dadded, String uadded, String dedited,
                      String uedited, String category_id, String type_id) {
            this.task_no = task_no;
            this.no = act_no;
            this.desc = act_desc;
            this.owner = owner;
            this.status = status;
            this.etd = etd;
            this.dlate = dlate;
            this.atd = atd;
            this.dwork = dwork;
            this.note = note;
            this.issue = issue;
            this.dadded = dadded;
            this.uadded = uadded;
            this.dedited = dedited;
            this.uedited = uedited;
            this.category_id = category_id;
            this.type_id = type_id;
        }

        public void setId(long id) {
            this.id = id;
        }

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