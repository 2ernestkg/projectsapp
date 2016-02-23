package models;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project implements Serializable {
  @Id
  @GeneratedValue()
  @Column(name="id")
  private int id;
  @Column(name = "name", nullable = false)
  @NotNull
  @NotBlank
  @Size(min = 2, max = 150)
  private String name;
  @Column(name = "customer", nullable = false)
  @NotNull
  @NotBlank
  @Size(min = 2, max = 255)
  private String customer;
  @Size(max = 255)
  @Column(name = "executor")
  private String executor;
  @Column(name = "begin_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date beginDate;
  @Column(name = "end_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date endDate;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
  @JoinTable(
          name = "project_employee",
          joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id")
  )
  private List<Employee> employees = new ArrayList<Employee>();

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public String getExecutor() {
    return executor;
  }

  public void setExecutor(String executor) {
    this.executor = executor;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public List<Employee> getEmployees() {
    return employees;
  }

  public void setEmployees(List<Employee> employees) {
    this.employees = employees;
  }

  public void addEmployee(Employee employee) {
    if (employees == null) {
      employees = new ArrayList<Employee>();
    }
    if (employees.contains(employee)) {
      return;
    }
    employees.add(employee);
  }

  public boolean hasEmployee(Employee employee) {
    if (employees == null) {
      return false;
    }
    return employees.contains(employee);
  }
}
