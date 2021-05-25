package com.erikschouten.cleanarchitecture.domain.entity

import java.util.*

abstract class UUIDEntity(
    val id: UUID = UUID.randomUUID(),
)
