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
        if (!description.isEmpty())
        {
            result.append(" (");
            result.append(description);
            result.append(")");
        }

        return result.toString();
    }
}
