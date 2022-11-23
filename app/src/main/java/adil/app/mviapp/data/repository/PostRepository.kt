package adil.app.mviapp.data.repository

import adil.app.mviapp.data.api.APIService

class PostRepository(private val apiService: APIService) {

    suspend fun getPosts() = apiService.getPosts()

}