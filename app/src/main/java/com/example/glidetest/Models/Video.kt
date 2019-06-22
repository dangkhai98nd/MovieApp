package com.example.glidetest.Models

import com.google.gson.annotations.SerializedName

class Video (
    @field:SerializedName("id")
    var id : String? = null,
    @field:SerializedName("iso_639_1")
    var iso_639_1 : String? = null,
    @field:SerializedName("iso_3166_1")
    var iso_3166_1 : String? = null,
    @field:SerializedName("key")
    var key : String? = null,
    @field:SerializedName("name")
    var name : String? = null,
    @field:SerializedName("site")
    var site : String? = null,
    @field:SerializedName("size")
    var size : Int? = null,
    @field:SerializedName("type")
    var type : String? = null
)