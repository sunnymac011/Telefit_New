package fit.tele.com.telefit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.utils.CommonUtils;

public class AddNutritionDialog extends Dialog implements View.OnClickListener {
    private EditText input_measur,input_per,input_serv;
    private Spinner spi_name;
    private Button btn_add;
    private SetDataListener setDataListener;
    private Context context;
    private List<String> list = new ArrayList<>();

    public AddNutritionDialog(@NonNull Context context, List<String> list, SetDataListener setDataListener) {
        super(context);
        this.setDataListener = setDataListener;
        this.context = context;
        this.list = list;
    }

    public AddNutritionDialog(@NonNull Context context, @StyleRes int themeResId, SetDataListener setDataListener) {
        super(context, themeResId);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    protected AddNutritionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, SetDataListener setDataListener) {
        super(context, cancelable, cancelListener);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.dialog_add_nutrition, null);
        setContentView(contentView);

        final Window window = getWindow();
        if(window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        spi_name = (Spinner) contentView.findViewById(R.id.spi_name);
        input_measur = (EditText) contentView.findViewById(R.id.input_measur);
        input_per = (EditText) contentView.findViewById(R.id.input_per);
        input_serv = (EditText) contentView.findViewById(R.id.input_serv);
        btn_add = (Button) contentView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_custom_spinner, list);
        spi_name.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                if (spi_name.getSelectedItem().toString().isEmpty())
                    CommonUtils.toast(context,"Please select name!");
                else if (input_measur.getText().toString().isEmpty())
                    CommonUtils.toast(context,"Please enter measurement!");
                else if (input_per.getText().toString().isEmpty())
                    CommonUtils.toast(context,"Please enter per 100g!");
                else if (input_serv.getText().toString().isEmpty())
                    CommonUtils.toast(context,"Please enter per serving!");
                else {
                    setDataListener.onContinueClick(spi_name.getSelectedItem().toString(),input_measur.getText().toString(),input_per.getText().toString(),input_serv.getText().toString());
                    dismiss();
                }
                break;
            case R.id.btn_no:
                dismiss();
                break;
        }
    }

    public interface SetDataListener {
        void onContinueClick(String name,String measur,String per100,String serving);
    }
}
