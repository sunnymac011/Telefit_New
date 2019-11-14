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
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.DecimalFormat;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.utils.CommonUtils;

public class SetTimeDialog extends Dialog implements View.OnClickListener {
    private Button btn_submit;
    private EditText edt_hours, edt_minute, edt_sec;
    private SetDataListener setDataListener;
    private Context context;
    private int intHours = 0, intMin = 0, intSec = 0;
    private DecimalFormat formatter;

    public SetTimeDialog(@NonNull Context context, int intHours, int intMin, int intSec, SetDataListener setDataListener) {
        super(context);
        this.setDataListener = setDataListener;
        this.context = context;
        this.intHours = intHours;
        this.intMin = intMin;
        this.intSec = intSec;
    }

    public SetTimeDialog(@NonNull Context context, @StyleRes int themeResId, SetDataListener setDataListener) {
        super(context, themeResId);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    protected SetTimeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, SetDataListener setDataListener) {
        super(context, cancelable, cancelListener);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.dialog_exercise_timer, null);
        setContentView(contentView);

        final Window window = getWindow();
        if(window != null)
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        formatter = new DecimalFormat("00");
        edt_hours = (EditText) contentView.findViewById(R.id.edt_hours);
        edt_minute = (EditText) contentView.findViewById(R.id.edt_minute);
        edt_sec = (EditText) contentView.findViewById(R.id.edt_sec);
        btn_submit = (Button) contentView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        if (intHours != 0)
            edt_hours.setText(""+intHours);
        if (intMin != 0)
            edt_minute.setText(""+intMin);
        if (intSec != 0)
            edt_sec.setText(""+intSec);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (!edt_hours.getText().toString().isEmpty() || !edt_minute.getText().toString().isEmpty() || !edt_sec.getText().toString().isEmpty())
                {
                    if (!edt_hours.getText().toString().equalsIgnoreCase(""))
                        intHours = Integer.parseInt(edt_hours.getText().toString());
                    else
                        intHours = 0;
                    if (!edt_minute.getText().toString().equalsIgnoreCase(""))
                        intMin = Integer.parseInt(edt_minute.getText().toString());
                    else
                        intMin = 0;
                    if (!edt_sec.getText().toString().equalsIgnoreCase(""))
                        intSec = Integer.parseInt(edt_sec.getText().toString());
                    else
                        intSec = 0;
                    setDataListener.onContinueClick(formatter.format(intHours),formatter.format(intMin),formatter.format(intSec));
                    dismiss();
                }
                else
                    CommonUtils.toast(context,"Please enter time");
                break;
        }
    }

    public interface SetDataListener {
        void onContinueClick(String strHour,String strMin,String strSec);
    }
}
