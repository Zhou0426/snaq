package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.SpecialCheck;

/**
 * @author mahui
 *
 * @date 2016年7月8日下午5:16:10
 */
public interface SpecialCheckService {
	//添加
	void save(SpecialCheck specialCheck);
	//更新
	void update(SpecialCheck specialCheck);
	//多条删除
	void deleteByIds(String ids);
	//编号获取
	SpecialCheck getBySpecialCheckSn(String specialCheckSn);
	//获取记录数
	long count(String hql);
	//分页查询
	List<SpecialCheck> query(String hql,int page,int rows);
	//hetById
	SpecialCheck getById(int id);
}
