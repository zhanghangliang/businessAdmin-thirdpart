package com.gov.wiki.business.service.impl;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.dao.BizStudyDao;
import com.gov.wiki.business.enums.StudyTypeEnum;
import com.gov.wiki.business.req.StudyQuery;
import com.gov.wiki.business.req.StudyReq;
import com.gov.wiki.business.res.StudyRes;
import com.gov.wiki.business.service.BizStudyService;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.MemberInfoReq;
import com.gov.wiki.common.entity.buss.BizStudy;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.*;
import com.gov.wiki.organization.service.IMemberService;
import com.gov.wiki.system.service.IFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class BizStudyServiceimpl extends BaseServiceImpl<BizStudy, String, BizStudyDao> implements BizStudyService {
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IFileService fileService;

    @Override
    public Page<BizStudy> findAll(Specification specification, Pageable pageable) {
        return this.baseRepository.findAll(specification, pageable);
    }

    @Override
    public StudyRes saveOrUpdate(StudyReq req) {
        CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在！");
        CheckClass.check(req);
        if (StringUtils.isNotBlank(req.getCompanyId())) {
            List<String> idByCompanyId = memberService.findIdByCompanyId(req.getCompanyId());
            String newStr = String.join(",", idByCompanyId);
            req.setLearners(newStr);
        }
        BizStudy study = BeanUtils.copyProperties(req, BizStudy.class);
        BizStudy old = null;
        if (StringUtils.isNotBlank(study.getId())) {
            old = this.findById(study.getId());
        }
        BizStudy parent = null;
        if (StringUtils.isNotBlank(req.getParentId())) {
            parent = this.findById(req.getParentId());
        }
        if (study.getOnline() == null) {// 默认启用
            study.setOnline(true);
        }
        if (parent != null) {
            CheckUtil.check(!parent.getId().equals(study.getId()), ResultCode.COMMON_ERROR, "资料父级不能是自己！");
            CheckUtil.check(parent.getType() != null && parent.getType().intValue() == StudyTypeEnum.folder.getKey(), ResultCode.COMMON_ERROR, "资料父级不是文件夹！");
        } else {
            study.setParentId(null);
        }
        List<BizStudy> childList = new ArrayList<BizStudy>();
        if (old == null) {// 新增
            int seq = getMaxSeq();
            seq += 1;
            study.setSeq(seq);
            if (parent == null) {
                study.setPath(LevelUtil.calculateLevel("", seq));
            } else {
                study.setPath(LevelUtil.calculateLevel(parent.getPath(), seq));
            }
        } else {// 修改
            String parentId = StringUtils.isBlank(study.getParentId()) ? "" : study.getParentId();
            String oldParentId = StringUtils.isBlank(old.getParentId()) ? "" : old.getParentId();
            if (!parentId.equals(oldParentId)) {//修改资料父级,重新生成编号
                String newPath = "";
                if (parent != null) {
                    newPath = LevelUtil.calculateLevel(parent.getPath(), old.getSeq());
                } else {
                    newPath = LevelUtil.calculateLevel("", old.getSeq());
                }
                study.setPath(newPath);
                String oldPath = old.getPath();
                List<BizStudy> cList = queryChildsByPath(oldPath);
                if (cList != null && !cList.isEmpty()) {
                    CheckUtil.check(!adjustRootDown(cList, study.getParentId()), ResultCode.COMMON_ERROR, "不允许向该资料子节点调整！");
                    for (BizStudy c : cList) {
                        if (!c.getId().equals(old.getId())) {
                            String cPath = c.getPath();
                            if (cPath.indexOf(oldPath) == 0) {
                                cPath = newPath + cPath.substring(oldPath.length());
                                c.setPath(cPath);
                            }
                            childList.add(c);
                        }
                    }
                }
            } else {
                study.setSeq(old.getSeq());
                study.setPath(old.getPath());
            }
        }
        study = this.saveOrUpdate(study);
        List<String> fileIds = req.getFileIds();
        List<SysFile> sysFiles = new ArrayList<>();
        if (fileIds != null && !fileIds.isEmpty()) {
            List<SysFile> fList = fileService.findByIds(fileIds);
            if (fList != null && !fList.isEmpty()) {
                for (SysFile f : fList) {
                    f.setReferenceId(study.getId());
                }
                fileService.saveAll(sysFiles);
            }
        }
        StudyRes studyRes = BeanUtils.copyProperties(study, StudyRes.class);
        if (StringUtils.isNotBlank(studyRes.getPermissionRange())) {
            String[] memberIds = studyRes.getPermissionRange().split(",");
            studyRes.setPermissionRangeName(memberService.findNamesByIds(Arrays.asList(memberIds)));
        }
        return studyRes;
    }

    /**
     * @param 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: adjustRootDown
     * @Description: 同根资料向下调整，不支持
     */
    private boolean adjustRootDown(List<BizStudy> cList, String parentId) {
        boolean flag = false;
        if (cList == null || cList.isEmpty()) {
            return flag;
        }
        for (BizStudy s : cList) {
            if (s == null) {
                continue;
            }
            if (s.getId().equals(parentId)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public int getMaxSeq() {
        return this.baseRepository.queryMaxSeq();
    }

    @Override
    public List<BizStudy> queryChildsByPath(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        return this.baseRepository.queryChildByPath(path);
    }

    @Override
    public void depthDelete(List<String> ids) {
        CheckUtil.check(ids != null && !ids.isEmpty(), ResultCode.COMMON_ERROR, "请求参数不存在！");
        List<BizStudy> list = this.findByIds(ids);
        List<String> paths = new ArrayList<String>();
        if (list != null && !list.isEmpty()) {
            for (BizStudy s : list) {
                if (s == null || StringUtils.isBlank(s.getPath())) {
                    continue;
                }
                paths.add(s.getPath());
            }
        }
        boolean canDel = checkAndGetDelIds(ids, paths);
        CheckUtil.check(canDel, ResultCode.COMMON_ERROR, "所选文件层级下，存在文件信息，不允许删除！");
        this.batchDelete(ids);
    }

    private boolean checkAndGetDelIds(List<String> ids, List<String> paths) {
        boolean canDel = true;
        if (paths == null || paths.isEmpty()) {
            return canDel;
        }
        PredicateBuilder<BizStudy> builder = Specifications.or();
        for (String path : paths) {
            builder.predicate(Specifications.and()
                    .ne("path", path)
                    .like("path", path + "%")
                    .build());
        }
        List<BizStudy> cList = this.baseRepository.findAll(builder.build());
        if (cList != null && !cList.isEmpty()) {
            for (BizStudy s : cList) {
                if (s == null) {
                    continue;
                }
                if (s.getType() == null || s.getType().intValue() == StudyTypeEnum.document.getKey()) {
                    canDel = false;
                }
                ids.add(s.getId());
            }
        }
        return canDel;
    }

    @Override
    public List<StudyRes> findByParams(StudyQuery query) {
        query = query == null ? new StudyQuery() : query;
        String userId = JwtUtil.getUserId();
        CheckUtil.notEmpty(userId, ResultCode.COMMON_ERROR, "用户未登录！");
        Sort sort = Sort.by(Direction.ASC, "path");
        PredicateBuilder<BizStudy> builder = Specifications.and();
        builder.eq("createBy", userId);
        if (StringUtils.isNotBlank(query.getKeywords())) {
            builder.like("name", "%" + query.getKeywords() + "%");
        }
        if (StringUtils.isBlank(query.getParentId())) {
            builder.isNull("parentId");
        } else {
            builder.eq("parentId", query.getParentId());
        }
        List<BizStudy> list = this.baseRepository.findAll(builder.build(), sort);
        List<String> paths = new ArrayList<String>();
        if (list == null) {
            list = new ArrayList<BizStudy>();
        }
        for (BizStudy s : list) {
            if (s == null || StringUtils.isBlank(s.getPath())) {
                continue;
            }
            paths.add(s.getPath());
        }

        BizStudy parent = null;
        if (StringUtils.isNotBlank(query.getParentId())) {
            parent = this.findById(query.getParentId());
        }

        // 查询共享
        PredicateBuilder<BizStudy> shareBuilder = Specifications.and();
        shareBuilder.eq("online", true);
        if (StringUtils.isNotBlank(query.getKeywords())) {
            shareBuilder.like("name", "%" + query.getKeywords() + "%");
        }
        shareBuilder.predicate(Specifications.or()
                .predicate(Specifications.and()
                        .eq("permissionType", 2)
                        .like("permissionRange", "%" + userId + "%")
                        .build())
                .eq("permissionType", 3)
                .build());
        if (!paths.isEmpty()) {
            for (String path : paths) {
                shareBuilder.notLike("path", path + "%");
            }
        }
        if (parent != null) {
            if (StringUtils.isNotBlank(parent.getPath())) {
                shareBuilder.like("path", parent.getPath() + "%");
            }
            shareBuilder.ne("id", parent.getId());
        }
        List<BizStudy> shareList = this.baseRepository.findAll(shareBuilder.build(), sort);
        List<String> sharePaths = new ArrayList<String>();
        if (shareList != null && !shareList.isEmpty()) {
            for (BizStudy share : shareList) {
                if (share == null) {
                    continue;
                }
                if (!hasPath(sharePaths, share.getPath())) {
                    list.add(share);
                    if (StringUtils.isNotBlank(share.getPath())) {
                        sharePaths.add(share.getPath());
                    }
                }
            }
        }
        for (BizStudy bizStudy : list) {
            String[] split = bizStudy.getLearners().split(",");
            List<String> strings = Arrays.asList(split);
            List<MemberInfoReq> nameById = memberService.findNameByIds(strings);
            bizStudy.setLearnersname(nameById);
            if (StringUtils.isNotBlank(bizStudy.getPermissionRange())) {
                String[] memberIds = bizStudy.getPermissionRange().split(",");
                bizStudy.setPermissionRangeName(memberService.findNamesByIds(Arrays.asList(memberIds)));
            }
        }
        List<StudyRes> resList = BeanUtils.listCopy(list, StudyRes.class);
        return resList;
    }

    private boolean hasPath(List<String> paths, String path) {
        boolean has = false;
        if (paths == null || paths.isEmpty() || StringUtils.isBlank(path)) {
            return has;
        }
        for (String p : paths) {
            if (StringUtils.isBlank(p)) {
                continue;
            }
            if (p.contains(path) || path.contains(p)) {
                has = true;
                break;
            }
        }
        return has;
    }

    @Override
    public Long findCountByName(Specification<BizStudy> specifications) {
        return this.baseRepository.count(specifications);
    }

    public String parserImgFile(String userId, String base64) {
        MultipartFile file = Base64DecodeMultipartFile.base64Convert(base64);
        String s = base64.substring(base64.indexOf("/")+1, base64.indexOf(";base64,"));
        String fileName = String.valueOf(new Date().getTime() / 2)+"."+s;
        SysFile sysFile = fileService.saveFile(file, fileName, userId, 0);
        if (sysFile != null) {
            if (StringUtils.isNotBlank(sysFile.getFileUrl())) {
                return sysFile.getFileUrl();
            }
        }
        return "";
    }
}