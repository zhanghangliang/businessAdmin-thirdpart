/**
 * @Title: WordUtils.java
 * @Package com.xiangtong.common.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年1月8日
 * @version V1.0
 */
package com.gov.wiki.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.gov.wiki.common.beans.ResultCode;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: WordUtils
 * @Description: WORD操作工具类
 * @author cys
 * @date 2020年1月8日
 */
@Slf4j
public class WordUtils {
	
	public static void main(String[] args) {
		String source = "F:/test.docx";
		String target = "F:/结果.docx";
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("${name}", "张三");
		dataMap.put("${phone}", "13526232653");
		dataMap.put("${buss_situation}", "良好");
		dataMap.put("${buss_year}", "5年");
		dataMap.put("${amount}", "30万");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> imgMap = new HashMap<String, String>();
		imgMap.put("url", "F:/qrcode.png");
		imgMap.put("imgType", "png");
		imgMap.put("name", "qrcode.png");
		list.add(imgMap);
		imgMap = new HashMap<String, String>();
		imgMap.put("url", "F:/qrcode.png");
		imgMap.put("imgType", "png");
		imgMap.put("name", "qrcode.png");
		list.add(imgMap);
		dataMap.put("${idcard_attach}", list);
		createWord(source, target, dataMap);
	}
	
	/**
	 * @Title: createAndDownloadWord
	 * @Description:  创建并下载word文件
	 * @param source
	 * @param dataMap
	 * @param response
	 * @return void 返回类型
	 * @throws
	 */
	public static void createAndDownloadWord(String source, String fileName, Map<String, Object> dataMap, HttpServletRequest request, HttpServletResponse response) {
		if(StringUtils.isBlank(source) || response == null) {
			log.info("文档生成参数异常");
			CheckUtil.check(false, ResultCode.COMMON_ERROR, "资料文档生成参数异常！");
			return;
		}
		dataMap = dataMap == null?new HashMap<String, Object>():dataMap;
		File sourceFile = new File(source);
		if(!sourceFile.exists()) {
			log.info("模板文件不存在");
			CheckUtil.check(false, ResultCode.COMMON_ERROR, "资料模板文件不存在！");
			return;
		}
		XWPFDocument doc;
		OutputStream os = null;
		String cellTxt = "";
		try {
			doc = new XWPFDocument(new FileInputStream(sourceFile));
			for(XWPFTable table : doc.getTables()) {
				for(XWPFTableRow row : table.getRows()) {
					for(XWPFTableCell cell : row.getTableCells()) {//遍历每一个单元格
						cellTxt = cell.getText() != null?cell.getText().trim():"";
						//log.info("cellTxt:{},paragraph:{}", cellTxt, cell.getParagraphs());
						if(StringUtils.isBlank(cellTxt) || !cellTxt.startsWith("${") || !cellTxt.endsWith("}")) {
							continue;
						}
						cell.removeParagraph(0);
						if(dataMap.containsKey(cellTxt)) {
							Object obj = dataMap.get(cellTxt);
							if(obj instanceof String) {
								cell.setText(String.valueOf(obj));
							}else if(obj instanceof List){
								List<Map<String, String>> list = (List) obj;
								int num = 0;
								for(Map<String, String> m:list) {
									if(m == null || m.isEmpty()) {
										continue;
									}
									String fileUrl = m.get("url");
									String imgType = m.get("imgType");
									String name = m.get("name");
									int width = StringUtils.isBlank(m.get("width"))?300:Integer.parseInt(m.get("width"));
									int height = StringUtils.isBlank(m.get("height"))?300:Integer.parseInt(m.get("height"));
									if(StringUtils.isBlank(fileUrl) || StringUtils.isBlank(name)) {
										continue;
									}
									File addFile = new File(fileUrl);
									if(!addFile.exists()) {
										continue;
									}
									if(cell.getParagraphArray(num) == null) {
										cell.addParagraph();
									}
									XWPFParagraph p = cell.getParagraphArray(num);
									XWPFRun r = p.createRun();
									FileInputStream is = new FileInputStream(addFile);
									r.addPicture(is, getPictureType(imgType), name, Units.toEMU(width), Units.toEMU(height));
									num++;
								}
							}
						}else {
							cell.setText("");
						}
					}
				}
			}
			// 获得请求头中的User-Agent
			String agent = request.getHeader("User-Agent").toUpperCase();
			//解决ie下载时文件名乱码的问题
			if (agent.contains("MSIE") || agent.contains("TRIDENT") || agent.contains("EDGE")) {//判断是否是ie浏览器
				fileName = URLEncoder.encode(fileName, "utf-8");
			}else{
				fileName = new String(fileName.getBytes(), "iso-8859-1");
			}
			response.setHeader("content-type", "application/octet-stream");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			os = response.getOutputStream();
			doc.write(os);
			os.close();
		} catch (Exception e) {
			log.error("异常:{}", e);
		}finally {
			try {
				if(os != null) {
					os.close();
					os = null;
				}
			}catch(Exception ex) {
				log.error("异常:{}", ex);
			}
		}
	}
	
	/**
	 * @Title: createWordFile
	 * @Description: 创建word文档
	 * @param source
	 * @param fileName
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return void 返回类型
	 * @throws
	 */
	public static void createWordFile(String source, String dest, Map<String, Object> dataMap) {
		if(StringUtils.isBlank(source) || StringUtils.isBlank(dest)) {
			log.info("文档生成参数异常");
			return;
		}
		dataMap = dataMap == null?new HashMap<String, Object>():dataMap;
		File sourceFile = new File(source);
		if(!sourceFile.exists()) {
			log.info("模板文件不存在");
			return;
		}
		File destFile = new File(dest);
		if(!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		XWPFDocument doc;
		OutputStream os = null;
		String cellTxt = "";
		try {
			doc = new XWPFDocument(new FileInputStream(sourceFile));
			for(XWPFTable table : doc.getTables()) {
				for(XWPFTableRow row : table.getRows()) {
					for(XWPFTableCell cell : row.getTableCells()) {//遍历每一个单元格
						cellTxt = cell.getText() != null?cell.getText().trim():"";
						//log.info("cellTxt:{},paragraph:{}", cellTxt, cell.getParagraphs());
						if(StringUtils.isBlank(cellTxt) || !cellTxt.startsWith("${") || !cellTxt.endsWith("}")) {
							continue;
						}
						cell.removeParagraph(0);
						if(dataMap.containsKey(cellTxt)) {
							Object obj = dataMap.get(cellTxt);
							if(obj instanceof String) {
								cell.setText(String.valueOf(obj));
							}else if(obj instanceof List){
								List<Map<String, String>> list = (List) obj;
								int num = 0;
								for(Map<String, String> m:list) {
									if(m == null || m.isEmpty()) {
										continue;
									}
									String fileUrl = m.get("url");
									String imgType = m.get("imgType");
									String name = m.get("name");
									int width = StringUtils.isBlank(m.get("width"))?300:Integer.parseInt(m.get("width"));
									int height = StringUtils.isBlank(m.get("height"))?300:Integer.parseInt(m.get("height"));
									if(StringUtils.isBlank(fileUrl) || StringUtils.isBlank(name)) {
										continue;
									}
									File addFile = new File(fileUrl);
									if(!addFile.exists()) {
										continue;
									}
									if(cell.getParagraphArray(num) == null) {
										cell.addParagraph();
									}
									XWPFParagraph p = cell.getParagraphArray(num);
									XWPFRun r = p.createRun();
									FileInputStream is = new FileInputStream(addFile);
									r.addPicture(is, getPictureType(imgType), name, Units.toEMU(width), Units.toEMU(height));
									num++;
								}
							}
						}else {
							cell.setText("");
						}
					}
				}
			}
			os = new FileOutputStream(destFile);
			doc.write(os);
			os.close();
		} catch (Exception e) {
			log.error("异常:{}", e);
		}finally {
			try {
				if(os != null) {
					os.close();
					os = null;
				}
			}catch(Exception ex) {
				log.error("异常:{}", ex);
			}
		}
	}

	/**
	 * @Title: createWord
	 * @Description: 创建word文件
	 * @param source
	 * @param target
	 * @param dataMap
	 * @return void 返回类型
	 * @throws
	 */
	public static void createWord(String source, String target, Map<String, Object> dataMap) {
		if(StringUtils.isBlank(source) || StringUtils.isBlank(target)) {
			log.info("文档生成参数异常");
			return;
		}
		dataMap = dataMap == null?new HashMap<String, Object>():dataMap;
		File sourceFile = new File(source);
		if(!sourceFile.exists()) {
			log.info("模板文件不存在");
			return;
		}
		XWPFDocument doc;
		FileOutputStream fos = null;
		String cellTxt = "";
		try {
			File targetFile = new File(target);
			if(!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			if(!targetFile.exists()) {
				targetFile.createNewFile();
			}
			doc = new XWPFDocument(new FileInputStream(sourceFile));
			for(XWPFTable table : doc.getTables()) {
				for(XWPFTableRow row : table.getRows()) {
					for(XWPFTableCell cell : row.getTableCells()) {//遍历每一个单元格
						cellTxt = cell.getText() != null?cell.getText().trim():"";
						log.info("cellTxt:{},paragraph:{}", cellTxt, cell.getParagraphs());
						if(StringUtils.isBlank(cellTxt) || !cellTxt.startsWith("${") || !cellTxt.endsWith("}")) {
							continue;
						}
						cell.removeParagraph(0);
						if(dataMap.containsKey(cellTxt)) {
							Object obj = dataMap.get(cellTxt);
							if(obj instanceof String) {
								cell.setText(String.valueOf(obj));
							}else if(obj instanceof List){
								List<Map<String, String>> list = (List) obj;
								int num = 0;
								for(Map<String, String> m:list) {
									if(m == null || m.isEmpty()) {
										continue;
									}
									String fileUrl = m.get("url");
									String imgType = m.get("imgType");
									String name = m.get("name");
									int width = StringUtils.isBlank(m.get("width"))?300:Integer.parseInt(m.get("width"));
									int height = StringUtils.isBlank(m.get("height"))?300:Integer.parseInt(m.get("height"));
									if(StringUtils.isBlank(fileUrl) || StringUtils.isBlank(name)) {
										continue;
									}
									File addFile = new File(fileUrl);
									if(!addFile.exists()) {
										continue;
									}
									if(cell.getParagraphArray(num) == null) {
										cell.addParagraph();
									}
									XWPFParagraph p = cell.getParagraphArray(num);
									XWPFRun r = p.createRun();
									FileInputStream is = new FileInputStream(addFile);
									r.addPicture(is, getPictureType(imgType), name, Units.toEMU(width), Units.toEMU(height));
									num++;
								}
							}
						}else {
							cell.setText("");
						}
					}
				}
			}
			
			fos = new FileOutputStream(targetFile);
			doc.write(fos);
			fos.close();  
		} catch (Exception e) {
			log.error("异常:{}", e);
		}finally {
			try {
				if(fos != null) {
					fos.close();
					fos = null;
				}
			}catch(Exception ex) {
				log.error("异常:{}", ex);
			}
		}
	}
	
	private static int getPictureType(String picType) {
        int res = XWPFDocument.PICTURE_TYPE_PICT;
        if (StringUtils.isNotBlank(picType)) {
        	picType = picType.toLowerCase();
            if (picType.endsWith("png")) {
                res = XWPFDocument.PICTURE_TYPE_PNG;
            } else if (picType.endsWith("dib")) {
                res = XWPFDocument.PICTURE_TYPE_DIB;
            } else if (picType.endsWith("emf")) {
                res = XWPFDocument.PICTURE_TYPE_EMF;
            } else if (picType.endsWith("jpg") || picType.endsWith("jpeg")) {
                res = XWPFDocument.PICTURE_TYPE_JPEG;
            } else if (picType.endsWith("wmf")) {
                res = XWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }
}