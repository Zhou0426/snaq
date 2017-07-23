package cn.jagl.aq.action;

import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.service.DepartmentTypeService;

public class DepartmentTypeAction extends BaseAction<DepartmentType>{
	private static final long serialVersionUID = 1L;
	protected DepartmentTypeService departmentTypeService;
	public DepartmentTypeService getDepartmentTypeService() {
		return departmentTypeService;
	}
	public void setDepartmentTypeService(DepartmentTypeService departmentTypeService) {
		this.departmentTypeService = departmentTypeService;
	}
	//Âí»Ô 2016/6/27
	public String query(){
		String hql="FROM DepartmentType d where d.isImplDepartmentType=true";
		jsonList=departmentTypeService.query(hql);
		return "jsonList";
	}

}
