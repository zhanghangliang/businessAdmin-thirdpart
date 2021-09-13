package com.gov.wiki.business.dao;

import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import com.gov.wiki.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BizMatterDepositoryMainDao extends BaseRepository<BizMatterDepositoryMain,String> {

    List<BizMatterDepositoryMain> findByUpmatterIdAndOnline(String upmatterid,Boolean online);

    @Query("select t.upmatterId,t.matterName from BizMatterDepositoryMain t where (t.attribute = 0 or t.attribute=2) and t.upmatterId <> '-1' and t.operationStatus=false and t.createBy in(?1) group by t.upmatterId")
    List<Object[]> groupUpMatterId(List<String> creater);

    @Query("select t.id,t.matterName from BizMatterDepositoryMain t where (t.attribute = 0 or t.attribute=2) and t.upmatterId=?1 and t.operationStatus=false and t.createBy in(?2)")
    List<Object[]> findIdByUpmatterId(String upmatterid,List<String> creater);

    @Query("select t from BizMatterDepositoryMain t where t.upmatterId in (?1)")
    List<BizMatterDepositoryMain> findsonbyids(List<String> ids);

    @Query("select t.id from BizMatterDepositoryMain t where t.online=true and t.upmatterId='-1'")
    List<String> findOnline();
    
    /**
     * @Title: findByUpmatterId
     * @Description: 根据上级查询下级事项
     * @param upmatterId
     * @return List<BizMatterDepositoryMain> 返回类型
     * @throws
     */
    List<BizMatterDepositoryMain> findByUpmatterId(String upmatterId);
}
