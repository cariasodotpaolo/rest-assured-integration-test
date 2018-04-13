package mp.cariaso.springboot.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import mp.cariaso.springboot.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<Employee> employees;

    public EmployeeRepository() {

        createEmployees();

        logger.debug("EMPLOYEES CREATED: {}", employees.size());
    }

    public List<Employee> getList() {

        logger.debug("EMPLOYEES FOUND: {}", employees.size());

        return employees;
    }

    public List<Employee> searchByName(String keyword) {

        List<Employee> result = employees.stream()
                 .filter(employee -> employee.getName().toLowerCase().contains(keyword.toLowerCase()))
                 .collect(Collectors.toList());

        result.forEach(employee ->
            logger.debug(employee.getName())
        );

        return result;
    }



    public Long add(Employee employee) {

        if(employee.getId() == null ) {
            employee.setId(getNewId());
        }

        employees.add(employee);

        return employee.getId();

    }

    public int update(Employee updatedEmployee) {

        int existingIndex = getIndex(updatedEmployee);

        employees.remove(existingIndex);
        employees.add(updatedEmployee);

        return getIndex(updatedEmployee);
    }

    public int getIndex(Employee employee) {

        int matchedIndex = IntStream.range(0, employees.size())
            .filter(empInd-> employees.get(empInd).getId().equals(employee.getId()))
            .findFirst()
            .orElse(-1);

        return matchedIndex;

    }

    public Employee get(Long id) {

        Optional<Employee> matchingObject = employees.stream().
            filter(e -> e.getId().equals(id)).
            findFirst();

        Employee employee = matchingObject.get();

        return employee;
    }

    public Long getNewId() {

        List<Employee> sortedList = employees.stream()
                                    .sorted(Comparator.comparing(Employee::getId).reversed())
                                    .collect(Collectors.toList());

        return sortedList.get(0).getId() + 1;

    }

    public int delete(Employee employee) {

        int index = getIndex(employee);

        if (index > -1) {
            employees.remove(index);
        }

        return index;
    }

    private List<Employee> createEmployees() {

        logger.debug("CREATING EMPLOYEES....");

        employees = new ArrayList<>();

        for ( int i = 0; i < 5; i++ ) {

            int id = i + 1;

            Employee employee = new Employee(new Long (id), "John_Smith_" + id,
                "DEPT_" + id, "TITLE_" + id);
            employees.add(employee);
        }

        for ( int i = 0; i < 5; i++ ) {

            int id = i + 1;

            Employee employee = new Employee(new Long (id), "Mary_Walters_" + id,
                "DEPT_" + id, "TITLE_" + id);
            employees.add(employee);
        }

        return employees;
    }

}
