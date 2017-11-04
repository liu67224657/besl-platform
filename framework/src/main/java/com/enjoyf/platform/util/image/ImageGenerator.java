package com.enjoyf.platform.util.image;

import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.image.cutstragy.CutImageProperty;
import com.enjoyf.platform.util.image.cutstragy.CutImageStragyByMiniSize;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ImageGenerator {
    public static String copyImage(InputStream inputStream, double quality, String outputPath) throws IOException, IM4JavaException, InterruptedException {
        Image img = ImageIO.read(inputStream);
        ConvertCmd cmd = new ConvertCmd(true);
        IMOperation op = new IMOperation();
//        op.flatten();
//        op.quality(quality);// 压缩率
        op.addImage();// input
        op.addImage(outputPath);// output

        cmd.run(op, img);
        return outputPath;
    }

    public static String cutImage(String srcPath, CutImageProperty property,double quality, String outPutPath)  throws IOException, IM4JavaException, InterruptedException{
        return cutImage(srcPath,property.getWidth(),property.getHeight(),property.getX(),property.getY(),quality,outPutPath);
    }

    public static String cutImage(String srcPath, int cutWidth, int cutHeight, int xAxis, int yAxis, double quality, String outPutPath) throws IOException, IM4JavaException, InterruptedException {
        ConvertCmd cutCmd = new ConvertCmd(true);

        IMOperation cutOp = new IMOperation();
        cutOp.quality(quality);// 压缩率
        cutOp.addImage();// input
        cutOp.crop(cutWidth, cutHeight, xAxis, yAxis);
        cutOp.addImage();// output
        cutCmd.run(cutOp, srcPath, outPutPath);

        return outPutPath;
    }

    public static String resizeImage(String srcPath, int resizeWidth, int resizeHeight, double quality, String outPutPath) throws IOException, IM4JavaException, InterruptedException {
        ConvertCmd cutCmd = new ConvertCmd(true);

        IMOperation cutOp = new IMOperation();
        cutOp.quality(quality);// 压缩率
        cutOp.addImage();// input
        cutOp.resize(resizeWidth, resizeHeight);
        cutOp.addImage();// output
        cutCmd.run(cutOp, srcPath, outPutPath);
        return outPutPath;
    }

    public static String compositeImage(ImageElement imageElement, String backgroundPath, double quality, String outPutPath) throws IOException, IM4JavaException, InterruptedException {
        CompositeCmd compositeCmd = new CompositeCmd(true);
        IMOperation compOp = new IMOperation();
        compOp.quality(quality);
        compOp.gravity(imageElement.getLocation().getCode());
        compOp.addRawArgs("-geometry", "+" + imageElement.getxAxis() + "+" + imageElement.getyAxis() + "");
        compOp.addImage();
        compOp.addImage();
        compOp.addImage();
        compositeCmd.run(compOp, imageElement.getImgSrc(), backgroundPath, outPutPath);
        return outPutPath;
    }

    public static String generatImages(int width, int height, String color, String outPutPath) throws IOException, IM4JavaException, InterruptedException {
        ConvertCmd convert = new ConvertCmd(true);
        IMOperation op = new IMOperation();
        op.size(width, height);
        op.addRawArgs("gradient:" + color + "-" + color);
        op.addImage();
        convert.run(op, outPutPath);
        return outPutPath;
    }

    public static String printImages(List<FontElement> fontElementList, String imgSrc, double quality, String outPutPath) throws IOException, IM4JavaException, InterruptedException {
        ConvertCmd convert = new ConvertCmd(true);
        IMOperation op = new IMOperation();
        op.quality(quality);
        for (FontElement fontElement : fontElementList) {
            op.font(fontElement.getTtfPath());
            op.gravity(fontElement.getLocation().getCode());
            op.pointsize(fontElement.getFontSize());
            op.fill(fontElement.getFontColor());
            String text = fontElement.getText().replace("'", "\\'");
            op.draw("text " + fontElement.getxAxis() + "," + fontElement.getyAxis() + " '" + text + "'");
        }

        op.addImage();
        op.addImage();
        convert.run(op, imgSrc, outPutPath);
        return outPutPath;
    }

    public static String addBorder(ImageBorder imageBorder, String imgSrc, double quality, String outPutPath) throws IOException, IM4JavaException, InterruptedException {
        ConvertCmd convert = new ConvertCmd(true);
        IMOperation op = new IMOperation();
        op.quality(quality);
        setBorder(op, imageBorder);
        op.addImage();
        op.addImage();
        convert.run(op, imgSrc, outPutPath);
        return outPutPath;
    }

    private static IMOperation setBorder(IMOperation imOperation, ImageBorder imageBorder) {
        if (imageBorder.getWidth() > 0 && imageBorder.getHeight() > 0) {
            imOperation.border(imageBorder.getWidth(), imageBorder.getHeight());
        } else {
            imOperation.border(imageBorder.getWidth());
        }
        if (!StringUtil.isEmpty(imageBorder.getColor())) {
            imOperation.bordercolor(imageBorder.getColor());
        }

        return imOperation;
    }


    public static void main(String[] args) {
        try {
            String outputPath = ImageGenerator.cutImage("E:\\app\\test.jpg", new CutImageStragyByMiniSize(100, 100).calCutproperty(200, 900, 50, 50), 100, "\\1.jpg");



//            int lineHeight = 0;
//            List<FontElement> fontElementList = new ArrayList<FontElement>();
//            for (int i = 0; i < 10; i++) {
//                lineHeight = (i + 1) * 14 + (i * 5);
//                FontElement fontElement = new FontElement();
//                fontElement.setText(new String("刘浩刘浩刘浩刘浩刘浩刘浩刘浩刘浩刘浩刘浩刘浩刘浩刘浩刘@".getBytes("UTF-8"), "GB18030"));
//                fontElement.setTtfPath("d:\\app\\msyh.ttf");
//                fontElement.setLocation(ElementLocation.LOCATION_NORTHWEST);
//                fontElement.setFontSize(14);
//                fontElement.setxAxis(5);
//                fontElement.setyAxis(lineHeight);
//                fontElement.setFontColor("#555");
//                fontElementList.add(fontElement);
//            }
//            outputPath = ImageGenerator.printImages(fontElementList, outputPath,100.00, "D:\\app\\content.png");
//
//            ImageElement imageElement = new ImageElement();
//            imageElement.setImgSrc("http://r001.joyme.test/r001/image/2012/01/15/72DD5DBCBF91C42269405C867BE515B1_S.jpg");
//            imageElement.setLocation(ElementLocation.LOCATION_NORTHWEST);
//            imageElement.setxAxis(5);
//            imageElement.setyAxis(lineHeight + 20);
//            outputPath = resizeImage("d:\\app\\8BACE8D1957E0E347A42A6CED6C91A04_SS.jpg", 10,10,100.00, "D:\\app\\content.png");

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IM4JavaException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
