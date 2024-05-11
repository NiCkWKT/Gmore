package edu.cuhk.csci3310.gmore.data.api.model

import com.google.gson.annotations.SerializedName
import java.io.File

data class OcrEntity(
    @SerializedName("image")
    val image: File,
)
