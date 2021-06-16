package com.erikschouten.cleanarchitecture.domain.entity

interface ValueClass<T: Any> {
    val value: T
}
