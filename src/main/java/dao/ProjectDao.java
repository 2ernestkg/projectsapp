package dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import models.Project;
import ninja.jpa.UnitOfWork;
import org.hibernate.jpa.criteria.OrderImpl;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProjectDao implements BaseDao<Project> {
  Logger log = Logger.getLogger(ProjectDao.class.getName());

  @Inject
  Provider<EntityManager> entityManagerProvider;

  @Override
  @UnitOfWork
  @SuppressWarnings("unchecked")
  public List<Project> findAll(int offset, int limit) {
    EntityManager em = entityManagerProvider.get();
    return em.createQuery("SELECT x FROM Project x")
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
  }

  @UnitOfWork
  public Pageable<Project> findAll(int offset, int limit, Project search, SortField... sortFields) {
    EntityManager em = entityManagerProvider.get();
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Project> criteria = builder.createQuery(Project.class);
    CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    Root<Project> from = criteria.from(Project.class);

    criteria.select(from);
    countCriteria.select(builder.count(countCriteria.from(Project.class)));
    if (search != null) {
      List<Predicate> filters = new ArrayList<Predicate>();
      if (search.getName() != null && !"".equals(search.getName())) {
        Expression<String> path = from.get("name");
        filters.add(builder.like(path, search.getName() + "%"));
      }
      if (search.getCustomer() != null && !"".equals(search.getCustomer())) {
        Expression<String> path = from.get("customer");
        filters.add(builder.like(path, search.getCustomer() + "%"));
      }
      if (search.getExecutor() != null && !"".equals(search.getExecutor())) {
        Expression<String> path = from.get("executor");
        filters.add(builder.like(path, search.getExecutor() + "%"));
      }
      Predicate[] predicates = filters.toArray(new Predicate[filters.size()]);
      countCriteria.where(builder.and(predicates));
      criteria.where(builder.and(predicates));
    }
    List<Order> orders = new ArrayList<Order>();
    if (sortFields != null && sortFields.length > 0) {
      for (int i = 0; i < sortFields.length; i++) {
        orders.add(new OrderImpl(from.get(sortFields[i].getField()), sortFields[i].isAsc()));
      }
    }
    criteria.orderBy(orders);
    List<Project> list = em.createQuery(criteria)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    long countAll = em.createQuery(countCriteria).getSingleResult();
    return new Pageable<Project>(list, countAll);
  }

  @Override
  @Transactional
  public boolean delete(Project object) {
    EntityManager em = entityManagerProvider.get();
    Project project = (Project) object;
    project = em.getReference(Project.class, project.getId());
    em.remove(project);
    return true;
  }

  @Override
  @Transactional
  public int save(Project object) {
    EntityManager em = entityManagerProvider.get();
    em.persist(object);
    return 0;
  }

  @Override
  @Transactional
  public boolean saveOrUpdate(Project object) {
    EntityManager em = entityManagerProvider.get();
    em.merge(object);
    return true;
  }

  @UnitOfWork
  public Project findById(int id) {
    EntityManager em = entityManagerProvider.get();
    Query q = em.createQuery("SELECT x FROM Project x WHERE x.id = :idParam");
    Project project = null;
    try {
      project = (Project) q.setParameter("idParam", id).getSingleResult();
    } catch (Exception e) {
      log.warning("User doesn't exists for id : " + id);
    }
    return project;
  }

  @UnitOfWork
  public long countAll() {
    EntityManager em = entityManagerProvider.get();
    Query q = em.createQuery("SELECT count(x.id) FROM Project x");

    return ((Long) q.getSingleResult()).longValue();
  }
}
