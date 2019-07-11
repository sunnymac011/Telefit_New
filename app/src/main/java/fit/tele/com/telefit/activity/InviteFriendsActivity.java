package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityInviteFriendsBinding;
import fit.tele.com.telefit.utils.CommonUtils;

public class InviteFriendsActivity extends BaseActivity {

    ActivityInviteFriendsBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_invite_friends;
    }

    @Override
    public void init() {

        binding = (ActivityInviteFriendsBinding)getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.btnSendItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
//
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto",binding.inputEmail.getText().toString(), null));
                   // emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello "+binding.inputFname.getText().toString()+" "
                            +binding.inputLname.getText().toString()+"\n");
                    startActivity(Intent.createChooser(emailIntent, "Send email"));

                }
            }
        });

    }

    private boolean validation() {
        if (binding.inputFname.getText().toString().isEmpty()) {
            binding.inputFname.setError("Please enter First Name!");
            return false;
        } else if (!CommonUtils.isValidEmail(binding.inputEmail.getText().toString().trim())) {
            CommonUtils.toast(context, "Please enter valid email");
            return false;
        } else if (binding.inputLname.getText().toString().isEmpty()) {
            binding.inputLname.setError("Please enter Last Name!");
            return false;
        } else
            return true;
    }
}
