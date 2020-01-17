package fit.tele.com.telefit.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fit.tele.com.telefit.BuildConfig;
import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityCreatePostBinding;
import fit.tele.com.telefit.dialog.AddGoalsDialog;
import fit.tele.com.telefit.dialog.MediaOption;
import fit.tele.com.telefit.dialog.SharePostDialog;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.FileUtils;
import fit.tele.com.telefit.utils.Preferences;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static fit.tele.com.telefit.utils.CommonUtils.decodeSampledBitmapFromResource;

public class CreatePost extends BaseActivity {

    ActivityCreatePostBinding binding;
    MediaOption mediaOption;
    private static final int SELECT_PICTURE = 1, SELECT_PICTURE_CAMARA = 2;
    private Uri outputFileUri;
    private File fileProfile;
    SharePostDialog sharePostDialog;
    AddGoalsDialog addGoalsDialog;
    Preferences preferences;
    Boolean is_snapchat=true,is_facebook=true,is_instagram=true,is_twitter=true,is_fried=true,is_trainer=true;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_create_post;
    }

    @Override
    public void init() {
        binding = (ActivityCreatePostBinding)getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.imgCreatepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomeShit();
            }
        });

        binding.txtNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation()) {
              //      callCreatePost();

                    if (is_fried){
                        callCreatePost();
                    }else {
                        sharePostDialog = new SharePostDialog(context, false, null, new SharePostDialog.SetDataListener() {

                            @Override
                            public void onContinueClick() {
                                Intent in = new Intent(CreatePost.this, SocialActivity.class);
                                setResult(RESULT_OK, in);
                                finish();
                            }

                            @Override
                            public void shareOnFacebook() {
                                if (isFacebookAvailable())
                                    shareOnFacebookNew();
                                else
                                    CommonUtils.toast(context, "Facebook is not installed on your phone");
                            }

                            @Override
                            public void shareOnInstagram() {
                                shareOnInsta();
                            }

                            @Override
                            public void shareOnSnapChat() {
                                shareSnapchat();
                            }

                            @Override
                            public void shareOnTwitter() {

                                if (!isTwitterAvailable()) {
                                    CreatePost.this.shareOnTwitter(binding.inputPost.getText().toString(), Uri.fromFile(fileProfile));
                                } else {
                                    CommonUtils.toast(context, "Twitter is not installed on your phone");
                                }

                                //CreatePost.this.shareOnTwitter(binding.inputPost.getText().toString(), Uri.fromFile(fileProfile));
                                // shareTwitter(binding.inputPost.getText().toString());
                            }
                        }, is_facebook, is_instagram, is_twitter, is_snapchat);
                        sharePostDialog.show();
                    }
                }
            }
        });


        preferences = new Preferences(this);
        if(preferences.getUserDataPref().getIs_snapchat_share().equals("1")){
            is_snapchat = true;
        }else {
            is_snapchat = false;
        }
        if(preferences.getUserDataPref().getIs_facebook_share().equals("1")){
            is_facebook = true;
        }else {
            is_facebook = false;
        }
        if(preferences.getUserDataPref().getIs_instagram_share().equals("1")){
            is_instagram = true;
        }else {
            is_instagram = false;
        }
        if(preferences.getUserDataPref().getIs_twiter_share().equals("1")){
            is_twitter = true;
        }else {
            is_twitter = false;
        }
        if(preferences.getUserDataPref().getIs_friend_share().equals("1")){
            is_fried = true;
        }else {
            is_fried = false;
        }
        if(preferences.getUserDataPref().getIs_trainer_share().equals("1")){
            is_trainer = true;
        }else {
            is_trainer = false;
        }

    }

    private boolean validation() {
        if (binding.inputPost.getText().toString().isEmpty()) {
            CommonUtils.toast(context, "Please write Something for create post!");
            return false;
        }else {
            return true;
        }
    }

    private void openBottomeShit() {

        mediaOption = new MediaOption(context, new MediaOption.AlertListener() {
            @Override
            public void onCameraClick() {
                RxPermissions rxPermissions1 = new RxPermissions((Activity) context);
                rxPermissions1.requestEach(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Action1<Permission>() {
                            @Override
                            public void call(Permission permission) { // will emit 2 Permission objects
                                if (permission.granted) {
                                    if (permission.name.equals("android.permission.CAMERA"))
                                        return;
                                    if (permission.name.equals("android.permission.WRITE_EXTERNAL_STORAGE"))
                                        return;
                                    if (CommonUtils.hasPermissions(context, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                        OnclickCamara();
                                    }

                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    CommonUtils.toast(context, getString(R.string.image_permission_txt));
                                }
                            }
                        });
            }

            @Override
            public void onGalleryClick() {
                RxPermissions rxPermissions = new RxPermissions((Activity) context);
                rxPermissions.requestEach(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Action1<Permission>() {
                            @Override
                            public void call(Permission permission) { // will emit 2 Permission objects
                                if (permission.granted) {
                                    if (permission.name.equals("android.permission.CAMERA"))
                                        return;
                                    if (permission.name.equals("android.permission.WRITE_EXTERNAL_STORAGE"))
                                        return;
                                    if (CommonUtils.hasPermissions(context, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                        OnclickGallery();
                                    }
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    CommonUtils.toast(context, getString(R.string.image_permission_txt));
                                }
                            }
                        });
            }
        });
        mediaOption.show();
    }


    public void OnclickCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ignored) {
            }
            if (photoFile != null) {
                Uri photoURI;
                if (Build.VERSION.SDK_INT >= 24)
                    photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                else
                    photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, SELECT_PICTURE_CAMARA);
            }
        }
    }

    public void OnclickGallery() {
        List<Intent> targets = new ArrayList<>();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        List<ResolveInfo> candidates = context.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo candidate : candidates) {
            String packageName = candidate.activityInfo.packageName;
            if (!packageName.equals("com.google.android.apps.photos") && !packageName.equals("com.google.android.apps.plus") && !packageName.equals("com.android.documentsui")) {
                Intent iWantThis = new Intent();
                iWantThis.setType("image/*");
                iWantThis.setAction(Intent.ACTION_PICK);
                iWantThis.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                iWantThis.setPackage(packageName);
                targets.add(iWantThis);
            }
        }
        if (targets.size() > 0) {
            Intent chooser = Intent.createChooser(targets.remove(0), "Select Picture");
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(new Parcelable[targets.size()]));
            startActivityForResult(chooser, SELECT_PICTURE);
        } else {
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType("image/*");
            startActivityForResult(Intent.createChooser(intent1, "Select Picture"), SELECT_PICTURE);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        outputFileUri = Uri.fromFile(image);
        return image;
    }

    Bitmap bit = null,adjustedBitmap;
    ExifInterface exif;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Place123 ",requestCode+" "+data);
        if (resultCode == RESULT_OK) {
            File finalFile;
            if (requestCode == SELECT_PICTURE_CAMARA) {
                try {
                    finalFile = new File((FileUtils.getFile(context, outputFileUri).getPath()));
                    bit = decodeSampledBitmapFromResource(finalFile.getPath(), 250, 250);
                    exif = new ExifInterface(finalFile.getPath());

                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int rotationInDegrees = CommonUtils.exifToDegrees(rotation);
                    Matrix matrix = new Matrix();
                    if (rotation != 0f) {
                        matrix.preRotate(rotationInDegrees);
                    }

                    adjustedBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);

                    String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), adjustedBitmap, "Title", null);

                    fileProfile = new File(CommonUtils.compressImage(CommonUtils.getRealPathFromURI(Uri.parse(path), CreatePost.this), CreatePost.this));

                    Picasso.with(context)
                            .load(fileProfile)
                            .error(R.drawable.user_placeholder)
                            .placeholder(R.drawable.user_placeholder)
                         //   .transform(new CircleTransform())
                            .into(binding.imgCreatepost);

                }catch (Exception e)
                {

                }
            }
            if (requestCode == SELECT_PICTURE) {
                if (data != null && data.getData() != null && FileUtils.getFile(context, data.getData()) != null) {
                    try {
                        finalFile = new File((FileUtils.getFile(context, data.getData()).getPath()));

                        bit = decodeSampledBitmapFromResource(finalFile.getPath(), 250, 250);
                        exif = new ExifInterface(finalFile.getPath());

                        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int rotationInDegrees = CommonUtils.exifToDegrees(rotation);
                        Matrix matrix = new Matrix();
                        if (rotation != 0f) {
                            matrix.preRotate(rotationInDegrees);
                        }

                        adjustedBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);

                        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), adjustedBitmap, "Title", null);

                        fileProfile = new File(CommonUtils.compressImage(CommonUtils.getRealPathFromURI(Uri.parse(path), CreatePost.this), CreatePost.this));

                        Picasso.with(context)
                                .load(fileProfile)
                                .error(R.drawable.user_placeholder)
                                .placeholder(R.drawable.user_placeholder)
                              //  .transform(new CircleTransform())
                                .into(binding.imgCreatepost);
                    }catch (Exception e)
                    {

                    }
                } else
                    CommonUtils.toast(context, context.getString(R.string.toast_image_empty));
            }
        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }



    private void callCreatePost() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String,String> map = new HashMap<>();
            map.put("post_desc", binding.inputPost.getText().toString());

            MultipartBody.Part body = null;
            if (fileProfile != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), fileProfile);
                body = MultipartBody.Part.createFormData("post_img", fileProfile.getName(), requestFile);
            }

            Observable<ModelBean<LoginBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context)
                    .createPost(CommonUtils.converRequestBodyFromMap(map), body);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<LoginBean>>() {
                        @Override
                        public void onCompleted() {
                            binding.progress.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            binding.progress.setVisibility(View.GONE);
                            CommonUtils.toast(getApplicationContext(), e.getMessage());
                        }
                        @Override
                        public void onNext(ModelBean<LoginBean> loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (loginBean.getStatus() == 1) {
                                CommonUtils.toast(context, "Your post is successfully created.");

  //                              shareOnFacebook();

                                sharePostDialog = new SharePostDialog(context,false,null,  new SharePostDialog.SetDataListener() {

                                    @Override
                                    public void onContinueClick() {
                                        Intent in = new Intent(CreatePost.this,SocialActivity.class);
                                        setResult(RESULT_OK,in);
                                        finish();
                                    }

                                    @Override
                                    public void shareOnFacebook() {
                                        if(isFacebookAvailable())
                                            shareOnFacebookNew();
                                        else
                                            CommonUtils.toast(context,"Facebook not installed");

                                    }

                                    @Override
                                    public void shareOnInstagram() {
                                        shareOnInsta();
                                    }

                                    @Override
                                    public void shareOnSnapChat() {
                                        shareSnapchat();
                                    }

                                    @Override
                                    public void shareOnTwitter() {
                                        if (!isTwitterAvailable()) {
                                            CreatePost.this.shareOnTwitter(binding.inputPost.getText().toString(), Uri.fromFile(fileProfile));
                                        }else {
                                            CommonUtils.toast(context, "Twitter is not installed on your phone");
                                        }
//                                        shareTwitter(binding.inputPost.getText().toString());


                                    }
                                },is_facebook,is_instagram,is_twitter,is_snapchat);
                                sharePostDialog.show();


                            } else {
                                CommonUtils.toast(context, loginBean.getMessage());
                            }
                        }
                    });
        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    public void shareOnFacebookNew() {
        if (adjustedBitmap != null) {
            if (validation()) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(adjustedBitmap)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                ShareDialog shareDialog = new ShareDialog(this);
                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
            }
        }else {
            CommonUtils.toast(context,"Please select photo upload on social media");
        }
    }

    public boolean isFacebookAvailable() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Test; please ignore");
        intent.setType("text/plain");
        final PackageManager pm = this.getApplicationContext().getPackageManager();
        for(ResolveInfo resolveInfo: pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)){
            ActivityInfo activity = resolveInfo.activityInfo;
            if (activity.name.contains("com.facebook")) {
                return true;
            }
        }
        return false;
    }

    public void shareOnInsta(){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null)
        {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.instagram.android");
            try {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),adjustedBitmap , "Telefit", binding.inputPost.getText().toString())));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            shareIntent.setType("image/jpeg");

            startActivity(shareIntent);
        }
        else
        {
            // bring user to the market to download the app.
            // or let them choose an app?
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.setData(Uri.parse("market://details?id="+"com.instagram.android"));
            startActivity(intent1);
        }
    }



    private void shareTwitter(String message) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, "This is a Test.");
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
            startActivity(i);
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("Check", "UTF-8 should always be supported", e);
            return "";
        }
    }

    public void shareSnapchat(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.setPackage("com.snapchat.android");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),adjustedBitmap , "Telefit", binding.inputPost.getText().toString())));
        startActivity(Intent.createChooser(intent, "Open Snapchat"));
    }

    public boolean isTwitterAvailable() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Test; please ignore");
        intent.setType("text/plain");
        final PackageManager pm = this.getApplicationContext().getPackageManager();
        for(ResolveInfo resolveInfo: pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)){
            ActivityInfo activity = resolveInfo.activityInfo;
            // Log.i("actividad ->", activity.name);
            if (activity.name.contains("com.twitter.android")) {
                return true;
            }
        }
        return false;
    }

    public void shareOnTwitter(String textBody, Uri fileUri) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage("com.twitter.android");
            intent.putExtra(Intent.EXTRA_TEXT, !TextUtils.isEmpty(textBody) ? textBody : "");

            if (fileUri != null) {
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");
            }

            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
               // showWarningDialog(appCompatActivity, appCompatActivity.getString(R.string.error_activity_not_found));
            }
        }



}
