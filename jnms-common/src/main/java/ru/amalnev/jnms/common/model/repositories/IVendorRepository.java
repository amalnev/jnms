package ru.amalnev.jnms.common.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.model.entities.network.Vendor;

@Repository
@EntityClass(Vendor.class)
public interface IVendorRepository extends CrudRepository<Vendor, Long>
{
}
