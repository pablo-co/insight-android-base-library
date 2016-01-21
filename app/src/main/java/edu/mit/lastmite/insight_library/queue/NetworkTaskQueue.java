package edu.mit.lastmite.insight_library.queue;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.TaskQueue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.mit.lastmite.insight_library.service.NetworkQueueService;
import edu.mit.lastmite.insight_library.task.NetworkTask;


public class NetworkTaskQueue extends TaskQueue<NetworkTask> {
    public static final String FILENAME = "network_task_queue";

    protected transient final Context mContext;
    protected transient final Bus mBus;
    protected transient final FileObjectQueue<NetworkTask> mDelegate;
    protected final String mFileName;

    public NetworkTaskQueue(FileObjectQueue<NetworkTask> delegate, Context context, Bus bus, String fileName) {
        super(delegate);
        mDelegate = delegate;
        mContext = context;
        mBus = bus;
        mFileName = fileName;
    }

    public void startServiceIfNotEmpty() {
        if (size() > 0) {
            startService();
        }
    }

    public void startService() {
        Intent intent = new Intent(mContext, NetworkQueueService.class);
        intent.putExtra(NetworkQueueService.EXTRA_QUEUE_NAME, mFileName);
        mContext.startService(intent);
    }

    @SuppressWarnings("UnusedDeclaration")
    @Override
    public void add(NetworkTask entry) {
        super.add(entry);
        produceSizeChanged();
    }

    @SuppressWarnings("UnusedDeclaration")
    @Override
    public void remove() {
        super.remove();
        produceSizeChanged();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void clear() {
        try {
            mDelegate.remove(mDelegate.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getFileName() {
        return mFileName;
    }

    @SuppressWarnings("UnusedDeclaration")
    public List<NetworkTask> peek(int max) {
        return mDelegate.peek(max);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void produceSizeChanged() {
        mBus.post(new NetworkQueueSizeEvent(size()));
    }

    @SuppressWarnings("UnusedDeclaration")
    public static NetworkTaskQueue create(Context context, Gson gson, Bus bus) {
        return create(context, gson, bus, FILENAME);
    }

    public static NetworkTaskQueue create(Context context, Gson gson, Bus bus, String fileName) {
        FileObjectQueue<NetworkTask> delegate = createFileObjectQueue(context, gson, fileName);
        NetworkTaskQueue taskQueue = new NetworkTaskQueue(delegate, context, bus, fileName);
        taskQueue.startServiceIfNotEmpty();
        return taskQueue;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static NetworkTaskQueue createWithoutProcessing(Context context, Gson gson, Bus bus, String fileName) {
        FileObjectQueue<NetworkTask> delegate = createFileObjectQueue(context, gson, fileName);
        return new NetworkTaskQueue(delegate, context, bus, fileName);
    }

    public static FileObjectQueue<NetworkTask> createFileObjectQueue(Context context, Gson gson, String fileName) {
        FileObjectQueue.Converter<NetworkTask> converter = new GsonConverter<>(gson);
        File queueFile = new File(context.getFilesDir(), fileName);
        FileObjectQueue<NetworkTask> delegate;
        try {
            delegate = new FileObjectQueue<>(queueFile, converter);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file queue.", e);
        }
        return delegate;
    }
}