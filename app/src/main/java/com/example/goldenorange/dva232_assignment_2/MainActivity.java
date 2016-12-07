package com.example.goldenorange.dva232_assignment_2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView capturedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button greyScaleButton = (Button) findViewById(R.id.greyScale);
        Button negateButton = (Button) findViewById(R.id.Negate);

        greyScaleButton.setVisibility(View.INVISIBLE);
        negateButton.setVisibility(View.INVISIBLE);


        Button cameraButton = (Button) findViewById(R.id.cameraButton);
        capturedImage = (ImageView) findViewById(R.id.capturedImage);


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


    //Launch the camera.
    public void launchCamera(View view)
    {
        Button greyScaleButton = (Button) findViewById(R.id.greyScale);
        Button negateButton = (Button) findViewById(R.id.Negate);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
            Bitmap photo = (Bitmap) extras.get("data");
            capturedImage.setImageBitmap(photo);
        }
    }
}
