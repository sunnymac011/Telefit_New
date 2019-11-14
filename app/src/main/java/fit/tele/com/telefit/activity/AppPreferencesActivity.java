package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.PopupMenu;

import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityAppPreferencesBinding;
import fit.tele.com.telefit.databinding.ActivityNotificationSettingsBinding;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppPreferencesActivity extends BaseActivity {

    ActivityAppPreferencesBinding binding;
    PopupMenu popup;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_app_preferences;
    }

    @Override
    public void init() {
        binding = (ActivityAppPreferencesBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setListener();
    }

    private void setListener() {
        binding.txtList.setText(preferences.getMyFoodListPref());
        if (preferences.getScreenOnPref().equalsIgnoreCase("1"))
            binding.switchScreenOn.setChecked(true);
        else
            binding.switchScreenOn.setChecked(false);
        if (preferences.getLookScreenPref().equalsIgnoreCase("1"))
            binding.switchLookScreen.setChecked(true);
        else
            binding.switchLookScreen.setChecked(false);
        if (preferences.getSoundPref().equalsIgnoreCase("1"))
            binding.switchSound.setChecked(true);
        else
            binding.switchSound.setChecked(false);
        if (preferences.getVibratePref().equalsIgnoreCase("1"))
            binding.switchVibrate.setChecked(true);
        else
            binding.switchVibrate.setChecked(false);

        binding.txtList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = new PopupMenu(context, binding.txtList);
                popup.inflate(R.menu.menu_myfood);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_one:
                                preferences.saveMyFoodListData(item.getTitle().toString());
                                binding.txtList.setText(item.getTitle().toString());
                                break;
                            case R.id.menu_two:
                                preferences.saveMyFoodListData(item.getTitle().toString());
                                binding.txtList.setText(item.getTitle().toString());
                                break;
                            case R.id.menu_three:
                                preferences.saveMyFoodListData(item.getTitle().toString());
                                binding.txtList.setText(item.getTitle().toString());
                                break;
                            case R.id.menu_four:
                                preferences.saveMyFoodListData(item.getTitle().toString());
                                binding.txtList.setText(item.getTitle().toString());
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        binding.switchScreenOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    preferences.saveScreenOnData("1");
                else
                    preferences.saveScreenOnData("0");
            }
        });

        binding.switchLookScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    preferences.saveLookScreenData("1");
                else
                    preferences.saveLookScreenData("0");
            }
        });

        binding.switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    preferences.saveSoundData("1");
                else
                    preferences.saveSoundData("0");
            }
        });

        binding.switchVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    preferences.saveVibrateData("1");
                else
                    preferences.saveVibrateData("0");
            }
        });

    }
}
