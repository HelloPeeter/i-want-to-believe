package com.peter.iwanttobelieve.util

import androidx.annotation.StringRes
import com.peter.iwanttobelieve.R

sealed class ErrorType(@StringRes val messageResId: Int) {
    // Gerais
    object NetworkError : ErrorType(R.string.error_network)
    object UnknownError : ErrorType(R.string.error_unknown)
    object PermissionDenied : ErrorType(R.string.error_permission_denied)
    object ServiceUnavailable : ErrorType(R.string.error_service_unavailable)
    object InvalidArgument : ErrorType(R.string.error_invalid_argument)
    object Canceled : ErrorType(R.string.error_cancelled)
    object OperationFailed : ErrorType(R.string.error_operation_failed)

    // Firebase Auth
    object InvalidCredentials : ErrorType(R.string.error_invalid_credentials)
    object InvalidEmail : ErrorType(R.string.error_invalid_email)
    object UserDisabled : ErrorType(R.string.error_user_disabled)
    object UserNotFound : ErrorType(R.string.error_user_not_found)
    object EmailAlreadyInUse : ErrorType(R.string.error_email_already_in_use)
    object WeakPassword : ErrorType(R.string.error_weak_password)
    object WrongPassword : ErrorType(R.string.error_wrong_password)
    object TooManyRequests : ErrorType(R.string.error_too_many_requests)
    object OperationNotAllowed : ErrorType(R.string.error_operation_not_allowed)
    object AccountExistsDifferentCredential : ErrorType(R.string.error_account_exists_different_credential)
    object InvalidCredential : ErrorType(R.string.error_invalid_credential)
    object UserMismatch : ErrorType(R.string.error_user_mismatch)
    object ExpiredActionCode : ErrorType(R.string.error_expired_action_code)
    object InvalidVerificationCode : ErrorType(R.string.error_invalid_verification_code)
    object InvalidVerificationId : ErrorType(R.string.error_invalid_verification_id)
    object ReauthenticationFailed : ErrorType(R.string.error_reauthentication_failed)

    // Custom dos Data Sources
    object UserNotLoggedIn : ErrorType(R.string.error_user_not_logged_in)
    object UserNotSaved : ErrorType(R.string.error_user_not_saved)
    object UserNotLoggedInSuccessfully : ErrorType(R.string.error_user_not_logged_in_successfully)
    object UpdatePasswordFailed : ErrorType(R.string.error_update_password_failed)
    object UpdateEmailFailed : ErrorType(R.string.error_update_email_failed)
    object NullUserError : ErrorType(R.string.error_null_user)

    // Firestore
    object DocumentNotFound : ErrorType(R.string.error_document_not_found)
    object DocumentAlreadyExists : ErrorType(R.string.error_document_already_exists)
    object FirestorePermissionDenied : ErrorType(R.string.error_firestore_permission_denied)
    object FirestoreUnavailable : ErrorType(R.string.error_firestore_unavailable)
    object FirestoreOperationFailed : ErrorType(R.string.error_firestore_operation_failed)
    object FirestoreUpdateFailed : ErrorType(R.string.error_firestore_update_failed)
    object UserDocumentNotFound : ErrorType(R.string.error_user_document_not_found)
    object UserConversionFailed : ErrorType(R.string.error_user_conversion_failed)

    // Storage
    object StorageObjectNotFound : ErrorType(R.string.error_storage_object_not_found)
    object StorageNotAuthorized : ErrorType(R.string.error_storage_not_authorized)
    object StorageQuotaExceeded : ErrorType(R.string.error_storage_quota_exceeded)
    object StorageRetryLimitExceeded : ErrorType(R.string.error_storage_retry_limit_exceeded)
    object StorageNotAuthenticated : ErrorType(R.string.error_storage_not_authenticated)
    object ImageUploadFailed : ErrorType(R.string.error_image_upload_failed)
    object ImageNotSaved : ErrorType(R.string.error_image_not_saved)
    object InvalidImageFormat : ErrorType(R.string.error_invalid_image_format)
    object DownloadUrlFailed : ErrorType(R.string.error_download_url_failed)
}