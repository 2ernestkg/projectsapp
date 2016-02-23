package conf;

import com.google.inject.Inject;
import controllers.ApplicationController;
import controllers.EmployeeController;
import controllers.ProjectController;
import models.Employee;
import ninja.AssetsController;
import ninja.Results;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import ninja.utils.NinjaProperties;

public class Routes implements ApplicationRoutes {

  @Inject
  NinjaProperties ninjaProperties;

  @Override
  public void init(Router router) {
    //redirect to projects list page
    router.GET().route("/").with(Results.redirect("/project"));

    router.GET().route("/project").with(ProjectController.class, "index");
    router.GET().route("/project/add").with(ProjectController.class, "add");
    router.POST().route("/project").with(ProjectController.class, "create");
    router.GET().route("/project/{id: \\d+}").with(ProjectController.class, "view");
    router.GET().route("/project/{id: \\d+}/edit").with(ProjectController.class, "edit");
    router.GET().route("/project/{id: \\d+}/delete").with(ProjectController.class, "delete");
    router.POST().route("/project/{id: \\d+}").with(ProjectController.class, "update");
    router.POST().route("/project/{id: \\d+}/employee").with(ProjectController.class, "addEmployee");
    router.GET().route("/project/{id: \\d+}/employee/delete").with(ProjectController.class, "removeEmployee");

    router.GET().route("/employee/suggest").with(EmployeeController.class, "suggest");
    router.GET().route("/employee").with(EmployeeController.class, "list");
    router.POST().route("/employee").with(EmployeeController.class, "create");
    router.GET().route("/employee/add").with(EmployeeController.class, "add");
    router.GET().route("/employee/{id: \\d+}").with(EmployeeController.class, "view");
    router.GET().route("/employee/{id: \\d+}/edit").with(EmployeeController.class, "edit");
    router.GET().route("/employee/{id: \\d+}/delete").with(EmployeeController.class, "delete");
    router.POST().route("/employee/{id: \\d+}").with(EmployeeController.class, "update");

    ///////////////////////////////////////////////////////////////////////
    // Assets (pictures / javascript)
    ///////////////////////////////////////////////////////////////////////
    router.GET().route("/assets/webjars/{fileName: .*}").with(AssetsController.class, "serveWebJars");
    router.GET().route("/assets/{fileName: .*}").with(AssetsController.class, "serveStatic");

    ///////////////////////////////////////////////////////////////////////
    // Index / Catchall shows index page, leave this at the end load this after all resources are loaded
    ///////////////////////////////////////////////////////////////////////
    router.GET().route("/.*").with(ApplicationController.class, "index");
  }

}
