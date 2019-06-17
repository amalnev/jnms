package ru.amalnev.jnmscommon.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnmscommon.entities.model.Device;

@Repository
@EntityClass(Device.class)
public interface IDeviceRepository extends CrudRepository<Device, Long>
{
}
