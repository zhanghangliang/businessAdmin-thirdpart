package com.gov.wiki.common.entity.buss;

import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.MemberInfoReq;
import com.gov.wiki.common.entity.system.SysFile;
import lombok.Data;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "biz_study")
@Where(clause = "del_flag != 1")
@SQLDelete(sql = "update biz_study set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_study set del_flag=1 where id=?")
public class BizStudy extends BaseEntity {
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "classification")
	private Integer classification;

	@Column(name = "study_describe")
	private String studyDescribe;

	@Column(name = "length_of_study")
	private String lengthOfStudy;

	@Column(name = "learners")
	private String learners;

	@Column(name = "online")
	private Boolean online;

	@Column(name = "permission_type")
	private Integer permissionType;

	@Column(name = "permission_range")
	private String permissionRange;

	@Transient
	private String permissionRangeName;

	@Transient
	private List<MemberInfoReq> learnersname;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "reference_id", referencedColumnName = "id")
	private List<SysFile> sysFiles;

	/**
	 * 类型 1-文件夹 2-文件
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 父级
	 */
	@Column(name = "parent_id")
	private String parentId;

	/**
	 * 长编码
	 */
	@Column(name = "path")
	private String path;

	/**
	 * 序号
	 */
	@Column(name = "seq")
	private Integer seq;

	@Column(name = "keywords")
	private String keywords;
	
	@Formula("(select count(*) from biz_study o where o.parent_id = id)")
	private int leaf;
	
	/**
	 * 父级名称
	 */
	@Formula("(select o.name from biz_study o where o.id=parent_id)")
	private String parentName;

}