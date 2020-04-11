package com.example.retrofittest.Models

import com.google.gson.annotations.SerializedName

class ApiMovies (
    @field:SerializedName("results")
    var results : MutableList<Movie>,
    @field:SerializedName("page")
    var page : Int? = 0,
    @field:SerializedName("total_results")
    var total_results : Int? = 0,
    @field:SerializedName( "dates")
    var dates : Dates? = null,
    @field:SerializedName("total_pages")
    var total_pages : Int? = 0

)