package ru.amalnev.jnmscommon.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnmscommon.entities.model.Vendor;

@Repository
@EntityClass(Vendor.class)
public interface IVendorRepository extends CrudRepository<Vendor, Long>
{
}
