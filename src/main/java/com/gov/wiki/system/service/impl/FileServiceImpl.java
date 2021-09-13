/**
 * @Title: FileServiceImpl.java 
 * @Package com.gov.wiki.system.service.impl
 * @Description: 文件管理Service接口实现
 * @author cys 
 * @date 2019年11月5日 下午9:26:06 
 * @version V1.0 
 */
package com.gov.wiki.system.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.DateUtils;
import com.gov.wiki.common.utils.FileUtils;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.common.utils.UUIDLong;
import com.gov.wiki.config.FileConfig;
import com.gov.wiki.system.dao.SysFileDao;
import com.gov.wiki.system.req.FileInfoReq;
import com.gov.wiki.system.req.FileRotateReq;
import com.gov.wiki.system.service.IFileService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl extends BaseServiceImpl<SysFile, String, SysFileDao> implements IFileService {

	@Autowired
	private FileConfig customPro;

	@Override
	public List<SysFile> upload(MultipartFile[] files, String userId, HttpServletRequest request) {
		List<SysFile> entitys = new ArrayList<>();
		String referenceId = getReferenceId(request);
		int maxSortNo = this.baseRepository.queryMaxSortNo(referenceId);
		for (MultipartFile file : files) {
			SysFile sysFile = uploadFiles(userId, file, referenceId, 0);
			if (sysFile == null) {
				continue;
			}
			sysFile.setSortNo(maxSortNo);
			entitys.add(sysFile);
			maxSortNo++;
		}
		entitys = saveAll(entitys);
		return entitys;
	}

	private String getReferenceId(HttpServletRequest request) {
		String id = request.getParameter("referenceId");
		if (StringUtils.isBlank(id)) {
			id = String.valueOf(UUIDLong.longUUID());
		}
		return id;
	}

	private int getRotate(HttpServletRequest request) {
		String rotateStr = request.getParameter("rotate");
		int rotate = 0;
		if (StringUtils.isNotBlank(rotateStr)) {
			rotate = Integer.parseInt(rotateStr);
		}
		return rotate;
	}

	@Override
	public SysFile upload(MultipartFile file, String userId, HttpServletRequest request) {
		String referenceId = getReferenceId(request);
		int rotate = getRotate(request);
		log.info("文件旋转度数：{}", rotate);
		int maxSortNo = this.baseRepository.queryMaxSortNo(referenceId);
		SysFile sysFile = uploadFiles(userId, file, referenceId, rotate);
		if (sysFile != null) {
			sysFile.setSortNo(maxSortNo);
			sysFile = saveOrUpdate(sysFile);
		}
		return sysFile;
	}

	private SysFile uploadFiles(String userId, MultipartFile file, String referenceId, int rotate) {
		String fileName = file.getOriginalFilename();
		log.debug("文件名称:{}", fileName);
		SysFile entityFile = saveFile(file, fileName, userId, rotate);
		if (StringUtils.isBlank(entityFile.getFileUrl())) {// 文件访问路径未生成代表上传失败
			log.info("文件：" + fileName + "上传失败！！！");
			return null;
		}
		entityFile.setReferenceId(referenceId);
		return entityFile;
	}

	public SysFile saveFile(MultipartFile file, String fileName, String userId, int rotate) {
		boolean check = fileName.endsWith(".unknown");
		fileName = check ? fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg" : fileName;
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		String md5 = "";
		String mimeType = check ? "image/jpeg" : file.getContentType();

		try {
			if (rotate > 0 && FileUtils.isImage(mimeType)) {
				BufferedImage src = ImageIO.read(file.getInputStream());
				BufferedImage rs = FileUtils.rotate(src, rotate);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(rs, (suffix.indexOf(".") >= 0 ? suffix.replace(".", "") : suffix), os);
				md5 = DigestUtils.md5DigestAsHex(new ByteArrayInputStream(os.toByteArray()));
			} else {
				md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
			}
		} catch (Exception ex) {
			log.error("获取文件MD5码失败:" + ex.getMessage());
		}

		SysFile entityFile = new SysFile();
		entityFile.setCreateTime(new Date());
		entityFile.setCreateBy(userId);
		entityFile.setFileName(fileName);
		entityFile.setUniqueCode(md5);
		entityFile.setMimeType(mimeType);

		SysFile hasFile = queryByMd5(md5);
		if (hasFile != null) {
			entityFile.setFileUrl(hasFile.getFileUrl());
			entityFile.setFileSize(hasFile.getFileSize());
			entityFile.setThumbnailUrl(hasFile.getThumbnailUrl());
			entityFile.setFileSuffix(hasFile.getFileSuffix());
			entityFile.setMimeType(hasFile.getMimeType());
		} else {

			entityFile.setFileUrl(getFileUrl(System.currentTimeMillis()) + suffix);
			entityFile.setFileSuffix(suffix);
			entityFile.setThumbnailUrl(getThumbnailsUrl(System.currentTimeMillis()));
			saveFile(entityFile, file, fileName, suffix, rotate);

			entityFile.setFileUrl(customPro.getShowPrefix() + entityFile.getFileUrl());
			if (StringUtils.isNotBlank(entityFile.getThumbnailUrl())) {
				entityFile.setThumbnailUrl(customPro.getShowPrefix() + entityFile.getThumbnailUrl());
			}

		}
		return entityFile;
	}

	private String getFileUrl(Long id) {
		String date = DateUtils.getDateFormat("/yyyy/MM/dd/");
		return date + id;
	}

	private String getThumbnailsUrl(Long id) {
		String date = DateUtils.getDateFormat("/yyyy/MM/dd/");
		return date + "thumbnails/" + id + ".jpg";
	}

	public void saveFile(SysFile entityFile, MultipartFile multipartFile, String fileName, String suffix, int rotate) {
		String filePath = customPro.getSavePath() + entityFile.getFileUrl();
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			multipartFile.transferTo(file);
			entityFile.setFileSize(multipartFile.getSize());
			// 需要对图片进行旋转
			if (rotate > 0 && FileUtils.isImage(entityFile.getMimeType())) {
				FileUtils.rotate(file, rotate, suffix);
			}
			createThumbnails(entityFile, filePath);
		} catch (Exception e) {
			log.error("文件上传失败：{}", e);
			entityFile.setFileUrl("");
		}
	}

	/**
	 * @Title: createThumbnails 
	 * @Description: 创建缩略图
	 * @param filePath
	 * @return void 返回类型 
	 * @throws
	 */
	public void createThumbnails(SysFile entityFile, String filePath) {
		try {
			if (StringUtils.isBlank(filePath) || !FileUtils.isImage(entityFile.getMimeType())) {
				entityFile.setThumbnailUrl("");
				return;
			}
			File file = new File(filePath);
			if (!file.exists()) {
				entityFile.setThumbnailUrl("");
				return;
			}
			Float scale = customPro.getScale() == null ? 0.7f : customPro.getScale();
			String thumbnailsPath = customPro.getSavePath() + entityFile.getThumbnailUrl();
			/*
			 * BufferedImage bi = null; bi = Thumbnails.of(file) .scale(scale)
			 * .outputQuality(1.0) .asBufferedImage(); if(bi != null) { ImageIO.write(bi,
			 * suffix, thumbnailsFile); }
			 */
			FileUtils.zipImageFile(filePath, thumbnailsPath, customPro.getWidth(), scale);
		} catch (Exception ex) {
			log.error("转换缩略图失败", ex.getMessage());
			entityFile.setThumbnailUrl("");
		}
	}

	@Override
	public ResultBean sortFiles(List<String> fileIds) {
		List<SysFile> fList = null;
		if (fileIds != null && !fileIds.isEmpty()) {
			fList = this.findByIds(fileIds);
		}
		CheckUtil.check(fList != null && !fList.isEmpty(), ResultCode.COMMON_ERROR, "待排序文件不存在！");
		List<String> hasIds = new ArrayList<String>();
		Map<String, SysFile> fMap = new HashMap<String, SysFile>();
		for (SysFile f : fList) {
			if (f == null || StringUtils.isBlank(f.getId())) {
				continue;
			}
			fMap.put(f.getId(), f);
			if (StringUtils.isNotBlank(f.getReferenceId()) && !hasIds.contains(f.getReferenceId())) {
				hasIds.add(f.getReferenceId());
			}
		}
		CheckUtil.check(hasIds.size() == 1, ResultCode.COMMON_ERROR, "待排序文件不是同一组文件！");
		int sortNo = 1;
		List<SysFile> needList = new ArrayList<SysFile>();
		for (String fileId : fileIds) {
			if (StringUtils.isBlank(fileId) || fMap.get(fileId) == null) {
				continue;
			}
			SysFile f = fMap.get(fileId);
			f.setSortNo(sortNo);
			needList.add(f);
			sortNo++;
		}
		this.saveAll(needList);
		return new ResultBean();
	}

	@Override
	public ResultBean changeFileInfo(ReqBean<FileInfoReq> bean) {
		FileInfoReq req = bean == null ? null : bean.getBody();
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "参数不存在！");
		CheckUtil.notEmpty(req.getFileId(), ResultCode.COMMON_ERROR, "文件ID参数不存在！");
		SysFile file = this.findById(req.getFileId());
		CheckUtil.notNull(file, ResultCode.COMMON_ERROR, "待调整文件不存在！");
		file.setAlias(req.getAlias());
		this.saveOrUpdate(file);
		return new ResultBean();
	}

	@Override
	public ResultBean<String> queryReferenceId() {
		return new ResultBean<String>(createReferenceId());
	}

	/**
	 * @Title: createReferenceId @Description: 迭代创建唯一关联ID @return String
	 *         返回类型 @throws
	 */
	private String createReferenceId() {
		String referenceId = String.valueOf(UUIDLong.longUUID());
		List<SysFile> fileList = this.baseRepository.findByReferenceId(referenceId);
		if (fileList == null || fileList.isEmpty()) {
			return referenceId;
		} else {
			return createReferenceId();
		}
	}

	@Override
	public SysFile queryByMd5(String md5) {
		SysFile file = null;
		if (StringUtils.isBlank(md5)) {
			return file;
		}
		List<SysFile> fList = this.baseRepository.findByUniqueCode(md5);
		if (fList != null && !fList.isEmpty()) {
			for (SysFile f : fList) {
				if (f == null || StringUtils.isBlank(f.getFileUrl())) {
					continue;
				}
				String filePath = f.getFileUrl();
				if (filePath.indexOf(customPro.getShowPrefix()) >= 0) {
					filePath = filePath.replace(customPro.getShowPrefix(), "");
				}
				filePath = customPro.getSavePath() + filePath;
				File realFile = new File(filePath);
				if (realFile.exists() && realFile.isFile()) {
					file = f;
					break;
				}
			}
		}
		return file;
	}

	@Override
	public ResultBean rotateFile(FileRotateReq req) throws IOException {
		String fileId = req.getFileId();
		int rotate = req.getRotate();
		SysFile file = null;
		if (StringUtils.isNotBlank(fileId)) {
			file = this.findById(fileId);
		}
		boolean exists = false;
		File realFile = null;
		if (file != null && StringUtils.isNotBlank(file.getFileName()) && StringUtils.isNotBlank(file.getFileUrl())
				&& FileUtils.isImage(file.getMimeType())) {

			String filePath = file.getFileUrl();
			if (filePath.indexOf(customPro.getShowPrefix()) >= 0) {
				filePath = filePath.replace(customPro.getShowPrefix(), "");
			}
			filePath = customPro.getSavePath() + filePath;
			realFile = new File(filePath);
			if (realFile.exists() && realFile.isFile()) {
				exists = true;
			}
		}
		CheckUtil.check(exists, ResultCode.COMMON_ERROR, "待旋转文件不存在或不是图片！");
		if (rotate > 0 && realFile != null) {
			FileUtils.rotate(realFile, rotate, file.getFileSuffix());
			String filePath = file.getThumbnailUrl();
			if (StringUtils.isNotBlank(filePath) && filePath.indexOf(customPro.getShowPrefix()) >= 0) {
				filePath = filePath.replace(customPro.getShowPrefix(), "");
			}
			filePath = customPro.getSavePath() + filePath;
			File thumbnailFile = new File(filePath);
			if (thumbnailFile.exists() && thumbnailFile.isFile()) {
				FileUtils.rotate(thumbnailFile, rotate, "jpg");
			}

		}
		return new ResultBean();
	}

	@Override
	public ResultBean deleteFile(String fileId) {
		if (StringUtils.isNotBlank(fileId)) {
			this.deleteById(fileId);
		}
		return new ResultBean();
	}

	@Override
	public List<SysFile> findByReferenceId(String referenceId) {
		return this.baseRepository.findByReferenceId(referenceId);
	}

	@Override
	public List<SysFile> findByReferenceIds(List<String> referenceIds) {
		return this.baseRepository.findByReferenceIds(referenceIds);
	}
}