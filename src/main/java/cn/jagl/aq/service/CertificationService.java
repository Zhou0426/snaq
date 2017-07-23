package cn.jagl.aq.service;

import java.util.List;
import cn.jagl.aq.domain.Certification;

public interface CertificationService {
	    //hql查询总数
		public long countHql(String hql) ;
		//分页查询
		List<Certification> findByPage(String hql , int pageNo, int pageSize);
		//根据证件编号查询实体
		public Certification getByCertificationSn(String certificationSn);	
		/**
		 * 根据证件编号和证件类型编号查询实体
		 * @param certificationSn
		 * @param certificationTypeSn
		 * @return
		 */
		public Certification getByCertificationSnAndTypeSn(String certificationSn, String certificationTypeSn);	
		//增加未遂事件实体
		void add(Certification certification);
		//删除未遂事件实体
		void delete(Certification certification);
		//更新未遂事件实体
		void update(Certification certification);
		//根据id获取实体
		public Certification getById(int id);
		/**
		 * 批量删除
		 * @param ids
		 */
		void deleteByIds(String ids);
}
