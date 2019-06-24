package ru.amalnev.jnms.common.entities.model;

import lombok.Getter;
import ru.amalnev.jnms.common.entities.DisplayName;
import ru.amalnev.jnms.common.entities.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@DisplayName(value = "Vendors", orderOfAppearance = 4)
public class Vendor extends NamedEntity
{
    @Getter
    @DisplayName(value = "Devices", orderOfAppearance = 6)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "vendor", orphanRemoval = true)
    private Set<Device> devices;
}
