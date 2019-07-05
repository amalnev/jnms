package ru.amalnev.jnms.common.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.model.entities.ttms.ServiceQualityProblemDetails;

@Repository
@EntityClass(ServiceQualityProblemDetails.class)
public interface IServiceQualityProblemDetails extends CrudRepository<ServiceQualityProblemDetails, Long>
{
}
