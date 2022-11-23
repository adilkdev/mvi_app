package adil.app.mviapp.data.api

import adil.app.mviapp.data.model.Post
import retrofit2.http.GET

interface APIService {

    @GET("posts")
    suspend fun getPosts(): List<Post>

}