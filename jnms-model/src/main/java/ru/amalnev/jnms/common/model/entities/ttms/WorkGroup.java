package ru.amalnev.jnms.common.model.entities.ttms;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.Backup;
import ru.amalnev.jnms.common.model.entities.DisplayName;
import ru.amalnev.jnms.common.model.entities.MinPrivilege;
import ru.amalnev.jnms.common.model.entities.NamedEntity;
import ru.amalnev.jnms.common.model.entities.security.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Backup
@MinPrivilege(3)
@Table(name = "WorkGroup")
@DisplayName(value = "Work groups", orderOfAppearance = 1)
public class WorkGroup extends NamedEntity
{
    @Getter
    @Setter
    @Column(name = "description")
    @DisplayName(value = "Description", orderOfAppearance = 3)
    private String description;

    @Getter
    @JsonBackReference
    @DisplayName(value = "Users", orderOfAppearance = 4)
    @OneToMany(mappedBy = "workGroup", orphanRemoval = true)
    private Set<User> users;
}
