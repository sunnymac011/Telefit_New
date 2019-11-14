package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityAboutTelefitBinding;
import fit.tele.com.telefit.themes.AboutTeleFitActivityTheme;

public class AboutTeleFitActivity extends BaseActivity implements View.OnClickListener {

    ActivityAboutTelefitBinding binding;
    AboutTeleFitActivityTheme aboutTeleFitActivityTheme;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_about_telefit;
    }

    @Override
    public void init() {

        binding = (ActivityAboutTelefitBinding) getBindingObj();
        aboutTeleFitActivityTheme = new AboutTeleFitActivityTheme();
        aboutTeleFitActivityTheme.setTheme(binding,context);
        setListner();

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            binding.txtVersionCount.setText(""+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setListner(){
        binding.rlPrivacy.setOnClickListener(this);
        binding.rlTerms.setOnClickListener(this);
        binding.imgSideBar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.img_side_bar :
                onBackPressed();
                break;

            case R.id.rl_privacy :
                intent = new Intent(this, PrivacyPolicyActivity.class);
                intent.putExtra("from","privacy");
                startActivity(intent);
                break;

            case R.id.rl_terms :
                intent = new Intent(this, PrivacyPolicyActivity.class);
                intent.putExtra("from","terms");
                startActivity(intent);
                break;
        }

        //Changes sunny
        //New thing
        // again
        //HEllo
    }
}
