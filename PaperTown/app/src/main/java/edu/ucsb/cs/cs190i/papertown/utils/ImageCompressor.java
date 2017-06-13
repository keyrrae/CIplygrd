package edu.ucsb.cs.cs190i.papertown.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageCompressor {
    private CompressFinishedListener compressFinishedListener = null;

    public interface CompressFinishedListener {
        void onCompressFinished(List<String> imgUris);
    }
    public void setOnCompressFinishedListener(CompressFinishedListener listener) {
        this.compressFinishedListener = listener;
    }
    public void doThething(List<String> selected, Activity activity){
        final List<String> inputUris = selected;
        final Activity finalActivity = activity;
        final List<String> outputUris = new ArrayList<>();

        final ProgressDialog progress = new ProgressDialog(finalActivity);
        progress.setTitle("Compressing");
        progress.setMessage("Dealing with images");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        Thread thread = new Thread() {
            @Override
            public void run() {

                for (int i = 0; i < inputUris.size(); i++) {
                    Uri uri = Uri.parse(inputUris.get(i));
                    String[] split = uri.toString().split(":");
                    Log.i("split", "split[0]. = " + split[0]);
                    if (!split[0].equals("file")) {


                        if(!progress.isShowing()) {
                            finalActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.show();
                                }
                            });
                        }
                        uri = compress(inputUris.get(i).toString(), finalActivity.getApplicationContext());
                        outputUris.add(uri.toString());
                    }
                    else {
                        outputUris.add(uri.toString());
                    }
                }
                progress.dismiss();
                if(compressFinishedListener!=null){
                    compressFinishedListener.onCompressFinished(outputUris);
                }

            }

        };
        thread.start();
    }

    public void doThething2(List<Uri> selected, Activity activity){
        final List<Uri> inputUris = selected;
        final List<String> output  = new ArrayList<>();

        for(int i = 0;i<selected.size();i++){
            output.add(selected.get(i).toString());
        }
        doThething(output, activity);
    }

    public Uri compress(String s, Context context){
        //compress the image from the uri
        Uri uri = Uri.parse(s);
        File f = new File(uri.toString());
        f = new File(resizeAndCompressImageBeforeSend(context, uri, "/" + f.getName() + UUID.randomUUID().toString() + System.currentTimeMillis() + ".jpg"));
        uri = (Uri.fromFile(f));
        return uri;
    }

    public Uri compress(Uri s, Context context){
        //compress the image from the uri
        Uri uri = s;
        File f = new File(uri.toString());
        f = new File(resizeAndCompressImageBeforeSend(context, uri, "/" + f.getName() + UUID.randomUUID().toString() + System.currentTimeMillis() + ".jpg"));
        uri = (Uri.fromFile(f));
        return uri;
    }

    public String resizeAndCompressImageBeforeSend(Context context, Uri inputUri, String fileName) {
        String filePath = getRealPathFromURI2(context, inputUri);
        //String filePath = inputUri.toString();
        final int MAX_IMAGE_SIZE = 600 * 800; // max final file size in kilobytes   700 * 1024;

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath, options);


        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        do {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb");
        } while (streamLength >= MAX_IMAGE_SIZE);

        try {
            //save the resized and compressed file to disk cache
            Log.d("compressBitmap", "cacheDir: " + context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir() + fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }
        //return the path of resized and compressed file
        return context.getCacheDir() + fileName;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

    public String getRealPathFromURI(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";


        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public String getRealPathFromURI2(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
