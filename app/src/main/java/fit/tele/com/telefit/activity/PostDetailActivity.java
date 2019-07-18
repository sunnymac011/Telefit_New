package fit.tele.com.telefit.activity;

import android.text.TextUtils;
import android.view.View;

import com.squareup.picasso.Picasso;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityPostDetailBinding;
import fit.tele.com.telefit.modelBean.CreatePostBean;

public class PostDetailActivity extends BaseActivity {

    ActivityPostDetailBinding binding;
    CreatePostBean createPostBean = new CreatePostBean();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_post_detail;
    }

    @Override
    public void init() {

        binding = (ActivityPostDetailBinding)getBindingObj();

        if(getIntent()!=null){
            createPostBean  = getIntent().getParcelableExtra("postDetail");
        }

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (createPostBean!=null) {
            if (createPostBean.getPost_img() != null && !createPostBean.getPost_img().equalsIgnoreCase("")
                    && !TextUtils.isEmpty(createPostBean.getPost_img())) {
                Picasso.with(context)
                        .load(createPostBean.getPost_img())
                        .error(R.drawable.camera_new)
                        .placeholder(R.drawable.camera_new)
                      //  .transform(new CircleTransform())
                        .into(binding.imgCreatepost);
            }

            if (createPostBean.getPost_desc()!=null && !TextUtils.isEmpty(createPostBean.getPost_desc())){
                binding.lblViewpost.setText(createPostBean.getPost_desc());
            }

        }



    }
}
