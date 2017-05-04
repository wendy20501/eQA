package com.project.icube.eqa;

import android.provider.BaseColumns;

/**
 * Created by yiwenwang on 2017/5/1.
 */

public abstract class DataColumns implements BaseColumns {
    public static final String TASK_TABLE_NAME = "taskhd";
    public static final String TASK_NO = "tkno";
    public static final String TASK_DESCRIPTION = "tkdesc";
    public static final String TASK_CATEGORY = "category";
    public static final String TASK_TYPE = "type";
    public static final String TASK_STATUS = "status";
    public static final String TASK_OBJECT = "object";
    public static final String TASK_ERP = "erp";
    public static final String TASK_PRIORITY = "priority";
    public static final String TASK_NUM_DOC = "numdoc";
    public static final String TASK_NUM_ACT = "numact";
    public static final String TASK_ETD = "etd";
    public static final String TASK_DAY_LEFT = "dleft";
    public static final String TASK_DAY_END = "dend";
    public static final String TASK_ALERT = "alert";
    public static final String TASK_NOTE = "tknote";
    public static final String TASK_ISSUE = "tkissue";
    public static final String TASK_DADDED = "dadded";
    public static final String TASK_UADDED = "uadded";
    public static final String TASK_DEDITED = "dedited";
    public static final String TASK_UEDITED = "uadited";
    public static final String TASK_ALERT_DAYS = "alertdays";

    public static final String ACTION_TABLE_NAME = "taskaction";
    public static final String ACTION_NO = "actno";
    public static final String ACTION_DESC = "actdesc";
    public static final String ACTION_OWNER = "actowner";
    public static final String ACTION_STATUS = "actstatus";
    public static final String ACTION_ETD = "actetd";
    public static final String ACTION_DAY_LATE = "actdlate";
    public static final String ACTION_ATD = "actatd";
    public static final String ACTION_DAY_WORK = "actdwork";
    public static final String ACTION_NOTE = "actnote";
    public static final String ACTION_ISSUE = "actissue";
    public static final String ACTION_DADDED = "actdadded";
    public static final String ACTION_UADDED = "actuadded";
    public static final String ACTION_DEDITED = "actdedited";
    public static final String ACTION_UEDITED = "actuedited";
    public static final String ACTION_CATEGORY_ID = "actcategid";
    public static final String ACTION_TYPE_ID = "acttypeid";

    public static final String STATUS_CREATE = "0";
    public static final String STATUS_URGENT = "1";
    public static final String STATUS_END = "2";

    public static final String AUTHORITY = "com.project.icube.eqa";

    public static final String USER_NAME = "usrname";

    public static final int URGENT_TIME = 60 * 60 * 24 * 1000;

    public static final int[] STATUS_COLOR = {R.color.task_start, R.color.task_urgent, R.color.task_done};
}
