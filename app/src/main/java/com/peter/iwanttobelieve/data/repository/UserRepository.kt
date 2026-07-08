package com.peter.iwanttobelieve.data.repository

import com.peter.iwanttobelieve.data.datasource.UserAuthDataSource
import com.peter.iwanttobelieve.data.datasource.UserRemoteDataSource
import com.peter.iwanttobelieve.data.model.User
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.delay

class UserRepository(
    private val auth: UserAuthDataSource,
    private val remote: UserRemoteDataSource
) {

    // Retorna o uid do usuário logado, ou null se não houver sessão ativa.
    // Usado, por exemplo, para saber se o usuário atual já curtiu um post.
    fun getCurrentUserIdOrNull(): String? {
        return try {
            auth.getCurrentUserId()
        } catch (e: Exception) {
            null
        }
    }

    fun isUserLoggedIn(): Result<Boolean> {
        return try {
            val result = auth.isUserLoggedIn()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun createUser(email: String, name: String, password: String): Result<Unit>  {
        return try {
            val uid = auth.signUp(email, password)
            val user = User(uid, email, name)

            // A primeira escrita no Firestore logo após o cadastro pode, raramente,
            // ser rejeitada com PERMISSION_DENIED por uma corrida entre o token do
            // Auth e o Firestore (mesmo já forçando refresh do token em signUp()).
            // Tenta de novo algumas vezes antes de desistir.
            createUserWithRetry(uid, user)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun createUserWithRetry(
        uid: String,
        user: User,
        attempts: Int = 3
    ) {
        repeat(attempts) { attempt ->
            try {
                remote.createUser(uid, user)
                return
            } catch (e: FirebaseFirestoreException) {
                val isLastAttempt = attempt == attempts - 1
                if (e.code != FirebaseFirestoreException.Code.PERMISSION_DENIED || isLastAttempt) {
                    throw e
                }
                delay(500L * (attempt + 1))
            }
        }
    }

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            auth.signIn(email, password)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut(): Result<Unit> {
        return try {
            auth.signOut()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun setUserImage(image: ByteArray): Result<Unit> {
        return try {
            val uid = auth.getCurrentUserId()

            remote.setUserImage(uid, image)

            val url = remote.getUserImageUrl(uid)

            remote.updateUser(uid, mapOf("imageUrl" to url))

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): Result<User> {
        return try {
            val uid = auth.getCurrentUserId()
            val user = remote.getUser(uid)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsersByIds(userIds: List<String>): Result<Map<String, User>> {
        return try {
            val map = remote.getUsersByIds(userIds)

            Result.success(map)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Atualiza nome e e-mail sem exigir a senha: a senha só é necessária quando o
    // próprio Firebase pedir uma reautenticação (ex.: sessão antiga), o que é
    // sinalizado por ErrorType.ReauthenticationFailed para a UI.
    suspend fun updateProfile(email: String, name: String): Result<Unit> {
        return try {
            val uid = auth.getCurrentUserId()

            auth.updateEmail(email)
            remote.updateUser(uid, mapOf("email" to email, "name" to name))

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Trocar a senha continua exigindo a senha atual, pois é uma operação sensível.
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        return try {
            auth.reauthenticateWithPassword(currentPassword)
            auth.updatePassword(newPassword)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}