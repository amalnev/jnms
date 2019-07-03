package ru.amalnev.jnms.common.model.entities.model;

import lombok.Getter;
import ru.amalnev.jnms.common.model.entities.DisplayName;
import ru.amalnev.jnms.common.model.entities.MinPrivilege;
import ru.amalnev.jnms.common.model.entities.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Сущность "производитель оборудования". CRUD-операции разрешены с правами 3 и выше.
 * Автоматически переносится в стандартный CRUD-UI, будет подписана "Vendors",
 * появится в меню на 4-й позиции.
 *
 * @author Aleksei Malnev
 */
@Entity
@MinPrivilege(3)
@DisplayName(value = "Vendors", orderOfAppearance = 4)
public class Vendor extends NamedEntity
{
    @Getter
    @DisplayName(value = "Devices", orderOfAppearance = 6)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "vendor", orphanRemoval = true)
    private Set<Device> devices;
}
