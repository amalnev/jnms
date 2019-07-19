package ru.amalnev.jnms.common.model.entities.network;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import ru.amalnev.jnms.common.model.entities.DisplayName;
import ru.amalnev.jnms.common.model.entities.MinPrivilege;
import ru.amalnev.jnms.common.model.entities.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Сущность "тип сетевого устройства". CRUD-операции разрешены с правами 3 и выше.
 * Автоматически переносится в стандартный CRUD-UI, будет подписан "Device types",
 * появится в меню на 5-й позиции.
 *
 * @author Aleksei Malnev
 */
@Entity
@MinPrivilege(3)
@DisplayName(value = "Device types", orderOfAppearance = 5)
public class DeviceType extends NamedEntity
{
    /**
     * Список устройств, принадлежащих к данному типу.
     * Отображается в UI с подписью "Devices" на 3-й позиции.
     */
    @Getter
    @JsonBackReference
    @DisplayName(value = "Devices", orderOfAppearance = 3)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "type", orphanRemoval = true)
    private Set<Device> devices;
}
