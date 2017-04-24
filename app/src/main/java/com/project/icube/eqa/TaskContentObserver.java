package com.project.icube.eqa;

import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;

/**
 * Created by yiwenwang on 2017/4/23.
 */

public class TaskContentObserver extends ContentObserver {
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    private Handler mHadler;

    public TaskContentObserver(Handler handler) {
        super(handler);
        mHadler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Message msg = new Message();
        msg.what = 1;
        mHadler.sendMessage(msg);
    }
}
