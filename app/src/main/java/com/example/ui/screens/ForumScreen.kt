package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ForumPost
import com.example.model.ForumReply
import com.example.ui.theme.*

@Composable
fun ForumScreen(
    posts: List<ForumPost>,
    isCreatePostDialogOpen: Boolean,
    onOpenCreatePostDialog: () -> Unit,
    onCloseCreatePostDialog: () -> Unit,
    onSubmitPost: (String, String, String) -> Unit,
    onReplyToPost: (String, String) -> Unit,
    onToggleLike: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Todos") }
    val categories = listOf("Todos", "Partidos", "Tácticas", "Fichajes")

    val filteredPosts = remember(posts, selectedCategory) {
        if (selectedCategory == "Todos") posts else posts.filter { it.category == selectedCategory }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NervaBlueDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "FORO DE DIRECTORES TÉCNICOS",
                        color = NervaCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Comunidad oficial de managers de la liga",
                        color = NervaTextMuted,
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = onOpenCreatePostDialog,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NervaCyan,
                        contentColor = NervaBlueDeep
                    ),
                    modifier = Modifier.testTag("create_post_button"),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Publicar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Categories Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NervaCyan,
                            selectedLabelColor = NervaBlueDeep,
                            containerColor = NervaBlueSurface,
                            labelColor = NervaWhite
                        )
                    )
                }
            }

            // Forum Posts List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredPosts) { post ->
                    ForumPostCard(
                        post = post,
                        onToggleLike = { onToggleLike(post.id) },
                        onReply = { replyText -> onReplyToPost(post.id, replyText) }
                    )
                }
            }
        }
    }

    if (isCreatePostDialogOpen) {
        CreatePostDialog(
            onDismiss = onCloseCreatePostDialog,
            onSubmit = onSubmitPost
        )
    }
}

@Composable
private fun ForumPostCard(
    post: ForumPost,
    onToggleLike: () -> Unit,
    onReply: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var replyInput by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("forum_post_${post.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NervaBlueSurface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(NervaBlueSurfaceVariant)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Author Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(NervaBlueElectric),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.authorName.take(2).uppercase(),
                            color = NervaWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    Column {
                        Text(
                            text = post.authorName,
                            color = NervaWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "${post.authorRole} • ${post.timeAgo}",
                            color = NervaTextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                // Category pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NervaBlueContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = post.category,
                        color = NervaCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Post Title & Content
            Text(
                text = post.title,
                color = NervaWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Text(
                text = post.content,
                color = NervaWhite.copy(alpha = 0.85f),
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Divider(color = NervaBlueSurfaceVariant, thickness = 0.8.dp)

            // Interaction Bar (Likes & Comments toggle)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onToggleLike() }
                ) {
                    Icon(
                        imageVector = if (post.isLikedByUser) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Me gusta",
                        tint = if (post.isLikedByUser) NervaRed else NervaTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.likesCount}",
                        color = if (post.isLikedByUser) NervaRed else NervaTextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isExpanded = !isExpanded }
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Comentarios",
                        tint = NervaCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.replies.size} respuestas",
                        color = NervaCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Expandable Replies
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    post.replies.forEach { reply ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = NervaBlueDeep)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${reply.authorName} (${reply.authorRole})",
                                        color = NervaCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = reply.timeAgo,
                                        color = NervaTextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                                Text(
                                    text = reply.content,
                                    color = NervaWhite,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    // Reply Input field
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedTextField(
                            value = replyInput,
                            onValueChange = { replyInput = it },
                            placeholder = { Text("Escribe una respuesta...", color = NervaTextMuted, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = NervaBlueDeep,
                                unfocusedContainerColor = NervaBlueDeep,
                                focusedTextColor = NervaWhite,
                                unfocusedTextColor = NervaWhite
                            )
                        )
                        IconButton(
                            onClick = {
                                if (replyInput.isNotBlank()) {
                                    onReply(replyInput)
                                    replyInput = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Enviar", tint = NervaCyan)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CreatePostDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Partidos") }
    val categories = listOf("Partidos", "Tácticas", "Fichajes", "Polémica")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NervaBlueSurface,
        title = {
            Text(
                text = "Nueva Publicación de DT",
                color = NervaCyan,
                fontWeight = FontWeight.Black,
                fontSize = 17.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Category Picker
                Text(text = "Categoría:", color = NervaTextMuted, fontSize = 11.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NervaCyan,
                                selectedLabelColor = NervaBlueDeep,
                                containerColor = NervaBlueDeep,
                                labelColor = NervaWhite
                            )
                        )
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Título llamativo...", color = NervaTextMuted, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NervaBlueDeep,
                        unfocusedContainerColor = NervaBlueDeep,
                        focusedTextColor = NervaWhite,
                        unfocusedTextColor = NervaWhite
                    )
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Escribe tu análisis, opinión táctica o propuesta a los colegas...", color = NervaTextMuted, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NervaBlueDeep,
                        unfocusedContainerColor = NervaBlueDeep,
                        focusedTextColor = NervaWhite,
                        unfocusedTextColor = NervaWhite
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(title, content, selectedCategory) },
                colors = ButtonDefaults.buttonColors(containerColor = NervaCyan, contentColor = NervaBlueDeep),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Publicar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = NervaTextMuted)
            }
        }
    )
}
