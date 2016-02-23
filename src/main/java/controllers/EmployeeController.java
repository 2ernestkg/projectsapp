package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dao.EmployeeDao;
import models.Employee;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.Router;
import ninja.params.Param;
import ninja.params.PathParam;
import ninja.validation.JSR303Validation;
import ninja.validation.Validation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class EmployeeController {
  private static int DEFAULT_LIMIT = 100;
  private static int DEFAULT_SUGGEST_SIZE = 5;

  @Inject
  private EmployeeDao employeeDao;
  @Inject
  private Router route;

  public Result list(@Param("page") int page, @Param("pageSize") int pageSize) {
    if (page == 0) {
      page = 1;
    }
    if (pageSize == 0) {
      pageSize = DEFAULT_LIMIT;
    }
    int offset = (page - 1) * pageSize;
    long totalCount = employeeDao.countAll();
    List<Employee> employees = employeeDao.findAll(offset, pageSize);
    int pages = (int) Math.ceil(totalCount * 1.0 / pageSize);
    if (pages == 0) {
      pages = 1;
    }
    return Results.html().render("employees", employees)
            .render("pages", pages)
            .render("currentPage", page);
  }

  public Result suggest(@Param("text") String search) {
    if (search == null || "".equals(search)) {
      return Results.json().render("suggestions", Collections.emptyList());
    }
    List<Employee> list = employeeDao.findAllMatch(search, DEFAULT_SUGGEST_SIZE);
    return Results.json().render("suggestions", list);
  }

  public Result view(@PathParam("id") int id) {
    Employee employee = employeeDao.findById(id);
    return Results.ok().render("employee", employee);
  }

  public Result edit(@PathParam("id") int id) {
    Employee employee = employeeDao.findById(id);
    return Results.html().render("employee", employee);
  }

  public Result update(Context context, @JSR303Validation Employee employee, Validation validation) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", employee.getId());

    if (validation.hasBeanViolations()) {
      flashErrors(context, employee);
      return Results.redirect(route.getReverseRoute(EmployeeController.class, "edit", params));
    }

    employeeDao.saveOrUpdate(employee);
    context.getFlashScope().success("save.success");
    return Results.redirect(route.getReverseRoute(EmployeeController.class, "view", params));
  }

  public Result add() {
    return Results.html();
  }

  public Result create(Context context, @JSR303Validation Employee employee, Validation validation) {
    if (validation.hasViolations()) {
      flashErrors(context, employee);
      return Results.redirect(route.getReverseRoute(EmployeeController.class, "add"));
    }
    employeeDao.save(employee);
    context.getFlashScope().put("success", "save.success");
    return Results.redirect(route.getReverseRoute(EmployeeController.class, "list"));
  }

  private void flashErrors(Context context, Employee employee) {
    context.getFlashScope().error("missing.required");
    if (employee.getfName() == null || employee.getfName().length() < 3) {
      context.getFlashScope().put("invalidFirstName", "employee.fname.short");
    }
    if (employee.getlName() == null || employee.getlName().length() < 3) {
      context.getFlashScope().put("invalidLastName", "employee.lname.short");
    }
  }

  public Result delete(Context context, @PathParam("id") int empId) {
    Employee employee = employeeDao.findById(empId);
    if (employee == null) {
      return Results.redirect(route.getReverseRoute(EmployeeController.class, "list"));
    }
    if (employeeDao.delete(employee)) {
      context.getFlashScope().success("delete.success");
      return Results.redirect(route.getReverseRoute(EmployeeController.class, "list"));
    }
    context.getFlashScope().error("delete.failure");
    return Results.redirect(route.getReverseRoute(EmployeeController.class, "list"));
  }
}
