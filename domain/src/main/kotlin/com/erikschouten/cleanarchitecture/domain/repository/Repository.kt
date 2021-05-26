package com.erikschouten.cleanarchitecture.domain.repository

interface Repository<Entity, ID> {
    fun findById(id: ID): Entity?
    fun findAll(): List<Entity>
    fun create(entity: Entity): Entity
    fun update(entity: Entity): Entity
}
