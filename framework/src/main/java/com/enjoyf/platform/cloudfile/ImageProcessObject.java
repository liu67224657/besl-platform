package com.enjoyf.platform.cloudfile;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/8/24
 * Description:
 */
public class ImageProcessObject {
    private double q = 100.00;//图片质量
    private Resizer resizer;
    private Croper croper;


    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public Resizer getResizer() {
        return resizer;
    }

    public void setResizer(Resizer resizer) {
        this.resizer = resizer;
    }

    public Croper getCroper() {
        return croper;
    }

    public void setCroper(Croper croper) {
        this.croper = croper;
    }

    public static class Resizer {

        public static int MODEL_0 = 0;//短边
        public static int MODEL_1 = 1;//长边
        public static int MODEL_2 = 2;//强制
        public static int MODEL_3 = 3;
        public static int MODEL_4 = 4;
        public static int MODEL_5 = 5;

        private int width;
        private int height;
        private int format = 1;


        Resizer(int width, int height) {
            this(MODEL_1, width, height);
        }

        Resizer(int format, int width, int height) {
            this.width = width;
            this.height = height;
            this.format = format;
        }


        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getFormat() {
            return format;
        }
    }

    public static class Croper {
        private int x;
        private int y;
        private int width;
        private int height;


        public Croper(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        Croper(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }


}
