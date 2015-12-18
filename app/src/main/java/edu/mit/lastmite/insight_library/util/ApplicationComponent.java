package edu.mit.lastmite.insight_library.util;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import edu.mit.lastmite.insight_library.fragment.InsightMapsFragment;
import edu.mit.lastmite.insight_library.queue.NetworkQueueService;

@Singleton
@Component(modules = {AppModule.class})
public interface ApplicationComponent {
    void inject(Application application);

    void inject(NetworkQueueService service);

    void inject(InsightMapsFragment fragment);
}