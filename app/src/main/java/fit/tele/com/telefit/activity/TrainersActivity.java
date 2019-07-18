package fit.tele.com.telefit.activity;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityTrainersBinding;

public class TrainersActivity extends BaseActivity {

    ActivityTrainersBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_trainers;
    }

    @Override
    public void init() {
        binding = (ActivityTrainersBinding) getBindingObj();

    }
}
