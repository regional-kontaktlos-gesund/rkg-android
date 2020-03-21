package org.wirvsvirus.rkg.model

data class Store(
    val _id: String?,
    val name: String,
    val vendor: String,
    val latitude: Double,
    val longitude: Double,
    val stripeAccountId: String,
    val products: List<Product>,
    val openingHours: List<OpeningHour>,
    val opened: Boolean
)