package fit.tele.com.telefit.activity;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityDatabaseCountryBinding;

public class DatabaseCountryActivity extends BaseActivity {

    ActivityDatabaseCountryBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_database_country;
    }

    @Override
    public void init() {
        binding = (ActivityDatabaseCountryBinding) getBindingObj();

    }
}
