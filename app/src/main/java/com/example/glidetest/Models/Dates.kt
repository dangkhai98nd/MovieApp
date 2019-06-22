package com.example.retrofittest.Models

import com.google.gson.annotations.SerializedName
import java.util.*

class Dates (
    @field:SerializedName("maximum")
    var maximum : String?,
    @field:SerializedName("minimum")
    var minimum : String?
)