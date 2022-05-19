package com.kaganga.ocr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class MatchingActivity extends AppCompatActivity {

   /* private List<MatTemplate> matTemplate;
    private List<MatTemplate> sandangan_Mat_Template;
    private List<MatTemplate> basic_Mat_template, ga_Mat_template, ba_Mat_template, pa_Mat_template, nya_Mat_template;
    private List<Rect> rectList;
    private List<Template> letters, sandangans;
    private float ratio=1.0f;
    private int templateWidth = 40;
    private int templateHeight = 72;
    private String imageUri = "";
    private DatabaseHandler databaseHandler;
    private History history;*/
    /*@BindView(R.id.editTextResult1) EditText editText1;
    @BindView(R.id.editTextResult2) EditText editText2;
    @BindView(R.id.cropped_image) ImageView imageView;
    @BindView(R.id.button) Button button;
    @BindView(R.id.seekBar) SeekBar seekBar;*/

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);
        ButterKnife.bind(this);

        /*matTemplate = new ArrayList<MatTemplate>();
        databaseHandler = new DatabaseHandler(this);
        databaseHandler.open();
        history = new History();
        seekBar.setMax(1000);
        seekBar.setProgress(0);
        seekBar.incrementProgressBy(100);

        prepareTemplate();

        Bundle b = getIntent().getExtras();
        if(b != null) {
            imageUri = b.getString("imageUri");
            preProcess(Uri.parse(imageUri), 0);
        }

        EasyImage.configuration(this)
                .setImagesFolderName(getString(R.string.app_name))
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true);

        Toolbar myToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);*/
    }

    /*private void prepareTemplate(){

        letters = databaseHandler.getTemplateHuruf();
        sandangans = databaseHandler.getTemplateSandangan();
        matTemplate = new ArrayList<MatTemplate>();
        sandangan_Mat_Template = new ArrayList<MatTemplate>();

        for(int i = 0; i < letters.size(); i++){
            Mat templ = convertImage(Uri.parse("android.resource://com.kaganga.ocr/drawable/"+letters.get(i).getFile()));
            Mat templCanny = convertToCanny(templ);
            MatTemplate temp = new MatTemplate(letters.get(i).getHuruf(), templCanny);
            matTemplate.add(temp);
        }

        for(int i = 0; i < sandangans.size(); i++){
            Mat templ = convertImage(Uri.parse("android.resource://com.kaganga.ocr/drawable/"+sandangans.get(i).getFile()));
            Mat templCanny = convertToCanny(templ);
            MatTemplate temp = new MatTemplate(sandangans.get(i).getHuruf(), templCanny);
            sandangan_Mat_Template.add(temp);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_matching, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_camera) {
            //EasyImage.openCameraForImage(MatchingActivity.this,0);
            //imageView.setVisibility(View.VISIBLE);
            Mat imgTreshed = convertToCanny(convertImage(Uri.parse("android.resource://com.kaganga.ocr/drawable/ga1")));
            Bitmap bitmap = Bitmap.createBitmap(imgTreshed.cols(), imgTreshed.rows(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(imgTreshed, bitmap);
            //imageView.setImageBitmap(bitmap);
        } else if (id == R.id.action_galery) {
            //EasyImage.openDocuments(MatchingActivity.this, 0);
            //imageView.setVisibility(View.VISIBLE);
            Mat imgTreshed = doThreshold(convertImage(Uri.parse(imageUri)));
            Bitmap bitmap = Bitmap.createBitmap(imgTreshed.cols(), imgTreshed.rows(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(imgTreshed, bitmap);
            imageView.setImageBitmap(bitmap);
        } else if(id == R.id.action_save){
            if (history!=null){
                try {
                    databaseHandler.insertHistory(history);
                    Toast.makeText(this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(this, "Gagal Menyimpan", Toast.LENGTH_SHORT).show();
                }
            }else {
                return true;
            }
        } else if(id == R.id.action_home){
            Intent intent = new Intent(MatchingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    //Some error handling
                }

                @Override
                public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                    //Handle the images
                    onPickDone(Uri.fromFile(imagesFiles.get(0)));
                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                    //Cancel handling, you might wanna remove taken photo if it was canceled
                    if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                        File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MatchingActivity.this);
                        if (photoFile != null) photoFile.delete();
                    }
                }
            });
        }
    }

    private void onPickDone(Uri uri){
        CropImage.activity(uri)
                .start(this);
    }

    private void preProcess(Uri uri, int progress){

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.preprocess);
        }

        Mat imgTreshed = doThreshold(convertImage(uri));
        Mat prep = convertToCanny(convertImage(uri));
        history.setImageuri(uri.toString());

        //Find contour to get number of letters
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        //NO. 3 Mencari Kontur
        Imgproc.findContours(imgTreshed, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Log.d("contour",""+contours.size());

        // Save contour to List
        rectList = new ArrayList<Rect>();
        int maxArea = 0;
        for (int i = 0; i < contours.size(); i++) {

            // Get bounding rect of contour
            MatOfPoint points = new MatOfPoint(contours.get(i).toArray());
            Rect rect = Imgproc.boundingRect(points);
            int area = rect.width;
            if(maxArea<area){
                maxArea=area;
            }
        }

        //NO. 3 Menentukan ukuran citra huruf inputan

        int minOfAreas = 0;
        if(progress==0){
            minOfAreas = (int)(maxArea/3.0f);
            if(minOfAreas < 1000) {
                seekBar.setProgress(minOfAreas);
            }else{
                seekBar.setProgress(1000);
            }
        }else{
            seekBar.setProgress(progress);
            minOfAreas = progress;
            maxArea = progress*3;
        }

        int maxWidth = 0;
        for (int i = 0; i < contours.size(); i++) {

            // Get bounding rect of contour
            MatOfPoint points = new MatOfPoint(contours.get(i).toArray());
            Rect rect = Imgproc.boundingRect(points);
            int area = rect.width;

            //Get only contour with best area
            if (area > minOfAreas && area <= maxArea){
                rectList.add(rect);
                maxWidth += rect.width;

            }
        }
        Log.d("rect",""+rectList.size()+"minArea"+minOfAreas+"maxWidth"+maxWidth);
        //

        // No 3. Urutkan huruf
        //Sort rectangle from top left
        Collections.sort(rectList, new Comparator<Rect>() {
            @Override
            public int compare(Rect o1, Rect o2) {
                int cellSize = 20;
                Integer o1X = o1.x;
                Integer o2X = o2.x;
                Integer o1Y = (int)Math.floor(o1.y/cellSize);
                Integer o2Y = (int)Math.floor(o2.y/cellSize);
                //Compare y first, x later
                int result = o1Y.compareTo(o2Y);
                if (result == 0) {
                    result = o1X.compareTo(o2X);
                }
                return result;
            }
        });

        for (int i = 0; i < rectList.size(); i++) {
            Rect rect = rectList.get(i);
            rect = new Rect(rect.x, rect.y-(rect.height/4), rect.width, rect.height+(rect.height/2));
            Imgproc.rectangle(prep, new Point(rect.x, rect.y), new Point(rect.x + rect.width,rect.y + rect.height), new Scalar(255, 255, 255));
            Imgproc.putText(prep, ""+i , new Point(rect.x, rect.y+rect.height), 0, 0.5, new Scalar(255, 0, 0));
        }

        int meanWidth = 1;
        if (rectList.size()>0) {
             meanWidth = maxWidth / rectList.size();
        }

        //NO 3. Me
        //Set ratio to macth the matTemplate
        ratio = (float)templateWidth/meanWidth;
        ratio = (float)Math.floor(ratio * 1000) / 1000;
        Log.d("ratio",""+ratio);

        Bitmap bitmap = Bitmap.createBitmap(prep.cols(), prep.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(prep, bitmap);
        imageView.setImageBitmap(bitmap);
        button.setVisibility(View.VISIBLE);
        button.setText(R.string.process);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                Mat img = convertImage(uri);
                templateMatching(img);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                preProcess(uri, progressValue);
            }
        });


    }

    public void templateMatching(Mat mat) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.result);
        }

        Mat img = mat;
        String out = "";
        String out2 = "";
        //Loop over detected letters saved on list
        for (int i = 0; i < rectList.size(); i++) {


                // Masking inputed image to separate each detected letter
                Rect rect = rectList.get(i);
                rect = new Rect(rect.x - (rect.width / 2), rect.y - (rect.height), rect.width + (rect.width), rect.height + (rect.height * 2));

                Mat mask = img.submat(rect);

                if (ratio < 1){ //Scale down
                    if (mask.width()*ratio > templateWidth && mask.height()*ratio > templateHeight){
                        Imgproc.resize(mask, mask, new Size(), ratio, ratio, Imgproc.INTER_AREA);
                    }
                } else{         //Scale up
                    Imgproc.resize(mask, mask, new Size(), ratio, ratio, Imgproc.INTER_CUBIC);
                }
                Log.d("size",""+mask.width()+" x "+mask.height());

                Mat masked = convertToCanny(mask);

                double threshold = 0.75;
                String wordMatch = "";
                Point mL = null;
                double mV = 0;
                MatTemplate bestMatch = null;

                for (int j = 0; j < matTemplate.size(); j++) {




                    // Create the result matrix for MatTemplate Matching
                    MatTemplate temp = matTemplate.get(j);
                    Mat templCanny = temp.getMat();
                    int result_cols = masked.cols() - templCanny.cols() + 1;
                    int result_rows = masked.rows() - templCanny.rows() + 1;
                    Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

                    // Do the MatTemplate Matching using Normalized Cross Correlation Coefficient
                    Imgproc.matchTemplate(masked, templCanny, result, Imgproc.TM_CCOEFF_NORMED);

                    // Get the best match with minMaxLoc
                    Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

                    Point matchLoc;
                    double maxValue;
                    matchLoc = mmr.maxLoc;
                    maxValue = mmr.maxVal;

                    if (mL == null || maxValue > mV) {
                        mL = matchLoc;
                        mV = maxValue;
                        bestMatch = temp;
                    }
                    if (mV >= threshold) {
                        break;
                    }
                }

                Log.d("First Iteration", "" + i + "  maxValue : " + mV + " Detected : " + bestMatch.getLetter());
                wordMatch = bestMatch.getLetter();
                out2 += wordMatch + " ";

                Point NmL = null;
                double NmV = 0;
                MatTemplate NbestMatch = null;

                for (int j = 0; j < sandangan_Mat_Template.size(); j++) {
                    // Create the result matrix for MatTemplate Matching
                    MatTemplate temp = sandangan_Mat_Template.get(j);
                    Mat templCanny = temp.getMat();
                    int result_cols = masked.cols() - templCanny.cols() + 1;
                    int result_rows = masked.rows() - templCanny.rows() + 1;
                    Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

                    // Do the MatTemplate Matching using Normalized Cross Correlation Coefficient
                    Imgproc.matchTemplate(masked, templCanny, result, Imgproc.TM_CCOEFF_NORMED);

                    // Get the best match with minMaxLoc
                    Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

                    Point matchLoc;
                    double maxValue;
                    matchLoc = mmr.maxLoc;
                    maxValue = mmr.maxVal;

                    if (NmL == null || maxValue > NmV) {
                        NmL = matchLoc;
                        NmV = maxValue;
                        NbestMatch = temp;
                    }
                    if (NmV >= threshold) {
                        break;
                    }
                }

                Log.d("Second Iteration", "" + i + "  maxValue : " + NmV + " Detected : " + NbestMatch.getLetter());

                if (wordMatch != "") {
                    wordMatch = wordMatch.substring(0, wordMatch.length() - 1) + NbestMatch.getLetter();
                }
                out += wordMatch + " ";
//
        }

        Splitter splitter = new Splitter(out, this);
        String result = splitter.doLettersToWords();
        Splitter splitter2 = new Splitter(out2, this);
        String result2 = splitter2.doLettersToWords();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        history.setDate(df.format(date));
        history.setResult(result);

        editText1.setVisibility(View.VISIBLE);
        editText1.setText(result2, TextView.BufferType.EDITABLE);
        editText2.setVisibility(View.VISIBLE);
        editText2.setText(result, TextView.BufferType.EDITABLE);
        imageView.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);

    }

    // No 1. Convert jpg to mat
    public Mat convertImage(Uri uri){
        InputStream stream = null;
        try {
            stream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bmp = BitmapFactory.decodeStream(stream, null, bmpFactoryOptions);
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat);
        return mat;
    }
    // No 1.

    //convert rgb to canny
    private Mat convertToCanny(Mat mat){
        Mat imgCanny = mat;
        Imgproc.cvtColor(imgCanny, imgCanny, Imgproc.COLOR_RGB2GRAY);
        Imgproc.Canny(imgCanny, imgCanny, 85, 255);
        return imgCanny;
    }

    // No 2. Thresholding
    public Mat doThreshold(Mat mat){
        Mat imgThreshed = mat;
        Imgproc.cvtColor(imgThreshed, imgThreshed, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(imgThreshed, imgThreshed, new Size(5,5), 0);
        Imgproc.threshold(imgThreshed, imgThreshed, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
        Imgproc.erode(imgThreshed, imgThreshed, kernel);
        return imgThreshed;
    }*/
}
