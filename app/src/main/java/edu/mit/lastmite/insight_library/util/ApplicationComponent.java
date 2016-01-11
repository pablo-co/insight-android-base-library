package edu.mit.lastmite.insight_library.util;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import edu.mit.lastmite.insight_library.activity.SingleFragmentActivity;
import edu.mit.lastmite.insight_library.fragment.BaseFragment;
import edu.mit.lastmite.insight_library.fragment.InsightMapsFragment;
import edu.mit.lastmite.insight_library.fragment.TrackFragment;
import edu.mit.lastmite.insight_library.service.NetworkQueueService;
import edu.mit.lastmite.insight_library.task.NetworkTask;

@Singleton
@Component(modules = {AppModule.class})
public interface ApplicationComponent {
    void inject(Application application);

    void inject(SingleFragmentActivity activity);

    void inject(NetworkQueueService service);

    void inject(BaseFragment fragment);

    void inject(InsightMapsFragment fragment);

    void inject(TrackFragment fragment);

    void inject(NetworkTask task);
}