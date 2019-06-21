package ru.amalnev.jnmscommon.entities.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import ru.amalnev.jnmscommon.entities.AbstractEntity;
import ru.amalnev.jnmscommon.entities.MinPrivilege;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@MinPrivilege(15)
public class User extends AbstractEntity implements UserDetails
{
    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    @Transient
    private String passwordConfirmation;

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