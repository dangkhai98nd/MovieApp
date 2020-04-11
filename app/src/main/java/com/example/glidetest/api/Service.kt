package com.example.glidetest.api

import com.example.glidetest.BuildConfig
import com.example.glidetest.Models.ApiImages
import com.example.glidetest.Models.ApiVideos
import com.example.retrofittest.Models.ApiMovies
import com.example.retrofittest.Models.Movie
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @GET("movie/upcoming")
    fun getApiMoviesUpcoming(
        @Query("page") page : Int = 1,
        @Query("api_key") api_key : String = BuildConfig.API_KEY
    ) : Call<ApiMovies>

    @GET ("movie/popular")
    fun getApiMoviesPopular(
        @Query("page") page : Int = 1,
        @Query("api_key") api_key : String = BuildConfig.API_KEY
    ) : Call<ApiMovies>

    @GET("movie/now_playing")
    fun getApiMovies(
        @Query("page") page : Int = 1,
        @Query("api_key") api_key : String = BuildConfig.API_KEY
    ) : Call<ApiMovies>

    @GET("movie/{id}")
    fun getApiMovieDetail(@Path("id") movieID : Int,
                    @Query("api_key") api_key: String = BuildConfig.API_KEY
    ) : Call<Movie>

    @GET("movie/{id}/videos")
    fun getApiMovieVideos(
        @Path("id") movieID: Int,
        @Query("api_key") api_key: String = BuildConfig.API_KEY
    ) : Call<ApiVideos>

    @GET("/3/movie/{id}/images")     // wtf???
    fun getApiImages(
        @Path("id") movieID: Int,
        @Query("api_key") api_key: String = BuildConfig.API_KEY
    ) : Call<ApiImages>

    @GET("movie/now_playing")
    fun getApiMoviesObservable(
        @Query("page") page : Int = 1,
        @Query("api_key") api_key : String = BuildConfig.API_KEY
    ) : Observable<ApiMovies>
}