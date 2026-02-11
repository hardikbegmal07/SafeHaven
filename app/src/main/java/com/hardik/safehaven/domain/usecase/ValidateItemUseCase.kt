package com.hardik.safehaven.domain.usecase

import com.hardik.safehaven.domain.validation.ValidationResult

class ValidateItemUseCase {

    operator fun invoke(
        title: String,
        content: String
    ): ValidationResult {
        if(title.isBlank()) {
            return ValidationResult.Error("Title cannot be empty")
        }
        if(content.isBlank()) {
            return ValidationResult.Error("Content cannot be empty")
        }
        if(title.length < 4) {
            return ValidationResult.Error("title too short")
        }

        return ValidationResult.Success
    }
}
// why validation is a use-case ??
//  Business rule
//  Reusable (API, AI, Backend later)
//  Testable