package com.peter.iwanttobelieve.data.datasource

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
    suspend fun reauthenticateWithPassword(password: String) {
        val user = auth.currentUser ?: throw IllegalStateException("User is not logged in.")
        val credential = EmailAuthProvider.getCredential(user.email!!, password)
        user.reauthenticate(credential).await()
    }

    fun getCurrentUserId(): String {
      return auth.currentUser?.uid ?: throw IllegalStateException("User is not logged in.")
    }

    suspend fun signIn(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email, password).await()

        return result.user?.uid ?: throw IllegalStateException("User not logged in successfully.")
    }

    suspend fun signUp(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw IllegalStateException("User not saved successfully.")

        // Força a renovação do token de ID antes de qualquer escrita no Firestore.
        // Sem isso, a primeira escrita logo após o cadastro pode ser rejeitada com
        // PERMISSION_DENIED: o Firestore às vezes ainda não "recebeu" o token do
        // usuário recém-criado no exato instante da chamada seguinte.
        user.getIdToken(true).await()

        return user.uid
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun updatePassword(newPassword: String) {
        val user = auth.currentUser ?: throw IllegalStateException("User is not logged in.")

        user.updatePassword(newPassword).await()
    }

    suspend fun updateEmail(newEmail: String) {
        val user = auth.currentUser ?: throw IllegalStateException("User is not logged in.")

        user.updateEmail(newEmail).await()
    }
}