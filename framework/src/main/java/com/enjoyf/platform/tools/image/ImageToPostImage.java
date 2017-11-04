package com.enjoyf.platform.tools.image;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.IOException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class ImageToPostImage {

    public static void main(String[] args) {
        try {
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "11", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "12", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "13", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "21", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "22", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "31", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "32", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "33", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "41", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "51", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "52", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "53", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "61", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "62", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "71", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "81", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "82", "jpg");
            copyImage("C:\\Users\\ericliu.ENJOYF\\Desktop\\yx\\", "83", "jpg");
//             ImageGenerator.copyImage(new FileInputStream("C:\\Users\\Public\\Pictures\\Sample Pictures\\jh.jpg"), 100.00, "C:\\Users\\ericliu.ENJOYF\\Desktop\\1.jpg");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IM4JavaException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void copyImage(String path, String imageName, String orgExtname) throws IOException, IM4JavaException, InterruptedException {

        ConvertCmd cmd = new ConvertCmd(true);
        IMOperation op = new IMOperation();
        op.quality(100.00);// 压缩率
        op.addImage();
        op.addImage();
        cmd.run(op, path + imageName + "." + orgExtname, path + imageName + ".jpg");
        cmd.run(op, path + imageName + "." + orgExtname, path + imageName + "_S.jpg");
        cmd.run(op, path + imageName + "." + orgExtname, path + imageName + "_SS.jpg");
        cmd.run(op, path + imageName + "." + orgExtname, path + imageName + "_M.jpg");
    }


}
