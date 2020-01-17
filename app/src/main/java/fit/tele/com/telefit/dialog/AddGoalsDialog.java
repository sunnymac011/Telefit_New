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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.utils.CommonUtils;

public class AddGoalsDialog extends Dialog implements View.OnClickListener {
    private EditText input_goal,input_consume;
    private Button btn_set;
    private SetDataListener setDataListener;
    private Context context;
    private String strHint,strGoalValue;

    public AddGoalsDialog(@NonNull Context context, String strHint, String strGoalValue, SetDataListener setDataListener) {
        super(context);
        this.setDataListener = setDataListener;
        this.context = context;
        this.strHint = strHint;
        this.strGoalValue = strGoalValue;
    }

    public AddGoalsDialog(@NonNull Context context, @StyleRes int themeResId, SetDataListener setDataListener) {
        super(context, themeResId);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    protected AddGoalsDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, SetDataListener setDataListener) {
        super(context, cancelable, cancelListener);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.dialog_add_goal, null);
        setContentView(contentView);

        final Window window = getWindow();
        if(window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        input_goal = (EditText) contentView.findViewById(R.id.input_goal);
        input_consume = (EditText) contentView.findViewById(R.id.input_consume);
        input_consume.setHint(strHint);
        btn_set = (Button) contentView.findViewById(R.id.btn_set);
        btn_set.setOnClickListener(this);

        input_goal.setText(strGoalValue);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set:
                if (input_goal.getText().toString().isEmpty())
                    CommonUtils.toast(context,"Please enter goal!");
                else if (input_consume.getText().toString().isEmpty())
                    CommonUtils.toast(context,"Please enter consumed!");
                else {
                    setDataListener.onContinueClick(input_goal.getText().toString(),input_consume.getText().toString());
                    dismiss();
                }
                break;
        }
    }

    public interface SetDataListener {
        void onContinueClick(String goal, String consumed);
    }
}
