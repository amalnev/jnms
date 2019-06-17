package ru.amalnev.jnmscommon.entities.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import ru.amalnev.jnmscommon.entities.AbstractEntity;
import ru.amalnev.jnmscommon.entities.DisplayName;
import ru.amalnev.jnmscommon.entities.MinPrivilege;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@MinPrivilege(15)
@DisplayName(value = "User authorities", orderOfAppearance = 2)
public class Authority extends AbstractEntity implements GrantedAuthority
{
    @Getter
    @Setter
    @ManyToOne
    @DisplayName(value = "User", orderOfAppearance = 3)
    private User user;

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
