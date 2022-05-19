package com.kaganga.ocr;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;



public class Main2Activity extends Activity implements JavaCameraView.CvCameraViewListener2 {
    private static final String TAG = "MainActivity";
    JavaCameraView javaCameraView;
    private TextView tvAverageFish;
    private ArrayList<MatOfPoint> countours;
    private int frameCount  ;
    private int averageFish  ;
    private int allFish  ;

    Mat mRgba;
    Mat mRgbaFiltered;
    private boolean runIdentification = false;
    private Button btnStart, btnStop;

    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }

            }


        }

    };

    // Used to load the 'native-lib' library on application startup.




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);


        tvAverageFish = (TextView) findViewById(R.id.tvAverageFish);
        btnStart = (Button) findViewById(R.id.btn_str);
        btnStop = (Button) findViewById(R.id.btn_stp);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runIdentification = true;
                frameCount = 0;
                averageFish = 0;
                allFish = 0;
                tvAverageFish.setVisibility(View.VISIBLE);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runIdentification = false;
                tvAverageFish.setVisibility(View.VISIBLE);
            }
        });





    }


    @Override
    protected void onPause() {
        super.onPause();
        if (javaCameraView != null) ;
        javaCameraView.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView != null) ;
        javaCameraView.disableView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV loaded Succes");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            Log.i(TAG, "OpenCv gagal");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        }

    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaFiltered = new Mat(height, width, CvType.CV_8UC4);


    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        //Convert to HSV
        //Imgproc.cvtColor(mRgba, mRgbaFiltered, Imgproc.COLOR_RGB2HSV);
        Imgproc.cvtColor(mRgba, mRgbaFiltered, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(mRgbaFiltered,mRgbaFiltered, 3);
        Mat edges = new Mat();
        Imgproc.Canny(mRgbaFiltered,edges,60,60*3);

        if(runIdentification) {

            //Put yellow in pixel on range hsv
            //Scalar lower = new Scalar(50, 100, 100);
            //Scalar upper = new Scalar(70, 255, 255);
            //Core.inRange(mRgbaFiltered, lower, upper, mRgbaFiltered);

            //Dilasi
            Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new  Size(10, 10));
            Imgproc.dilate(edges, edges, element);

            //Erosi
            Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_ERODE, new  Size(2, 2));
            Imgproc.erode(edges, edges, element1);


            //Smooth with medianblur
            //Imgproc.medianBlur(edges,edges, 3);


            //Contours
            ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

            for (int i = 0; i < contours.size(); i++) {

                if (contours.get(i).total() > 100 && contours.get(i).total() < 5000) {
                    MatOfPoint points = new MatOfPoint(contours.get(i).toArray());
                    Rect rect = Imgproc.boundingRect(points);

                    //Draw rectangle
                    Imgproc.rectangle(mRgba, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0, 255), 5);

                    //Imgproc.putText(mRgba,"Berat Ikan:"+contours.get(i).size(),new Point(rect.x, rect.y),1,3,new Scalar(255, 0, 0, 255), 5);

                    allFish ++;
                    if(frameCount==1){
                        Main2Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvAverageFish.setText(String.valueOf(rect.width+" x "+rect.height +" cm"));
                            }
                        });
                    }
                }
            }
            frameCount++;

            //Count Object
            averageFish = allFish/frameCount;


        }else{
            Main2Activity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvAverageFish.setText(String.valueOf("0 x 0 cm"));
                }
            });
        }
        return mRgba;
    }

}