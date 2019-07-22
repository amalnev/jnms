package ru.amalnev.jnms.common.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.model.entities.network.Device;
import ru.amalnev.jnms.common.model.entities.network.IcmpEvent;

@Repository
@EntityClass(IcmpEvent.class)
public interface IIcmpEventRepository extends CrudRepository<IcmpEvent, Long>
{
    IcmpEvent findFirstByDeviceOrderByTimestampDesc(Device device);
}
