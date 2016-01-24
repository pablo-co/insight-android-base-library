package edu.mit.lastmite.insight_library.util;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.mit.lastmite.insight_library.http.APIFetch;
import edu.mit.lastmite.insight_library.queue.NetworkTaskQueue;

@Module
public class AppModule {

    protected Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    APIFetch provideAPIFetch(Application application) {
        return new APIFetch(application);
    }

    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    NetworkTaskQueue provideNetworkTaskQueue(Application application, Gson gson, Bus bus) {
        return NetworkTaskQueue.create(application, gson, bus);
    }

    @Provides
    @Singleton
    Helper provideHelper(Application application) {
        return new Helper(application);
    }

    @Provides
    @Singleton
    TextSpeaker provideTextSpeaker(Application application) {
        return new TextSpeaker(application);
    }

    @Provides
    @Singleton
    Storage provideStorage(Application application) {
        return new Storage(application);
    }
}