package ru.amalnev.jnms.common.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.entities.model.Site;

@Repository
@EntityClass(Site.class)
public interface ISiteRepository extends CrudRepository<Site, Long>
{
}
