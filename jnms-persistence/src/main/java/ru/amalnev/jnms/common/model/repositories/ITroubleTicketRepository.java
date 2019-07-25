package ru.amalnev.jnms.common.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.model.entities.ttms.TroubleTicket;

@Repository
@EntityClass(TroubleTicket.class)
public interface ITroubleTicketRepository extends CrudRepository<TroubleTicket, Long>
{
}
