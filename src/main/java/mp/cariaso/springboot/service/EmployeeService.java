package mp.cariaso.springboot.service;

import java.util.List;
import mp.cariaso.springboot.api.exception.NotFoundException;
import mp.cariaso.springboot.model.Employee;

public interface EmployeeService {

    List<Employee> getList();

    Employee get(Long id);

    List<Employee> searchByName(String keyword);

    Long add(Employee employee);

    void update(Employee employee) throws NotFoundException;

    void delete(Employee employee) throws NotFoundException;
}
