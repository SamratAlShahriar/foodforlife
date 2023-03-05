package tws.foodforlife.app.others;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageResizer {
    private static final int MAX_SIZE = 700 * 400;

    public static Bitmap getReduceBitmap(Context context, Uri imageUri) {
        Bitmap bitmap = null;
        bitmap = getBitmap(context, imageUri);
        if (bitmap == null) {
            return null;
        }
        double ratioSquare;
        int bitmapHeight, bitmapWidth;
        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();
        ratioSquare = (bitmapHeight * bitmapWidth) / MAX_SIZE;
        if (ratioSquare <= 1) {
            return bitmap;
        }
        double ratio = Math.sqrt(ratioSquare);
        Log.d("mylog", "Ratio: " + ratio);
        int requiredHeight = (int) Math.round(bitmapHeight / ratio);
        int requiredWidth = (int) Math.round(bitmapWidth / ratio);
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true);
    }

    public static Bitmap getBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static File getFileFromBitmap(Bitmap bitmap) {
        File folder = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "temp");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        byte[] byteData = outputStream.toByteArray();

        try {
            File file = new File(folder, "image.jpeg");
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteData);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Uri getUriFromFile(File file) {
        return Uri.fromFile(file);
    }

    public static Uri uriToReducedUri(Context context, Uri uri) {
        Uri reducedUri = null;
        try {
            Bitmap reduceBitmap = getReduceBitmap(context, uri);
            File convertFile = getFileFromBitmap(reduceBitmap);
            reducedUri = getUriFromFile(convertFile);
        } catch (Exception e) {
            return reducedUri;
        }
        return reducedUri;
    }
}
