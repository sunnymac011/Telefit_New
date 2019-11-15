package fit.tele.com.telefit.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fit.tele.com.telefit.BuildConfig;
import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityEditProfileBinding;
import fit.tele.com.telefit.dialog.MediaOption;
import fit.tele.com.telefit.dialog.SetHeightDialog;
import fit.tele.com.telefit.dialog.SetNumbersDialog;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.themes.EditProfileActivityTheme;
import fit.tele.com.telefit.utils.CircleTransform;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static fit.tele.com.telefit.utils.CommonUtils.decodeSampledBitmapFromResource;

public class EditProfileActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    ActivityEditProfileBinding binding;
    LoginBean saveLogiBean;
    private DatePickerDialog dpd;
    HashMap<String, String> map;
    private static final int SELECT_PICTURE = 1, SELECT_PICTURE_CAMARA = 2;
    private Uri outputFileUri;
    private File fileProfile;
    GoogleApiClient mGoogleApiClient;
    private SetNumbersDialog setNumbersDialog;
    private SetHeightDialog setHeightDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public void init() {
        if (!isGooglePlayServicesAvailable()) {
            CommonUtils.toast(this,"GPS Error Restart app");
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        binding = (ActivityEditProfileBinding) getBindingObj();
        EditProfileActivityTheme editProfileActivityTheme = new EditProfileActivityTheme();
        editProfileActivityTheme.setTheme(binding, context);
        map = new HashMap<>();
        setListner();
        setData();
    }

    private void setListner(){
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ProfileActivity.class));
            }
        });

        List<String> list = new ArrayList<>();
        list.add("Male");
        list.add("Female");
        list.add("Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_custom_spinner, list);
        binding.spiGender.setAdapter(adapter);

        List<String> list1 = new ArrayList<>();
        list1.add("Sedentary");
        list1.add("Lightly active");
        list1.add("Moderately active");
        list1.add("Very active");
        list1.add("Extra active");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.item_custom_spinner, list1);
        binding.spiActivity.setAdapter(adapter1);

        List<String> list2 = new ArrayList<>();
        list2.add("Lose 1 pound per week");
        list2.add("Lose 1.5 pound per week");
        list2.add("Lose 2 pounds per week");
        list2.add("Gain 0.5 pound per week");
        list2.add("Gain 1 pound per week");
        list2.add("Gain 1.5 pounds per week");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.item_custom_spinner, list2);
        binding.spiMaintain.setAdapter(adapter2);

        binding.btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomeShit();
            }
        });

        binding.txtHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeightDialog = new SetHeightDialog(context, "Set Height", new SetHeightDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String strNumbers, String strNumbersTwo, String strWeightType) {
                        if (strWeightType.equalsIgnoreCase("cm"))
                        {
                            binding.txtHeight.setText(strNumbers);
                            binding.txtWeightType.setText("kg");
                        }
                        else
                        {
                            binding.txtHeight.setText(strNumbers+"'"+strNumbersTwo);
                            binding.txtWeightType.setText("lbs");
                        }
                        binding.txtHeightType.setText(strWeightType);
                    }
                });
                setHeightDialog.show();
            }
        });

        binding.txtWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNumbersDialog = new SetNumbersDialog(context, "Set Weight", false,false, new SetNumbersDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String strNumbers, String strNumbersTwo, String strWeightType) {
                        binding.txtWeight.setText(strNumbers);
                    }
                });
                setNumbersDialog.show();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation())
                    callEditProfileApi();
            }

            private boolean validation() {
                if (binding.spiGender.getSelectedItem().toString().isEmpty()) {
                    CommonUtils.toast(context,"Please select Gender!");
                    return false;
                } else if (binding.inputFname.getText().toString().isEmpty()) {
                    binding.inputFname.setError("Please enter First name");
                    return false;
                } else if (binding.inputLname.getText().toString().isEmpty()) {
                    binding.inputLname.setError("Please enter Last name");
                    return false;
                } else if (binding.txtHeight.getText().toString().isEmpty()) {
                    binding.txtHeight.setError("Please enter Height!");
                    return false;
                } else if (binding.txtWeight.getText().toString().isEmpty()) {
                    binding.txtWeight.setError("Please enter Weight!");
                    return false;
                } else if (binding.inputAddress.getText().toString().isEmpty()) {
                    binding.inputAddress.setError("Please enter Address!");
                    return false;
                } else if (binding.inputEmail.getText().toString().isEmpty()) {
                    binding.inputEmail.setError("Please enter Email!");
                    return false;
                } else if (!CommonUtils.isValidEmail(binding.inputEmail.getText().toString().trim())) {
                    CommonUtils.toast(context, "Please enter valid email");
                    return false;
                } else if (fileProfile == null && saveLogiBean.getProfilePic() == null) {
                    CommonUtils.toast(context,"Please upload profile image!");
                    return false;
                } else if (binding.edtMm.getText().toString().isEmpty() || binding.edtDd.getText().toString().isEmpty() || binding.edtYyyy.getText().toString().isEmpty()) {
                    CommonUtils.toast(context, "Please enter Birth Date!");
                    return false;
                } else
                    return true;
            }
        });
    }

    private void setData() {
        saveLogiBean = preferences.getUserDataPref();
        if (saveLogiBean != null && saveLogiBean.getProfilePic() != null && !TextUtils.isEmpty(saveLogiBean.getProfilePic())) {
            Picasso.with(this)
                    .load(saveLogiBean.getProfilePic())
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder)
                    .transform(new CircleTransform())
                    .into(binding.imgUser);
        }

        if (saveLogiBean != null && saveLogiBean.getName() != null
                && !TextUtils.isEmpty(saveLogiBean.getName()))
            binding.inputFname.setText(saveLogiBean.getName());
        if (saveLogiBean != null && saveLogiBean.getlName() != null
                && !TextUtils.isEmpty(saveLogiBean.getlName()))
            binding.inputLname.setText(saveLogiBean.getlName());
        if (saveLogiBean != null && saveLogiBean.getEmail() != null
                && !TextUtils.isEmpty(saveLogiBean.getEmail())) {
            binding.inputEmail.setText(saveLogiBean.getEmail());
            binding.inputEmail.setEnabled(false);
        }
        else
            binding.inputEmail.setEnabled(true);
        if (saveLogiBean != null && saveLogiBean.getAddress() != null
                && !TextUtils.isEmpty(saveLogiBean.getAddress()))
            binding.inputAddress.setText(saveLogiBean.getAddress());
        if (saveLogiBean != null && saveLogiBean.getDob() != null
                && !TextUtils.isEmpty(saveLogiBean.getDob()))
        {
            String[] items1 = saveLogiBean.getDob().split("[/-]");
            String year = items1[0];
            String month = items1[1];
            String date1 = items1[2];

            binding.edtMm.setText(month);
            binding.edtDd.setText(date1);
            binding.edtYyyy.setText(year);
        }
        if (saveLogiBean != null && saveLogiBean.getGender() != null
                && !TextUtils.isEmpty(saveLogiBean.getGender())) {
            if (saveLogiBean.getGender().equalsIgnoreCase("male"))
                binding.spiGender.setSelection(0);
            if (saveLogiBean.getGender().equalsIgnoreCase("female"))
                binding.spiGender.setSelection(1);
            if (saveLogiBean.getGender().equalsIgnoreCase("other"))
                binding.spiGender.setSelection(2);
        }
        if (saveLogiBean != null && saveLogiBean.getActivity() != null
                && !TextUtils.isEmpty(saveLogiBean.getActivity())) {
            if (saveLogiBean.getActivity().equalsIgnoreCase("Sedentary"))
                binding.spiActivity.setSelection(0);
            if (saveLogiBean.getActivity().equalsIgnoreCase("Lightly active"))
                binding.spiActivity.setSelection(1);
            if (saveLogiBean.getActivity().equalsIgnoreCase("Moderately active"))
                binding.spiActivity.setSelection(2);
            if (saveLogiBean.getActivity().equalsIgnoreCase("Very active"))
                binding.spiActivity.setSelection(3);
            if (saveLogiBean.getActivity().equalsIgnoreCase("Extra active"))
                binding.spiActivity.setSelection(4);
        }
        if (saveLogiBean != null && saveLogiBean.getMaintainWeight() != null
                && !TextUtils.isEmpty(saveLogiBean.getMaintainWeight())) {
            if (saveLogiBean.getMaintainWeight().equalsIgnoreCase("Lose 1 pound per week"))
                binding.spiMaintain.setSelection(0);
            if (saveLogiBean.getMaintainWeight().equalsIgnoreCase("Lose 1.5 pound per week"))
                binding.spiMaintain.setSelection(1);
            if (saveLogiBean.getMaintainWeight().equalsIgnoreCase("Lose 2 pounds per week"))
                binding.spiMaintain.setSelection(2);
            if (saveLogiBean.getMaintainWeight().equalsIgnoreCase("Gain 0.5 pound per week"))
                binding.spiMaintain.setSelection(3);
            if (saveLogiBean.getMaintainWeight().equalsIgnoreCase("Gain 1 pound per week"))
                binding.spiMaintain.setSelection(4);
            if (saveLogiBean.getMaintainWeight().equalsIgnoreCase("Gain 1.5 pounds per week"))
                binding.spiMaintain.setSelection(5);
        }
        if (saveLogiBean != null && saveLogiBean.getHeight() != null
                && !TextUtils.isEmpty(saveLogiBean.getHeight()))
            binding.txtHeight.setText(saveLogiBean.getHeight());
        if (saveLogiBean != null && saveLogiBean.getWeight() != null
                && !TextUtils.isEmpty(saveLogiBean.getWeight()))
            binding.txtWeight.setText(saveLogiBean.getWeight());
        if (saveLogiBean != null && saveLogiBean.getWeightType() != null
                && !TextUtils.isEmpty(saveLogiBean.getWeightType()))
            binding.txtWeightType.setText(saveLogiBean.getWeightType());
    }

    private void datePicker() {
//        if (dpd == null || !dpd.isVisible()) {
//            Calendar now = Calendar.getInstance();
//            int year = now.get(Calendar.YEAR);
//            int mm = now.get(Calendar.MONTH);
//            int dd = now.get(Calendar.DAY_OF_MONTH);
//
//            if(binding.txtDob != null && !binding.txtDob.getText().toString().equalsIgnoreCase("Birth Date")) {
//                String[] date = binding.txtDob.getText().toString().split("/");
//                if(date.length == 3) {
//                    dd = Integer.parseInt(date[1]);
//                    mm = Integer.parseInt(date[0]) - 1;
//                    year = Integer.parseInt(date[2]);
//                }
//            }
//
//            dpd = DatePickerDialog.newInstance(EditProfileActivity.this, year, mm, dd);
//            dpd.setThemeDark(false);
//            dpd.vibrate(false);
//            dpd.dismissOnPause(true);
//            dpd.setMaxDate(now);
//            dpd.showYearPickerFirst(false);
//            dpd.setVersion(DatePickerDialog.Version.VERSION_1);
//            dpd.setAccentColor(ContextCompat.getColor(context, R.color.colorAccent));
//            dpd.setTitle("Select date");
//
//            dpd.show(getFragmentManager(), "Datepickerdialog");
//        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = ((monthOfYear+1) > 9 ? (monthOfYear+1) : ("0"+(monthOfYear+1))) + "/" + dayOfMonth + "/" + year;

        map.put("bdate", date);
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

                    fileProfile = new File(CommonUtils.compressImage(CommonUtils.getRealPathFromURI(Uri.parse(path), EditProfileActivity.this), EditProfileActivity.this));

                    Picasso.with(context)
                            .load(fileProfile)
                            .error(R.drawable.user_placeholder)
                            .placeholder(R.drawable.user_placeholder)
                            .transform(new CircleTransform())
                            .into(binding.imgUser);

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

                        fileProfile = new File(CommonUtils.compressImage(CommonUtils.getRealPathFromURI(Uri.parse(path), EditProfileActivity.this), EditProfileActivity.this));

                        Picasso.with(context)
                                .load(fileProfile)
                                .error(R.drawable.user_placeholder)
                                .placeholder(R.drawable.user_placeholder)
                                .transform(new CircleTransform())
                                .into(binding.imgUser);
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

    private void callEditProfileApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            map.put("email", binding.inputEmail.getText().toString());
            map.put("name", binding.inputFname.getText().toString());
            map.put("l_name", binding.inputLname.getText().toString());
            map.put("gender", binding.spiGender.getSelectedItem().toString());
            map.put("activity", binding.spiActivity.getSelectedItem().toString());
            map.put("maintain_weight", binding.spiMaintain.getSelectedItem().toString());
            map.put("height", binding.txtHeight.getText().toString());
            map.put("height_type", binding.txtHeightType.getText().toString());
            map.put("weight", binding.txtWeight.getText().toString());
            map.put("weight_type", binding.txtWeightType.getText().toString());
            map.put("address", binding.inputAddress.getText().toString());
            map.put("bdate", binding.edtMm.getText().toString().trim()+"/"+binding.edtDd.getText().toString().trim()+"/"+binding.edtYyyy.getText().toString().trim());

            MultipartBody.Part body = null;
            if (fileProfile != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), fileProfile);
                body = MultipartBody.Part.createFormData("profile_pic", fileProfile.getName(), requestFile);
            }

            Observable<ModelBean<LoginBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context)
                    .editProfileCustomer(CommonUtils.converRequestBodyFromMap(map), body);
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
                                deleteImages();
                                CommonUtils.toast(context, "Profile Updated Successfully");
                                if(loginBean.getResult() != null) {
                                    saveLogiBean = loginBean.getResult();
                                    preferences.saveUserData(saveLogiBean);
                                    startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                                }
                            } else {
                                CommonUtils.toast(context, loginBean.getMessage());
                            }
                        }
                    });
        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    private void deleteImages()
    {
        if (fileProfile != null && fileProfile.exists()) {
            if (fileProfile.delete()) {
                Log.e("file Deleted :" , "fileProfile Deleted");
            } else {
                Log.e("file not Deleted :" , "fileProfile not Deleted");
            }
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

    @Override
    protected void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null)
            dpd.setOnDateSetListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
}
