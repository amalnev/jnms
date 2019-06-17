package ru.amalnev.jnmscommon.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnmscommon.entities.model.DeviceType;

@Repository
@EntityClass(DeviceType.class)
public interface IDeviceTypeRepository extends CrudRepository<DeviceType, Long>
{
}
