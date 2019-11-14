package fit.tele.com.telefit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.utils.CommonUtils;

public class SetsRepsDialog extends Dialog implements View.OnClickListener {
    private EditText input_sets,input_reps,input_time,input_weight,input_exe_time;
    private Button btn_ok,btn_cancel;
    private LinearLayout ll_all_details;
    private TextInputLayout input_layout_exe_time;
    private SetDataListener setDataListener;
    private Context context;
    private boolean isExerciseTime;

    public SetsRepsDialog(@NonNull Context context, SetDataListener setDataListener, boolean isExerciseTime) {
        super(context);
        this.isExerciseTime = isExerciseTime;
        this.setDataListener = setDataListener;
        this.context = context;
    }

    public SetsRepsDialog(@NonNull Context context, @StyleRes int themeResId, SetDataListener setDataListener) {
        super(context, themeResId);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    protected SetsRepsDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, SetDataListener setDataListener) {
        super(context, cancelable, cancelListener);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.dialog_set_reps, null);
        setContentView(contentView);

        final Window window = getWindow();
        if(window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        input_sets = (EditText) contentView.findViewById(R.id.input_sets);
        input_reps = (EditText) contentView.findViewById(R.id.input_reps);
        input_time = (EditText) contentView.findViewById(R.id.input_time);
        input_weight = (EditText) contentView.findViewById(R.id.input_weight);
        input_exe_time = (EditText) contentView.findViewById(R.id.input_exe_time);
        input_layout_exe_time = (TextInputLayout) contentView.findViewById(R.id.input_layout_exe_time);
        ll_all_details = (LinearLayout) contentView.findViewById(R.id.ll_all_details);
        if (isExerciseTime)
        {
            ll_all_details.setVisibility(View.GONE);
            input_layout_exe_time.setVisibility(View.VISIBLE);
        }
        else {

            ll_all_details.setVisibility(View.VISIBLE);
            input_layout_exe_time.setVisibility(View.GONE);
        }
        btn_ok = (Button) contentView.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        btn_cancel = (Button) contentView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        input_sets.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input_sets.getText().toString().equalsIgnoreCase("1") && input_sets.getText().toString().equalsIgnoreCase("0"))
                    input_time.setEnabled(false);
                else
                    input_time.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                dismiss();
                if (isExerciseTime) {
                    if (TextUtils.isEmpty(input_exe_time.getText().toString()) && input_exe_time.getText().toString().equalsIgnoreCase("0"))
                        CommonUtils.toast(context,"Please enter Exercise Time");
                    else {
                        if(setDataListener != null)
                            setDataListener.onContinueClick(input_sets.getText().toString().trim(),input_reps.getText().toString().trim(),input_exe_time.getText().toString().trim(),input_weight.getText().toString().trim());
                    }
                }
                else
                {
                    if (TextUtils.isEmpty(input_sets.getText().toString()) && input_sets.getText().toString().equalsIgnoreCase("0"))
                        CommonUtils.toast(context,"Please enter Sets");
                    else if (TextUtils.isEmpty(input_reps.getText().toString()) && input_reps.getText().toString().equalsIgnoreCase("0"))
                        CommonUtils.toast(context,"Please enter Reps");
                    else if (TextUtils.isEmpty(input_weight.getText().toString()) && input_weight.getText().toString().equalsIgnoreCase("0"))
                        CommonUtils.toast(context,"Please enter Weight");
                    else {
                        if(setDataListener != null)
                            setDataListener.onContinueClick(input_sets.getText().toString().trim(),input_reps.getText().toString().trim(),input_time.getText().toString().trim(),input_weight.getText().toString().trim());
                    }
                }

                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    public interface SetDataListener {
        void onContinueClick(String strSets, String strReps, String strTime, String strWeight);
    }
}
