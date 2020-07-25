package com.example.tmdb.di.modules

import android.app.Application
import com.example.tmdb.api.AppConstants
import com.example.tmdb.api.TmdbService
import com.example.tmdb.repository.MovieRepository
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class AppModule {

    @Provides
      fun provideRepository( tmdbService: TmdbService, application: Application )
            : MovieRepository {
        return MovieRepository(tmdbService, application)
    }

    @Provides
    fun provideTmdbClient(): OkHttpClient {
         val authInterceptor = Interceptor {chain->
            val newUrl = chain.request().url
                .newBuilder()
                .addQueryParameter("api_key", AppConstants.TMDB_API_KEY)
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
         val tmdbClient = OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .build()
        return tmdbClient
    }

    @Provides
    fun retrofitService() : Retrofit {
        return Retrofit.Builder()
            .client(provideTmdbClient())
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun provideMovieService(): TmdbService {
        return retrofitService().create(TmdbService::class.java)
    }

}