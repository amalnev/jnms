package ru.amalnev.jnms.common.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.entities.security.Authority;

@Repository
@EntityClass(Authority.class)
public interface IAuthorityRepository extends CrudRepository<Authority, Long>
{
}
