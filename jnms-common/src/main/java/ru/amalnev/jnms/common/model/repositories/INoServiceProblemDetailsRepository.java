package ru.amalnev.jnms.common.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.model.entities.ttms.NoServiceProblemDetails;

@Repository
@EntityClass(NoServiceProblemDetails.class)
public interface INoServiceProblemDetailsRepository extends CrudRepository<NoServiceProblemDetails, Long>
{
}
