package ru.amalnev.jnmscommon.entities.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.amalnev.jnmscommon.entities.DisplayName;
import ru.amalnev.jnmscommon.entities.MinPrivilege;
import ru.amalnev.jnmscommon.entities.NamedEntity;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@NoArgsConstructor
@MinPrivilege(15)
@DisplayName(value = "User roles", orderOfAppearance = 1)
public class UserRole extends NamedEntity
{
    @Getter
    @Setter
    @DisplayName(value = "Role description", orderOfAppearance = 4)
    private String description;

    @Getter
    @Setter
    @Min(0)
    @Max(15)
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
        if(!description.isEmpty())
        {
            result.append(" (");
            result.append(description);
            result.append(")");
        }

        return result.toString();
    }
}
