/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myopencv;

import java.awt.image.BufferedImage;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author Oddy
 */
public class VideoCap {

    static{
        System.loadLibrary("libopencv_java342");
    }

    VideoCapture cap;
    Mat2Image mat2Img = new Mat2Image();

    VideoCap(){
        cap = new VideoCapture();
        cap.open(0);
    }

    BufferedImage getOneFrame() {
        cap.read(mat2Img.mat);
        return mat2Img.getImage(mat2Img.mat);
    }
}
