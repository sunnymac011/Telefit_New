package fit.tele.com.telefit.activity;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivitySetConferenceBinding;

public class SetConferenceActivity extends BaseActivity {

    ActivitySetConferenceBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_set_conference;
    }

    @Override
    public void init() {
        binding = (ActivitySetConferenceBinding) getBindingObj();

    }
}
