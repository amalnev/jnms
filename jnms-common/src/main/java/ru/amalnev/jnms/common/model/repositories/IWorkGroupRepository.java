package ru.amalnev.jnms.common.model.repositories;

import org.hibernate.jdbc.Work;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.model.entities.ttms.WorkGroup;

@Repository
@EntityClass(WorkGroup.class)
public interface IWorkGroupRepository extends CrudRepository<WorkGroup, Long>
{
}
