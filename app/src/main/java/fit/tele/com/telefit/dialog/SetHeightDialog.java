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

public class SetHeightDialog extends Dialog implements View.OnClickListener {
    private TextView txt_header_txt;
    private Button btn_submit;
    private EditText edt_numbers, edt_numbers_two;
    private NumberPicker np_height_type;
    private SetDataListener setDataListener;
    private Context context;
    private String strNumbers = "0",strNumbersTwo = "0", strHeightType = "ft", headerText = "Set Detail";

    public SetHeightDialog(@NonNull Context context, String headerText, SetDataListener setDataListener) {
        super(context);
        this.setDataListener = setDataListener;
        this.context = context;
        this.headerText = headerText;
    }

    public SetHeightDialog(@NonNull Context context, @StyleRes int themeResId, SetDataListener setDataListener) {
        super(context, themeResId);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    protected SetHeightDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, SetDataListener setDataListener) {
        super(context, cancelable, cancelListener);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.dialog_height, null);
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
        np_height_type = (NumberPicker) contentView.findViewById(R.id.np_height_type);
        btn_submit = (Button) contentView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        np_height_type.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        txt_header_txt.setText(headerText.trim());

        final String[] values= {"ft","centimeter"};

        np_height_type.setMinValue(0);
        np_height_type.setMaxValue(values.length-1);
        np_height_type.setDisplayedValues(values);
        np_height_type.setWrapSelectorWheel(true);
        np_height_type.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                if (newVal == 0)
                {
                    strHeightType = "ft";
                    edt_numbers_two.setVisibility(View.VISIBLE);
                    edt_numbers.setHint("Feet");
                    edt_numbers_two.setHint("Inch");
                }
                if (newVal == 1)
                {
                    strHeightType = "cm";
                    edt_numbers_two.setVisibility(View.GONE);
                    edt_numbers.setHint("Centimeter");
                }
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
                    setDataListener.onContinueClick(edt_numbers.getText().toString(), edt_numbers_two.getText().toString(),strHeightType);
                    dismiss();
                }
                break;
        }
    }

    public interface SetDataListener {
        void onContinueClick(String strNumbers, String strNumbersTwo, String strWeightType);
    }
}
