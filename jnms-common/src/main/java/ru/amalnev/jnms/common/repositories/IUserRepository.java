package ru.amalnev.jnms.common.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.entities.security.User;

@Repository
@EntityClass(User.class)
public interface IUserRepository extends CrudRepository<User, Long>
{
    User findByUsername(String username);
}
