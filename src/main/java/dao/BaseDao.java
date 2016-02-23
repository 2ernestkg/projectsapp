package dao;

import java.util.List;

public interface BaseDao<T> {
	List<T> findAll(int offset, int limit);
	boolean delete(T object);
	int save(T object);
	boolean saveOrUpdate(T object);
	Pageable<T> findAll(int offset, int limit, T search, SortField... sortFields);
}
