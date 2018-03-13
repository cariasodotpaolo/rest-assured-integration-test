package mp.cariaso.springboot.service;

import java.util.List;
import mp.cariaso.springboot.api.exception.NotFoundException;
import mp.cariaso.springboot.model.Employee;

public interface EmployeeService {

    List<Employee> getList();

    Employee get(Long id);

    Long add(Employee employee);

    void update(Employee employee) throws NotFoundException;

    void delete(Employee employee) throws NotFoundException;
}
