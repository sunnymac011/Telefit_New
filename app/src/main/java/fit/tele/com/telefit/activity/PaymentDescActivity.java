package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityPaymentDescBinding;
import fit.tele.com.telefit.modelBean.PaymentInfoBean;
import fit.tele.com.telefit.utils.MyTagHandler;

public class PaymentDescActivity extends BaseActivity {

    ActivityPaymentDescBinding binding;
    private PaymentInfoBean paymentInfoBean;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_payment_desc;
    }

    @Override
    public void init() {
        binding = (ActivityPaymentDescBinding) getBindingObj();
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setListner();
    }

    private void setListner() {
        if(getIntent() != null && getIntent().hasExtra("paymentInfo"))
            paymentInfoBean = getIntent().getParcelableExtra("paymentInfo");

        if (paymentInfoBean != null && paymentInfoBean.getTerms() != null
                && !TextUtils.isEmpty(paymentInfoBean.getTerms().getDetails()))
            binding.txtPaymentDesc.setText(Html.fromHtml(paymentInfoBean.getTerms().getDetails()));
        if (paymentInfoBean != null && paymentInfoBean.getUsePackage() != null && paymentInfoBean.getUsePackage().get(0) != null
                && !TextUtils.isEmpty(paymentInfoBean.getUsePackage().get(0).getPrice()))
        {
            binding.txtPrice.setText("Only $ "+paymentInfoBean.getUsePackage().get(0).getPrice());
            binding.rlPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PaymentDescActivity.this,PaymentActivity.class);
                    intent.putExtra("packagePrice",paymentInfoBean.getUsePackage().get(0).getPrice());
                    intent.putExtra("subscriptionId",paymentInfoBean.getUsePackage().get(0).getId());
                    startActivity(intent);
                }
            });
        }
    }
}
