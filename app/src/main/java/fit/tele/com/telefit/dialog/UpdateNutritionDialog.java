package fit.tele.com.telefit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.NutritionLabelBean;
import fit.tele.com.telefit.utils.CommonUtils;

public class UpdateNutritionDialog extends Dialog implements View.OnClickListener {
    private EditText input_name,input_measur,input_per,input_serv;
    private Button btn_add;
    private SetDataListener setDataListener;
    private Context context;
    private NutritionLabelBean nutritionLabelBean;

    public UpdateNutritionDialog(@NonNull Context context, NutritionLabelBean nutritionLabelBean, SetDataListener setDataListener) {
        super(context);
        this.setDataListener = setDataListener;
        this.context = context;
        this.nutritionLabelBean = nutritionLabelBean;
    }

    public UpdateNutritionDialog(@NonNull Context context, @StyleRes int themeResId, SetDataListener setDataListener) {
        super(context, themeResId);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    protected UpdateNutritionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, SetDataListener setDataListener) {
        super(context, cancelable, cancelListener);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.dialog_update_nutrition, null);
        setContentView(contentView);

        final Window window = getWindow();
        if(window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        input_name = (EditText) contentView.findViewById(R.id.input_name);
        input_measur = (EditText) contentView.findViewById(R.id.input_measur);
        input_per = (EditText) contentView.findViewById(R.id.input_per);
        input_serv = (EditText) contentView.findViewById(R.id.input_serv);
        btn_add = (Button) contentView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        if (nutritionLabelBean != null && !TextUtils.isEmpty(nutritionLabelBean.getNutritionType()))
            input_name.setText(nutritionLabelBean.getNutritionType());
        if (nutritionLabelBean != null && !TextUtils.isEmpty(nutritionLabelBean.getMeasurement()))
            input_measur.setText(nutritionLabelBean.getMeasurement());
        if (nutritionLabelBean != null && !TextUtils.isEmpty(nutritionLabelBean.getPer100g()))
            input_per.setText(nutritionLabelBean.getPer100g());
        if (nutritionLabelBean != null && !TextUtils.isEmpty(nutritionLabelBean.getPerServing()))
            input_serv.setText(nutritionLabelBean.getPerServing());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                if (input_name.getText().toString().isEmpty())
                    CommonUtils.toast(context,"Please enter name!");
                else if (input_measur.getText().toString().isEmpty())
                    CommonUtils.toast(context,"Please enter measurement!");
                else if (input_per.getText().toString().isEmpty())
                    CommonUtils.toast(context,"Please enter per 100g!");
                else if (input_serv.getText().toString().isEmpty())
                    CommonUtils.toast(context,"Please enter per serving!");
                else {
                    setDataListener.onContinueClick(input_name.getText().toString(),input_measur.getText().toString(),input_per.getText().toString(),input_serv.getText().toString());
                    dismiss();
                }
                break;
            case R.id.btn_no:
                dismiss();
                break;
        }
    }

    public interface SetDataListener {
        void onContinueClick(String name, String measur, String per100, String serving);
    }
}
