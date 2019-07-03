package ru.amalnev.jnms.common.model.entities.model;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.DisplayName;
import ru.amalnev.jnms.common.model.entities.MinPrivilege;
import ru.amalnev.jnms.common.model.entities.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Сущность "сетевое устройство". CRUD-операции разрешены с правами 3 и выше.
 * Автоматически переносится в стандартный CRUD-UI, будет подписана "Network devices",
 * появится в меню на 6-й позиции.
 *
 * @author Aleksei Malnev
 */
@Entity
@MinPrivilege(3)
@DisplayName(value = "Network devices", orderOfAppearance = 6)
public class Device extends NamedEntity
{
    /**
     * Тип данного устройства.
     * Отображается в UI с подписью "Device type" на 3-й позиции.
     */
    @Getter
    @Setter
    @ManyToOne
    @DisplayName(value = "Device type", orderOfAppearance = 3)
    private DeviceType type;

    /**
     * Производитель устройства.
     * Отображается в UI с подписью "Vendor" на 4-й позиции.
     */
    @Getter
    @Setter
    @ManyToOne
    @DisplayName(value = "Vendor", orderOfAppearance = 4)
    private Vendor vendor;

    /**
     * Площадка, где установлено данное устройство.
     * Отображается в UI с подписью "Installation site" на 5-й позиции.
     */
    @Getter
    @Setter
    @ManyToOne
    @DisplayName(value = "Installation site", orderOfAppearance = 5)
    private Site site;

    /**
     * Серийный номер.
     * Отображается в UI с подписью "Serial number" на 6-й позиции.
     */
    @Getter
    @Setter
    @DisplayName(value = "Serial number", orderOfAppearance = 6)
    private String serialNumber;

    /**
     * IP-адрес, использующийся для управления данным устройством.
     * Отображается в UI с подписью "OAM IP address" на 7-й позиции.
     */
    @Getter
    @Setter
    @DisplayName(value = "OAM IP address", orderOfAppearance = 7)
    private String oamIp;
}
