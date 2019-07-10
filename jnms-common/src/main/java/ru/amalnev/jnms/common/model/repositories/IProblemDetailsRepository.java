package ru.amalnev.jnms.common.model.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.model.entities.ttms.ProblemDetails;

@Repository
@EntityClass(ProblemDetails.class)
public interface IProblemDetailsRepository extends CrudRepository<ProblemDetails, Long>
{
}
