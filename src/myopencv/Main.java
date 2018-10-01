/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myopencv;

import myopencv.lib.ImageHelper;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Oddy
 */
public class Main {

    public static void main(String[] args) {

        System.loadLibrary("libopencv_java342");
        Mat mat = Imgcodecs.imread("buah1.jpg", Imgcodecs.CV_LOAD_IMAGE_COLOR);
////        System.out.println("mat = " + mat.dump());
        Mat matGs = ImageHelper.rgb2gray(mat);
        Mat imNoise = ImageHelper.addSaltPepper(matGs, 0.01, 0.05);
        
        HighGui.imshow("Source image", matGs);
        HighGui.imshow("Grayscale", imNoise);
        HighGui.waitKey(0);
        System.exit(0);
//        ImageHelper.imshow(mat);
//        ImageHelper.imshow(matGs);
    }

}
