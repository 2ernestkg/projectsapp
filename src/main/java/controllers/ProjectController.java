/**
 * Copyright (C) 2013 the original author or authors.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dao.EmployeeDao;
import dao.Pageable;
import dao.ProjectDao;
import dao.SortField;
import etc.ExtractedProject;
import models.Employee;
import models.Project;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.Router;
import ninja.params.Param;
import ninja.params.PathParam;
import ninja.validation.JSR303Validation;
import ninja.validation.Validation;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
public class ProjectController {
  Logger log = Logger.getLogger(ProjectController.class.getName());

  private static int DEFAULT_LIMIT = 5;

  @Inject
  ProjectDao projectDao;

  @Inject
  EmployeeDao employeeDao;

  @Inject
  Router route;


  public ProjectController() {
  }

  public Result index(@Param("page") int page, @Param("pageSize") int pageSize,
                      @Param("sortBy") String sortBy, @Param("sortDir") String sortDir,
                      @ExtractedProject Project filter) {
    if (page == 0) {
      page = 1;
    }
    if (pageSize == 0) {
      pageSize = DEFAULT_LIMIT;
    }
    if (filter == null) {
      filter = new Project();
    }
    int offset = (page - 1) * pageSize;
    SortField sortField = new SortField("id", true);
    if (sortBy != null && !"".equals(sortBy) && sortDir != null) {
      boolean asc = "DESC".equals(sortDir.toUpperCase()) ? false : true;
      sortField = new SortField(sortBy, asc);
    } else {
      sortBy = "id";
      sortDir = "asc";
    }
    Pageable<models.Project> projects = projectDao.findAll(offset, pageSize, filter, sortField);
    int pages = (int) Math.ceil(projects.getTotalRecords() * 1.0 / pageSize);
    if (pages == 0) {
      pages = 1;
    }
    return Results.html().render("projects", projects.getResultList())
            .render("pages", pages)
            .render("currentPage", page)
            .render("sortBy", sortBy).render("sortDir", sortDir)
            .render("project", filter);
  }

  public Result add() {
    return Results.html();
  }

  public Result create(Context context, @JSR303Validation models.Project project, Validation validation) {
    if (validation.hasViolations()) {
      flashError(context, project);
      return Results.redirect(route.getReverseRoute(ProjectController.class, "add"));
    }
    projectDao.save(project);
    context.getFlashScope().put("success", "save.success");
    return Results.redirect(route.getReverseRoute(ProjectController.class, "index"));
  }

  public Result view(@PathParam("id") int id) {
    models.Project project = projectDao.findById(id);
    return Results.html().render("project", project);
  }

  public Result edit(@PathParam("id") int id) {
    models.Project project = projectDao.findById(id);
    return Results.html().render("project", project);
  }

  public Result delete(Context context, @PathParam("id") int id) {
    Project project = projectDao.findById(id);
    if (project == null) {
      context.getFlashScope().error("project.notfound");
      return Results.redirect(route.getReverseRoute(ProjectController.class, "index"));
    }
    projectDao.delete(project);
    context.getFlashScope().success("delete.success");
    return Results.redirect(route.getReverseRoute(ProjectController.class, "index"));
  }

  public Result update(Context context, @JSR303Validation models.Project project, Validation validation) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", project.getId());
    if (validation.hasBeanViolations()) {
      flashError(context, project);
      return Results.redirect(route.getReverseRoute(ProjectController.class, "edit", params));
    }
    // update functionality do not update employees, so keep it
    Project update = projectDao.findById(project.getId());
    project.setEmployees(update.getEmployees());

    // update
    projectDao.saveOrUpdate(project);
    context.getFlashScope().success("update.success");
    return Results.redirect(route.getReverseRoute(ProjectController.class, "view", params));
  }

  public Result addEmployee(Context context, @PathParam("id") int projectId, @JSR303Validation models.Employee employee, Validation validation) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", projectId);
    Result viewResult = Results.redirect(route.getReverseRoute(ProjectController.class, "view", params));
    if (validation.hasBeanViolations()) {
      flashError(context, employee);
      return viewResult;
    }
    Project project = projectDao.findById(projectId);
    if (project == null) {
      context.getFlashScope().error("project.notfound");
      return viewResult;
    }
    if (project.hasEmployee(employee)) {
      context.getFlashScope().error("project.employee.exists");
      return viewResult;
    }
    project.addEmployee(employee);
    projectDao.saveOrUpdate(project);
    context.getFlashScope().success("add.success");
    return viewResult;
  }

  public Result removeEmployee(Context context, @PathParam("id") int projectId, @Param("empId")int employeeId) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", projectId);
    Result viewResult = Results.redirect(route.getReverseRoute(ProjectController.class, "view", params));

    Project project = projectDao.findById(projectId);
    if (project == null) {
      context.getFlashScope().error("project.notfound");
      return viewResult;
    }
    Employee employee = employeeDao.findById(employeeId);
    if (employee == null) {
      context.getFlashScope().error("employee.notfound");
      return viewResult;
    }
    project.getEmployees().remove(employee);
    projectDao.saveOrUpdate(project);
    context.getFlashScope().success("employee.remove.success");
    return viewResult;
  }

  private void flashError(Context context, models.Employee employee) {
    context.getFlashScope().put("error", "missing.required");
    if (employee.getfName() != null && employee.getfName().trim().length() < 3) {
      context.getFlashScope().put("invalidName", "employee.fname.short");
    }
    if (employee.getlName() != null && employee.getlName().trim().length() < 2) {
      context.getFlashScope().put("invalidName", "employee.lname.short");
    }
  }

  private void flashError(Context context, models.Project project) {
    context.getFlashScope().put("error", "missing.required");
    if (project.getName().trim().length() < 3) {
      context.getFlashScope().put("invalidName", "project.name.short");
    }
    if (project.getName().trim().length() < 3) {
      context.getFlashScope().put("invalidCustomer", "project.customer.short");
    }
    context.getFlashScope().put("name", project.getName());
  }


}
