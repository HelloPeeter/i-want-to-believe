package com.peter.iwanttobelieve.data.datasource

import com.peter.iwanttobelieve.data.model.User
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    suspend fun createUser(uid: String, user: User): String {
        firestore.collection("users")
            .document(uid)
            .set(user)
            .await()

        return uid
    }

    suspend fun setUserImage(uid: String, image: ByteArray) {
        val path = "profiles/$uid/profile.jpg"

        val reference = storage.reference.child(path)
        reference.putBytes(image).await()
    }

    suspend fun getUserImageUrl(uid: String): String {
        val path = "profiles/$uid/profile.jpg"
        val reference = storage.reference.child(path)
        val url = reference.downloadUrl.await().toString()
        return url
    }


    suspend fun getUser(uid: String): User {
        val user = firestore.collection("users")
            .document(uid)
            .get()
            .await()
            .toObject(User::class.java) ?: throw IllegalStateException("User not found.")

        return user
    }

    suspend fun updateUser(uid:String, updates: Map<String, Any>): String {
        firestore.collection("users")
            .document(uid)
            .update(updates)
            .await()

        return uid
    }

    suspend fun getUsersByIds(userIds: List<String>): Map<String, User> {
        val result = mutableMapOf<String, User>()
        if (userIds.isEmpty()) return result

        val chunks = userIds.chunked(10)
        for (chunk in chunks) {
            val snapshot = firestore.collection("users")
                .whereIn(FieldPath.documentId(), chunk)
                .get()
                .await()
            snapshot.documents.forEach { doc ->
                doc.toObject(User::class.java)?.let { user ->
                    result[doc.id] = user
                }
            }
        }

        return result
    }
}