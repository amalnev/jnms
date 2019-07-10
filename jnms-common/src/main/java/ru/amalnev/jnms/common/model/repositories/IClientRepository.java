package ru.amalnev.jnms.common.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.model.entities.ttms.Client;

@Repository
@EntityClass(Client.class)
public interface IClientRepository extends CrudRepository<Client, Long>
{
    Client findClientByName(final String clientName);

    Client findClientByNameAndAddress(final String clientName, final String address);

    Client findClientByNameAndAddressAndContractNumber(final String clientName,
                                                       final String address,
                                                       final String contractNumber);

    Client findClientByAddress(final String address);

    Client findClientByAddressAndContractNumber(final String address,
                                                final String contractNumber);

    Client findClientByContractNumber(final String contractNumber);
}
