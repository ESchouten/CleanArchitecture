package repositories

interface Repository<Entity, ID> {
    fun findById(id: ID): Entity?
    fun findAll(): List<Entity>
    fun save(entity: Entity): Entity
}
