package mp.cariaso.springboot.service;

import java.util.List;
import mp.cariaso.springboot.api.exception.NotFoundException;
import mp.cariaso.springboot.data.EmployeeRepository;
import mp.cariaso.springboot.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getList() {

        return employeeRepository.getList();
    }

    @Override
    public Employee get(Long id) {

        return employeeRepository.get(id);
    }

    @Override
    public List<Employee> searchByName(String keyword) {

         return employeeRepository.searchByName(keyword);
    }

    @Override
    public Long add(Employee employee) {

        employee.setId(employeeRepository.getNewId());
        return employeeRepository.add(employee);
    }

    @Override
    public void update(Employee employee) throws NotFoundException {

        int index = employeeRepository.update(employee);

        if (index == -1 ) {
            throw new NotFoundException("Employee not found.");
        }
    }

    @Override
    public void delete(Employee employee) throws NotFoundException {

        int index = employeeRepository.delete(employee);

        if (index == -1 ) {
            throw new NotFoundException("Employee not found.");
        }
    }

}
