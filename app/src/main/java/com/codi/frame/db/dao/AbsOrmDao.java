package com.codi.frame.db.dao;

import java.util.List;

import com.codi.frame.App;
import com.codi.frame.db.OrmDaoManager;
import com.codi.frame.model.BaseModel;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Title: BaseOrmDao
 * Description: 数据库对象操作的基本类，如果是大量数据操作，则通过TransactionManager.callInTransaction方法来
 * 使用事务进行操作
 * @author Codi
 * @date 2013-10-30
 */
public abstract class AbsOrmDao<T extends BaseModel> {

	OrmDaoManager manager = null;
	Dao<T, ?> baseDao = null;
	ConnectionSource connectionSource = null;
	
	public AbsOrmDao() {
		manager = OrmDaoManager.getOrmDaoManagerInstance(App.getInstance());
		connectionSource = manager.getConnectionSource();
	}
	
	/**
	 * 获得底层的基本查询操作，供上层使用
	 * @return
	 */
	public Dao<T, ?> getBaseDao() {
		return baseDao;
	}
	
	/**
	 * 插入一个对象到数据库
	 * @param obj
	 * @throws Exception
	 */
	public void insert(T obj) throws Exception {
		baseDao.create(obj);
	}
	
	/**
	 * 插入或更新一个对象到数据库
	 * @param obj
	 * @throws Exception
	 */
	public void insertOrUpdate(T obj) throws Exception {
		baseDao.createOrUpdate(obj);
	}
	
	/**
	 * 插入一个对象数组到数据库
	 * @param objs
	 * @throws Exception
	 */
	public void insertAll(List<T> objs) throws Exception {
		for(T obj : objs) {
			insert(obj);
		}
	}
	
	/**
	 * 插入或更新一个对象数组到数据库
	 * @param objs
	 * @throws Exception
	 */
	public void insertOrUpdateAll(List<T> objs) throws Exception {
		for(T obj : objs) {
			insertOrUpdate(obj);
		}
	}
	
	/**
	 * 查询数据库表中所有的记录
	 * @return
	 * @throws Exception
	 */
	public List<T> queryAll() throws Exception {
		return baseDao.queryForAll();
	}
	
	/**
	 * 更新数据库中的某条记录
	 * @param obj
	 * @throws Exception
	 */
	public void update(T obj) throws Exception {
		baseDao.update(obj);
	}
	
	/**
	 * 删除数据库中的某条记录
	 * @param obj
	 * @throws Exception
	 */
	public void delete(T obj) throws Exception {
		baseDao.delete(obj);
	}
	
}
