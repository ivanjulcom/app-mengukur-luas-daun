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
import android.widget.EditText;
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
    private TextView tvLuas;
    private TextView tvLuas2;
    private ArrayList<MatOfPoint> countours;
    private int frameCount  ;
    private int averageFish  ;
    private int allFish  ;

    Mat mRgba;
    Mat mRgbaFiltered;
    private boolean runIdentification = false;
    private Button btnStart, btnStop;
    EditText jarak;
    private int const_jarak = 10;

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


        tvLuas = (TextView) findViewById(R.id.tvAverageFish);
        tvLuas2 = (TextView) findViewById(R.id.tvAverageFish2);
        btnStart = (Button) findViewById(R.id.btn_str);
        btnStop = (Button) findViewById(R.id.btn_stp);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runIdentification = true;
                frameCount = 0;
                averageFish = 0;
                allFish = 0;
                tvLuas.setVisibility(View.VISIBLE);
                tvLuas2.setVisibility(View.VISIBLE);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runIdentification = false;
                tvLuas.setVisibility(View.VISIBLE);
                tvLuas2.setVisibility(View.VISIBLE);
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
        //RGB2Gray
        Imgproc.cvtColor(mRgba, mRgbaFiltered, Imgproc.COLOR_BGR2GRAY);
        //Gaussian
        Imgproc.GaussianBlur(mRgbaFiltered,mRgbaFiltered, new Size(7,7),0);
        //Edge Detection
        Mat edges = new Mat();
        Imgproc.Canny(mRgbaFiltered,edges,50,100);

        if(runIdentification) {
            jarak = (EditText) findViewById(R.id.jarak);

            //Dilasi
            Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new  Size(12, 12));
            Imgproc.dilate(edges, edges, element);

            //Erosi
            Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_ERODE, new  Size(6, 6));
            Imgproc.erode(edges, edges, element1);

            //Contours
            ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

            for (int i = 0; i < contours.size(); i++) {

                if (contours.get(i).total() > 500 && contours.get(i).total() < 5000) {
                    MatOfPoint points = new MatOfPoint(contours.get(i).toArray());
                    Rect rect = Imgproc.boundingRect(points);

                    //Draw rectangle
                    Imgproc.rectangle(mRgba, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0, 255), 5);
                    if(frameCount==1){
                        Main2Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Rumus mencari luas daun
                                Float jaraks = Float.parseFloat(jarak.getText().toString());
                                Float width = Float.parseFloat(String.valueOf(rect.width));
                                Float height = Float.parseFloat(String.valueOf(rect.height));
                                double consts = jaraks/8.5;
                                double panjang = (width/602)*5*consts;
                                double lebar = (height/171)*1.4*consts;
                                tvLuas.setText(String.format("%.2f",panjang)+" cm"+" x "+String.format("%.2f",lebar)+" cm");
                                tvLuas2.setText("("+String.format("%.2f",panjang*lebar)+" cm2)");
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
                    tvLuas.setText(String.valueOf("0 cm x 0 cm"));
                    tvLuas2.setText(String.valueOf("(0 cm2)"));
                }
            });
        }
        return mRgba;
    }

}
