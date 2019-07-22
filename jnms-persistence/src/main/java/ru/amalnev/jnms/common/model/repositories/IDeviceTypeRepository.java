package ru.amalnev.jnms.common.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.model.entities.network.DeviceType;

@Repository
@EntityClass(DeviceType.class)
public interface IDeviceTypeRepository extends CrudRepository<DeviceType, Long>
{
}
