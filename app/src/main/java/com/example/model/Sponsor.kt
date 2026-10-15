package com.example.model

data class Sponsor(
    val id: String,
    val name: String,
    val type: String, // "Principal", "Manga", "Ropa Deportiva"
    val logoTag: String,
    val weeklyIncome: Long,
    val winBonus: Long,
    val contractDurationMonths: Int,
    val isSigned: Boolean = false,
    val description: String
) {
    val formattedWeeklyIncome: String
        get() = "$ " + String.format("%,d", weeklyIncome).replace(",", ".") + "/sem"

    val formattedWinBonus: String
        get() = "+$ " + String.format("%,d", winBonus).replace(",", ".") + " por victoria"
}
