package com.peter.iwanttobelieve.data.repository

import com.peter.iwanttobelieve.data.datasource.PostRemoteDataSource
import com.peter.iwanttobelieve.data.datasource.UserAuthDataSource
import com.peter.iwanttobelieve.data.datasource.UserRemoteDataSource
import com.peter.iwanttobelieve.data.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postRemote: PostRemoteDataSource,
    private val userAuth: UserAuthDataSource,
    private val userRemote: UserRemoteDataSource
) {
    suspend fun createPostWithImage(image: ByteArray, description: String): Result<Unit> {
        return try {

            val userId = userAuth.getCurrentUserId()
            val url = postRemote.uploadPostImage(userId, image)
            val timestamp = Date()

            val post = Post(
                description = description,
                imageUrl = url,
                timestamp = timestamp,
                userId = userId
            )

            val result = postRemote.createPost(userId, post)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun observePostsByTimestamp(): Flow<Result<List<Post>>> {
        return postRemote.observePostsByTimestamp()
            .map<List<Post>, Result<List<Post>>> { posts -> Result.success(posts) }
            .catch { e -> emit(Result.failure(e)) }
    }

    suspend fun toggleLike(postId: String, isCurrentlyLiked: Boolean): Result<Unit> {
        return try {
            val userId = userAuth.getCurrentUserId()
            postRemote.toggleLike(postId, userId, isCurrentlyLiked)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}