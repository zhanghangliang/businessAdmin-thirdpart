/**
 * @Title: BizWorkTask.java
 * @Package com.gov.wiki.common.entity.buss
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年11月25日
 * @version V1.0
 */
package com.gov.wiki.common.entity.buss;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.system.SysFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: BizWorkTask
 * @Description: 工作任务
 * @author cys
 * @date 2020年11月25日
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "biz_work_task")
@Where(clause = "del_flag != 1")
@SQLDelete(sql = "update biz_work_task set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_work_task set del_flag=1 where id=?")
@ApiModel(value = "BizWorkTask", description = "工作任务结构对象")
public class BizWorkTask extends BaseEntity {
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*任务名称
	*/
	@Column(name = "name", nullable = true)
	@ApiModelProperty(value = "任务名称")
	private String name;
	
	/**
	*描述
	*/
	@Column(name = "description", nullable = true)
	@ApiModelProperty(value = "描述")
	private String description;
	
	/**
	*附件
	*/
	@Column(name = "annex", nullable = true)
	@ApiModelProperty(value = "附件")
	private String annex;
	
	@NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "reference_id", referencedColumnName = "id", insertable = false, updatable = false)
	@ApiModelProperty(value = "附件列表")
    private List<SysFile> sysFiles;
}