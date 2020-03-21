package org.wirvsvirus.rkg.model

data class Store(
    val name: String,
    val owner: Owner,
    val latitude: Double,
    val longitude: Double,
    val openingHours: OpeningHours,
    val opened: Boolean
)