package nl.smuldr.fancyjson;


import javax.inject.Singleton;

import dagger.Component;
import nl.smuldr.fancyjson.post.details.PostDetailsActivity;
import nl.smuldr.fancyjson.post.overview.PostListActivity;
import nl.smuldr.fancyjson.shared.AppModule;
import nl.smuldr.fancyjson.shared.network.NetworkModule;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface MainComponent {

    void inject(PostListActivity activity);

    void inject(PostDetailsActivity activity);
}
