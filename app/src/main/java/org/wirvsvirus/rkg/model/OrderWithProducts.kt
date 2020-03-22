package org.wirvsvirus.rkg.model

data class OrderWithProducts(
    val __v: Int,
    val _id: String,
    val code: String,
    val items: List<OrderProductItem>,
    val store: String,
    val sumTotal: Int
)