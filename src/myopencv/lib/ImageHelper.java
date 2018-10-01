/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myopencv.lib;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_8U;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Oddy
 */
public class ImageHelper {

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static Mat addSaltPepper(Mat mat, double pa, double pb) {
        Mat matNew = new Mat(mat.width(), mat.height(), CvType.CV_8U);
        mat.copyTo(matNew);
        int amount1 = (int) (mat.rows() * mat.cols() * pa);
        int amount2 = (int) (mat.rows() * mat.cols() * pb);
        for (int counter = 0; counter < amount1; ++counter) {

            int x = getRandomNumberInRange(0, mat.rows());
            int y = getRandomNumberInRange(0, mat.cols());
            matNew.put(x, y, 0);

        }
        for (int counter = 0; counter < amount2; ++counter) {
            int x = getRandomNumberInRange(0, mat.rows());
            int y = getRandomNumberInRange(0, mat.cols());
            matNew.put(x, y, 255);
        }
        
        return matNew;
    }

    public static Mat imhist(Mat mat) {
        List<Mat> bgrPlanes = new ArrayList<>();
        Core.split(mat, bgrPlanes);
        int histSize = 256;
        float[] range = {0, 256}; //the upper boundary is exclusive
        MatOfFloat histRange = new MatOfFloat(range);
        boolean accumulate = false;
        Mat bHist = new Mat();
//                , gHist = new Mat(), rHist = new Mat();
        Imgproc.calcHist(bgrPlanes, new MatOfInt(0), new Mat(), bHist, new MatOfInt(histSize), histRange, accumulate);
//        Imgproc.calcHist(bgrPlanes, new MatOfInt(1), new Mat(), gHist, new MatOfInt(histSize), histRange, accumulate);
//        Imgproc.calcHist(bgrPlanes, new MatOfInt(2), new Mat(), rHist, new MatOfInt(histSize), histRange, accumulate);
        int histW = 512, histH = 400;
        int binW = (int) Math.round((double) histW / histSize);
        Mat histImage = new Mat(histH, histW, CvType.CV_8UC3, new Scalar(0, 0, 0));
        Core.normalize(bHist, bHist, 0, histImage.rows(), Core.NORM_MINMAX);
//        Core.normalize(gHist, gHist, 0, histImage.rows(), Core.NORM_MINMAX);
//        Core.normalize(rHist, rHist, 0, histImage.rows(), Core.NORM_MINMAX);
        float[] bHistData = new float[(int) (bHist.total() * bHist.channels())];
        bHist.get(0, 0, bHistData);
//        float[] gHistData = new float[(int) (gHist.total() * gHist.channels())];
//        gHist.get(0, 0, gHistData);
//        float[] rHistData = new float[(int) (rHist.total() * rHist.channels())];
//        rHist.get(0, 0, rHistData);
        for (int i = 1; i < histSize; i++) {
            Imgproc.line(histImage, new Point(binW * (i - 1), histH - Math.round(bHistData[i - 1])),
                    new Point(binW * (i), histH - Math.round(bHistData[i])), new Scalar(127, 127, 127), 2);
//            Imgproc.line(histImage, new Point(binW * (i - 1), histH - Math.round(gHistData[i - 1])),
//                    new Point(binW * (i), histH - Math.round(gHistData[i])), new Scalar(0, 255, 0), 2);
//            Imgproc.line(histImage, new Point(binW * (i - 1), histH - Math.round(rHistData[i - 1])),
//                    new Point(binW * (i), histH - Math.round(rHistData[i])), new Scalar(0, 0, 255), 2);
        }

        return histImage;
    }

    private static BufferedImage Mat2BufferedImage(Mat m) {
        // Fastest code
        // output can be assigned either to a BufferedImage or to an Image

        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    private static void displayImage(Image img2) {

        //BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
        ImageIcon icon = new ImageIcon(img2);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img2.getWidth(null) + 50, img2.getHeight(null) + 50);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static Mat rgb2gray(Mat mat) {
        Mat matGs = new Mat(mat.width(), mat.height(), CvType.CV_8U);
        
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                double[] data = mat.get(i, j);

                double avg = 0;
                double r = data[0];
                double g = data[1];
                double b = data[2];
                avg = (r + g + b) / 3;
                matGs.put(i, j, avg);
//                System.out.println(avg);
            }
        }

        return matGs;
    }

    public static void imshow(Mat mat) {
        BufferedImage img = Mat2BufferedImage(mat);
        displayImage(img);
    }
}
