package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bhavi on 4/28/2017.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
}