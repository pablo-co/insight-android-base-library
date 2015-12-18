package edu.mit.lastmite.insight_library.task;

import com.squareup.tape.Task;

public class NetworkTask implements Task<NetworkTask.Callback> {

    public interface Callback {
        void onSuccess();
        void onFailure();
    }

    public NetworkTask() {
    }

    @Override
    public void execute(Callback callback) {

    }
}