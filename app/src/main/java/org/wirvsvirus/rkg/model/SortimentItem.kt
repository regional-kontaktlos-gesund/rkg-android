package org.wirvsvirus.rkg.model

data class SortimentItem(
    val name: String,
    val type: String, // enum?
    val price: Int,
    val available: Int,
    val amount: String
)