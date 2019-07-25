package ru.amalnev.jnms.common.model.entities.network;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.Backup;
import ru.amalnev.jnms.common.model.entities.DisplayName;
import ru.amalnev.jnms.common.model.entities.MinPrivilege;
import ru.amalnev.jnms.common.model.entities.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Сущность "технологическая площадка". CRUD-операции разрешены с правами 3 и выше.
 * Автоматически переносится в стандартный CRUD-UI, будет подписана "Sites",
 * появится в меню на 3-й позиции.
 *
 * @author Aleksei Malnev
 */
@Entity
@Backup
@NoArgsConstructor
@MinPrivilege(3)
@DisplayName(value = "Sites", orderOfAppearance = 3)
public class Site extends NamedEntity
{
    public Site(String name)
    {
        super(name);
    }

    /**
     * Адрес площадки.
     * Отображается в UI с подписью "Address" на 3-й позиции.
     */
    @Getter
    @Setter
    @DisplayName(value = "Address", orderOfAppearance = 3)
    private String address;

    /**
     * Географическая широта расположения площадки.
     * Отображается в UI с подписью "Latitude" на 4-й позиции.
     */
    @Getter
    @Setter
    @DisplayName(value = "Latitude", orderOfAppearance = 4)
    private Double latitude;

    /**
     * Географическая долгота расположения площадки.
     * Отображается в UI с подписью "Longitude" на 5-й позиции.
     */
    @Getter
    @Setter
    @DisplayName(value = "Longitude", orderOfAppearance = 5)
    private Double longitude;

    /**
     * Список устройств, расположенных на данной площадке.
     * Отображается в UI с подписью "Devices" на 6-й позиции.
     */
    @Getter
    @JsonBackReference
    @DisplayName(value = "Devices", orderOfAppearance = 6)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "site", orphanRemoval = true)
    private Set<Device> devices;
}
