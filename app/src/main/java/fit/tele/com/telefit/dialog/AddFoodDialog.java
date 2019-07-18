package fit.tele.com.telefit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.utils.CommonUtils;

public class AddFoodDialog extends Dialog implements View.OnClickListener {
    private EditText input_sets,input_reps,input_time;
    private Button btn_meal,btn_recipe;
    private SetDataListener setDataListener;
    private Context context;

    public AddFoodDialog(@NonNull Context context, SetDataListener setDataListener) {
        super(context);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    public AddFoodDialog(@NonNull Context context, @StyleRes int themeResId, SetDataListener setDataListener) {
        super(context, themeResId);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    protected AddFoodDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, SetDataListener setDataListener) {
        super(context, cancelable, cancelListener);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.dialog_add_food, null);
        setContentView(contentView);

        final Window window = getWindow();
        if(window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        btn_meal = (Button) contentView.findViewById(R.id.btn_meal);
        btn_meal.setOnClickListener(this);
        btn_recipe = (Button) contentView.findViewById(R.id.btn_recipe);
        btn_recipe.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_recipe:
                setDataListener.onContinueClick("recipe");
                dismiss();
                break;
            case R.id.btn_meal:
                setDataListener.onContinueClick("meal");
                dismiss();
                break;
        }
    }

    public interface SetDataListener {
        void onContinueClick(String strToAdd);
    }
}
