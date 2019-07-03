package ru.amalnev.jnms.common.model.entities.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.common.model.entities.DisplayName;
import ru.amalnev.jnms.common.model.entities.MinPrivilege;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Сущность "назначение роли пользователю". Фактически это join-table между пользователями
 * и ролями. CRUD-операции разрешены только с наивысшими правами. Автоматически переносится
 * в стандартный CRUD-UI, будет подписана "User authorities", появится в меню на 2-й позиции.
 *
 * @author Aleksei Malnev
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@MinPrivilege(15)
@DisplayName(value = "User authorities", orderOfAppearance = 2)
public class Authority extends AbstractEntity implements GrantedAuthority
{
    /**
     * Пользователь, которому назначена роль.
     * Отображается в UI c подписью "User" на 3-й позиции
     */
    @Getter
    @Setter
    @ManyToOne
    @DisplayName(value = "User", orderOfAppearance = 3)
    private User user;

    /**
     * Роль, назначенная пользователю.
     * Отображается в UI c подписью "User role" на 4-й позиции
     */
    @Getter
    @Setter
    @ManyToOne
    @DisplayName(value = "User role", orderOfAppearance = 4)
    private UserRole userRole;

    public String getAuthority()
    {
        return userRole.getName();
    }

    @Override
    public String toString()
    {
        return getAuthority();
    }
}
