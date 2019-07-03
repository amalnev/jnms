package ru.amalnev.jnms.common.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.model.entities.security.Authority;

@Repository
@EntityClass(Authority.class)
public interface IAuthorityRepository extends CrudRepository<Authority, Long>
{
}
