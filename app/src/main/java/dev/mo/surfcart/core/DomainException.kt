package dev.mo.surfcart.core

import java.io.IOException

/**
 * A sealed class representing domain-specific exceptions that can be handled by the UI layer.
 */
sealed class DomainException(message: String) : Exception(message)

/**
 * Thrown when a network error occurs (e.g., no internet connection).
 */
class NetworkException(message: String = "Network error occurred") : DomainException(message)

/**
 * Thrown when attempting to create an entry that already exists (e.g., adding a duplicate item to the cart).
 */
class DuplicateEntryException(message: String = "This item already exists.") : DomainException(message)

/**
 * Thrown for any other unhandled or unknown errors.
 */
class UnknownException(message: String = "An unknown error occurred.") : DomainException(message)
