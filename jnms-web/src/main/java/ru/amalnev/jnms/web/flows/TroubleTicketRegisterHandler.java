package ru.amalnev.jnms.web.flows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import ru.amalnev.jnms.common.model.entities.ttms.Client;
import ru.amalnev.jnms.common.model.entities.ttms.ProblemDetails;
import ru.amalnev.jnms.common.model.entities.ttms.TroubleTicket;
import ru.amalnev.jnms.common.model.repositories.IClientRepository;
import ru.amalnev.jnms.common.model.repositories.IProblemDetailsRepository;
import ru.amalnev.jnms.common.model.repositories.ITroubleTicketRepository;

public class TroubleTicketRegisterHandler
{
    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IProblemDetailsRepository problemDetailsRepository;

    @Autowired
    private ITroubleTicketRepository troubleTicketRepository;

    private static final String FAILURE = "failure";
    private static final String SUCCESS = "success";

    public TroubleTicketRegisterModel makeModel()
    {
        return new TroubleTicketRegisterModel();
    }

    public void setClient(final TroubleTicketRegisterModel troubleTicketRegisterModel,
                          final Client client)
    {
        troubleTicketRegisterModel.setClient(client);
    }

    public String validateClientInfo(final TroubleTicketRegisterModel troubleTicketRegisterModel,
                                     final MessageContext error)
    {
        if(troubleTicketRegisterModel.getClient() == null) return FAILURE;
        final Client searchCriteria = troubleTicketRegisterModel.getClient();
        Client existingClient = null;
        if(searchCriteria.getName() != null && !searchCriteria.getName().isEmpty())
        {
            existingClient = clientRepository.findClientByName(searchCriteria.getName());
        }
        else if(searchCriteria.getContractNumber() != null && !searchCriteria.getContractNumber().isEmpty())
        {
            existingClient = clientRepository.findClientByContractNumber(searchCriteria.getContractNumber());
        }
        else if(searchCriteria.getAddress() != null && !searchCriteria.getAddress().isEmpty())
        {
            existingClient = clientRepository.findClientByAddress(searchCriteria.getAddress());
        }

        if (existingClient == null)
        {
            error.addMessage(new MessageBuilder()
                                     .error()
                                     .source("id")
                                     .defaultText("There is no such client")
                                     .build());
            return FAILURE;
        }

        troubleTicketRegisterModel.setClient(existingClient);
        return SUCCESS;
    }

    public void setProblemDetails(final TroubleTicketRegisterModel troubleTicketRegisterModel,
                                  final ProblemDetails problemDetails)
    {
        troubleTicketRegisterModel.setProblemDetails(problemDetails);
    }

    public String validateProblemDetails(final TroubleTicketRegisterModel troubleTicketRegisterModel,
                                         final MessageContext error)
    {
        return SUCCESS;
    }

    public void setTroubleTicket(final TroubleTicketRegisterModel troubleTicketRegisterModel,
                                 final TroubleTicket troubleTicket)
    {
        troubleTicketRegisterModel.setTroubleTicket(troubleTicket);
    }

    public String validateTroubleTicket(final TroubleTicketRegisterModel troubleTicketRegisterModel,
                                        final MessageContext error)
    {
        if(troubleTicketRegisterModel.getTroubleTicket() == null) return FAILURE;
        final TroubleTicket troubleTicket = troubleTicketRegisterModel.getTroubleTicket();
        final ProblemDetails problemDetails = troubleTicketRegisterModel.getProblemDetails();
        problemDetailsRepository.save(problemDetails);

        troubleTicket.setClient(troubleTicketRegisterModel.getClient());
        troubleTicket.setProblemDetails(problemDetails);

        troubleTicketRepository.save(troubleTicket);
        return SUCCESS;
    }
}
