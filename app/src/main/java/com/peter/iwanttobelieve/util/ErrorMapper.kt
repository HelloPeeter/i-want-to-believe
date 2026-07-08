package com.peter.iwanttobelieve.util

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.StorageException
import java.io.IOException
import java.net.HttpRetryException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

class ErrorMapper {

	fun map(throwable: Throwable?): ErrorType = when (throwable) {
		// Gerais
		is IOException -> ErrorType.NetworkError
		is SocketTimeoutException -> ErrorType.NetworkError
		is UnknownHostException -> ErrorType.NetworkError
		is SecurityException -> ErrorType.PermissionDenied
		is HttpRetryException -> ErrorType.ServiceUnavailable
		is IllegalArgumentException -> ErrorType.InvalidArgument
		is CancellationException -> ErrorType.Canceled
		is InterruptedException -> ErrorType.Canceled
		is UnsupportedOperationException -> ErrorType.OperationFailed

		// Firebase Auth
		is FirebaseAuthException -> mapFirebaseAuthException(throwable)

		// Custom dos Data Sources
		is IllegalStateException -> mapIllegalStateException(throwable)
		is NullPointerException -> ErrorType.NullUserError

		// Firestore
		is FirebaseFirestoreException -> mapFirestoreException(throwable)

		// Storage
		is StorageException -> mapStorageException(throwable)

		// Fallback
		else -> ErrorType.UnknownError
	}

    private fun mapFirebaseAuthException(ex: FirebaseAuthException): ErrorType {
        val code = ex.errorCode ?: ex.message.orEmpty()
        return when (code) {
            "ERROR_INVALID_CUSTOM_TOKEN" -> ErrorType.InvalidCredentials
            "ERROR_INVALID_EMAIL" -> ErrorType.InvalidEmail
            "ERROR_USER_DISABLED" -> ErrorType.UserDisabled
            "ERROR_USER_NOT_FOUND" -> ErrorType.UserNotFound
            "ERROR_EMAIL_ALREADY_IN_USE" -> ErrorType.EmailAlreadyInUse
            "ERROR_WEAK_PASSWORD" -> ErrorType.WeakPassword
            "ERROR_WRONG_PASSWORD" -> ErrorType.WrongPassword
            "ERROR_TOO_MANY_REQUESTS" -> ErrorType.TooManyRequests
            "ERROR_OPERATION_NOT_ALLOWED" -> ErrorType.OperationNotAllowed
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL", "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIALS" ->
                ErrorType.AccountExistsDifferentCredential
            "ERROR_INVALID_CREDENTIAL" -> ErrorType.InvalidCredential
            "ERROR_USER_MISMATCH" -> ErrorType.UserMismatch
            "ERROR_EXPIRED_ACTION_CODE" -> ErrorType.ExpiredActionCode
            "ERROR_INVALID_VERIFICATION_CODE" -> ErrorType.InvalidVerificationCode
            "ERROR_INVALID_VERIFICATION_ID" -> ErrorType.InvalidVerificationId
            "ERROR_REQUIRES_RECENT_LOGIN", "ERROR_USER_TOKEN_EXPIRED" -> ErrorType.ReauthenticationFailed
            "ERROR_NETWORK_REQUEST_FAILED" -> ErrorType.NetworkError
            else -> ErrorType.UnknownError
        }
    }

    private fun mapIllegalStateException(ex: IllegalStateException): ErrorType {
        return when (ex.message) {
            "User is not logged in." -> ErrorType.UserNotLoggedIn
            "User not saved successfully." -> ErrorType.UserNotSaved
            "User not logged in successfully." -> ErrorType.UserNotLoggedInSuccessfully
            "Failed to update password." -> ErrorType.UpdatePasswordFailed
            "Failed to update email." -> ErrorType.UpdateEmailFailed
            "Image not saved successfully." -> ErrorType.ImageNotSaved
            "Invalid image format." -> ErrorType.InvalidImageFormat
            "Failed to get download url." -> ErrorType.DownloadUrlFailed
            "Failed to convert user." -> ErrorType.UserConversionFailed
            "User not found." -> ErrorType.UserDocumentNotFound
            "User is null." -> ErrorType.NullUserError
            else -> ErrorType.UnknownError
        }
    }

    private fun mapFirestoreException(ex: FirebaseFirestoreException): ErrorType {
        return when (ex.code) {
            FirebaseFirestoreException.Code.NOT_FOUND -> ErrorType.DocumentNotFound
            FirebaseFirestoreException.Code.ALREADY_EXISTS -> ErrorType.DocumentAlreadyExists
            FirebaseFirestoreException.Code.PERMISSION_DENIED -> ErrorType.FirestorePermissionDenied
            FirebaseFirestoreException.Code.UNAVAILABLE,
            FirebaseFirestoreException.Code.INTERNAL -> ErrorType.FirestoreUnavailable
            FirebaseFirestoreException.Code.UNKNOWN,
            FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED,
            FirebaseFirestoreException.Code.OUT_OF_RANGE -> ErrorType.FirestoreOperationFailed
            FirebaseFirestoreException.Code.ABORTED,
            FirebaseFirestoreException.Code.FAILED_PRECONDITION -> ErrorType.FirestoreUpdateFailed
            else -> ErrorType.UnknownError
        }
    }

    private fun mapStorageException(ex: StorageException): ErrorType {
        return when (ex.errorCode) {
            StorageException.ERROR_OBJECT_NOT_FOUND -> ErrorType.StorageObjectNotFound
            StorageException.ERROR_NOT_AUTHORIZED -> ErrorType.StorageNotAuthorized
            StorageException.ERROR_QUOTA_EXCEEDED -> ErrorType.StorageQuotaExceeded
            StorageException.ERROR_RETRY_LIMIT_EXCEEDED -> ErrorType.StorageRetryLimitExceeded
            StorageException.ERROR_NOT_AUTHENTICATED -> ErrorType.StorageNotAuthenticated
            StorageException.ERROR_CANCELED -> ErrorType.ImageUploadFailed
            StorageException.ERROR_UNKNOWN -> ErrorType.DownloadUrlFailed
            else -> ErrorType.UnknownError
        }
    }
}