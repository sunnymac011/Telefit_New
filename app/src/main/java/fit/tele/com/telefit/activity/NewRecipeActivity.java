package fit.tele.com.telefit.activity;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityNewRecipeBinding;

public class NewRecipeActivity extends BaseActivity {

    ActivityNewRecipeBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_new_recipe;
    }

    @Override
    public void init() {
        binding = (ActivityNewRecipeBinding) getBindingObj();
    }
}
