package com.example.goldenorange.dva232_assignment_2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.Manifest;
import android.widget.Toast;
import android.hardware.Camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnClickListener{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public int greyscaleSet = 0, negateSet = 0;
    private ImageView photoView;
    private Bitmap photo, editedPhoto;
    public final String DIR_TAG = "directoryLogTag";
    private static final int APPDEF_REQUEST_WRITE_FILE = 1;
    Button greyScaleButton;
    Button negateButton;
    Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        greyScaleButton = (Button) findViewById(R.id.Greyscale);
        negateButton = (Button) findViewById(R.id.Negate);
        saveButton = (Button) findViewById(R.id.saveButton);
        negateButton.setOnClickListener(this);
        greyScaleButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        negateButton.setEnabled(false);
        greyScaleButton.setEnabled(false);
        saveButton.setEnabled(false);

        Button cameraButton = (Button) findViewById(R.id.cameraButton);
        photoView = (ImageView) findViewById(R.id.capturedImage);

        //Check if the device has a camera or not.
        if (!hasCamera()) {
            cameraButton.setEnabled(false);
        }
    }

    public void onClick(View v)
    {
        photoView = (ImageView) findViewById(R.id.capturedImage);
        switch (v.getId())
        {
            case R.id.Negate:
                editedPhoto = setNegate(photo);
                photoView.setImageBitmap(editedPhoto);
                break;

            case R.id.Greyscale:
                editedPhoto = setGreyscale(photo);
                photoView.setImageBitmap(editedPhoto);
                break;

            case R.id.saveButton:
                if(checkExternalStorage())
                {
                    checkPermission();
                }
                else
                {
                    Log.e(DIR_TAG, "File was not saved");
                }
                break;

            default:
                break;

        }
    }

    //Check if the device has any camera.
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public Bitmap setNegate(Bitmap original)
    {
        //Create a copy of the bitmap image with it's width and height. The config describes how the bitmap has stored its pixels.
        Bitmap resultImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        int A, R, G, B;
        int height = resultImage.getHeight();
        int width = resultImage.getWidth();
        int pixelcolour;

        //Loop over each pixel in the bitmap.

        if(negateSet == 1)
        {
            negateSet = 0;
            return photo;
        }

        else
        {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //Get the pixels colours.
                    pixelcolour = original.getPixel(x, y);
                    A = Color.alpha(pixelcolour);
                    R = 255 - Color.red(pixelcolour);
                    G = 255 - Color.green(pixelcolour);
                    B = 255 - Color.blue(pixelcolour);
                    resultImage.setPixel(x, y, Color.argb(A, R, G, B));
                }
            }
            negateSet = 1;
            return resultImage;
        }
    }

    public Bitmap setGreyscale(Bitmap original)
    {
        //Create a copy of the bitmap image with it's width and height. The config describes how the bitmap has stored its pixels.
        Bitmap resultImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        int A, R, G, B, grey;
        int height = resultImage.getHeight();
        int width = resultImage.getWidth();
        int pixelcolour;

        //Loop over each pixel in the bitmap.

        if(greyscaleSet == 1)
        {
            resultImage = photo;
            greyscaleSet = 0;
            return resultImage;
        }
        else
        {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //Get the pixels colours.
                    pixelcolour = original.getPixel(x, y);
                    A = Color.alpha(pixelcolour);
                    R = Color.red(pixelcolour);
                    G = Color.green(pixelcolour);
                    B = Color.blue(pixelcolour);
                    grey = (R + G + B)/3;
                    resultImage.setPixel(x, y, Color.argb(A, grey, grey, grey));
                }
            }
            greyscaleSet = 1;
            return resultImage;
        }

    }
    //Launch the device default camera app via an intent and create a temporary file for the image.
    public void launchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    //Save the returned image and set the imageview to it.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            photoView.setImageBitmap(photo);
            greyScaleButton.setEnabled(true);
            negateButton.setEnabled(true);
            saveButton.setEnabled(true);
        }
    }

    //Check if permission is granted for write_to_external - For android with the new run-time permission model.

    private void checkPermission()
    {
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Checking Permissions", Toast.LENGTH_LONG);
            toast.show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPDEF_REQUEST_WRITE_FILE);
        }
    }

    //If permission is granted then do what the operations.

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case APPDEF_REQUEST_WRITE_FILE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //Create a unique filename using a timestamp.
                    String filename = new SimpleDateFormat("yyyy.MM.dd'T'HH:mm:ss", Locale.ENGLISH).format(new Date());
                    filename = filename + ".jpg";
                    File newFile = new File(getPhotoStorageDir(), filename);

                    try
                    {
                        //create a fileOutput stream and compress the image so it's writable to a file.
                        FileOutputStream f = new FileOutputStream(newFile);
                        if(photo.compress(Bitmap.CompressFormat.JPEG, 100, f))
                        {
                            Toast toast = Toast.makeText(getApplicationContext(), "Picture saved", Toast.LENGTH_LONG);
                            toast.show();
                            f.flush();
                        }
                        else
                        {
                            Toast toast = Toast.makeText(getApplicationContext(), "Picture was not saved", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                    catch (IOException e)
                    {
                        Log.e(DIR_TAG, e.getMessage());
                    }


                    Log.e(DIR_TAG, newFile.getAbsolutePath());
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Permission was not granted", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    }

    //Check if external storage is readable and writeable. True if mounted else then false.
    private boolean checkExternalStorage()
    {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    //Get the storage directory for the photos the user wants saved. Creates one if not existing.
    public File getPhotoStorageDir()
    {
        //Get a new directory that is public, with the given directoryname.
        File photoDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath(), "editedPhotos");
        //if the directory does exist then log a message saying the below.
        if(!photoDir.mkdirs())
        {
            Log.e(DIR_TAG, "Directory not created");
        }
        return photoDir;
    }


    //Create a temporary file so that thee full sized image can be displayed.
    private File createTemporaryFile(String name, String suff) throws Exception
    {
        //Create an internal directory with the name temp.
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            if(tempDir.mkdir())
            {
                Log.e(DIR_TAG, "Temporary directory has been created");
            }
            else
            {
                Log.e(DIR_TAG, "Temporary directory has not been created");
            }
        }
        return File.createTempFile(name, suff, tempDir);
    }
}

