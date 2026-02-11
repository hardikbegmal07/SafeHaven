package com.hardik.safehaven.domain.validation

sealed class ValidationResult {
    object Success : ValidationResult() // it means -
    // title is valid
    // content is valid
    // rules satisfied
    data class Error(val message: String) : ValidationResult() // something violated business rules
}
// Did business logic validation pass ??