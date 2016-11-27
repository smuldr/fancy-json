package nl.smuldr.fancyjson.shared.network;


import java.util.List;

import nl.smuldr.fancyjson.shared.model.PartialPost;
import nl.smuldr.fancyjson.shared.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * See: https://jsonplaceholder.typicode.com/
 */
interface PlaceholderBackend {

    @GET("posts")
    Call<List<PartialPost>> getPosts();

    @GET("users/{id}")
    Call<User> getUserDetails(@Path("id") long userId);
}
