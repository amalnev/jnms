package ru.amalnev.jnms.common.entities.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import ru.amalnev.jnms.common.entities.AbstractEntity;
import ru.amalnev.jnms.common.entities.MinPrivilege;

import javax.persistence.*;
import java.util.Set;

/**
 * Сущность "пользователь системы". CRUD-операции над пользователями
 * могут выполнять только пользователи с наивысшими правами (15). Данная
 * сущность не переносится автоматически в стандартный CRUD-UI. Для нее созданы
 * отдельные view.
 *
 * @author Aleksei Malnev
 */
@Entity
@NoArgsConstructor
@MinPrivilege(15)
public class User extends AbstractEntity implements UserDetails
{
    /**
     * Имя пользователя (логин)
     */
    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Пароль.
     */
    @Getter
    @Setter
    private String password;

    /**
     * Подтверждение пароля. Заполняется в UI, не хранится в БД.
     */
    @Getter
    @Setter
    @Transient
    private String passwordConfirmation;

    /**
     * Роли, назначенные данному пользователю.
     */
    @Getter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true)
    private Set<Authority> authorities;

    @Getter
    @Transient
    private final boolean accountNonExpired = true;

    @Getter
    @Transient
    private final boolean accountNonLocked = true;

    @Getter
    @Transient
    private final boolean credentialsNonExpired = true;

    @Getter
    @Transient
    private final boolean enabled = true;

    public User(final String username, final String password)
    {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString()
    {
        return username;
    }
}