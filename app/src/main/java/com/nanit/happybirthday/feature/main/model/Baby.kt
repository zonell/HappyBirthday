package com.nanit.happybirthday.feature.main.model

data class Baby(
    var name: String = "",
    var bDay: String = "",
    var dataType: DataType? = null,
    var photoUri: String? = null
)