package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityProfileBinding;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.themes.ProfileActivityTheme;
import fit.tele.com.telefit.utils.CircleTransform;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{

    ActivityProfileBinding binding;
    LoginBean saveLogiBean;
    RelativeLayout rl_notifications, rl_themes, rl_logout, rl_units, rl_about;
    TextView txt_themes_count;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_profile;
    }

    @Override
    public void init() {

        binding = (ActivityProfileBinding) getBindingObj();
        ProfileActivityTheme profileActivityTheme = new ProfileActivityTheme();
        profileActivityTheme.setTheme(binding, ProfileActivity.this);
        setListner();
        setData();
    }

    private void setListner() {

        rl_logout = (RelativeLayout) findViewById(R.id.rl_logout);
        rl_notifications = (RelativeLayout) findViewById(R.id.rl_notifications);
        rl_themes = (RelativeLayout) findViewById(R.id.rl_themes);
        rl_units = (RelativeLayout) findViewById(R.id.rl_units);
        rl_about = (RelativeLayout) findViewById(R.id.rl_about);
        txt_themes_count = (TextView) findViewById(R.id.txt_themes_count);
        txt_themes_count.setText(preferences.getTheme());

        binding.llNutrition.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);

        binding.txtEdit.setOnClickListener(this);
        rl_about.setOnClickListener(this);

        binding.llNutritionTab.setOnClickListener(this);
        binding.llFitnessTab.setOnClickListener(this);
        binding.llMoreTab.setOnClickListener(this);

        rl_notifications.setOnClickListener(this);
        rl_themes.setOnClickListener(this);
        rl_units.setOnClickListener(this);

        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.cleanAlltoken();
                Intent intent = new Intent(context, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setData(){
        saveLogiBean = preferences.getUserDataPref();
        if (saveLogiBean != null && saveLogiBean.getProfilePic() != null && !TextUtils.isEmpty(saveLogiBean.getProfilePic())) {
            Picasso.with(this)
                    .load(saveLogiBean.getProfilePic())
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder)
                    .transform(new CircleTransform())
                    .into(binding.imgUser);
        }

        if (saveLogiBean != null && saveLogiBean.getName() != null
                && !TextUtils.isEmpty(saveLogiBean.getName()))
        {
            if (saveLogiBean != null && saveLogiBean.getlName() != null
                    && !TextUtils.isEmpty(saveLogiBean.getlName()))
                binding.txtName.setText(saveLogiBean.getName()+" "+saveLogiBean.getlName());
            else
                binding.txtName.setText(saveLogiBean.getName());
        }
        if (saveLogiBean != null && saveLogiBean.getHeight() != null
                && !TextUtils.isEmpty(saveLogiBean.getHeight()))
        {
            if (saveLogiBean != null && saveLogiBean.getWeight() != null
                    && !TextUtils.isEmpty(saveLogiBean.getWeight()))
                binding.txtHeightWeight.setText(saveLogiBean.getHeight()+"ft / "+saveLogiBean.getWeight()+" kg");
        }

        if (saveLogiBean != null && saveLogiBean.getDob() != null
                && !TextUtils.isEmpty(saveLogiBean.getDob()))
            binding.txtAge.setText(""+getAge(saveLogiBean.getDob()));

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.ll_nutrition :
                intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_fitness :
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

            case R.id.txt_edit :
                intent = new Intent(context, EditProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_nutrition_tab :
                binding.vf.setDisplayedChild(0);
                binding.txtNutritionTab.setTextColor(getResources().getColor(R.color.white));
                binding.viewNutrition.setVisibility(View.VISIBLE);
                binding.txtFitnessTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewFitness.setVisibility(View.GONE);
                binding.txtMoreTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewMore.setVisibility(View.GONE);
                break;

            case R.id.ll_fitness_tab :
                binding.vf.setDisplayedChild(1);
                binding.txtNutritionTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewNutrition.setVisibility(View.GONE);
                binding.txtFitnessTab.setTextColor(getResources().getColor(R.color.white));
                binding.viewFitness.setVisibility(View.VISIBLE);
                binding.txtMoreTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewMore.setVisibility(View.GONE);
                break;

            case R.id.ll_more_tab :
                binding.vf.setDisplayedChild(2);
                binding.txtNutritionTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewNutrition.setVisibility(View.GONE);
                binding.txtFitnessTab.setTextColor(getResources().getColor(R.color.light_gray));
                binding.viewFitness.setVisibility(View.GONE);
                binding.txtMoreTab.setTextColor(getResources().getColor(R.color.white));
                binding.viewMore.setVisibility(View.VISIBLE);
                break;

            case R.id.rl_about :
                intent = new Intent(context, AboutTeleFitActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_themes :
                intent = new Intent(context, ThemesActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_units :
                intent = new Intent(context, UnitsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProfileActivityTheme profileActivityTheme = new ProfileActivityTheme();
        profileActivityTheme.setTheme(binding, ProfileActivity.this);
        txt_themes_count.setText(preferences.getTheme());
    }

    private int getAge(String dobString){

        Log.e("dobString befor ",""+dobString);
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("dobString befor ",""+date);
        if(date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month+1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }



        return age;
    }
}
