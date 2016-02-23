package dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import models.Employee;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class EmployeeDao implements BaseDao<Employee> {
  Logger log = Logger.getLogger(EmployeeDao.class.getName());

  @Inject
  Provider<EntityManager> entityManagerProvider;

  @UnitOfWork
  public List<Employee> findAllMatch(String text, int limit) {
    Objects.requireNonNull(text);
    EntityManager em = entityManagerProvider.get();
    Query query = em.createQuery("select emp from Employee emp where lower(emp.fName) like :text or lower(emp.lName) like :text");
    query.setParameter("text", text.toLowerCase() + "%");
    return query.setMaxResults(limit).getResultList();
  }

  @Override
  public Pageable<Employee> findAll(int offset, int limit, Employee search, SortField... sortFields) {
    return null;
  }

  @Override
  @Transactional
  public boolean saveOrUpdate(Employee object) {
    EntityManager em = entityManagerProvider.get();
    em.merge(object);
    return true;
  }

  @Override
  @Transactional
  public int save(Employee object) {
    EntityManager em = entityManagerProvider.get();
    em.persist(object);
    return 0;
  }

  @Override
  @Transactional
  public boolean delete(Employee employee) {
    EntityManager em = entityManagerProvider.get();
    employee = em.getReference(Employee.class, employee.getId());
    em.remove(employee);
    return true;
  }

  @Override
  @UnitOfWork
  public List<Employee> findAll(int offset, int limit) {
    EntityManager em = entityManagerProvider.get();
    Query query = em.createQuery("select emp from Employee emp");
    return query.setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
  }

  @UnitOfWork
  public Employee findById(int employeeId) {
    Query query = entityManagerProvider.get()
            .createQuery("select emp from Employee emp where emp.id=:id")
            .setParameter("id", employeeId);

    Employee employee = null;
    try {
      employee = (Employee) query.getSingleResult();
    } catch (Exception ex) {
      log.warning("Employee with id '" + employeeId + "' was not found");
    }
    return employee;
  }

  @UnitOfWork
  public long countAll() {
    EntityManager em = entityManagerProvider.get();
    Query q = em.createQuery("SELECT count(x.id) FROM Employee x");
    return ((Long) q.getSingleResult()).longValue();
  }
}
