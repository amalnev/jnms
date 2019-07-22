package ru.amalnev.jnms.common.model.entities.ttms;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.DisplayName;
import ru.amalnev.jnms.common.model.entities.MinPrivilege;
import ru.amalnev.jnms.common.model.entities.NamedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Entity
@MinPrivilege(3)
@Table(name = "Client")
@DisplayName(value = "Clients")
public class Client extends NamedEntity
{
    @Getter
    @Setter
    @Column(name = "address")
    @DisplayName(value = "Address", orderOfAppearance = 3)
    private String address;

    @Getter
    @Setter
    @Column(name = "contactPerson")
    @DisplayName(value = "Contact person", orderOfAppearance = 4)
    private String contactPerson;

    @Getter
    @Setter
    @Column(name = "contractNumber")
    @DisplayName(value = "Contract", orderOfAppearance = 5)
    private String contractNumber;

    @Getter
    @Setter
    @Email
    @Column(name = "email")
    @DisplayName(value = "Email", orderOfAppearance = 6)
    private String email;

    @Getter
    @Setter
    @Column(name = "phone")
    @DisplayName(value = "Phone", orderOfAppearance = 7)
    private String phone;
}
