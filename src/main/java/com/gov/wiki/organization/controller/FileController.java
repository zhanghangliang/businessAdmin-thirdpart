package com.gov.wiki.organization.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.FileUtils;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.config.FileConfig;
import com.gov.wiki.system.req.FileInfoReq;
import com.gov.wiki.system.req.FileRotateReq;
import com.gov.wiki.system.service.IFileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

/**
 * @ClassName: FileController
 * @Description: 文件管理控制器
 * @author cys
 * @date 2019年12月10日
 */
@Slf4j
@RestController
@RequestMapping(value = "/file")
@Api(tags = "文件管理")
public class FileController {

	/**
	 * 注入fileService
	 */
	@Autowired
	private IFileService fileService;

	@Autowired
	private FileConfig customPro;

	/**
	 * 上传多个文件
	 * 
	 * @param files
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/uploads")
	@ApiOperation(value = "多文件上传")
	@ControllerMonitor(description = "多文件上传", operType = 2)
	public ResultBean<List<SysFile>> files(@RequestParam(value = "files") MultipartFile[] files,
			HttpServletRequest request) {
		// 获取用户信息
		String userId = JwtUtil.getUserId();
		List<SysFile> sysFiles = fileService.upload(files, userId, request);
		return new ResultBean<List<SysFile>>(sysFiles);
	}

	/**
	 * 上传一个文件
	 * 
	 * @param files
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/upload")
	@ApiOperation(value = "单文件上传")
	@ControllerMonitor(description = "单文件上传", operType = 2)
	public ResultBean<SysFile> upload(@RequestParam(value = "files") MultipartFile files, HttpServletRequest request) {
		// 获取用户信息
		String userId = JwtUtil.getUserId();
		SysFile sysFiles = fileService.upload(files, userId, request);
		CheckUtil.notNull(sysFiles, ResultCode.COMMON_ERROR, "文件上传失败！！！");
		return new ResultBean<SysFile>(sysFiles);
	}

	/**
	 * @Title: findById @Description: 根据ID查询文件信息 @param bean @return @return
	 * ResultBean<SysFile> 返回类型 @throws
	 */
	@PostMapping(value = "/findById")
	@ApiOperation(value = "根据文件ID查询文件信息")
	@ControllerMonitor(description = "根据文件ID查询文件信息")
	public ResultBean<SysFile> findById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "文件ID");
		return new ResultBean<SysFile>(fileService.findById(id));
	}

	/**
	 * 删除
	 * 
	 * @param bean
	 * @return
	 */
	@PostMapping(value = "/deleteById")
	@ApiOperation(value = "删除文件")
	@ControllerMonitor(description = "删除文件", operType = 4)
	public ResultBean<String> deleteById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "文件ID");
		return fileService.deleteFile(id);
	}

	/**
	 * @Title: findByReferenceId @Description: 根据关联ID查询文件信息 @param
	 * bean @return @return ResultBean<List<SysFile>> 返回类型 @throws
	 */
	@PostMapping(value = "/findByReferenceId")
	@ApiOperation(value = "根据关联ID查询文件信息")
	@ControllerMonitor(description = "根据关联ID查询文件信息")
	public ResultBean<List<SysFile>> findByReferenceId(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "文件ID");
		SysFile file = new SysFile();
		file.setReferenceId(id);
		return new ResultBean<List<SysFile>>(fileService.findAll(Example.of(file)));
	}

	/**
	 * @Title: download @Description: 文件下载 @param fileId @param request @param
	 * response @return ResultBean 返回类型 @throws
	 */
	@GetMapping(value = "/download")
	@ApiOperation(value = "文件下载")
	@ControllerMonitor(description = "文件下载")
	public ResultBean download(@RequestParam(value = "fileId", required = true) String fileId,
			HttpServletRequest request, HttpServletResponse response) {
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		SysFile fileEntity = fileService.findById(fileId);
		CheckUtil.notNull(fileEntity, ResultCode.DATA_NOT_EXIST, "文件");
		String fileUrl = fileEntity.getFileUrl();
		CheckUtil.notEmpty(fileUrl, ResultCode.DATA_NOT_EXIST, "文件访问路径");
		if (StringUtils.isNotBlank(fileUrl) && fileUrl.indexOf(customPro.getShowPrefix()) != -1) {
			fileUrl = fileUrl.replace(customPro.getShowPrefix(), "");
		}
		File file = new File(customPro.getSavePath() + fileUrl);
		try {
			String filename = fileEntity.getFileName();
			// 获得请求头中的User-Agent
			String agent = request.getHeader("User-Agent").toUpperCase();
			// 解决ie下载时文件名乱码的问题
			if (agent.contains("MSIE") || agent.contains("TRIDENT") || agent.contains("EDGE")) {// 判断是否是ie浏览器
				filename = URLEncoder.encode(filename, "utf-8");
			} else {
				filename = new String(filename.getBytes(), "iso-8859-1");
			}
			String mimeType = StringUtils.isBlank(fileEntity.getMimeType()) ? "application/octet-stream"
					: fileEntity.getMimeType();
			response.setHeader("content-type", mimeType);
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			os = response.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(file));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, i);
				i = bis.read(buff);
			}
			bis.close();
		} catch (Exception ex) {
			log.error("文件下载失败：", ex);
			CheckUtil.check(false, ResultCode.COMMON_ERROR, "文件下载失败:" + ex.getMessage());
		} finally {
			try {
				if (bis != null) {
					bis.close();
					bis = null;
				}
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
		}
		return null;
	}

	/**
	 * @Title: thumbnail @Description: 对图片生成缩略图 @param fileId @param width @param
	 * height @param scale @param keepAspectRatio @return String 返回类型 @throws
	 */
	@ApiOperation(value = "对图片生成缩略图", notes = "提供对图片文件生成缩略图接口功能")
	@ApiImplicitParams({ @ApiImplicitParam(name = "fileId", value = "文件ID", required = true, paramType = "query"),
			@ApiImplicitParam(name = "img-width", value = "缩略图宽度", required = false, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "img-heigth", value = "缩略图高度", required = false, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "img-scale", value = "缩略图缩放比例", required = false, paramType = "query", dataType = "Double"),
			@ApiImplicitParam(name = "keepAspectRatio", value = "是否按缩放比例生成缩略图", required = false, paramType = "query", dataType = "Boolean") })
	@ControllerMonitor(description = "获取图片缩略图")
	@GetMapping("/thumbnail")
	public String thumbnail(@RequestParam(value = "fileId", required = true) String fileId,
			@RequestParam(value = "img-width", required = false, defaultValue = "200") Integer width,
			@RequestParam(value = "img-heigth", required = false, defaultValue = "200") Integer height,
			@RequestParam(value = "img-scale", required = false, defaultValue = "0.25") Double scale,
			@RequestParam(value = "keepAspectRatio", required = false, defaultValue = "true") Boolean keepAspectRatio) {
		String result = "";
		ByteArrayOutputStream baos = null;
		try {
			if (StringUtils.isBlank(customPro.getSavePath())) {
				return result;
			}
			SysFile fileEntity = fileService.findById(fileId);
			if (fileEntity == null || StringUtils.isBlank(fileEntity.getFileUrl())) {
				return result;
			}
			if (!FileUtils.isImage(fileEntity.getMimeType())) {
				return result;
			}
			String fileUrl = fileEntity.getFileUrl();
			if (StringUtils.isNotBlank(fileUrl) && fileUrl.indexOf(customPro.getShowPrefix()) != -1) {
				fileUrl = fileUrl.replace(customPro.getShowPrefix(), "");
			}
			String suffix = fileEntity.getFileSuffix();
			suffix = StringUtils.isBlank(suffix) ? "jpg" : suffix.replace(".", "");
			BufferedImage bi = null;
			if (keepAspectRatio) {
				bi = Thumbnails.of(customPro.getSavePath() + fileUrl).scale(scale).keepAspectRatio(true)
						.asBufferedImage();
			} else {
				bi = Thumbnails.of(customPro.getSavePath() + fileUrl).size(width, height).keepAspectRatio(false)
						.asBufferedImage();
			}
			if (bi != null) {
				baos = new ByteArrayOutputStream();
				ImageIO.write(bi, suffix, baos);
				String base64Img = Base64Utils.encodeToString(baos.toByteArray());
				result = "data:image/jpg;base64," + base64Img.toString();
				baos.close();
			}
		} catch (Exception ex) {
			log.error("转换缩略图失败", ex.getMessage());
			result = "";
		} finally {
			try {
				if (baos != null) {
					baos.close();
					baos = null;
				}
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
		}
		return result;
	}

	/**
	 * @Title: sortFiles @Description: 文件重新排序 @param bean @return ResultBean
	 * 返回类型 @throws
	 */
	@PostMapping(value = "/sortFiles")
	@ApiOperation(value = "文件重新排序")
	@ControllerMonitor(description = "文件重新排序", operType = 3)
	public ResultBean sortFiles(@RequestBody ReqBean<List<String>> bean) {
		CheckUtil.notNull(bean, ResultCode.COMMON_ERROR, "参数不存在！");
		return this.fileService.sortFiles(bean.getBody());
	}

	/**
	 * @throws IOException @Title: rotateFile @Description: 文件旋转 @param bean @return
	 * ResultBean 返回类型 @throws
	 */
	@PostMapping(value = "/rotateFile")
	@ApiOperation(value = "文件旋转")
	@ControllerMonitor(description = "文件旋转", operType = 3)
	public ResultBean rotateFile(@RequestBody ReqBean<FileRotateReq> bean) throws IOException {
		FileRotateReq req = bean != null ? bean.getBody() : null;
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "参数不存在！");
		return this.fileService.rotateFile(req);
	}

	/**
	 * @Title: changeFileInfo @Description: 文件信息调整 @param bean @return ResultBean
	 * 返回类型 @throws
	 */
	@PostMapping(value = "/changeFileInfo")
	@ApiOperation(value = "文件信息调整")
	@ControllerMonitor(description = "文件信息调整", operType = 3)
	public ResultBean changeFileInfo(@RequestBody ReqBean<FileInfoReq> bean) {
		return this.fileService.changeFileInfo(bean);
	}

	/**
	 * @Title: queryReferenceId @Description: 创建文件关联ID @return ResultBean
	 * 返回类型 @throws
	 */
	@PostMapping(value = "/queryReferenceId")
	@ApiOperation(value = "创建文件关联ID")
	@ControllerMonitor(description = "创建文件关联ID")
	public ResultBean<String> queryReferenceId() {
		return this.fileService.queryReferenceId();
	}
}