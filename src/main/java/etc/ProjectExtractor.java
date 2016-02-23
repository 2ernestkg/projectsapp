package etc;

import models.Project;
import ninja.Context;
import ninja.params.ArgumentExtractor;

public class ProjectExtractor implements ArgumentExtractor<Project> {
  @Override
  public Project extract(Context context) {
    String name = context.getParameter("project.name", null);
    String customer = context.getParameter("project.customer", null);
    String executor = context.getParameter("project.executor", null);

    Project project = new Project();
    project.setName(name);
    project.setCustomer(customer);
    project.setExecutor(executor);
    return project;
  }

  @Override
  public Class<Project> getExtractedType() {
    return Project.class;
  }

  @Override
  public String getFieldName() {
    return null;
  }
}
