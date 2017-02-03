package com.project.icube.eqa;

/**
 * Created by yiwenwang on 2016/10/8.
 */
public interface DataMgr {
    String getDBName();
    int getDBVersion();
    String getCreateTaskTableCmd();
    String getDeleteTaskTableCmd();
    String getCreateActionTableCmd();
    String getDeleteActionTableCmd();
}
