package edu.mit.lastmite.insight_library.queue;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskQueue;

import java.io.File;
import java.io.IOException;

import edu.mit.lastmite.insight_library.task.NetworkTask;


public class NetworkTaskQueue extends TaskQueue<NetworkTask> {
    private static final String FILENAME = "network_task_queue";

    private final Context context;
    private final Bus bus;

    private NetworkTaskQueue(ObjectQueue<NetworkTask> delegate, Context context, Bus bus) {
        super(delegate);
        this.context = context;
        this.bus = bus;
        bus.register(this);

        if (size() > 0) {
            startService();
        }
    }

    private void startService() {
        context.startService(new Intent(context, NetworkQueueService.class));
    }

    @Override
    public void add(NetworkTask entry) {
        super.add(entry);
        bus.post(produceSizeChanged());
        startService();
    }

    @Override
    public void remove() {
        super.remove();
        bus.post(produceSizeChanged());
    }

    @SuppressWarnings("UnusedDeclaration")
    @Produce
    public NetworkQueueSizeEvent produceSizeChanged() {
        return new NetworkQueueSizeEvent(size());
    }

    public static NetworkTaskQueue create(Context context, Gson gson, Bus bus) {
        FileObjectQueue.Converter<NetworkTask> converter = new GsonConverter<NetworkTask>(gson, NetworkTask.class);
        File queueFile = new File(context.getFilesDir(), FILENAME);
        FileObjectQueue<NetworkTask> delegate;
        try {
            delegate = new FileObjectQueue<NetworkTask>(queueFile, converter);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file queue.", e);
        }
        return new NetworkTaskQueue(delegate, context, bus);
    }
}