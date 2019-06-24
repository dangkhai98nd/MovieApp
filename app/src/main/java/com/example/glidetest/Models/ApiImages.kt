package com.example.glidetest.Models


import com.google.gson.annotations.SerializedName

class ApiImages (
    @field:SerializedName("id")
    var id : Int? = null,
    @field:SerializedName("backdrops")
    var backdrops : List<Image>? = null,
    @field:SerializedName("posters")
    var poster : List<Image>? = null
) {
    class Image (
        @field:SerializedName("aspect_ratio")
        var aspect_ratio : Float? = null,
        @field:SerializedName("file_path")
        var file_path : String? = null,
        @field:SerializedName("height")
        var height : Int? = null,
        @field:SerializedName("iso_639_1")
        var iso_639_1 : String? = null,
        @field:SerializedName("vote_average")
        var vote_average : Float? = null,
        @field:SerializedName("vote_count")
        var vote_count : Int? = null,
        @field:SerializedName("width")
        var width : Int? = null
    ) {
        fun get_image_path() : String? = "https://image.tmdb.org/t/p/w500$file_path"
    }


}