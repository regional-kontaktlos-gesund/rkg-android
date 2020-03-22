package org.wirvsvirus.rkg.model

data class Order(
    val __v: Int,
    val _id: String,
    val code: String,
    val items: List<OrderItem>,
    val store: String,
    val sumTotal: Int
)