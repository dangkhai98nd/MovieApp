package com.example.glidetest.Models

import com.google.gson.annotations.SerializedName

class Genre (
    @field:SerializedName("id")
    var id : Int? = null,
    @field:SerializedName("name")
    var name : String? = null
)