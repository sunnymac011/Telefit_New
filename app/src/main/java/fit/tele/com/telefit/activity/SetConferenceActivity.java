package fit.tele.com.telefit.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivitySetConferenceBinding;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SetConferenceActivity extends BaseActivity {

    ActivitySetConferenceBinding binding;
    private TimePickerDialog mTimePicker;
    private String selectedTime = "0", selectedDate = "0";
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Calendar mcurrentTime;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_set_conference;
    }

    @Override
    public void init() {
        binding = (ActivitySetConferenceBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mcurrentTime = Calendar.getInstance();
        binding.txtTimeConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                mTimePicker = new TimePickerDialog(SetConferenceActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        binding.txtTimeConf.setText(selectedHour+":"+selectedMinute);
                        selectedTime = selectedHour+":"+selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        binding.calConf.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = dayOfMonth+"/"+month+"/"+year;
            }
        });

        binding.txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedTime.equalsIgnoreCase("0")) {
                    String currentDate = dateFormat.format(mcurrentTime.getTime());
                    if (selectedDate.equalsIgnoreCase("0"))
                        callSetConferenceApi("1",currentDate,selectedTime);
                    else
                        callSetConferenceApi("1",selectedDate,selectedTime);
                }
                else
                    CommonUtils.toast(context,"Please select time!");
            }
        });
    }

    private void callSetConferenceApi(String strTrainerId,String strDate, String strTime) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("trainer_id", strTrainerId);
            map.put("date", strDate);
            map.put("time", strTime);

            Observable<ModelBean<LoginBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).setConference(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<LoginBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callSetConferenceApi "," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<LoginBean> loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (loginBean.getStatus() == 1) {
                                Intent intent = new Intent(context, VideoConferenceActivity.class);
                                startActivity(intent);
                                finish();
                            } else
                                CommonUtils.toast(context,loginBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
