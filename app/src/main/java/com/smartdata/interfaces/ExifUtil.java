package com.smartdata.interfaces;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("ALL")
public class ExifUtil {
    private final String TAG = ExifUtil.class.getSimpleName();

    /**
     * @see "http://sylvana.net/jpegcrop/exif_orientation.html"
     */
    public static Bitmap rotateBitmap(String src, Bitmap bitmap) {

        try {
            int orientation = getExifOrientation(src);

            if (orientation == 1) {
                return bitmap;
            }

            Matrix matrix = new Matrix();
            switch (orientation) {
                case 0:
                    matrix.setRotate(90);
                    break;
                case 2:
                    matrix.setScale(-1, 1);
                    break;

                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;

                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(90);
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }

            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                Bitmap updateBitmap = BitmapFactory.decodeFile(src, options);
                updateBitmap = Bitmap.createBitmap(updateBitmap, 0, 0, updateBitmap.getWidth(), updateBitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return updateBitmap;
            } catch (OutOfMemoryError e) {
                Log.e("ExifUtil.class", e.getMessage(), e);
                return bitmap;
            }
        } catch (IOException e) {
            Log.e("ExifUtil.class", e.getMessage(), e);
        }

        return bitmap;
    }

    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5
             * ExifInterface exif = new ExifInterface(src);
             * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class.forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass.getConstructor(String.class);
                Object exifInstance = exifConstructor.newInstance(src);
                Method getAttributeInt = exifClass.getMethod("getAttributeInt", String.class, int.class);
                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance, tagOrientation, 1);
            }
        } catch (ClassNotFoundException e) {
            Log.e("ExifUtil.class", e.getMessage(), e);
        } catch (SecurityException e) {
            Log.e("ExifUtil.class", e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            Log.e("ExifUtil.class", e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            Log.e("ExifUtil.class", e.getMessage(), e);
        } catch (InstantiationException e) {
            Log.e("ExifUtil.class", e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Log.e("ExifUtil.class", e.getMessage(), e);
        } catch (InvocationTargetException e) {
            Log.e("ExifUtil.class", e.getMessage(), e);
        } catch (NoSuchFieldException e) {
            Log.e("ExifUtil.class", e.getMessage(), e);
        }

        return orientation;
    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
                                                     int reqHeight) {
        Bitmap bmp = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            //BitmapFactory.decodeFile(path, options);

            options.inSampleSize = 2;// calculateInSampleSize(options, reqWidth,
            // reqHeight);

            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = false; // Disable Dithering mode

            options.inPurgeable = true; // Tell to gc that whether it needs free
            // memory,
            // the Bitmap can be cleared

            options.inInputShareable = true; // Which kind of reference will be used
            // to
            // recover the Bitmap data after
            // being
            // clear, when it will be used in
            // the future
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Log.v("Image path:-------", path);

            bmp = BitmapFactory.decodeFile(path, options);

        } catch (Exception e) {
            Log.e("ExifUtil.class", e.getMessage(), e);
            Log.v("Inside catch-------", path);
        }
        return bmp;
    }

}