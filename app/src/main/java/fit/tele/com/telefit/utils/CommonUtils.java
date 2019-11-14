package fit.tele.com.telefit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CommonUtils {
    //public static final String URL_STORAGE_REFERENCE = "gs://gatecrasher-partner.appspot.com";
    //public static final String FOLDER_STORAGE_IMG = "images";

    public static String local(String latitudeFinal, String longitudeFinal){
        return "https://maps.googleapis.com/maps/api/staticmap?center="+latitudeFinal+","+longitudeFinal+"&zoom=18&size=280x280&markers=color:red|"+latitudeFinal+","+longitudeFinal;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static Boolean checkEmail(String email) {
        Pattern p = Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_])+(\\.([a-zA-Z]{2,4}+))?(\\.([a-zA-Z]{2,4}))$");
        Matcher m = p.matcher(email);
        return !m.matches();
    }

    public static Boolean checkDate(String date) {
        Pattern p = Pattern.compile("(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\\\d\\\\d)");
        Matcher m = p.matcher(date);
        return !m.matches();
    }

    public static boolean checkFloat(String input) {
        Pattern p = Pattern.compile(
                "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                        "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                        "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                        "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");
        Matcher m = p.matcher(input);
        return !m.matches();
    }

    public static boolean checkYoutubeUrl(String inputurl) {
        Pattern p = Pattern.compile("^(https?\\:\\/\\/)?(www\\.)?(youtube\\.com|youtu\\.?be)\\/.+$");
        Matcher m = p.matcher(inputurl);
        return m.matches();
    }

    public static void toast(Context context, String msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isInternetOn(Context context) {
        final boolean[] haveConnectedWifi = {false};
        final boolean[] haveConnectedMobile = {false};

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService
                    (Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi[0] = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile[0] = true;
            }
        } catch (Exception e) {
            haveConnectedWifi[0] = true;
            e.printStackTrace();
        }
        return haveConnectedWifi[0] || haveConnectedMobile[0];

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Map<String, RequestBody> converRequestBodyFromMap(Map<String, String> map) {
        Map<String, RequestBody> map1 = new HashMap<>();
        int size = map.size();
        for (int i = 0; i < size; i++) {
            try {
                String key = (String) map.keySet().toArray()[i];
                RequestBody value = RequestBody.create(MediaType.parse("text/plain"), map.get(key));
                map1.put(key, value);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        return map1;
    }

    public static ArrayList<MultipartBody.Part> converRequestBodyFromMapImage(Map<String, File> map, String[] type) {
        ArrayList<MultipartBody.Part> map1 = new ArrayList<>();
        int size = map.size();
        RequestBody requestFile = null;

        for (int i = 0; i < size; i++) {
            try {
                String key = (String) map.keySet().toArray()[i];
                switch (type[i]) {
                    case "1":
                        requestFile = RequestBody.create(MediaType.parse("video/mp4"), map.get(key));
                        break;

                    case "2":
                        requestFile = RequestBody.create(MediaType.parse("image/*"), map.get(key));
                        break;

                    case "3":
                        requestFile = RequestBody.create(MediaType.parse("audio/m4a"), map.get(key));
                        break;

                    case "4":
                        requestFile = RequestBody.create(MediaType.parse("audio/m4a"), map.get(key));
                        break;
                }
                MultipartBody.Part body = MultipartBody.Part.createFormData(key, map.get(key).getName(), requestFile);
                map1.add(body);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        return map1;
    }

//    public static String GetTimeZoneValue(List<CoutryCodeBean> loginBean){
//        String time = "";
//        if(loginBean != null && loginBean.size() > 0 &&
//                loginBean.get(0).getTimezones() != null && loginBean.get(0).getTimezones().size() > 0
//                && loginBean.get(0).getTimezones().get(0)!=null)
//            time = loginBean.get(0).getTimezones().get(0).replaceAll("[^\\d:+]", "");
//        else
//            time = StaticField.time;
//        return time;
//    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdFormat = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.getDefault());
        return mdFormat.format(calendar.getTime());
    }

    public static String compressImage(String imageUri, Activity activity) {

        String filePath = getRealPathFromURI(imageUri, activity);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

//        float maxHeight = 816.0f;
//        float maxWidth = 612.0f;
        float maxHeight = 1632.0f;
        float maxWidth = 1224.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            //   Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                //    Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                //      Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                //     Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "telefit/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public static String getRealPathFromURI(String contentURI, Activity activity) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    public static String getRealPathFromURI(Uri contentURI, Activity activity) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null,
                null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static Bitmap decodeSampledBitmapFromResource(String resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(resId, options);
    }

    public static void displayLocationSettingsRequest(Context context, final Activity activity) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
//                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(activity, 15);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("SettingRequest", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("SettingRequest", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    public static Integer getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);

        return ageInt;
    }
}
