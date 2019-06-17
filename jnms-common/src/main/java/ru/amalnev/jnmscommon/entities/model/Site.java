package ru.amalnev.jnmscommon.entities.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.amalnev.jnmscommon.entities.DisplayName;
import ru.amalnev.jnmscommon.entities.MinPrivilege;
import ru.amalnev.jnmscommon.entities.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@NoArgsConstructor
@DisplayName(value = "Sites", orderOfAppearance = 3)
@MinPrivilege(3)
public class Site extends NamedEntity
{
    public Site(String name)
    {
        super(name);
    }

    @Getter
    @Setter
    @DisplayName(value = "Address", orderOfAppearance = 3)
    private String address;

    @Getter
    @Setter
    @DisplayName(value = "Latitude", orderOfAppearance = 4)
    private Double latitude;

    @Getter
    @Setter
    @DisplayName(value = "Longitude", orderOfAppearance = 5)
    private Double longitude;

    @Getter
    @DisplayName(value = "Devices", orderOfAppearance = 6)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "site", orphanRemoval = true)
    private Set<Device> devices;
}
