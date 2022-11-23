package adil.app.mviapp.di

import adil.app.mviapp.data.api.APIService
import adil.app.mviapp.data.repository.PostRepository
import adil.app.mviapp.utils.AppConstants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providesApiService(): APIService =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)

    @Provides
    fun providesPostRepository(apiService: APIService) =
        PostRepository(apiService)

}