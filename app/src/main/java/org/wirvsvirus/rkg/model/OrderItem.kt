package org.wirvsvirus.rkg.model

data class OrderItem(
    val productId: String,
    val amount: Int,
    val price: Int
)