package com.example.model

data class ForumReply(
    val id: String,
    val authorName: String,
    val authorRole: String,
    val content: String,
    val timeAgo: String,
    val likesCount: Int = 0
)

data class ForumPost(
    val id: String,
    val authorName: String,
    val authorClubId: String,
    val authorRole: String,
    val title: String,
    val content: String,
    val category: String, // "Tácticas", "Fichajes", "Partidos", "Polémica"
    val timeAgo: String,
    val likesCount: Int = 0,
    val isLikedByUser: Boolean = false,
    val replies: List<ForumReply> = emptyList()
)
