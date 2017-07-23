package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.ManageObjectDao;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.ManageObject;
import cn.jagl.aq.service.ManageObjectService;
@Service("manageObjectService")
public class ManageObjectServiceImpl implements ManageObjectService {
	
	@Resource(name="manageObjectDao")
	private ManageObjectDao manageObjectDao;
	//增加方法
	@Override
	public int addManageObject(ManageObject manageObject) {
		
		return (Integer) manageObjectDao.save(manageObject);
	}
	//获取所有对象
	@Override
	public List<ManageObject> getAllManageObjects() {

		return manageObjectDao.findAll(ManageObject.class);
	}
	
	//删除方法
	@Override
	public void deleteManageObject(int id) {
		ManageObject manageObject=this.getById(ManageObject.class, id);
		manageObject.getHazards().clear();
		this.update(manageObject);
		manageObjectDao.delete(ManageObject.class, id);
	}
	//根据部门编号获取数据
	@Override
	public ManageObject getByManageObjectSn(String manageObjectSn) {
		
		return manageObjectDao.getByManageObjectSn(manageObjectSn);
	}
	//更新数据
	@Override
	public void update(ManageObject manageObject) {
		// TODO Auto-generated method stub
		 manageObjectDao.update(manageObject);
	}
	//根据部门类型，管理对象查询，并分页
	@Override
	public long findNum(String parentManageObjectSn){
		// TODO Auto-generated method stub
		return manageObjectDao.findNum(parentManageObjectSn);
	}
	//分页查询
	@Override
	public List<ManageObject> findByPage(String hql, int page, int rows) {
		// TODO Auto-generated method stub
		return manageObjectDao.findByPage(hql, page, rows);
	}
	//获取总数
	@Override
	public long findCount(Class<ManageObject> ManageObject) {
		// TODO Auto-generated method stub
		return manageObjectDao.findCount(ManageObject);
	}
	//根据Id查找数据
	@Override
	public ManageObject getById(Class<ManageObject> ManageObject,int id) {
		// TODO Auto-generated method stub
		ManageObject mo=manageObjectDao.get(ManageObject, id);
		return mo;
	}
	@Override
	public List<ManageObject> getByParentSn(String parentSn,String departmentTypeSn) {
		// TODO Auto-generated method stub
		return manageObjectDao.getByParentSn(parentSn,departmentTypeSn);
	}
	@Override
	public List<ManageObject> getByMoHuFind(String manageObjectSn) {
		// TODO Auto-generated method stub
		return manageObjectDao.getByMoHuFind(manageObjectSn);
	}
	@Override
	public List<ManageObject> getManageObjectsByHql(String hql) {
		// TODO Auto-generated method stub
		return manageObjectDao.getManageObjectsByHql(hql);
	}

	

}
