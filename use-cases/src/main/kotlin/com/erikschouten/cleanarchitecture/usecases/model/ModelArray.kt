package com.erikschouten.cleanarchitecture.usecases.model

abstract class ModelArray<T : Any>(
    val items: Array<T>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserModelArray) return false

        if (!items.contentEquals(other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        return items.contentHashCode()
    }
}
