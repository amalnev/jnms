package ru.amalnev.jnms.common.entities.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.amalnev.jnms.common.entities.DisplayName;
import ru.amalnev.jnms.common.entities.MinPrivilege;
import ru.amalnev.jnms.common.entities.NamedEntity;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Сущность "роль пользователя в системе". CRUD-операции над ролями пользователей
 * могут выполнять только пользователи с наивысшими правами (15). Данная сущность
 * автоматически переносится в стандартный CRUD-UI. Там она будет подписана "User roles"
 * и появится на первом месте в меню.
 *
 * @author Aleksei Malnev
 */
@Entity
@NoArgsConstructor
@MinPrivilege(15)
@DisplayName(value = "User roles", orderOfAppearance = 1)
public class UserRole extends NamedEntity
{
    /**
     * Строка описания роли пользователя.
     * Отображается в UI. Будет подписана "Role description" и отображаться на 4-й позиции
     */
    @Getter
    @Setter
    @DisplayName(value = "Role description", orderOfAppearance = 4)
    private String description;

    /**
     * Уровень привилегий, ассоциированный с данной ролью.
     * Может принимать значения от 0 (ничего нельзя) до 15 (все можно).
     * Отображается в CRUD-UI. Будет подписан "Privilege level" и появится на 3-й позиции
     */
    @Getter
    @Setter
    @Min(value = 0, message = "Minimum privilege level is 0")
    @Max(value = 15, message = "Maximum privilege level is 15")
    @DisplayName(value = "Privilege level", orderOfAppearance = 3)
    private int privilegeLevel;

    public UserRole(final String name, final int privilegeLevel)
    {
        super(name);
        this.privilegeLevel = privilegeLevel;
    }

    @Override
    public String toString()
    {
        final StringBuilder result = new StringBuilder();
        result.append(getName());
        if (description != null && !description.isEmpty())
        {
            result.append(" (");
            result.append(description);
            result.append(")");
        }

        return result.toString();
    }
}
