package com.example.glidetest.Models

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

class ApiVideos (
    @field:SerializedName("id")
    var id : Int? = null,
    @field:SerializedName("results")
    var results : List<Video>? = null

)
