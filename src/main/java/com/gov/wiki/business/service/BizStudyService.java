package com.gov.wiki.business.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.gov.wiki.business.req.StudyQuery;
import com.gov.wiki.business.req.StudyReq;
import com.gov.wiki.business.res.StudyRes;
import com.gov.wiki.common.entity.buss.BizStudy;
import com.gov.wiki.common.service.IBaseService;

public interface BizStudyService extends IBaseService<BizStudy, String> {
    Page<BizStudy> findAll(Specification specification, Pageable pageable);
    
    /**
     * @Title: saveOrUpdate
     * @Description: 新增或者修改学习资料信息
     * @param req
     * @return StudyRes 返回类型
     * @throws
     */
    @Transactional
    StudyRes saveOrUpdate(StudyReq req);
    
    /**
     * @Title: getMaxSeq
     * @Description: 查询最大序号值
     * @return int 返回类型
     * @throws
     */
    int getMaxSeq();
    
    /**
     * @Title: queryChildsByPath
     * @Description: 根据长编码查询子节点
     * @param path
     * @return List<BizStudy> 返回类型
     * @throws
     */
    List<BizStudy> queryChildsByPath(String path);
 
    /**
     * @Title: depthDelete
     * @Description: 深度删除资料文件夹及文件信息
     * @param ids
     * @return void 返回类型
     * @throws
     */
    void depthDelete(List<String> ids);
    
    /**
     * @Title: findByParams
     * @Description: 根据参数查询学习文件夹及资料信息
     * @param query
     * @return List<StudyRes> 返回类型
     * @throws
     */
    List<StudyRes> findByParams(StudyQuery query);

    /**
     * @Title:
     * @Description: 根据specifications查找数量
     * @param
     * @param specifications
     * @return   返回类型
     * @throws
     */
    Long findCountByName(Specification<BizStudy> specifications);
    /**
     * @Title: parserImgFile
     * @Description: 解析并保存图片
     * @param [base64]
     * @return java.lang.String  返回类型
     * @throws
     */
     String parserImgFile( String userId, String base64);
}