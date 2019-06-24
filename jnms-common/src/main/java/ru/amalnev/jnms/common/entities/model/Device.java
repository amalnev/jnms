package ru.amalnev.jnms.common.entities.model;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.entities.DisplayName;
import ru.amalnev.jnms.common.entities.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DisplayName(value = "Network devices", orderOfAppearance = 6)
public class Device extends NamedEntity
{
    @Getter
    @Setter
    @ManyToOne
    @DisplayName(value = "Device type", orderOfAppearance = 3)
    private DeviceType type;

    @Getter
    @Setter
    @ManyToOne
    @DisplayName(value = "Vendor", orderOfAppearance = 4)
    private Vendor vendor;

    @Getter
    @Setter
    @ManyToOne
    @DisplayName(value = "Installation site", orderOfAppearance = 5)
    private Site site;

    @Getter
    @Setter
    @DisplayName(value = "Serial number", orderOfAppearance = 6)
    private String serialNumber;

    @Getter
    @Setter
    @DisplayName(value = "OAM IP address", orderOfAppearance = 7)
    private String oamIp;
}
