package ru.amalnev.jnmswatcher;

import org.springframework.data.repository.CrudRepository;
import ru.amalnev.jnms.common.model.entities.network.Device;

public interface IProbe
{
    Device getDevice();

    void setDevice(Device device);

    void run();

    Long getPeriodMillis();

    void setPeriodMillis(Long periodMillis);

    CrudRepository getEventRepository();
}
