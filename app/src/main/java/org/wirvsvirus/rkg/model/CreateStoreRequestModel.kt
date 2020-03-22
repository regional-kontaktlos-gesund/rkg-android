package org.wirvsvirus.rkg.model

data class CreateStoreRequestModel(
    val name: String,
    val vendor: String,
    val latitude: Double,
    val longitude: Double
)