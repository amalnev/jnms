package ru.amalnev.jnms.common.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.entities.security.UserRole;

@Repository
@EntityClass(UserRole.class)
public interface IUserRoleRepository extends CrudRepository<UserRole, Long>
{
    public UserRole findByName(final String name);
}
