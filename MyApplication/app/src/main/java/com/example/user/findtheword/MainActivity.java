package com.example.user.findtheword;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity {

    static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %% ID: %d";
    static final String SCALE_TOAST_STRING = "Scaled to: %.2ff";

    private TextView mCurrMatrixTv;

    private PhotoViewAttacher mAttacher;

    private Toast mCurrentToast;

    private Matrix mCurrentDisplayMatrix = null;

    private ImageView mImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.iv_photo);
        mCurrMatrixTv = (TextView) findViewById(R.id.tv_current_matrix);

        Drawable bitmap = getResources().getDrawable(R.drawable.graphe);
        mImageView.setImageDrawable(bitmap);

        // The MAGIC happens here!
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setScaleLevels(3.0f, 4.0f, 5.0f);
        //mAttacher.setScale(mAttacher.getMinimumScale(), 0, 0, true);

        mAttacher.setScale(mAttacher.getMinimumScale(), 535, 850, true);

        // Lets attach some listeners, not required though!
        mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
        mAttacher.setOnPhotoTapListener(new PhotoTapListener());

    }

    public void movePicture(View v){
        mAttacher.setScale(mAttacher.getMinimumScale(), 100,100 , false);


        //showToast("You need to capture display matrix first");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Need to call clean-up
        mAttacher.cleanup();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem zoomToggle = menu.findItem(R.id.menu_zoom_toggle);
        assert null != zoomToggle;
        zoomToggle.setTitle(mAttacher.canZoom() ? R.string.menu_zoom_disable : R.string.menu_zoom_enable);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_zoom_toggle:
                mAttacher.setZoomable(!mAttacher.canZoom());
                return true;

            case R.id.menu_scale_fit_center:
                mAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return true;

            case R.id.menu_scale_fit_start:
                mAttacher.setScaleType(ImageView.ScaleType.FIT_START);
                return true;

            case R.id.menu_scale_fit_end:
                mAttacher.setScaleType(ImageView.ScaleType.FIT_END);
                return true;

            case R.id.menu_scale_fit_xy:
                mAttacher.setScaleType(ImageView.ScaleType.FIT_XY);
                return true;

            case R.id.menu_scale_scale_center:
                mAttacher.setScaleType(ImageView.ScaleType.CENTER);
                return true;

            case R.id.menu_scale_scale_center_crop:
                mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return true;

            case R.id.menu_scale_scale_center_inside:
                mAttacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                return true;

            case R.id.menu_scale_random_animate:
            case R.id.menu_scale_random:
                Random r = new Random();

                float minScale = mAttacher.getMinimumScale();
                float maxScale = mAttacher.getMaximumScale();
                float randomScale = minScale + (r.nextFloat() * (maxScale - minScale));
                mAttacher.setScale(randomScale, item.getItemId() == R.id.menu_scale_random_animate);

                showToast(String.format(SCALE_TOAST_STRING, randomScale));

                return true;
            case R.id.menu_matrix_restore:
                if (mCurrentDisplayMatrix == null)
                    showToast("You need to capture display matrix first");
                else
                    mAttacher.setDisplayMatrix(mCurrentDisplayMatrix);
                return true;
            case R.id.menu_matrix_capture:
                mCurrentDisplayMatrix = mAttacher.getDisplayMatrix();
                return true;
            case R.id.extract_visible_bitmap:
                try {
                    Bitmap bmp = mAttacher.getVisibleRectangleBitmap();
                    File tmpFile = File.createTempFile("photoview", ".png",
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                    FileOutputStream out = new FileOutputStream(tmpFile);
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.close();
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/png");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tmpFile));
                    startActivity(share);
                    Toast.makeText(this, String.format("Extracted into: %s", tmpFile.getAbsolutePath()), Toast.LENGTH_SHORT).show();
                } catch (Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(this, "Error occured while extracting bitmap", Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class PhotoTapListener implements PhotoViewAttacher.OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
            float xPercentage = x * 100f;
            float yPercentage = y * 100f;
            //mAttacher.setScale(mAttacher.getMinimumScale(), x-10,y-10 , false);
            showToast(String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage, view == null ? 0 : view.getId()));
        }
    }

    private void showToast(CharSequence text) {
        if (null != mCurrentToast) {
            mCurrentToast.cancel();
        }

        mCurrentToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mCurrentToast.show();
    }

    private class MatrixChangeListener implements PhotoViewAttacher.OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {
            //mCurrMatrixTv.setText(rect.toString());
            mCurrMatrixTv.setText(mAttacher.getDisplayMatrix().toString());
        }
    }

}
