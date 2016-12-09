package com.example.goldenorange.dva232_assignment_2;

import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.net.Uri;
import android.widget.Toast;
import android.content.Context;


public class MainActivity extends AppCompatActivity implements OnClickListener{
    String currentPhotoPath = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView photoView;
    private Bitmap photo;
    private Drawable testImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button greyScaleButton = (Button) findViewById(R.id.Greyscale);
        Button negateButton = (Button) findViewById(R.id.Negate);
        negateButton.setOnClickListener(this);
        greyScaleButton.setOnClickListener(this);

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
                photo = setNegate(photo);
                photoView.setImageBitmap(photo);

                break;

            case R.id.Greyscale:
                photo = setGreyscale(photo);
                photoView.setImageBitmap(photo);
                break;

            default:
                break;

        }
    }

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
        return resultImage;
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
        return resultImage;
    }

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
        }
    }
}

