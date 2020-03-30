package com.kthw.common;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

/**
 * 图像处理工具类。
 * 
 * @version 1.00 2010-1-15
 * @since 1.5
 * @author ZhangShixi
 */
public class ImageUtil {

  /**
   * 旋转图像。
   * 
   * @param bufferedImage 图像。
   * @param degree 旋转角度。
   * @return 旋转后的图像。
   */
  private static BufferedImage rotateImage(final BufferedImage bufferedImage,
      final int degree) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    int type = bufferedImage.getColorModel().getTransparency();

    BufferedImage image = new BufferedImage(height, width, type);
    Graphics2D graphics2D = image.createGraphics();
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

    if (degree == -90 || degree == 270) {
      graphics2D.rotate(Math.toRadians(degree), 0, width);
      graphics2D.translate(0, width);
    }
    else if (degree == 90) {
      graphics2D.rotate(Math.toRadians(degree), height, 0);
      graphics2D.translate(height, 0);
    }
    graphics2D.drawImage(bufferedImage, 0, 0, null);

    try {
      return image;
    }
    finally {
      if (graphics2D != null) {
        graphics2D.dispose();
      }
    }
  }

  public static BufferedImage rotateImage90N(final BufferedImage bufferedImage) {
    return rotateImage(bufferedImage, -90);
  }

  public static BufferedImage rotateImage90(final BufferedImage bufferedImage) {
    return rotateImage(bufferedImage, 90);
  }

  /**
   * 将图像按照指定的比例缩放。 比如需要将图像放大20%，那么调用时scale参数的值就为20；如果是缩小，则scale值为-20。
   * 
   * @param bufferedImage 图像。
   * @param scale 缩放比例。
   * @return 缩放后的图像。
   */
  public static BufferedImage resizeImageScale(
      final BufferedImage bufferedImage, final int scale) {
    if (scale == 0) {
      return bufferedImage;
    }

    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();

    int newWidth = 0;
    int newHeight = 0;

    double nowScale = (double) Math.abs(scale) / 100;
    if (scale > 0) {
      newWidth = (int) (width * (1 + nowScale));
      newHeight = (int) (height * (1 + nowScale));
    }
    else if (scale < 0) {
      newWidth = (int) (width * (1 - nowScale));
      newHeight = (int) (height * (1 - nowScale));
    }

    return resizeImage(bufferedImage, newWidth, newHeight);
  }

  /**
   * 将图像缩放到指定的宽高大小。
   * 
   * @param bufferedImage 图像。
   * @param width 新的宽度。
   * @param height 新的高度。
   * @return 缩放后的图像。
   */
  public static BufferedImage resizeImage(final BufferedImage bufferedImage,
      final int width, final int height) {
    int type = bufferedImage.getColorModel().getTransparency();
    BufferedImage image = new BufferedImage(width, height, type);

    Graphics2D graphics2D = image.createGraphics();
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

    graphics2D.drawImage(bufferedImage, 0, 0, width, height, 0, 0,
        bufferedImage.getWidth(), bufferedImage.getHeight(), null);

    try {
      return image;
    }
    finally {
      if (graphics2D != null) {
        graphics2D.dispose();
      }
    }
  }

  /**
   * 将图像水平翻转。
   * 
   * @param bufferedImage 图像。
   * @return 翻转后的图像。
   */
  public static BufferedImage flipImageV(final BufferedImage bufferedImage) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    int type = bufferedImage.getColorModel().getTransparency();

    BufferedImage image = new BufferedImage(width, height, type);
    Graphics2D graphics2D = image.createGraphics();
    graphics2D.drawImage(bufferedImage, 0, 0, width, height, 0, height, width,
        0, null);

    try {
      return image;
    }
    finally {
      if (graphics2D != null) {
        graphics2D.dispose();
      }
    }
  }

  /**
   * 将图像垂直翻转。
   * 
   * @param bufferedImage 图像。
   * @return 翻转后的图像。
   */
  public static BufferedImage flipImageH(final BufferedImage bufferedImage) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    int type = bufferedImage.getColorModel().getTransparency();

    BufferedImage image = new BufferedImage(width, height, type);
    Graphics2D graphics2D = image.createGraphics();
    graphics2D.drawImage(bufferedImage, 0, 0, width, height, width, 0, 0,
        height, null);

    try {
      return image;
    }
    finally {
      if (graphics2D != null) {
        graphics2D.dispose();
      }
    }
  }

  public static BufferedImage brakeImage(final BufferedImage bufferedImage) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    Graphics2D graphics2D;
    int type = bufferedImage.getColorModel().getTransparency();
    BufferedImage image = new BufferedImage(width, height, type);
    graphics2D = image.createGraphics();
    graphics2D.drawImage(bufferedImage, width, 0, 0, height, 0, 0, width,
        height, null); // 水平镜像
    graphics2D.dispose();

    // -90度旋转
    int degree = -90;
    BufferedImage image2 = new BufferedImage(height, width, type);
    graphics2D = image2.createGraphics();
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

    if (degree == -90 || degree == 270) {
      graphics2D.rotate(Math.toRadians(degree), 0, width);
      graphics2D.translate(0, width);
    }
    else if (degree == 90) {
      graphics2D.rotate(Math.toRadians(degree), height, 0);
      graphics2D.translate(height, 0);
    }
    graphics2D.drawImage(image, 0, 0, null);

    try {
      return image2;
    }
    finally {
      if (graphics2D != null) {
        graphics2D.dispose();
      }
    }
  }

  public static BufferedImage copyImage(final BufferedImage bufferedImage) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    int type = bufferedImage.getColorModel().getTransparency();

    BufferedImage image = new BufferedImage(width, height, type);
    Graphics2D graphics2D = image.createGraphics();
    graphics2D.drawImage(bufferedImage, 0, 0, width, height, 0, 0, width,
        height, null);

    try {
      return image;
    }
    finally {
      if (graphics2D != null) {
        graphics2D.dispose();
      }
    }
  }

  /**
   * 获取图片的类型。如果是 gif、jpg、png、bmp 以外的类型则返回null。
   * 
   * @param imageBytes 图片字节数组。
   * @return 图片类型。
   * @throws IOException IO异常。
   */
  public static String getImageType(final byte[] imageBytes) throws IOException {
    ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
    ImageInputStream imageInput = ImageIO.createImageInputStream(input);
    Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInput);
    String type = null;
    if (iterator.hasNext()) {
      ImageReader reader = iterator.next();
      type = reader.getFormatName().toUpperCase();
    }

    try {
      return type;
    }
    finally {
      if (imageInput != null) {
        imageInput.close();
      }
    }
  }

  /**
   * 验证图片大小是否超出指定的尺寸。未超出指定大小返回true，超出指定大小则返回false。
   *
   * @param imageBytes 图片字节数组。
   * @param width 图片宽度。
   * @param height 图片高度。
   * @return 验证结果。未超出指定大小返回true，超出指定大小则返回false。
   * @throws IOException IO异常。
   */
  public static boolean checkImageSize(final byte[] imageBytes,
      final int width, final int height) throws IOException {
    BufferedImage image = byteToImage(imageBytes);
    int sourceWidth = image.getWidth();
    int sourceHeight = image.getHeight();
    if (sourceWidth > width || sourceHeight > height) {
      return false;
    }
    else {
      return true;
    }
  }

  /**
   * 将图像字节数组转化为BufferedImage图像实例。
   *
   * @param imageBytes 图像字节数组。
   * @return BufferedImage图像实例。
   * @throws IOException IO异常。
   */
  public static BufferedImage byteToImage(final byte[] imageBytes)
      throws IOException {
    ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
    BufferedImage image = ImageIO.read(input);

    try {
      return image;
    }
    finally {
      if (input != null) {
        input.close();
      }
    }
  }

  /**
   * 将 BufferedImage持有的图像转化为指定图像格式的字节数组。
   *
   * @param bufferedImage 图像。
   * @param formatName 图像格式名称。
   * @return 指定图像格式的字节数组。
   * @throws IOException IO异常。
   */
  public static byte[] imageToByte(final BufferedImage bufferedImage,
      final String formatName) throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    ImageIO.write(bufferedImage, formatName, output);

    try {
      return output.toByteArray();
    }
    finally {
      if (output != null) {
        output.close();
      }
    }
  }

  public static BufferedImage loadImage(String pathname) {
    BufferedImage image = null;
    URL url;
    try {
      url = new URL("file:///" + pathname);
      image = ImageIO.read(url);

    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return image;
  }

  /**
   * 将图像以指定的格式进行输出，调用者需自行关闭输出流。
   *
   * @param bufferedImage 图像。
   * @param output 输出流。
   * @param formatName 图片格式名称。
   * @throws IOException IO异常。
   */
  public static void writeImage(final BufferedImage bufferedImage,
      final OutputStream output, final String formatName) throws IOException {
    ImageIO.write(bufferedImage, formatName, output);
  }

  /**
   * 将图像以指定的格式进行输出，调用者需自行关闭输出流。
   *
   * @param imageBytes 图像的byte数组。
   * @param output 输出流。
   * @param formatName 图片格式名称。
   * @throws IOException IO异常。
   */
  public static void writeImage(final byte[] imageBytes,
      final OutputStream output, final String formatName) throws IOException {
    BufferedImage image = byteToImage(imageBytes);
    ImageIO.write(image, formatName, output);
  }
  
  //chengliang add 2013.2.18 图片裁剪,第一个参数为图片的绝对地址
  public static BufferedImage cutImage(String src,int x,int y,int w,int h) throws IOException{   
      Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");   
      ImageReader reader = (ImageReader)iterator.next();   
      InputStream in=new FileInputStream(src);  
      ImageInputStream iis = ImageIO.createImageInputStream(in);   
      reader.setInput(iis, true);   
      ImageReadParam param = reader.getDefaultReadParam();   
      Rectangle rect = new Rectangle(x, y, w,h);    
      param.setSourceRegion(rect);   
      BufferedImage bi = reader.read(0,param);   
      return bi;
  }
/*
 * processImage方法参数说明
 * src：被处理图片的源路径
 * cc： 相机参数配置
 * coeff：缩放比例
 * camId：被处理图片对应的相机编号
 * bottomPicHeight：4K或者2K的底部相机高度，只能取4096或2048
 * */
/*public static BufferedImage processImage(String src, PojoCameraConfig cc,double coeff,int camId,int bottomPicHeight) throws IOException {
	BufferedImage bi = null;
	//先裁剪再翻转,前台是先翻转再剪切，需要调整
	int picHeight = 1400;
	if(camId == 5){
		picHeight = bottomPicHeight;
	}
	int hAjust;
	if(cc.getI_flipv() == 1){
		hAjust = picHeight - cc.getI_offset_y() - cc.getI_height();
		if(hAjust<0)hAjust=0;
	}else{
		hAjust = cc.getI_offset_y();
	}
	int wAjust = 2048 - cc.getI_offset_x() - cc.getI_width();
	bi = ImageUtil.cutImage(src,(int)(wAjust*coeff),(int)(hAjust*coeff),(int)(cc.getI_width()*coeff),(int)(cc.getI_height()*coeff));
	if(cc.getI_flipv() == 1){
		bi = ImageUtil.flipImageV(bi);
	}
	if(cc.getI_fliph() == 1){
		bi = ImageUtil.flipImageH(bi);
	}
	return bi;
}*/
}