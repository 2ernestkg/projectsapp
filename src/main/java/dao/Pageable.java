package dao;

import java.util.List;

public class Pageable<T> {
  private List<T> resultList;
  private long totalRecords;

  public Pageable(List<T> list, long totalCount) {
    this.resultList = list;
    this.totalRecords = totalCount;
  }

  public List<T> getResultList() {
    return resultList;
  }

  public long getTotalRecords() {
    return totalRecords;
  }
}
