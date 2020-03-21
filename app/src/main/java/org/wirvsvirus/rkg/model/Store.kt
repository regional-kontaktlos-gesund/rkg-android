package org.wirvsvirus.rkg.model

data class Store(
    // TODO id??
    val name: String,
    val vendor: Owner,
    val latitude: Double,
    val longitude: Double,
    val stripeAccountId: String,
    val products: List<Product>,
    val openingHours: OpeningHours,
    val opened: Boolean
)