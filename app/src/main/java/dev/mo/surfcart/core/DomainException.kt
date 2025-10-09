package dev.mo.surfcart.core

sealed class DomainException(message: String) : Exception(message)

class NetworkException(message: String = "Network error occurred") : DomainException(message)

class DuplicateEntryException(message: String = "This item already exists.") : DomainException(message)

class UnknownException(message: String = "An unknown error occurred.") : DomainException(message)
