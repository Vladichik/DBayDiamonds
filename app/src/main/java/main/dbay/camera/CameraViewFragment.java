package main.dbay.camera;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import main.dbay.BitmapManager;
import main.dbay.Constants;
import main.dbay.MainActivity;
import main.dbay.R;

/**
 * Created by vladvidavsky on 13/05/15.
 */
public class CameraViewFragment extends Fragment implements View.OnClickListener {

    private SurfaceHolder previewHolder = null;
    private Camera camera = null;
    private boolean inPreview = false;
    ImageView image;
    Bitmap bmp, itembmp;
    static Bitmap mutableBitmap;
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    File imageFileName = null;
    File imageFileFolder = null;
    private MediaScannerConnection msConn;
    Display d;
    int screenhgt, screenwdh;
    ProgressDialog dialog;
    SurfaceView preview;
    View cameraView;
    FrameLayout mainFrame;
    private static boolean gotFlash;
    LinearLayout infoText;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        } //CLEARS PREVIOUS FRAGMENT IF IT EXISTS
        cameraView = inflater.inflate(R.layout.camera, container, false);

        image = (ImageView) cameraView.findViewById(R.id.image_surface);
        preview = (SurfaceView) cameraView.findViewById(R.id.camerapreview);
        mainFrame = (FrameLayout) cameraView.findViewById(R.id.camera_elements_holder);
        infoText = (LinearLayout) cameraView.findViewById(R.id.camera_info_text);

        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        previewHolder.setFixedSize(getActivity().getWindow().getWindowManager().getDefaultDisplay().getWidth(),getActivity().getWindow().getWindowManager().getDefaultDisplay().getHeight());
        gotFlash = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                infoText.startAnimation(animationFadeOut);
                infoText.setVisibility(View.GONE);
            }
        }, 4000);


        return cameraView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.camera_image_placeholder));
        BitmapManager.INSTANCE.loadBitmap(Constants.BIG_APPLICATION_IMAGES_URL + MainActivity.cameraImageLink, image, 150, 113, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        //camera = Camera.open();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private Camera.Size getBestPreviewSize(int height, int width, Camera.Parameters parameters) {
        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }
        return (result);
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open();
                camera.setPreviewDisplay(previewHolder);
            } catch (Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }

        public void surfaceChanged(SurfaceHolder holder,
                                   int format, int width,
                                   int height) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = getBestPreviewSize(width, height,
                    parameters);

            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                if(gotFlash && !MainActivity.iAmOnTablet){
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                }

                camera.setParameters(parameters);
                camera.startPreview();
                inPreview = true;
            }

            if (!MainActivity.iAmOnTablet) {
                Display display = ((WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE)).getDefaultDisplay();
                if (display.getRotation() == Surface.ROTATION_0) {
                    parameters.setPreviewSize(height, width);
                    camera.setDisplayOrientation(90);
                }
            }
            ;
        }


        public void surfaceDestroyed(SurfaceHolder holder) {
            BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.img_loading));
            camera.stopPreview();
            camera.release();
            // no-op
        }
    };


    Camera.PictureCallback photoCallback = new Camera.PictureCallback() {
        public void onPictureTaken(final byte[] data, final Camera camera) {
            dialog = ProgressDialog.show(getActivity(), "", "Saving Photo");
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                    }
                    onPictureTake(data, camera);
                }
            }.start();
        }
    };


    public void onPictureTake(byte[] data, Camera camera) {

        bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
        savePhoto(mutableBitmap);
        dialog.dismiss();
    }


    class SavePhotoTask extends AsyncTask<byte[], String, String> {
        @Override
        protected String doInBackground(byte[]... jpeg) {
            File photo = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
            if (photo.exists()) {
                photo.delete();
            }
            try {
                FileOutputStream fos = new FileOutputStream(photo.getPath());
                fos.write(jpeg[0]);
                fos.close();
            } catch (java.io.IOException e) {
                Log.e("PictureDemo", "Exception in photoCallback", e);
            }
            return (null);
        }
    }


    public void savePhoto(Bitmap bmp) {
        imageFileFolder = new File(Environment.getExternalStorageDirectory(), "Rotate");
        imageFileFolder.mkdir();
        FileOutputStream out = null;
        Calendar c = Calendar.getInstance();
        String date = fromInt(c.get(Calendar.MONTH)) + fromInt(c.get(Calendar.DAY_OF_MONTH)) + fromInt(c.get(Calendar.YEAR)) + fromInt(c.get(Calendar.HOUR_OF_DAY)) + fromInt(c.get(Calendar.MINUTE)) + fromInt(c.get(Calendar.SECOND));
        imageFileName = new File(imageFileFolder, date.toString() + ".jpg");
        try {
            out = new FileOutputStream(imageFileName);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            scanPhoto(imageFileName.toString());
            out = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String fromInt(int val) {
        return String.valueOf(val);
    }

    public void scanPhoto(final String imageFileName) {
        msConn = new MediaScannerConnection(getActivity(), new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
                msConn.scanFile(imageFileName, null);
                //Log.i("msClient obj  in Photo Utility", "connection established");
            }


            public void onScanCompleted(String path, Uri uri) {
                msConn.disconnect();
                // Log.i("msClient obj in Photo Utility", "scan completed");
            }
        });
        msConn.connect();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {
//            onBack();
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    public void onBack() {
        Log.e("onBack :", "yes");
        camera.takePicture(null, null, photoCallback);
        inPreview = false;
    }

    @Override
    public void onClick(View v) {

    }

}
