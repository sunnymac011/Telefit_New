package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.CardException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Source;
import com.stripe.android.model.SourceParams;
import com.stripe.android.model.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityPaymentBinding;
import fit.tele.com.telefit.modelBean.ExerciseDetailsBean;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PaymentActivity extends BaseActivity {

    ActivityPaymentBinding binding;
    private Stripe mStripe;
    private Card card;
    LoginBean saveLogiBean;
    private String peckagePrice = "",subscriptionId = "";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_payment;
    }

    @Override
    public void init() {
        binding = (ActivityPaymentBinding) getBindingObj();
        PaymentConfiguration.init(getString(R.string.stripe_key));
        mStripe = new Stripe(this,
                PaymentConfiguration.getInstance().getPublishableKey());
        setData();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    card = new Card(
                            binding.inputCardNumber.getText().toString().trim(),
                            Integer.valueOf(binding.inputMm.getText().toString().trim()),
                            Integer.valueOf(binding.inputYy.getText().toString().trim()),
                            binding.inputCvv.getText().toString().trim()
                    );
                    if (card != null)
                    {
                        if (validateCard(card))
                        {
                            card.setCurrency("usd");
                            card.setName(binding.inputFname.getText().toString()+" "+binding.inputLname.getText().toString());
                            generateSource();
                        }
                    }
                }
            }

            private boolean validation() {
                if (binding.inputCardName.getText().toString().isEmpty()) {
                    CommonUtils.toast(context,"Please enter card name!");
                    return false;
                } else if (binding.inputCardNumber.getText().toString().isEmpty()) {
                    CommonUtils.toast(context,"Please enter card number!");
                    return false;
                } else if (binding.inputCvv.getText().toString().isEmpty()) {
                    CommonUtils.toast(context,"Please enter card CVV!");
                    return false;
                } else if (binding.inputMm.getText().toString().isEmpty()) {
                    CommonUtils.toast(context,"Please enter card Expire Month!");
                    return false;
                } else if (binding.inputYy.getText().toString().isEmpty()) {
                    CommonUtils.toast(context,"Please enter card Expire Year!");
                    return false;
                } else
                    return true;
            }
        });
    }

    private void setData() {
        saveLogiBean = preferences.getUserDataPref();
        if(getIntent() != null && getIntent().hasExtra("packagePrice"))
            peckagePrice = getIntent().getStringExtra("packagePrice");
        if(getIntent() != null && getIntent().hasExtra("subscriptionId"))
            subscriptionId = getIntent().getStringExtra("subscriptionId");
        if (saveLogiBean != null && saveLogiBean.getName() != null
                && !TextUtils.isEmpty(saveLogiBean.getName()))
            binding.inputFname.setText(saveLogiBean.getName());
        if (saveLogiBean != null && saveLogiBean.getlName() != null
                && !TextUtils.isEmpty(saveLogiBean.getlName()))
            binding.inputLname.setText(saveLogiBean.getlName());
        if (saveLogiBean != null && saveLogiBean.getEmail() != null
                && !TextUtils.isEmpty(saveLogiBean.getEmail()))
            binding.inputEmail.setText(saveLogiBean.getEmail());
        if (peckagePrice != null && !TextUtils.isEmpty(peckagePrice))
            binding.txtTotal.setText(peckagePrice);
    }

    private boolean validateCard(Card card){

        if (card.validateCard())
            return true;
        else
            return false;
    }

    private void generateSource() {
        Observable<Source> tokenObservable1 =
                Observable.fromCallable(
                        new Callable<Source>() {
                            @Override
                            public Source call() throws Exception {
                                SourceParams cardSourceParams = SourceParams.createCardParams(card);
                                return mStripe.createSourceSynchronous(cardSourceParams);
                            }
                        });

        tokenObservable1
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(
                        new Action0() {
                            @Override
                            public void call() {
                                binding.progress.setVisibility(View.VISIBLE);
                            }
                        })
                .doOnUnsubscribe(
                        new Action0() {
                            @Override
                            public void call() {
                            }
                        })
                .subscribe(
                        new Action1<Source>() {
                            @Override
                            public void call(Source source) {
                                binding.progress.setVisibility(View.GONE);
                                // Send token to your own web service
                                generateToken(source.getId());
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // Tell the user about the error
                                Log.e("Throwable",throwable.getMessage());
                            }
                        });
    }

    private void generateToken(String source_id) {
        Observable<Token> tokenObservable =
                Observable.fromCallable(
                        new Callable<Token>() {
                            @Override
                            public Token call() throws Exception {
                                return mStripe.createTokenSynchronous(card);
                            }
                        });

        tokenObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(
                        new Action0() {
                            @Override
                            public void call() {
                                binding.progress.setVisibility(View.VISIBLE);
                            }
                        })
                .doOnUnsubscribe(
                        new Action0() {
                            @Override
                            public void call() {
                            }
                        })
                .subscribe(
                        new Action1<Token>() {
                            @Override
                            public void call(Token token) {
                                binding.progress.setVisibility(View.GONE);
                                // Send token to your own web service
//                                Log.e("token",token.getCard().toString());
                                if (peckagePrice != null && !TextUtils.isEmpty(peckagePrice) && subscriptionId != null && !TextUtils.isEmpty(subscriptionId))
                                    callPaymentApi(token.getId(),peckagePrice, subscriptionId,source_id);
                                else
                                    CommonUtils.toast(context,"Some thing went wrong. Please try again later.");
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // Tell the user about the error
                                Log.e("Throwable",throwable.getMessage());
                            }
                        });
    }

    private void callPaymentApi(String stripeToken, String strAmt, String subscription_id, String source_id) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("stripeToken", stripeToken);
            map.put("amount", strAmt);
            map.put("subscription_id", subscription_id);
            map.put("user_name", binding.inputFname.getText().toString().trim()+" "+binding.inputLname.getText().toString().trim());
            map.put("user_source", source_id);
            map.put("user_email", binding.inputEmail.getText().toString().trim());

            Observable<ModelBean<ArrayList<ExerciseDetailsBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getPaymentAPI(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<ExerciseDetailsBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callPaymentApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<ExerciseDetailsBean>> apiExercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiExercisesBean.getStatus().toString().equalsIgnoreCase("1"))
                            {
                                CommonUtils.toast(context,"Payment Successful");
                                Intent intent = new Intent(PaymentActivity.this,CreateRoutineActivity.class);
                                startActivity(intent);
                            }
                            else
                                CommonUtils.toast(context, ""+apiExercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
