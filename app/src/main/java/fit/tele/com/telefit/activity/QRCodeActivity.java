package fit.tele.com.telefit.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityQrCodeBinding;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.FileUtils;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class QRCodeActivity extends BaseActivity implements View.OnClickListener {

    ActivityQrCodeBinding binding;
    private BeepManager beepManager;
    private static final int SELECT_PICTURE = 1;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_qr_code;
    }

    private void setListner() {
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);

        binding.barcodeScanner.decodeContinuous(callback);
        binding.barcodeScanner.setStatusText("");
        beepManager = new BeepManager(QRCodeActivity.this);
        binding.barcodeScanner.resume();

        binding.txtBarcode.setOnClickListener(this);
        binding.txtGallery.setOnClickListener(this);
    }

    @Override
    public void init() {
        binding = (ActivityQrCodeBinding) getBindingObj();

        setListner();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.txt_barcode:
                binding.barcodeScanner.resume();
                break;

            case R.id.txt_gallery:
                binding.barcodeScanner.pause();
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
                break;

            case R.id.ll_nutrition:
                intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_profile:
                intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_fitness:
                intent = new Intent(context, FitnessActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_goals:
                intent = new Intent(context, GoalsActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_social:
                intent = new Intent(context, SocialActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                if (data != null && data.getData() != null && FileUtils.getFile(context, data.getData()) != null) {
                    try {
                        Uri contentURI = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        readQRImage(bitmap);
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

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            binding.barcodeScanner.pause();
            if (result.getText() != null)
            {
                try {
                    beepManager.playBeepSoundAndVibrate();
                    Log.e("Barcode Result: " , result.getText());
                    callBarCodeChompApi(result.getText());

                }catch (Exception e)
                {
                    binding.barcodeScanner.resume();
                    Log.e("Scan Exception: " , e.getMessage());
                }
            }
            else
                binding.barcodeScanner.resume();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints)
        {
            System.out.println("Possible Result points = " + resultPoints);
        }
    };

    public void readQRImage(Bitmap bMap) {

        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Reader reader = new MultiFormatReader();// use this otherwise ChecksumException
        try {
            Result result = reader.decode(bitmap);
            Log.e("Barcode Result11: " , result.getText());
        } catch (NotFoundException e) {

        }
        catch (ChecksumException e) { Log.e("ChecksumException: " , e.getMessage().toString()); }
        catch (FormatException e) { Log.e("FormatException: " , e.getMessage().toString()); }

    }

    private void callBarCodeChompApi(String strCode) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ResponseBody> signupusers = FetchServiceBase.getChompFetcherService(context).getBarCodeChomp("2smoc98kRRQNtsihq8",strCode);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callBarCodeChompApi "," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ResponseBody chompJSON) {
                            binding.progress.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject(chompJSON.string());
                                JSONObject productObject = jsonObject.getJSONObject("products");
                                Iterator<String> iter = productObject.keys();
                                while (iter.hasNext()) {
                                    String key = iter.next();
                                    String value = productObject.getString(key);
                                    ChompProductBean productBean = new Gson().fromJson(value.toString(), ChompProductBean.class);
                                    Intent intent = new Intent(context, AddFoodActivity.class);
                                    intent.putExtra("from","FoodAdapter");
                                    intent.putExtra("SelectedItems",productBean);
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                Log.e("Exception ",""+e.getMessage()+"\n msg "+e.getCause());
                                CommonUtils.toast(context,"No Data Found!");
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
