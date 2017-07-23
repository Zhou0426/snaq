package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.Hazard;
import cn.jagl.aq.domain.ManageObject;

public interface ManageObjectService {

	//增加数据
	int addManageObject(ManageObject manageObject);
	//获取所有数据
	List<ManageObject> getAllManageObjects();
	//获取部分数据
	List<ManageObject> getManageObjectsByHql(String hql);
	//删除数据
	void deleteManageObject(int id);
	//根据部门编号获取数据
	ManageObject getByManageObjectSn(String manageObjectSn);
	//更新数据
	void update(ManageObject manageObject);
	//根据条件查询所有记录数
	long findNum(String parentManageObjectSn);
	//分页查询
	List<ManageObject> findByPage(String hql , int page, int rows);
	//获取总记录数
	long findCount(Class<ManageObject> ManageObject);
	//根据ID获取实体
	ManageObject getById(Class<ManageObject> ManageObject,int id);
	//根据父级管理对象id获取数据
	List<ManageObject> getByParentSn(String parentSn,String departmentTypeSn);
	//模糊查找
	List<ManageObject> getByMoHuFind(String manageObjectSn);
}
