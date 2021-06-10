package com.erikschouten.cleanarchitecture.domain.repository

interface Repository<Entity, ID> {
    suspend fun findById(id: ID): Entity?
    suspend fun findAll(): List<Entity>
    suspend fun create(entity: Entity): Entity
    suspend fun update(entity: Entity): Entity
    suspend fun delete(id: ID)
    suspend fun count(): Long
}
