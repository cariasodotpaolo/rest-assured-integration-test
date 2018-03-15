package mp.cariaso.springboot.api.controller;


import javax.servlet.http.HttpServletRequest;
import mp.cariaso.springboot.api.exception.UnauthorizedException;
import mp.cariaso.springboot.api.request.EmployeeRequest;
import mp.cariaso.springboot.model.Employee;
import mp.cariaso.springboot.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getEmployees( HttpServletRequest request,
                                           @RequestHeader(value = "Authorization", required = false) String token)
                                           throws Exception {

        if(token == null) {
            throw new UnauthorizedException("Please login.");
        }

        return ResponseEntity.ok(employeeService.getList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getEmployee(@PathVariable long id,
        @RequestHeader(value = "Authorization", required = false) String token,
        HttpServletRequest request) throws Exception {

        if(token == null) {
            throw new UnauthorizedException("Please login.");
        }

        return ResponseEntity.ok(employeeService.get(id));

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addEmployee(@RequestHeader(value = "Authorization", required = false) String token,
                                         HttpServletRequest request,
                                         @RequestBody EmployeeRequest employeeRequest) throws Exception {

        if(token == null) {
            throw new UnauthorizedException("Please login.");
        }

        if (employeeRequest.getName() == null) {
            throw new IllegalArgumentException("Name is required.");
        }

        if (employeeRequest.getDepartment() == null) {
            throw new IllegalArgumentException("Department is required.");
        }

        if (employeeRequest.getTitle() == null) {
            throw new IllegalArgumentException("Title is required.");
        }

        return ResponseEntity.ok(employeeService.add(new Employee(employeeRequest.getName(),
                                                                  employeeRequest.getDepartment(),
                                                                  employeeRequest.getTitle())));

    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateEmployee(@PathVariable long id,
                                            @RequestHeader(value = "Authorization", required = false) String token,
                                            HttpServletRequest request,
                                            @RequestBody EmployeeRequest employeeRequest) throws Exception {

        if(token == null) {
            throw new UnauthorizedException("Please login.");
        }

        Employee employee = employeeService.get(id);

        employee.setDepartment(employeeRequest.getDepartment() != null && !employeeRequest.getDepartment().isEmpty() ?
                                employeeRequest.getDepartment() : employee.getDepartment());
        employee.setTitle(employeeRequest.getTitle() != null && !employeeRequest.getTitle().isEmpty() ?
                                employeeRequest.getTitle() : employee.getTitle());
        employee.setName(employeeRequest.getName() != null && !employeeRequest.getName().isEmpty() ?
                                employeeRequest.getName() : employee.getName());

        employeeService.update(employee);

        return ResponseEntity.ok("Successfully updated employee.");

    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE )
    public ResponseEntity<?> deleteEmployee(@PathVariable long id,
                                            @RequestHeader(value = "Authorization", required = false) String token,
                                            HttpServletRequest request,
                                            @RequestBody Employee employee) throws Exception {

        if(token == null) {
            throw new UnauthorizedException("Please login.");
        }

        employeeService.delete(employee);

        return ResponseEntity.ok("Successfully deleted employee.");

    }

}
