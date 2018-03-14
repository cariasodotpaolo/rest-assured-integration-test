package mp.cariaso.springboot.model;

public class Employee {

    private Long id;
    private String name;
    private String department;
    private String title;

    public Employee() {
    }

    public Employee(String name, String department, String title) {
        this.name = name;
        this.department = department;
        this.title = title;
    }

    public Employee(Long id, String name, String department, String title) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.title = title;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }
}
