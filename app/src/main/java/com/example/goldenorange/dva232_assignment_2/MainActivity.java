package com.example.goldenorange.dva232_assignment_2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;
import android.net.Uri;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    String currentPhotoPath = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public ImageView photoView = null;
    public Bitmap photo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button greyScaleButton = (Button) findViewById(R.id.Greyscale);
        Button negateButton = (Button) findViewById(R.id.Negate);

        greyScaleButton.setVisibility(View.INVISIBLE);
        negateButton.setVisibility(View.INVISIBLE);


        Button cameraButton = (Button) findViewById(R.id.cameraButton);
        photoView = (ImageView) findViewById(R.id.capturedImage);


        //Check if the device has a camera or not.
        if(!hasCamera())
        {
            cameraButton.setEnabled(false);
        }
    }



    private boolean hasCamera()
    {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void setGreyscale(View view)
    {
        //Create a copy of the bitmap image with it's width and height. The config describes how the bitmap has stored its pixels.
        Bitmap resultImage = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), photo.getConfig());
        double A = 0, R = 0, G = 0, B = 0;
        int height = resultImage.getHeight();
        int width = resultImage.getWidth();
        int pixelcolour = 0;

        //Loop over each pixel in the bitmap.

        for(int y = 0; y < height; ++y)
        {
            for(int x = 0; x < width; ++x)
            {
                //Get the pixels colours.
                pixelcolour = resultImage.getPixel(x ,y);
                A = (Color.alpha(pixelcolour));
                R = (0.2126 * Color.red(pixelcolour));
                G = (0.7152 * Color.green(pixelcolour));
                B = (0.0722 * Color.blue(pixelcolour));
                resultImage.setPixel(x, y, Color.argb((int)A, (int)R, (int)G, (int)B));
            }
        }
        photoView.setImageBitmap(resultImage);
    }

    public void setNegate(View view)
    {
        //Create a copy of the bitmap image with it's width and height. The config describes how the bitmap has stored its pixels.
        Bitmap resultImage = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), photo.getConfig());
        int A = 0, R = 0, G = 0, B = 0;
        int height = resultImage.getHeight();
        int width = resultImage.getWidth();
        int pixelcolour = 0;

        //Loop over each pixel in the bitmap.

        for(int y = 0; y < height; ++y)
        {
            for(int x = 0; x < width; ++x)
            {
                //Get the pixels colours.
                pixelcolour = resultImage.getPixel(x ,y);
                A = Color.alpha(pixelcolour);
                R = 255 - Color.red(pixelcolour);
                G = 255 - Color.green(pixelcolour);
                B = 255 - Color.blue(pixelcolour);
                resultImage.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        photo = resultImage;
        photoView.setImageBitmap(resultImage);
    }

    public void launchCamera(View view)
    {
        Button greyScaleButton = (Button) findViewById(R.id.Greyscale);
        Button negateButton = (Button) findViewById(R.id.Negate);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

       /* File photoFile = null;

        photoFile = createImageFile();

        if(photoFile != null)
        {
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        }*/

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        greyScaleButton.setVisibility(View.VISIBLE);
        negateButton.setVisibility(View.VISIBLE);
    }

    //Save the returned image and set the imageview to it.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            photoView.setImageBitmap(photo);
        }
    }

    /*private File createImageFile()
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "Photo_Camera" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        */


