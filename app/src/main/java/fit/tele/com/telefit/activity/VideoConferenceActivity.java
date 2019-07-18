package fit.tele.com.telefit.activity;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityConferenceBinding;

public class VideoConferenceActivity extends BaseActivity {

    ActivityConferenceBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_conference;
    }

    @Override
    public void init() {
        binding = (ActivityConferenceBinding) getBindingObj();

    }
}
