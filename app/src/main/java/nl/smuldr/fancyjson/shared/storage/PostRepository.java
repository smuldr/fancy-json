package nl.smuldr.fancyjson.shared.storage;


import android.text.format.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.smuldr.fancyjson.shared.model.Comment;
import nl.smuldr.fancyjson.shared.model.PartialPost;
import nl.smuldr.fancyjson.shared.model.Post;
import nl.smuldr.fancyjson.shared.model.User;
import nl.smuldr.fancyjson.shared.network.PlaceholderClient;


@Singleton
public class PostRepository {

    private final PlaceholderClient client;
    private final UserRepository userRepository;

    private List<Post> cachedPosts = null;
    private long cacheTimestamp = 0L;

    @Inject
    PostRepository(final PlaceholderClient client, final UserRepository userRepository) {
        this.client = client;
        this.userRepository = userRepository;
    }

    public List<Post> getPosts() throws IOException {
        if (cachedPosts == null || isCacheOutdated()) {
            // nothing in the cache, load fresh data from network
            cachedPosts = loadFromNetwork();
            cacheTimestamp = System.currentTimeMillis();
        }
        return cachedPosts;
    }

    public Post getPost(long postId) throws IOException {
        Post result = null;
        if (cachedPosts != null && !isCacheOutdated()) {
            for (final Post cachedPost : cachedPosts) {
                if (cachedPost.getId() == postId) {
                    result = cachedPost;
                }
            }
        }
        if (result == null) {
            final PartialPost partialPost = client.getPost(postId);
            final User user = client.getUserDetails(partialPost.getUserId());
            result = Post.createFromPartial(partialPost, user);
            if (cachedPosts != null) {
                cachedPosts.add(result);
            }
            // TODO: what if cachedPosts is null?
        }
        return result;
    }

    public List<Comment> getComments(long postId) throws IOException {
        return client.getComments(postId);
    }

    private List<Post> loadFromNetwork() throws IOException {
        final List<PartialPost> partialPosts = client.getPosts();
        final List<Post> result = new ArrayList<>(partialPosts.size());
        for (final PartialPost partialPost : partialPosts) {
            final User user = userRepository.getUserDetails(partialPost.getUserId());
            result.add(Post.createFromPartial(partialPost, user));
        }
        return result;
    }

    /**
     * @return true if the cache contains stale data
     */
    private boolean isCacheOutdated() {
        return System.currentTimeMillis() - cacheTimestamp > 5 * DateUtils.MINUTE_IN_MILLIS;
    }
}
