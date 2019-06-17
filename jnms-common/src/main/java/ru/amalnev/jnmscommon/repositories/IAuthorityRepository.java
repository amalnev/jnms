package ru.amalnev.jnmscommon.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnmscommon.entities.security.Authority;

@Repository
@EntityClass(Authority.class)
public interface IAuthorityRepository extends CrudRepository<Authority, Long>
{
}
