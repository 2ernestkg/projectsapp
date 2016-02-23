package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;

@Entity
@Table(name = "employee")
public class Employee implements Serializable {
  @Id
  @GeneratedValue
  @Column(name = "id")
  private int id;
  @NotNull
  @Size(min = 3, max = 50)
  @Column(name = "first_name")
  private String fName;
  @NotNull
  @Size(min = 3, max = 100)
  @Column(name = "last_name")
  private String lName;

  public Employee() {
    //default constructor
  }

  public Employee(String fName, String lName) {
    this.fName = fName;
    this.lName = lName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getfName() {
    return fName;
  }

  public void setfName(String fName) {
    this.fName = fName;
  }

  public String getlName() {
    return lName;
  }

  public void setlName(String lName) {
    this.lName = lName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Employee)) return false;

    Employee employee = (Employee) o;

    if (id != employee.id) return false;
    if (fName != null ? !fName.equals(employee.fName) : employee.fName != null) return false;
    return !(lName != null ? !lName.equals(employee.lName) : employee.lName != null);

  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (fName != null ? fName.hashCode() : 0);
    result = 31 * result + (lName != null ? lName.hashCode() : 0);
    return result;
  }
}
