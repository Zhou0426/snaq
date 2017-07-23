package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.Hazard;
import cn.jagl.aq.domain.ManageObject;

public interface ManageObjectDao extends BaseDao<ManageObject> {
	
	//根据管理对象编号查询数据
	public ManageObject getByManageObjectSn(String manageObjectSn);
	//根据父级管理对象id查询数据
	public List<ManageObject> getByParentSn(String parentSn,String departmentTypeSn);
	//根据条件查询记录数
	public long findNum(String parentManageObjectSn);
	//模糊查找
	public List<ManageObject> getByMoHuFind(String manageObjectSn);
	//获取部分数据
	public List<ManageObject> getManageObjectsByHql(String hql);
	
}
