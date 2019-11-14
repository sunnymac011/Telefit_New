package fit.tele.com.telefit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.utils.CommonUtils;

public class SetNumbersDialog extends Dialog implements View.OnClickListener {
    private TextView txt_header_txt;
    private Button btn_submit;
    private EditText edt_numbers, edt_numbers_two;
    private NumberPicker np_weight_type;
    private SetDataListener setDataListener;
    private Context context;
    private String strNumbers = "0",strNumbersTwo = "0", strWeightType = "lbs", headerText = "Set Detail";
    private boolean isWeight = false, isSignUp = false;

    public SetNumbersDialog(@NonNull Context context, String headerText, boolean isSignUp, boolean isWeight, SetDataListener setDataListener) {
        super(context);
        this.setDataListener = setDataListener;
        this.context = context;
        this.isWeight = isWeight;
        this.isSignUp = isSignUp;
        this.headerText = headerText;
    }

    public SetNumbersDialog(@NonNull Context context, @StyleRes int themeResId, SetDataListener setDataListener) {
        super(context, themeResId);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    protected SetNumbersDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, SetDataListener setDataListener) {
        super(context, cancelable, cancelListener);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.dialog_number_scroll, null);
        setContentView(contentView);

        final Window window = getWindow();
        if(window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        txt_header_txt = (TextView) contentView.findViewById(R.id.txt_header_txt);
        edt_numbers = (EditText) contentView.findViewById(R.id.edt_numbers);
        edt_numbers_two = (EditText) contentView.findViewById(R.id.edt_numbers_two);
        np_weight_type = (NumberPicker) contentView.findViewById(R.id.np_weight_type);
        btn_submit = (Button) contentView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        np_weight_type.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        txt_header_txt.setText(headerText.trim());

        if (headerText.equalsIgnoreCase("Set Height"))
        {
            edt_numbers.setHint("Feet");
            edt_numbers_two.setHint("Inch");
        }
        else if (headerText.equalsIgnoreCase("Set Weight"))
            edt_numbers.setHint("Weight");
        else if (headerText.equalsIgnoreCase("Set Reps"))
            edt_numbers.setHint("Reps");

        if (isWeight)
            np_weight_type.setVisibility(View.VISIBLE);
        else
            np_weight_type.setVisibility(View.GONE);

        if (isSignUp)
            edt_numbers_two.setVisibility(View.VISIBLE);
        else
            edt_numbers_two.setVisibility(View.GONE);

        final String[] values= {"lbs","kg"};

        np_weight_type.setMinValue(0);
        np_weight_type.setMaxValue(values.length-1);
        np_weight_type.setDisplayedValues(values);
        np_weight_type.setWrapSelectorWheel(true);
        np_weight_type.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                if (newVal == 0)
                    strWeightType = "lbs";
                if (newVal == 1)
                    strWeightType = "kg";
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (edt_numbers.getText().toString().isEmpty())
                    CommonUtils.toast(context,"Please enter value");
                else {
                    setDataListener.onContinueClick(edt_numbers.getText().toString(), edt_numbers_two.getText().toString(),strWeightType);
                    dismiss();
                }
                break;
        }
    }

    public interface SetDataListener {
        void onContinueClick(String strNumbers,String strNumbersTwo, String strWeightType);
    }
}
