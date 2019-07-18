package fit.tele.com.telefit.activity;

import android.view.View;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityNotificationSettingsBinding;

public class NotificationSettingsActiviry extends BaseActivity {

    ActivityNotificationSettingsBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_notification_settings;
    }

    @Override
    public void init() {
        binding = (ActivityNotificationSettingsBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
