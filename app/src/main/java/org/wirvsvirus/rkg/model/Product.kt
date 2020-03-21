package org.wirvsvirus.rkg.model

data class Product(
    val name: String,
    val type: String, // Enum!
    val amount: String,
    val price: Long,
    val available: Int // TODO Stufen statt Menge
)