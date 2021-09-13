/**
 * 
 */
package com.gov.wiki.watermaker;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.hibernate.dialect.FrontBaseDialect;

import com.gov.wiki.common.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;


/**
 * @author wangxiaoming
 *
 */
@Slf4j
public class WaterMarkForImage {
	
	
	
	/**
	 * 设置水印文字字体对象，其中字号依据图片的像素宽度来决定（以每300像素宽度来设置水印字体20像素）
	 * @param image 添加水印的图片对象
	 * @param fontName 水印字体,可不传，默认微软雅黑
	 * @param fontStyle 字体样式，如加粗、变细、斜体等，可不传，默认Font.BOLD
	 * @return 水印文字字体对象
	 */
	private Font setFont(Image image, String fontName, Integer fontStyle,int width) {
		//定义图片水印字体
		String defaultFontName = "Default";
		
		//定义图片水印字体加粗、变细、倾斜等样式
		int defaultFontStyle = Font.BOLD;
		
		//设置基础字体大小
		final int defaultFontSize = 20;
		int fontSize = defaultFontSize;
		
		if(null != image) {
			fontSize = width / 500 * 20;
		}
		
		if(StringUtils.isBlank(fontName)) {
			fontName = defaultFontName;
		}
		
		if(fontStyle < 0) {
			fontStyle = defaultFontStyle;
		}
		if(fontSize == 0) {
			fontSize = defaultFontSize;
		}
		System.out.println("frontSize="+fontSize);
		return new Font(fontName, fontStyle, fontSize);
		
	}
	
	/**
	 * 设置水印字体默认透明度
	 * @return 透明度默认0.5F
	 */
	private float defaultFontAlpha(float alpha) {
		//设置文字透明程度
		float defaultFontAlpha = 0.5F;
		if(alpha > 1 || alpha < 0) {
			return defaultFontAlpha;
		}
		return alpha;
	}
	
	/**
	 * 设定图片添加水印的起始方位坐标，提供左上角，左下角，右下角，右上角四个方向供选择
	 * @param position 常量类Position中的确定值
	 * @param image 添加水印的图片对象
	 * @return 起始方位坐标对象
	 */
	private StartingCoordinates getStartingCoordinates(String position, Image image) {
		StartingCoordinates startingCoordinates = null;
		switch(position) {
		case Position.UPPER_LEFT:
			startingCoordinates = new StartingCoordinates(0, 0);
			break;
		case Position.LOWER_LEFT:
			startingCoordinates = new StartingCoordinates(0, image.getHeight(null));
			break;
		case Position.LOWER_RIGHT:
			startingCoordinates = new StartingCoordinates(image.getWidth(null), image.getHeight(null));
			break;
		case Position.UPPER_RIGHT:
			startingCoordinates = new StartingCoordinates(image.getWidth(null), 0);
			break;
		case Position.CENTER:
			startingCoordinates = new StartingCoordinates(image.getWidth(null) / 2, image.getHeight(null) / 2);
		//default:
		//	startingCoordinates = new StartingCoordinates(image.getWidth(null), image.getHeight(null));
		}
		System.out.println("position = " + position + ", cx = " + startingCoordinates.getX() + ", cy = " + startingCoordinates.getY());
		return startingCoordinates;
	}
	
	/**
	 * 获取图片文件的格式
	 * @param obj 图片文件对象
	 * @return
	 */
	private String getImageFormat(Object obj) {
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(obj);
			Iterator<ImageReader> iterator = ImageIO.getImageReaders(iis);
			while(iterator.hasNext()) {
				ImageReader reader = (ImageReader)iterator.next();
				return reader.getFormatName();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 给图片添加多个多行文字水印，可设置水印文字的旋转角度
	 * @param sourcePath 需要添加水印的图片路径
	 * @param outputPath 添加水印后图片的输出路径
	 * @param imageName 添加水印后的图片名称
	 * @param fontName 水印文字的字体名称，为null默认微软雅黑
	 * @param fontStyle 水印文字的样式，为null默认加粗
	 * @param alpha 水印文字的透明度，为null默认透明度0.5
	 * @param color 水印文字的颜色
	 * @param words 水印文字
	 * @param degree 水印文字的旋转角度，为null代表不旋转
	 * @return
	 */
	
	public Boolean markImageByMultiLineText(String sourcePath, String outputPath, String imageName, String fontName, Integer fontStyle,
			Float alpha, Color color, String words, Integer degree) {
		
		try {
			//读取原图片信息
			File file = new File(sourcePath);
			if(!file.isFile()) {
				return false;
			}
			
			//获取原图片的格式
			String imageType = this.getImageFormat(file);
			
			//获取原图的宽度、高度
			Image image = ImageIO.read(file);
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			//创建绘图工具对象
			Graphics2D graphics2D = bufferedImage.createGraphics();
			
			//其中0代表和原图位置一样
			graphics2D.drawImage(image, 0, 0, width, height, null);
			
			//设置水印文字（设置水印字体样式、粗细、大小）
			Font waterMarkFont = this.setFont(image, fontName, fontStyle,width);
			graphics2D.setFont(waterMarkFont);
			
			//设置水印颜色
			graphics2D.setColor(color);
			
			//设置水印透明度
			graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, this.defaultFontAlpha(alpha)));
			
			//设置水印旋转
			if(null != degree) {
				graphics2D.rotate(Math.toRadians(degree), (double) bufferedImage.getWidth() / 2, (double) bufferedImage.getHeight() / 2);
			}
			int fontSize = waterMarkFont.getSize();
			System.out.println("原图尺寸，width = " + width + ", height = " + height + ", type = " + imageType + ", fontSize = " + fontSize);
			int x = width / 10;
			int y = fontSize;
			int space = height / fontSize;
			for(int i = 0; i < space; i ++) {
				System.out.println("x = " + x + ", y = " + y + ", space = " + space);
				//如果最后一个坐标的y轴比height高，直接退出
				if((y + fontSize) > height) {
					break;
				}
				
				//进行绘制
				graphics2D.drawString(words, x, y);
				y += (2 * fontSize);
			}
			graphics2D.dispose();
			
			//输出图片
			File sf = new File(outputPath, imageName + "." + imageType);
			//保存图片
			ImageIO.write(bufferedImage, imageType, sf);
		} catch (Exception e) {
			log.info("添加水印失败:{}",e);
		}
		return true;
	}
	
	/**
	 * 图片添加单行文本水印，主要添加在图片的左上、左下、右下、右上、居中五个位置
	 * @param sourcePath 原图片的文件路径
	 * @param outputPath 添加水印后图片的存放目录
	 * @param imageName 添加水印后图片的名称
	 * @param fontName 水印文字的字体，null为默认值微软雅黑
	 * @param fontStyle 水印文字的样式，null为默认值Font.BOLD
	 * @param position 水印文字添加在图片的位置，取Position中的固定值
	 * @param alpha 水印文字的透明度，null为默认值0.5
	 * @param color 水印文字的字体颜色
	 * @param words 水印文字的字体内容
	 * @param degree 水印文字的旋转度，null为不旋转
	 * @return
	 */
	public String markImageBySingleLineText(String sourcePath, String outputPath, String imageName, String fontName, Integer fontStyle,
			String position, Float alpha, Color color, String words, Integer degree) {
		String resultFilePath = null;
		try {
			//读取原图片信息
			File file = new File(sourcePath);
			if(!file.isFile()) {
				return null;
			}
			
			//获取原图片的格式
			String imageType = this.getImageFormat(file);
			
			//获取原图的宽度、高度
			Image image = ImageIO.read(file);
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			//创建绘图工具对象
			Graphics2D graphics2D = bufferedImage.createGraphics();
			
			//其中0代表和原图位置一样
			graphics2D.drawImage(image, 0, 0, width, height, null);
			
			//设置水印文字（设置水印字体样式、粗细、大小）
			Font waterMarkFont = this.setFont(image, fontName, fontStyle,width);
			graphics2D.setFont(waterMarkFont);
			
			//获取字符串的宽度与高度
			JComponent jComponent = new JLabel();
			FontMetrics fontMetrics = jComponent.getFontMetrics(waterMarkFont);
			int textWidth = fontMetrics.stringWidth(words);
			int textHeight = fontMetrics.getHeight();
			System.out.println("textWidth = " + textWidth + ", textHeight = " + textHeight);
			
			//设置水印颜色
			graphics2D.setColor(color);
			
			//设置水印透明度
			graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, this.defaultFontAlpha(alpha)));
			
			//设置水印旋转
			if(null != degree) {
				graphics2D.rotate(Math.toRadians(degree), (double) bufferedImage.getWidth() / 2, (double) bufferedImage.getHeight() / 2);
			}
			int fontSize = waterMarkFont.getSize();
			System.out.println("原图尺寸，width = " + width + ", height = " + height + ", type = " + imageType + ", fontSize = " + fontSize);
			StartingCoordinates startingCoordinates = null;
			if(Position.UPPER_LEFT.equals(position)) {
				startingCoordinates = this.getStartingCoordinates(Position.UPPER_LEFT, image);
				graphics2D.drawString(words, startingCoordinates.getX(), startingCoordinates.getY() + textHeight);
			}else if(Position.LOWER_LEFT.equals(position)) {
				startingCoordinates = this.getStartingCoordinates(Position.LOWER_LEFT, image);
				graphics2D.drawString(words, startingCoordinates.getX(), startingCoordinates.getY() - textHeight);
			}else if(Position.LOWER_RIGHT.equals(position)) {
				startingCoordinates = this.getStartingCoordinates(Position.LOWER_RIGHT, image);
				graphics2D.drawString(words, startingCoordinates.getX() - textWidth, startingCoordinates.getY() - textHeight);
			}else if(Position.UPPER_RIGHT.equals(position)) {
				startingCoordinates = this.getStartingCoordinates(Position.UPPER_RIGHT, image);
				graphics2D.drawString(words, startingCoordinates.getX() - textWidth, startingCoordinates.getY() + textHeight);
			}else if(Position.CENTER.equals(position)) {
				startingCoordinates = this.getStartingCoordinates(Position.CENTER, image);
				graphics2D.drawString(words, startingCoordinates.getX() - textWidth / 2, startingCoordinates.getY());
			}
			
			graphics2D.dispose();
			
			//输出图片
			File sf = new File(outputPath, imageName + "." + imageType);
			//保存图片
			ImageIO.write(bufferedImage, imageType, sf);
			resultFilePath = sf.getAbsolutePath();
		} catch (Exception e) {
			log.error("添加水印失败",e);
		}
		return resultFilePath;
	}

}
