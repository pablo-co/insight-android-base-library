package edu.mit.lastmite.insight_library.task;

import java.util.HashMap;

import edu.mit.lastmite.insight_library.model.Route;
import edu.mit.lastmite.insight_library.task.NetworkTask;

public class QueueHeaderTask extends NetworkTask {
    protected boolean mAutomaticSync;
    protected HashMap<String, Object> mData;

    public QueueHeaderTask() {
        mData = new HashMap<>();
        mAutomaticSync = false;
    }

    public QueueHeaderTask(HashMap<String, Object> data, boolean automaticSync) {
        mData = data;
        mAutomaticSync = automaticSync;
    }

    @Override
    public void execute(Callback callback) {
        mCallback = callback;
        activateCallback(true);
    }

    @Override
    public Object getModel() {
        return mData;
    }

    public boolean shouldSync() {
        return mAutomaticSync;
    }
}
