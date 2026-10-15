package com.example.model

enum class OfferStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    COUNTERED
}

data class TransferOffer(
    val id: String,
    val playerId: String,
    val playerName: String,
    val fromClubId: String,
    val fromClubName: String,
    val toClubId: String,
    val toClubName: String,
    val amount: Long,
    val playerWage: Long,
    val status: OfferStatus = OfferStatus.PENDING,
    val counterAmount: Long? = null,
    val message: String = ""
) {
    val formattedAmount: String
        get() = if (amount >= 1_000_000) {
            String.format("%.1fM €", amount / 1_000_000.0)
        } else {
            String.format("%dK €", amount / 1_000)
        }
}
