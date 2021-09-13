/**
 * @Title: FileUtils.java
 * @Package com.xiangtong.common.utils
 * @Description: 文件处理工具类
 * @author cys
 * @date 2019年12月22日
 * @version V1.0
 */
package com.gov.wiki.common.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Element;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {
	/**
	 * 得到图片字节流 数组大小
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}
	
	/**
     * 创建文件
     * @param path  全路径 指向文件
     * @return
     */
    public static boolean makeFile(String path) {  
        File file = new File(path);  
        if(file.exists()) {  
            log.info("文件已存在,直接覆盖");
            return true;  
        }  
        if (path.endsWith(File.separator)) {  
            log.info("不能为目录！");  
            return false;  
        }  
        if(!file.getParentFile().exists()) {   
            if(!file.getParentFile().mkdirs()) {  
                log.info("创建目标文件所在目录失败！");  
                return false;  
            }  
        }  
        try {  
            if (file.createNewFile()) {  
                log.info("创建文件" + path + "成功！");  
                return true;  
            } else {  
                log.info("创建文件" + path + "失败！");  
                return false;  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
            log.info("创建文件" + path + "失败！" + e.getMessage());  
            return false;  
        }  
    }
    
    /**
     * 输入流写入文件
     * 
     * @param is
     *            输入流
     * @param filePath
     *            文件保存目录路径
     * @throws IOException
     */
    public static void write2File(InputStream is, String filePath) throws IOException {
        OutputStream os = new FileOutputStream(filePath);
        int len = 8192;
        byte[] buffer = new byte[len];
        while ((len = is.read(buffer, 0, len)) != -1) {
            os.write(buffer, 0, len);
        }
        os.close();
        is.close();
    }
	
	public static void copyFile(String path,String newpath) {
		try {
            //创建新文件
            makeFile(newpath);
            //获取文件流
            InputStream in = new FileInputStream(path);
            //将流写入新文件
            write2File(in, newpath);
        } catch (IOException e) {
        	log.error("从 {} 到 {} 复制文件失败,message:{}",path,newpath,e.getMessage());
        }
	}

	/**
	 * 将文件转换成Byte数组
	 *
	 * @param file
	 * @return
	 */
	public static byte[] getBytesByFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			byte[] data = bos.toByteArray();
			bos.close();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * MultipartFile转File
	 *
	 * @param param
	 * @return
	 */
	public static File transfer(MultipartFile param) {
		if (!param.isEmpty()) {
			File file = null;
			try {
				InputStream in = param.getInputStream();
				file = new File(param.getOriginalFilename());
				OutputStream out = new FileOutputStream(file);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				in.close();
				out.close();
				return file;
			} catch (Exception e) {
				e.printStackTrace();
				return file;
			}
		}
		return null;
	}

	/**
	 * 获取指定文件的输入流
	 *
	 * @param logoPath 文件的路径
	 * @return
	 */
	public static InputStream getResourceAsStream(String logoPath) {
		return FileUtils.class.getResourceAsStream(logoPath);
	}

	/**
	 * 将InputStream写入到File中
	 *
	 * @param ins
	 * @param file
	 * @throws IOException
	 */
	public void inputstreamtofile(InputStream ins, File file) throws IOException {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}

	/**
	 * @Title: isImage @Description: 判断是否图片 @param mimeType @return boolean
	 * 返回类型 @throws
	 */
	public static boolean isImage(String mimeType) {
		boolean isImage = false;
		if (StringUtils.isNotBlank(mimeType)) {
			String type = mimeType.split("/")[0];
			isImage = type.toLowerCase().equals("image");
		}
		return isImage;
	}

	public static void delAllDepthFile(File file) {
		if (file == null || !file.exists()) {
			return;
		}

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File child : files) {
				delAllDepthFile(child);
			}
		}
		file.delete();
	}

	public static void zipImageFile(String source, String dest, int width, float per) throws IOException {
		if (StringUtils.isBlank(source) || StringUtils.isBlank(dest)) {
			return;
		}
		File sourceFile = new File(source);
		if (!sourceFile.exists() || !sourceFile.isFile()) {
			return;
		}
		File destFile = new File(dest);
		if (!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		Image src = ImageIO.read(sourceFile);
		int height = (int) ((double) src.getHeight(null) / (((double) src.getWidth(null)) / width));
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(src.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
		FileOutputStream out = new FileOutputStream(destFile);
		saveAsJPEG(100, tag, per, out);
		out.close();
	}

	/**
	 * 以JPEG编码保存图片
	 * 
	 * @param dpi             分辨率
	 * @param image_to_save   要处理的图像图片
	 * @param JPEGcompression 压缩比
	 * @param fos             文件输出流
	 * @throws IOException
	 */
	public static void saveAsJPEG(Integer dpi, BufferedImage tag, float JPEGcompression, FileOutputStream fos)
			throws IOException {
		JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpg").next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
		imageWriter.setOutput(ios);
		IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(tag), null);
		if (dpi != null) {
			Element tree = (Element) imageMetaData.getAsTree("javax_imageio_jpeg_image_1.0");
			Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
			jfif.setAttribute("Xdensity", Integer.toString(dpi));
			jfif.setAttribute("Ydensity", Integer.toString(dpi));
		}
		if (JPEGcompression >= 0 && JPEGcompression <= 1f) {
			JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
			jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
			jpegParams.setCompressionQuality(JPEGcompression);
		}
		imageWriter.write(imageMetaData, new IIOImage(tag, null, null), null);
		ios.close();
		imageWriter.dispose();
	}

	/**
	 * 对图片进行旋转
	 * @param src   被旋转图片
	 * @param angel 旋转角度
	 * @return 旋转后的图片
	 */
	public static BufferedImage rotate(Image src, int angel) {
		int src_width = src.getWidth(null);
		int src_height = src.getHeight(null);
		// 计算旋转后图片的尺寸
		Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
		BufferedImage res = null;
		res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = res.createGraphics();
		// 进行转换
		g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
		g2.drawImage(src, null, null);
		return res;
	}
	
	/**
	 * @Title: rotate
	 * @Description: 图片文件旋转
	 * @param file
	 * @param angel
	 * @param suffix
	 * @throws IOException
	 * @return void 返回类型
	 * @throws
	 */
	public static void rotate(File file, int angel, String suffix) throws IOException {
		if(file == null || !file.exists() || !file.isFile() || StringUtils.isBlank(suffix)) {
			return;
		}
		suffix = suffix.indexOf(".") >= 0?suffix.replace(".", ""):suffix;
		BufferedImage src = ImageIO.read(file);
		BufferedImage rs = FileUtils.rotate(src, angel);
		ImageIO.write(rs, suffix, file);
	}

	/**
	 * 计算旋转后的图片
	 *
	 * @param src   被旋转的图片
	 * @param angel 旋转角度
	 * @return 旋转后的图片
	 */
	public static Rectangle calcRotatedSize(Rectangle src, int angel) {
		// 如果旋转的角度大于90度做相应的转换
		if (angel >= 90) {
			if (angel / 90 % 2 == 1) {
				int temp = src.height;
				src.height = src.width;
				src.width = temp;
			}
			angel = angel % 90;
		}

		double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
		double angel_dalta_width = Math.atan((double) src.height / src.width);
		double angel_dalta_height = Math.atan((double) src.width / src.height);
		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
		int des_width = src.width + len_dalta_width * 2;
		int des_height = src.height + len_dalta_height * 2;
		return new Rectangle(new Dimension(des_width, des_height));
	}
}