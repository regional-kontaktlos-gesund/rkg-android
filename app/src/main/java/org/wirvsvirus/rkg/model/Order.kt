package org.wirvsvirus.rkg.model

data class Order(
    val __v: Int,
    val _id: String,
    val code: String,
    val items: List<Product>,
    val store: String,
    val sumTotal: Int
)