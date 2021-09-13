package com.gov.wiki.business.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;

import com.gov.wiki.business.req.DataDelReq;
import com.gov.wiki.business.req.query.SubjectQuery;
import com.gov.wiki.business.res.SubjectWxRes;
import com.gov.wiki.common.buss.vo.SimpleSubjectRes;
import com.gov.wiki.common.entity.buss.BizOpinionMaterial;
import com.gov.wiki.common.entity.buss.BizSubject;
import com.gov.wiki.common.entity.buss.BizSubjectQaRelationship;
import com.gov.wiki.common.res.PageResult;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.wechat.req.OperationReq;

/**
 * @ClassName: ISubjectService
 * @Description: 主题管理业务处理接口
 * @author cys
 * @date 2020年8月25日
 */
public interface ISubjectService extends IBaseService<BizSubject, String> {

	Page<BizSubject> page(ReqBean<SubjectQuery> bean);

	List<BizOpinionMaterial> findMaterial(OperationReq body);

	List<BizSubjectQaRelationship> findRelationShip(String lastQaRelationShipId);
	
	@Transactional
	void delSubject(DataDelReq req);
	
	/**
	 * @Title: findSimpleById
	 * @Description: 查询简单对象
	 * @param id
	 * @return SimpleSubjectRes 返回类型
	 * @throws
	 */
	SimpleSubjectRes findSimpleById(String id);

	PageResult<SubjectWxRes> wxPage(ReqBean<SubjectQuery> bean);
}
