package com.erikschouten.cleanarchitecture.usecases.model.common

abstract class ModelList<T : Any>(
    val items: List<T>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ModelList<*>) return false

        if (items != other.items) return false

        return true
    }

    override fun hashCode(): Int {
        return items.hashCode()
    }
}
