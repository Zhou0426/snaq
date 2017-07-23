package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Resource;
import cn.jagl.aq.domain.Role;
import cn.jagl.aq.domain.RoleResource;
import cn.jagl.aq.domain.RoleType;

@SuppressWarnings("serial")
public class RoleAssignmentAction extends BaseAction<Role> {
	JSONArray array = new JSONArray();
	private int rows;
	private int page;
	private String roleSn;
	private String departmentSn;
	private String q;
	private String personId;
	private File excel;
	private InputStream excelStream;
	private String excelFileName;
	private String data;
	private String id;
	private String roleName;
	private String roleTypeSn;
	private String resourceSn;
	private String resourceSns;
	private RoleType roleType;
	private String excelContentType;
	private String deleteornot;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResourceSns() {
		return resourceSns;
	}

	public void setResourceSns(String resourceSns) {
		this.resourceSns = resourceSns;
	}

	public String getResourceSn() {
		return resourceSn;
	}

	public void setResourceSn(String resourceSn) {
		this.resourceSn = resourceSn;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleTypeSn() {
		return roleTypeSn;
	}

	public void setRoleTypeSn(String roleTypeSn) {
		this.roleTypeSn = roleTypeSn;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public File getExcel() {
		return excel;
	}

	public void setExcel(File excel) {
		this.excel = excel;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getDepartmentSn() {
		return departmentSn;
	}

	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getRoleSn() {
		return roleSn;
	}

	public void setRoleSn(String roleSn) {
		this.roleSn = roleSn;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getExcelContentType() {
		return excelContentType;
	}

	public void setExcelContentType(String excelContentType) {
		this.excelContentType = excelContentType;
	}

	// 显示此人拥有的角色集合 菜单
	public String roleMenu() {
		String pId = (String) session.get("personId");
		Person personnow = personService.getByPersonId(pId);
		List<Department> departments = personService.getResourcePermissionScope(personnow.getPersonId(), "1403");
		String departmentSn = "";
		if (departments.size() > 0)
			departmentSn = departments.get(0).getDepartmentSn();
		@SuppressWarnings("unchecked")
		HashMap<String, String> myroles = (HashMap<String, String>) session.get("roles");
		List<Role> roles = new ArrayList<Role>();
		String roleType = null;
		if (myroles.get("jtxtgly") != null) {// 集团系统管理员
			roleType = "(0,1,2)";
		} else if (myroles.get("fgsxtgly") != null) {
			roleType = "(1,2)";
		} else if (myroles.get("dwxtgly") != null) {
			roleType = "(2)";
		}
		StringBuffer hql = new StringBuffer();
		StringBuffer hqlcount = new StringBuffer();
		hql.append("select r from Role r where r.roleType in " + roleType);
		hqlcount.append("select count(r) from Role r where r.roleType in " + roleType);
		if (deleteornot != null && deleteornot.equals("0")) {
			hql.append(" and r.deleted=false");
			hqlcount.append(" and r.deleted=false");
		}
		roles = roleService.findByPage(hql.toString(), page, rows);
		JSONArray array = new JSONArray();
		// 遍历此人拥有的每个角色编号
		for (Role role : roles) {
			JSONObject jo = new JSONObject();
			jo.put("roleSn", role.getRoleSn());
			jo.put("roleName", role.getRoleName());
			// 此人拥有角色编号
			String roleSnnow = role.getRoleSn();
			// 此角色编号下的所有人
			List<Person> persons = (List<Person>) roleService.getPersonByRoleSn(roleSnnow);
			// 筛选出此角色编号下所有人中属于当前部门下的人数
			long personNum = 0;
			if (persons.size() != 0) {
				personNum = personService.getPersonNumByDepartmentSn(persons, departmentSn);
			}
			jo.put("roleTypeName", role.getRoleType());
			jo.put("personNum", personNum);
			jo.put("deleted", role.getDeleted());
			array.put(jo);
		}
		long total = roleService.getCountByHql(hqlcount.toString());
		data = "{\"total\":" + total + ",\"rows\":" + array + "}";
		return SUCCESS;
	}

	// 筛选出当前角色编号和当前部门下的所有人的集合
	public String personDetail() {
		String pId = (String) session.get("personId");
		Person personnow = personService.getByPersonId(pId);
		List<Department> departments = personService.getResourcePermissionScope(personnow.getPersonId(), "1403");
		String departmentSn = "";
		if (departments.size() > 0)
			departmentSn = departments.get(0).getDepartmentSn();
		String sqlenitiy = "select  distinct person.* FROM person_role inner join person on person_role.person_id=person.person_id where role_sn='"
				+ roleSn + "' and person.department_sn like '" + departmentSn + "%'";
		List<Person> persons = personService.getBySql(sqlenitiy, page, rows);
		String sqlcount = "select count(*) FROM person_role inner join person on person_role.person_id=person.person_id where role_sn='"
				+ roleSn + "' and person.department_sn like '" + departmentSn + "%'";
		long total = personService.countBySQL(sqlcount);
		for (Person person : persons) {
			JSONObject jo = new JSONObject();
			jo.put("roleSn", roleSn);
			jo.put("personId", person.getPersonId());
			jo.put("personName", person.getPersonName());
			if (person.getDepartment().getDepartmentSn() != null) {
				jo.put("departmentName", person.getDepartment().getDepartmentName());
			}
			jo.put("implDepartmentName", person.getDepartment().getImplDepartmentName());
			array.put(jo);
		}
		data = "{\"total\":" + total + ",\"rows\":" + array + "}";
		return SUCCESS;
	}

	// 获取当前部门下的所有人 人的下拉框
	public String persons() {
		JSONArray array = new JSONArray();
		String pId = (String) session.get("personId");
		Person personnow = personService.getByPersonId(pId);
		String departmentSn = personnow.getDepartment().getDepartmentSn();
		StringBuffer hql = new StringBuffer();
		hql.append("FROM Person p where p.department.departmentSn like '" + departmentSn + "%'");
		if (q != null && !"".equals(q)) {
			hql.append(" AND (p.personId like '" + q + "%' or p.personName like '" + q + "%')");
		}
		List<Person> persons = personService.findByPage(hql.toString(), 1, 20);
		long total = personService.findTotalByDept(departmentSn);
		for (Person person : persons) {
			JSONObject jo = new JSONObject();
			jo.put("personId", person.getPersonId());
			jo.put("personName", person.getPersonName());
			array.put(jo);
		}
		data = "{\"total\":" + total + ",\"rows\":" + array + "}";
		return SUCCESS;
	}

	// 角色分配里的人 加载此角色可分配的人 如果是集团系统管理员就全部人如果是单位系统管理员就最近贯标单位下的人
	public String personss() {
		JSONArray array = new JSONArray();
		String pId = (String) session.get("personId");
		Person personnow = personService.getByPersonId(pId);
		Boolean isJtxtgly = false;
		for (Role role : personService.getRoles(personnow)) {
			if (role.getRoleSn().equals("jtxtgly")) {
				isJtxtgly = true;
			}
		}
		long total = 0;
		StringBuffer hql = new StringBuffer();
		StringBuffer count = new StringBuffer();
		if (isJtxtgly) {
			hql.append("FROM Person p where p.deleted=false");
			count.append("select count(*) FROM Person p where p.deleted=false");
			if (q != null && !"".equals(q)) {
				hql.append(" and (p.personId like '" + q.trim() + "%' or p.personName like '" + q.trim() + "%')");
			}
		} else {
			
			int roleType=(int) session.get("roleType");
			
			if(roleType==1)
			{
				departmentSn = departmentService.getUpNerestFgs(personnow.getDepartment().getDepartmentSn())
						.getDepartmentSn();
			}else{
				departmentSn = departmentService.getUpNearestImplDepartment(personnow.getDepartment().getDepartmentSn())
						.getDepartmentSn();
			}
			
			hql.append(
					"FROM Person p where p.deleted=false and p.department.departmentSn like '" + departmentSn + "%'");
			count.append("select count(*) FROM Person p where p.deleted=false and p.department.departmentSn like '"
					+ departmentSn + "%'");
			if (q != null && !"".equals(q)) {
				hql.append(" AND (p.personId like '%" + q.trim() + "%' or p.personName like '%" + q.trim() + "%')");
			}
		}
		// hql.append(" order by convert(p.personName, 'gbk')");
		// count.append(" order by convert(p.personName, 'gbk')");
		hql.append(" order by p.personName");
		count.append(" order by p.personName");
		List<Person> persons = personService.findByPage(hql.toString(), 1, 20);
		total = personService.countHql(count.toString());
		for (Person person : persons) {
			JSONObject jo = new JSONObject();
			jo.put("personId", person.getPersonId());
			jo.put("personName", person.getPersonName());
			array.put(jo);
		}
		data = "{\"total\":" + total + ",\"rows\":" + array + "}";
		return SUCCESS;
	}

	// 为角色添加人
	public String roleForPerson() {
		Boolean doit = true;
		Role role = roleService.getByRoleSn(roleSn);
		Set<Person> persons = role.getPersons();
		for (Person person : persons) {
			if (personId.equals(person.getPersonId())) {
				data = "该角色里已经存在此人！";
				doit = false;
			}
		}
		if (doit == true) {
			try {
				Person person = personService.getByPersonId(personId);
				role.getPersons().add(person);
				roleService.update(role);
				data = "添加成功！";
			} catch (Exception e) {
				data = "添加失败！";
			}
		}
		return SUCCESS;
	}

	// 在此种角色中删除此人
	public String roleDecPerson() throws IOException {
		try {
			Person person = personService.getByPersonId(personId);
			Role role = roleService.getByRoleSn(roleSn);
			Set<Person> persons = role.getPersons();
			persons.remove(person);
			role.setPersons(persons);
			roleService.update(role);
			data = "删除成功！";
		} catch (Exception e) {
			data = "删除失败！";
		}
		return SUCCESS;
	}

	// 转换并读取字符
	public String StringValue(XSSFCell before) {
		String after = "";
		if (before.getCellType() == Cell.CELL_TYPE_STRING) {
			after = before.toString();
		}
		if (before.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			BigDecimal bd = new BigDecimal(before.getNumericCellValue());
			after = bd.toPlainString();
		}
		return after.trim();
	}

	@SuppressWarnings({ "resource", "unchecked" })
	public String importRole() throws IOException {
		InputStream is0 = new FileInputStream(excel);
		XSSFWorkbook wb = new XSSFWorkbook(is0);
		Person person = null;
		float num = 0f;
		String isError = "第";
		String nullData = "第";
		String notFindRole = "第";
		String notFindPerson = "第";
		String notPersonPower = "第";
		String notRolePower = "第";
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			num = xssfSheet.getLastRowNum();
			// 循环行Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				session.put("progressValue", (int) ((float) rowNum * 100 / num));
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow == null) {
					// nullData+=(rowNum+1)+",";
					continue;
				}
				person = new Person();
				try {
					// 只能分配最近贯标单位下的员工
					XSSFCell personId = xssfRow.getCell(0);
					if (personId == null || personId.toString().trim().length() == 0) {
						nullData += (rowNum + 1) + ",";
						continue;
					}
					person = personService.getByPersonId(StringValue(personId));
					if (person == null) {
						notFindPerson += (rowNum + 1) + ",";
						continue;
					}
					List<String> personIds = null;
					// 根据此人的角色获取此人拥有的可分配的人的权限
					Boolean isJtxtgly = false;
					String pId = (String) session.get("personId");
					Person personnow = personService.getByPersonId(pId);
					for (Role role : personService.getRoles(personnow)) {
						if (role.getRoleSn().equals("jtxtgly")) {
							isJtxtgly = true;
							break;
						}
					}
					if (!isJtxtgly) {
						String departmentSn = departmentService
								.getUpNearestImplDepartment(personnow.getDepartment().getDepartmentSn())
								.getDepartmentSn();
						personIds = personService.getPersonSnByDepartmentSn(departmentSn);
						if (!personIds.contains(StringValue(personId))) {
							notPersonPower += (rowNum + 1) + ",";
							continue;
						}
					}
					// 只能分配自己角色类型对应的角色权限范围
					XSSFCell roleSn = xssfRow.getCell(2);
					if (roleSn == null || StringValue(roleSn).length() == 0) {
						nullData += (rowNum + 1) + ",";
						continue;
					}
					Role role = roleService.getByRoleSn(StringValue(roleSn));
					if (role == null) {
						notFindRole += (rowNum + 1) + ",";
						continue;
					}
					HashMap<String, String> myroles = (HashMap<String, String>) session.get("roles");
					String roleType = null;
					if (myroles.get("jtxtgly") != null) {// 集团系统管理员
						roleType = "(0,1,2)";
					} else if (myroles.get("fgsxtgly") != null) {
						roleType = "(1,2)";
					} else if (myroles.get("dwxtgly") != null) {
						roleType = "(2)";
					}
					List<String> roleSns = roleService.getAllRoleSns(roleType);
					if (!roleSns.contains(StringValue(roleSn))) {
						notRolePower += (rowNum + 1) + ",";
						continue;
					}
				} catch (Exception e) {
					isError += (rowNum + 1) + ",";
					continue;
				}
				try {
					XSSFCell roleSn = xssfRow.getCell(2);
					Role role = roleService.getByRoleSn(StringValue(roleSn));
					Set<Person> persons = role.getPersons();
					XSSFCell personId = xssfRow.getCell(0);
					person = personService.getByPersonId(StringValue(personId));
					person.setRoles(null);// 清空此人角色集合 那前几次循环设置的岂不是白设置了
											// 应该查找是否存在此角色 此人
					personService.updatePerson(person);// 需求难道不是清空此角色里的人
														// 但是好像现在就是
					persons.add(person);
					role.setPersons(persons);
					roleService.update(role);
				} catch (Exception e) {
					isError += (rowNum + 1) + ",";
				}
			}
		}
		data = "";
		String[] number;
		if (!"第".equals(nullData)) {
			nullData = nullData.substring(0, nullData.length() - 1) + "行有空数据！" + "<br />";
			data += nullData;
			number = nullData.split(",");
			data = data + "共" + number.length + "行" + "<br /><br />";
		}
		if (!"第".equals(notFindRole)) {
			notFindRole = notFindRole.substring(0, notFindRole.length() - 1) + "行角色编号不存在！" + "<br />";
			data += notFindRole;
			number = notFindRole.split(",");
			data = data + "共" + number.length + "行" + "<br /><br />";
		}
		if (!"第".equals(notFindPerson)) {
			notFindPerson = notFindPerson.substring(0, notFindPerson.length() - 1) + "行人员编号不存在！" + "<br />";
			data += notFindPerson;
			number = notFindPerson.split(",");
			data = data + "共" + number.length + "行" + "<br /><br />";
		}
		if (!"第".equals(notPersonPower)) {
			notPersonPower = notPersonPower.substring(0, notPersonPower.length() - 1) + "行人员编号越级，你无权为该人员分配角色！"
					+ "<br />";
			data += notPersonPower;
			number = notPersonPower.split(",");
			data = data + "共" + number.length + "行" + "<br /><br />";
		}
		if (!"第".equals(notRolePower)) {
			notRolePower = notRolePower.substring(0, notRolePower.length() - 1) + "行角色编号越级，你无权为该角色分配人员！" + "<br />";
			data += notRolePower;
			number = notRolePower.split(",");
			data = data + "共" + number.length + "行" + "<br /><br />";
		}
		if (!"第".equals(isError)) {
			isError = isError.substring(0, isError.length() - 1) + "行导入异常，请检查格式！" + "<br />";
			data += isError;
			number = isError.split(",");
			data = data + "共" + number.length + "行" + "<br /><br />";
		}
		if (data.equals("")) {
			data = "导入数据成功！";
		} else {
			data += "其他数据导入成功！";
		}
		session.put("progressValue", 0);
		return SUCCESS;

	}

	// 分配角色模板
	public String downloadRole() throws IOException {
		String path = ServletActionContext.getServletContext().getRealPath("/template/roles.xlsx");
		InputStream is0 = new FileInputStream(path);
		XSSFWorkbook wb = new XSSFWorkbook(is0);
		try {
			ByteArrayOutputStream fout = new ByteArrayOutputStream();
			wb.write(fout);
			wb.close();
			fout.close();
			byte[] fileContent = fout.toByteArray();
			ByteArrayInputStream is = new ByteArrayInputStream(fileContent);

			excelStream = is;
			excelFileName = URLEncoder.encode("角色分配模板.xlsx", "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "download";
	}

	// 输出指定字段
	public PrintWriter out() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setContentType("text/plain; charset=utf-8");
		PrintWriter out = response.getWriter();
		return out;
	}

	public String resourcetree() throws IOException {
		out();
		String hql = "";
		List<Resource> resources = new ArrayList<Resource>();
		if (id != null && id.trim().length() > 0) {
			resources = resourceService.getResourceByParentSn(id);
		} else {
			hql = "FROM Resource r where r.parent is null";
			resources = resourceService.getByHql(hql);
		}
		Role role = roleService.getByRoleSn(roleSn);
		JSONArray data = new JSONArray();
		if (role != null) {
			for (Resource resource : resources) {
				JSONObject jo = new JSONObject();
				RoleResource roleResource = new RoleResource();
				if (resource.getResourceSn().equals("140304") || resource.getResourceSn().equals("140305")
						|| resource.getResourceSn().equals("140306") || resource.getResourceSn().equals("140303")) {
					continue;
				}
				try {
					roleResource = roleResourceService.getByRoleResource(role, resource);
				} catch (Exception e) {
					roleResource = null;
				}
				if (roleResource != null) {
					jo.put("checked", true);
				}
				jo.put("id", resource.getResourceSn());
				jo.put("text", resource.getResourceName());
				if (resource.getChildren().size() > 0) {
					jo.put("state", "closed");
				} else {
					jo.put("state", "open");
				}
				data.put(jo);
			}
		}
		out().print(data.toString());
		out().flush();
		out().close();
		return SUCCESS;
	}

	public String resourceForRole() {
		Resource resource = new Resource();
		RoleResource roleResource;
		String[] resourceSnsList = resourceSns.split(",");
		Role role = roleService.getByRoleSn(roleSn);
		resource = resourceService.getByResourceSn(resourceSn);
		for (int i = 0; i < resourceSnsList.length; i++) {
			resource = resourceService.getByResourceSn(resourceSnsList[i]);
			roleResource = roleResourceService.getByRoleResource(role, resource);
			// 插入数据库里不存在的组合 更新新增的权限
			if (roleResource == null) {
				roleResource = new RoleResource();
				roleResource.setRole(role);
				roleResource.setResource(resource);
				roleResourceService.add(roleResource);
			}
		}
		data = "为角色添加权限成功！";
		return SUCCESS;
	}

	public String resourceDeForRole() {
		Resource resource = new Resource();
		RoleResource roleResource = new RoleResource();
		String[] resourceSnsList = resourceSns.split(",");
		Role role = roleService.getByRoleSn(roleSn);
		for (int i = 0; i < resourceSnsList.length; i++) {
			resource = resourceService.getByResourceSn(resourceSnsList[i]);
			roleResource = roleResourceService.getByRoleResource(role, resource);
			// 删除数据库里存在的
			if (roleResource != null) {
				roleResourceService.deleteRoleResource(role, resource);
			}
		}
		return SUCCESS;
	}

	// 获取所有角色类型
	public String roleType() throws IOException {
		out();
		JSONArray array = new JSONArray();
		for (RoleType r : RoleType.values()) {
			JSONObject jo = new JSONObject();
			jo.put("roleTypeName", r.name());
			jo.put("roleTypeSn", r.ordinal());
			array.put(jo);
		}
		out().print(array.toString());
		out().flush();
		out().close();
		return SUCCESS;
	}

	// 添加角色
	public String add() throws IOException {
		try {
			// 得到包含已删除的角色
			Role findRole = roleService.getByRoleSnIngnoreState(roleSn);
			if (findRole == null) {
				Role role = new Role();
				role.setDeleted(false);
				role.setRoleName(roleName.trim());
				role.setRoleSn(roleSn.trim());
				role.setRoleType(roleType);
				roleService.addRole(role);
				data = "添加角色成功！";
			} else {
				data = "添加失败，此角色编号已存在！";
			}
		} catch (Exception e) {
			data = "添加异常！请联系开发人员";
		}
		return SUCCESS;
	}

	// 逻辑删除角色
	public String delete() {
		// 清空它拥有的集合 留下编号和其他属性
		try {
			// 这个方法只得到未删除的
			Role role = roleService.getByRoleSn(roleSn);
			// role.setRoleResources(null);
			// role.setPersons(null);
			role.setDeleted(true);
			roleService.update(role);
			data = "删除成功！";
		} catch (Exception e) {
			data = "删除失败！";
		}
		return SUCCESS;
	}

	public String update() {
		try {
			Role role = roleService.getByRoleSn(roleSn);
			role.setRoleName(roleName);
			role.setRoleType(roleType);
			roleService.update(role);
			data = "修改成功！";
		} catch (Exception e) {
			data = "修改角色数据失败！";
		}
		return SUCCESS;
	}

	public String getDeleteornot() {
		return deleteornot;
	}

	public void setDeleteornot(String deleteornot) {
		this.deleteornot = deleteornot;
	}
}
