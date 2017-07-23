package cn.jagl.aq.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import cn.jagl.aq.service.AccidentLevelService;
import cn.jagl.aq.service.AccidentService;
import cn.jagl.aq.service.AccidentTypeService;
import cn.jagl.aq.service.CertificationService;
import cn.jagl.aq.service.CertificationTypeService;
import cn.jagl.aq.service.CheckTableCheckerService;
import cn.jagl.aq.service.CheckTableService;
import cn.jagl.aq.service.CheckTaskAppraisalsService;
import cn.jagl.aq.service.DepartmentService;
import cn.jagl.aq.service.DepartmentTypeService;
import cn.jagl.aq.service.DepartmentUseStatService;
import cn.jagl.aq.service.DocumentTemplateService;
import cn.jagl.aq.service.HazardReportedService;
import cn.jagl.aq.service.HazardService;
import cn.jagl.aq.service.InconformityAttachmentService;
import cn.jagl.aq.service.InconformityItemService;
import cn.jagl.aq.service.InteriorWorkAttachmentService;
import cn.jagl.aq.service.UnsafeConditionCorrectConfirmService;
import cn.jagl.aq.service.UnsafeConditionDeferService;
import cn.jagl.aq.service.UnsafeConditionReviewService;
import cn.jagl.aq.service.UnsafeConditionService;
import cn.jagl.aq.service.InteriorWorkService;
import cn.jagl.aq.service.LatentHazardAttachmentService;
import cn.jagl.aq.service.LatentHazardService;
import cn.jagl.aq.service.SessionInfoService;
import cn.jagl.aq.service.ManageObjectService;
import cn.jagl.aq.service.ManagementReviewAttachmentService;
import cn.jagl.aq.service.ManagementReviewService;
import cn.jagl.aq.service.ModuleStatisticsService;
import cn.jagl.aq.service.NearMissAuditService;
import cn.jagl.aq.service.NearMissService;
import cn.jagl.aq.service.NearMissTypeService;
import cn.jagl.aq.service.NoticeAttachmentService;
import cn.jagl.aq.service.NoticeService;
import cn.jagl.aq.service.PeriodicalCheckService;
import cn.jagl.aq.service.PersonRecordService;
import cn.jagl.aq.service.PersonService;
import cn.jagl.aq.service.PersonUseStatService;
import cn.jagl.aq.service.PostLevelService;
import cn.jagl.aq.service.ResourceService;
import cn.jagl.aq.service.RoleResourceService;
import cn.jagl.aq.service.RoleService;
import cn.jagl.aq.service.SmsService;
import cn.jagl.aq.service.SpecialCheckService;
import cn.jagl.aq.service.SpecialityService;
import cn.jagl.aq.service.StandardIndexAuditMethodService;
import cn.jagl.aq.service.StandardIndexService;
import cn.jagl.aq.service.StandardService;
import cn.jagl.aq.service.SuperviseItemService;
import cn.jagl.aq.service.SystemAuditAttachmentService;
import cn.jagl.aq.service.SuperviseDailyReportDetailsService;
import cn.jagl.aq.service.SuperviseDailyReportService;
import cn.jagl.aq.service.SystemAuditScoreDetailsService;
import cn.jagl.aq.service.SystemAuditScoreService;
import cn.jagl.aq.service.SystemAuditService;
import cn.jagl.aq.service.TemplateAttachmentService;
import cn.jagl.aq.service.UnitAppraisalsService;
import cn.jagl.aq.service.UnsafeActDeductionPointService;
import cn.jagl.aq.service.UnsafeActService;
import cn.jagl.aq.service.UnsafeActStandardService;
import cn.jagl.util.PageModel;
/**
 * 
 * @author 马辉 
 * 2016/06/28
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class BaseAction<T> extends ActionSupport implements RequestAware,
SessionAware, ApplicationAware, ModelDriven<T> {
	//可以传送多个id给该字段 形式以逗号隔开
	protected String ids;
	//泛型实体备用
	protected T entity;
	//获取泛型List集合，交给struts以json格式返回
	protected List<T> jsonList=null;
	//分页使用的模型，内部包括符合条件的行rows，总数total
	protected PageModel<T> pageModel=null;
	//page用于接收客户端传递的页码
	protected Integer page;
	//rows用于接收客户端传递的每页行数
	protected Integer rows;
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public void setPageModel(PageModel<T> pageModel) {
		this.pageModel = pageModel;
	}
	public PageModel<T> getPageModel() {
		return pageModel;
	}
	
	public void setPage(Integer page) {
		this.page = page;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public List<T> getJsonList() {
		return jsonList;
	}

	public void setJsonList(List<T> jsonList) {
		this.jsonList = jsonList;
	}
	
	//注入service
	@Resource(name="departmentService")
	protected DepartmentService departmentService;
	@Resource(name="departmentTypeService")
	protected DepartmentTypeService departmentTypeService;
	@Resource(name="documentTemplateService")
	protected DocumentTemplateService documentTemplateService;
	@Resource(name="hazardService")
	protected HazardService hazardService;
	@Resource(name="inconformityAttachmentService")
	protected InconformityAttachmentService inconformityAttachmentService;
	@Resource(name="unsafeConditionService")
	protected UnsafeConditionService unsafeConditionService;
	@Resource(name="interiorWorkService")
	protected InteriorWorkService interiorWorkService;
	@Resource(name="manageObjectService")
	protected ManageObjectService manageObjectService;
	@Resource(name="periodicalCheckService")
	protected PeriodicalCheckService periodicalCheckService;
	@Resource(name="personService")
	protected PersonService personService;	
	@Resource(name="specialCheckService")
	protected SpecialCheckService specialCheckService;
	@Resource(name="specialityService")
	protected SpecialityService specialityService;
	@Resource(name="standardService")
	protected StandardService standardService;
	@Resource(name="standardIndexService")
	protected StandardIndexService standardindexService;
	@Resource(name="superviseItemService")
	protected SuperviseItemService superviseItemService;
	@Resource(name="superviseDailyReportService")
	protected SuperviseDailyReportService superviseDailyReportService;
	@Resource(name="superviseDailyReportDetailsService")
	protected SuperviseDailyReportDetailsService superviseDailyReportDetailsService;
	@Resource(name="templateAttachmentService")
	protected TemplateAttachmentService templateAttachmentService;
	@Resource(name="accidentTypeService")
	protected AccidentTypeService accidentTypeService;
	@Resource(name="resourceService")
	protected ResourceService resourceService;
	@Resource(name="standardIndexAuditMethodService")
	protected StandardIndexAuditMethodService standardIndexAuditMethodService;	
	@Resource(name="roleService")
	protected RoleService roleService; 
	@Resource(name="unsafeConditionReviewService")
	protected UnsafeConditionReviewService unsafeConditionReviewService;
	@Resource(name="unsafeConditionCorrectConfirmService")
	protected UnsafeConditionCorrectConfirmService unsafeConditionCorrectConfirmService;
	@Resource(name="nearMissService")
	protected NearMissService nearMissService;
	@Resource(name="systemAuditService")
	protected SystemAuditService systemAuditService;
	@Resource(name="nearMissTypeService")
	protected NearMissTypeService nearMissTypeService;
	@Resource(name="nearMissAuditService")
	protected NearMissAuditService nearMissAuditService;
	@Resource(name="unsafeActService")
	protected UnsafeActService unsafeActService;
	@Resource(name="systemAuditScoreService")
	protected SystemAuditScoreService systemAuditScoreService;
	@Resource(name="unsafeActStandardService")
	protected UnsafeActStandardService unsafeActStandardService;
	@Resource(name="accidentService")
	protected AccidentService accidentService;
	@Resource(name="accidentLevelService")
	protected AccidentLevelService accidentLevelService;
	@Resource(name="checkTableService")
	protected CheckTableService checkTableService;
	@Resource(name="certificationService")
	protected CertificationService certificationService;
	@Resource(name="certificationTypeService")
	protected CertificationTypeService certificationTypeService;
	@Resource(name="managementReviewService")
	protected ManagementReviewService managementReviewService;
	@Resource(name="managementReviewAttachmentService")
	protected ManagementReviewAttachmentService managementReviewAttachmentService;
	@Resource(name="postLevelService")
	protected PostLevelService postLevelService;
	@Resource(name="checkTableCheckerService")
	protected CheckTableCheckerService checkTableCheckerService;
	@Resource(name="unitAppraisalsService")
	protected UnitAppraisalsService unitAppraisalsService;
	@Resource(name="checkTaskAppraisalsService")
	protected CheckTaskAppraisalsService checkTaskAppraisalsService;
	@Resource(name="inconformityItemService")
	protected InconformityItemService inconformityItemService;
	@Resource(name="personRecordService")
	protected PersonRecordService personRecordService;
	@Resource(name="roleResourceService")
	protected RoleResourceService roleResourceService;
	@Resource(name="hazardReportedService")
	protected HazardReportedService hazardReportedService;
	@Resource(name="systemAuditScoreDetailsService")
	protected SystemAuditScoreDetailsService systemAuditScoreDetailsService;
	@Resource(name="smsService")
	protected SmsService smsService;
	@Resource(name="sessionInfoService")
	protected SessionInfoService sessionInfoService;
	@Resource(name="personUseStatService")
	protected PersonUseStatService personUseStatService;
	@Resource(name="departmentUseStatService")
	protected DepartmentUseStatService departmentUseStatService;
	@Resource(name="moduleStatisticsService")
	protected ModuleStatisticsService moduleStatisticsService;
	@Resource(name="unsafeConditionDeferService")
	protected UnsafeConditionDeferService unsafeConditionDeferService;
	@Resource(name="latentHazardService")
	protected LatentHazardService latentHazardService;
	@Resource(name="latentHazardAttachmentService")
	protected LatentHazardAttachmentService latentHazardAttachmentService;
	@Resource(name="systemAuditAttachmentService")
	protected SystemAuditAttachmentService systemAuditAttachmentService;
	@Resource(name="unsafeActDeductionPointService")
	protected UnsafeActDeductionPointService unsafeActDeductionPointService;
	@Resource(name="noticeService")
	protected NoticeService noticeService;
	@Resource(name="noticeAttachmentService")
	protected NoticeAttachmentService noticeAttachmentService;
	@Resource(name="interiorWorkAttachmentService")
	protected InteriorWorkAttachmentService interiorWorkAttachmentService;
	
	public BaseAction() {
/*	ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		@SuppressWarnings("rawtypes")
		Class clazz = (Class) type.getActualTypeArguments()[0];
		try {
			entity = (T) clazz.
		} catch (Exception e) {
			throw new RuntimeException(e);
		}*/
	}
	@Override
	//锟斤拷锟截讹拷锟斤拷压栈
	public T getModel() {
		// TODO Auto-generated method stub
		return entity;
	}
	//用于封装请求request
	protected Map<String, Object> request;
	//用于封装会话session
	protected Map<String, Object> session;
	//用于封装application
	protected Map<String, Object> application;	

	@Override
	public void setApplication(Map<String, Object> application) {
		// TODO Auto-generated method stub
		this.application=application;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		this.session=session;
	}

	@Override
	public void setRequest(Map<String, Object> request) {
		// TODO Auto-generated method stub
		this.request=request;
	}
}
