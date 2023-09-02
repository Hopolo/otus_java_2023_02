package ru.otus.core.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.crm.model.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {

    Optional<Client> findByName(String name);

    Optional<Client> findById(long id);

    @Query("select * from client where :fieldName = :fieldValue")
    Optional<List<Client>> findByEntityField(
        @Param("fieldName") String entityFieldName,
        @Param("fieldValue") String entityFieldValue
    );

}
