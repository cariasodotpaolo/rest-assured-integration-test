package mp.cariaso.springboot.api.request;

public class EmployeeRequest {

    private String name;
    private String department;
    private String title;

    public EmployeeRequest() {

    }

    public EmployeeRequest(String name, String department, String title) {
        this.name = name;
        this.department = department;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getTitle() {
        return title;
    }
}
