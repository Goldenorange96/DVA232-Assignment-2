package com.example.goldenorange.dva232_assignment_2;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by GoldenOrange on 13/12/2016.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback
{
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera)
    {
        super(context);
        mCamera = camera;

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();;
        }
        catch (IOException e)
        {
            Log.d("CameraTag", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed (SurfaceHolder holder)
    {

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
    {
        if(mHolder.getSurface() == null)
        {
            return;
        }

        try
        {
            mCamera.stopPreview();
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getContext(), "No existing preview", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
