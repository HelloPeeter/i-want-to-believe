package com.peter.iwanttobelieve.data.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Post (
    @DocumentId val id: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val timestamp: Date = Date(),
    val userId: String = "",
    val likes: List<String> = emptyList(),
)
