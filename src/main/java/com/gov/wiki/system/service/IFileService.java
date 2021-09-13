/**
 * @Title: IFileService.java 
 * @Package com.gov.wiki.system.service
 * @Description: 文件处理service接口
 * @author cys 
 * @date 2019年11月5日 下午9:07:34 
 * @version V1.0 
 */
package com.gov.wiki.system.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.web.multipart.MultipartFile;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.system.req.FileInfoReq;
import com.gov.wiki.system.req.FileRotateReq;

public interface IFileService extends IBaseService<SysFile, String>{
	
	/**
	 * 多个文件上传
	 * @param files
	 * @param userId
	 * @param request 
	 * @return
	 */
	List<SysFile> upload(MultipartFile[] files,String userId, HttpServletRequest request);
	
	/**
	 * 单个文件上传
	 * @param file
	 * @param userId
	 * @return
	 */
	SysFile upload(MultipartFile file,String userId,HttpServletRequest request);


	SysFile saveFile(MultipartFile file, String fileName, String userId, int rotate);
	
	/**
	 * 
	 * @Title: sortFiles
	 * @Description: 文件排序
	 * @param fileIds
	 * @return ResultBean 返回类型
	 * @throws
	 */
	@Transactional
	ResultBean sortFiles(List<String> fileIds);
	
	/**
	 * @Title: changeFileInfo
	 * @Description: 调整文件信息
	 * @param bean
	 * @return ResultBean 返回类型
	 * @throws
	 */
	@Transactional
	ResultBean changeFileInfo(ReqBean<FileInfoReq> bean);
	
	/**
	 * @Title: queryReferenceId
	 * @Description: 创建文件关联ID
	 * @return ResultBean<String> 返回类型
	 * @throws
	 */
	ResultBean<String> queryReferenceId();
	
	/**
	 * @Title: queryByMd5
	 * @Description: 根据md5码查询文件
	 * @param md5
	 * @return SysFile 返回类型
	 * @throws
	 */
	SysFile queryByMd5(String md5);
	
	/**
	 * @Title: rotateFile
	 * @Description: 文件旋转
	 * @param req
	 * @return ResultBean 返回类型
	 * @throws
	 */
	ResultBean rotateFile(FileRotateReq req) throws IOException;
	
	/**
	 * @Title: deleteFile
	 * @Description: 根据ID删除文件信息
	 * @param fileId
	 * @return ResultBean 返回类型
	 * @throws
	 */
	ResultBean deleteFile(String fileId);

	List<SysFile> findByReferenceId(String referenceId);

	List<SysFile> findByReferenceIds(List<String> referenceIds);
}