package org.wirvsvirus.rkg.model

data class Product (
    val name: String,
    val category: Category,
    val price: Double,
    val numberType: NumberType  // TODO: Find better name for this
)

// TODO: Add icons for the POI based on category?
enum class Category {
    FRUITS,
    VEGETABLES
}

enum class NumberType {
    GRAMS,
    KILOGRAMS,
    PIECES
}

