package org.wirvsvirus.rkg.model

data class Product(
    val name: String,
    val type: String, // Enum!
    val unit: String,
    val price: Long,
    val stock: String
)