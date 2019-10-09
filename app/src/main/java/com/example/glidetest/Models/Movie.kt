package com.example.retrofittest.Models


import com.example.glidetest.Models.Genre
import com.google.gson.annotations.SerializedName

//@Parcelize

class Movie (
    @field:SerializedName("vote_count")
    var vote_count : Int? = null,
    @field:SerializedName("id")
    var id : Int? = null,
    @field:SerializedName("video")
    var video : Boolean? = null,
    @field:SerializedName("vote_average")
    var vote_average : Float? = null,
    @field:SerializedName("title")
    var name : String? = null,
    @field:SerializedName("popularity")
    var popularity : Float? = null,
    @field:SerializedName("poster_path")
    var poster_path : String? = null,
    @field:SerializedName("original_language")
    var language : String? = null,
    @field:SerializedName("original_title")
    var original_title : String? = null,
    @field:SerializedName("genre_ids")
    var genre_ids : List<Int>? = null,
    @field:SerializedName("backdrop_path")
    var backdrop_path : String? = null,
    @field:SerializedName("adult")
    var adult : Boolean? = null,
    @field:SerializedName("overview")
    var overview : String? = null,
    @field:SerializedName("release_date")
    var release_date : String? = null,


    @field:SerializedName("runtime")
    var runtime: Int? = null,
    @field:SerializedName("tagline")
    var tagline : String? = null,
    @field:SerializedName("genres")
    var genre : List<Genre>? = null

)
//    :Parcelable
{

    fun get_poster_path() : String? = "https://image.tmdb.org/t/p/w500$poster_path"
    fun get_backdrop_path() : String? = "https://image.tmdb.org/t/p/w500$backdrop_path"
}

