package ru.amalnev.jnms.common.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.model.entities.network.Device;

@Repository
@EntityClass(Device.class)
public interface IDeviceRepository extends CrudRepository<Device, Long>
{
}
