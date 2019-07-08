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

/**
 * Бин, поддерживающий заведение тикетов с помощью WebFlow
 *
 * @author Aleksei Malnev
 */
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

    /**
     * Создает DTO-объект, хранящий данные, заполняемые в WebFlow.
     * Данный метод выполняется при старте
     *
     * @return
     */
    public TroubleTicketRegisterModel makeModel()
    {
        return new TroubleTicketRegisterModel();
    }

    /**
     * Запоминаем объект, содержащий данные о клиенте в DTO WebFlow
     *
     * @param troubleTicketRegisterModel
     * @param client
     */
    public void setClient(final TroubleTicketRegisterModel troubleTicketRegisterModel,
                          final Client client)
    {
        troubleTicketRegisterModel.setClient(client);
    }

    /**
     * Проверяем валидность объекта "клиент", который был ранее сохранен в DTO
     *
     * @param troubleTicketRegisterModel
     * @param error
     * @return
     */
    public String validateClientInfo(final TroubleTicketRegisterModel troubleTicketRegisterModel,
                                     final MessageContext error)
    {
        //На данном этапе если клиента нет - то это ошибка
        if(troubleTicketRegisterModel.getClient() == null) return FAILURE;

        //Находим клиента в репозитории на основе данных, введенных оператором в форму поиска клиента.
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

        //Если данные, введенные оператором не позволяют идентифицировать клиента, то это ошибка
        if (existingClient == null)
        {
            error.addMessage(new MessageBuilder()
                                     .error()
                                     .source("id")
                                     .defaultText("There is no such client")
                                     .build());
            return FAILURE;
        }

        //Клиент найден в репозитории, запоминаем его.
        troubleTicketRegisterModel.setClient(existingClient);
        return SUCCESS;
    }

    /**
     * Запоминаем в DTO объект, содержащий детализацию проблемы. Этот объект приходит
     * из формы, заполняемой оператором при удаленной диагностке проблемы.
     *
     * @param troubleTicketRegisterModel
     * @param problemDetails
     */
    public void setProblemDetails(final TroubleTicketRegisterModel troubleTicketRegisterModel,
                                  final ProblemDetails problemDetails)
    {
        troubleTicketRegisterModel.setProblemDetails(problemDetails);
    }

    /**
     * Проводим валидацию объекта, содержащего подробности проблемы. Пока здесь нет никакой бизнес-логики
     *
     * @param troubleTicketRegisterModel
     * @param error
     * @return
     */
    public String validateProblemDetails(final TroubleTicketRegisterModel troubleTicketRegisterModel,
                                         final MessageContext error)
    {
        return SUCCESS;
    }

    /**
     * Записываем в DTO объект TroubleTicket-а.
     *
     * @param troubleTicketRegisterModel
     * @param troubleTicket
     */
    public void setTroubleTicket(final TroubleTicketRegisterModel troubleTicketRegisterModel,
                                 final TroubleTicket troubleTicket)
    {
        troubleTicketRegisterModel.setTroubleTicket(troubleTicket);
    }

    /**
     * Проводим окончаительную валидацию всей информации, сохраненной ранее в DTO, и
     * если все нормально - то сохраняем в репозитории.
     *
     * @param troubleTicketRegisterModel
     * @param error
     * @return
     */
    public String validateTroubleTicket(final TroubleTicketRegisterModel troubleTicketRegisterModel,
                                        final MessageContext error)
    {
        if(troubleTicketRegisterModel.getTroubleTicket() == null) return FAILURE;
        final TroubleTicket troubleTicket = troubleTicketRegisterModel.getTroubleTicket();
        final ProblemDetails problemDetails = troubleTicketRegisterModel.getProblemDetails();
        if(problemDetails != null) problemDetailsRepository.save(problemDetails);

        troubleTicket.setClient(troubleTicketRegisterModel.getClient());
        troubleTicket.setProblemDetails(problemDetails);

        troubleTicketRepository.save(troubleTicket);
        return SUCCESS;
    }
}
