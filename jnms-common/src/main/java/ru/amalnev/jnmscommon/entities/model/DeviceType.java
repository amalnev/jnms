package ru.amalnev.jnmscommon.entities.model;

import lombok.Getter;
import ru.amalnev.jnmscommon.entities.DisplayName;
import ru.amalnev.jnmscommon.entities.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@DisplayName(value = "Device types", orderOfAppearance = 5)
public class DeviceType extends NamedEntity
{
    @Getter
    @DisplayName(value = "Devices", orderOfAppearance = 3)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "type", orphanRemoval = true)
    private Set<Device> devices;
}
