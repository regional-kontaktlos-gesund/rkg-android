package org.wirvsvirus.rkg.model

data class OpeningHours(
    val monday: OpeningHour,
    val tuesday: OpeningHour,
    val wednesday: OpeningHour,
    val thursday: OpeningHour,
    val friday: OpeningHour,
    val saturday: OpeningHour,
    val sunday: OpeningHour
)