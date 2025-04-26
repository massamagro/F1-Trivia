package com.vozmediano.f1trivia.util

fun String.isValidUsername(minLength: Int, maxLength: Int): String? {
    if (isEmpty()) {
        return "Username cannot be empty."
    }
    if (length < minLength) {
        return "Username must be at least $minLength characters long."
    }
    if (length > maxLength) {
        return "Username cannot be longer than $maxLength characters."
    }
    if (!matches(Regex("^[a-zA-Z0-9]*$"))) {
        return "Username can only contain letters and numbers)."
    }
    return null
}