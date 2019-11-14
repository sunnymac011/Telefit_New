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
import android.widget.TextView;

import fit.tele.com.telefit.R;

public class CustomDialog extends Dialog implements View.OnClickListener {
    private TextView txt_header_txt,txt_content;
    private Button btn_yes,btn_no;
    private SetDataListener setDataListener;
    private Context context;
    private String strHeader = "", strContent = "";

    public CustomDialog(@NonNull Context context,String strHeader,String strContent, SetDataListener setDataListener) {
        super(context);
        this.setDataListener = setDataListener;
        this.context = context;
        this.strHeader = strHeader;
        this.strContent = strContent;
    }

    public CustomDialog(@NonNull Context context, @StyleRes int themeResId, SetDataListener setDataListener) {
        super(context, themeResId);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, SetDataListener setDataListener) {
        super(context, cancelable, cancelListener);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.dialog_custom, null);
        setContentView(contentView);

        final Window window = getWindow();
        if(window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        txt_header_txt = (TextView) contentView.findViewById(R.id.txt_header_txt);
        txt_content = (TextView) contentView.findViewById(R.id.txt_content);
        btn_yes = (Button) contentView.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(this);
        btn_no = (Button) contentView.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(this);

        txt_header_txt.setText(strHeader);
        txt_content.setText(strContent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                setDataListener.onContinueClick("yes");
                dismiss();
                break;
            case R.id.btn_no:
                dismiss();
                break;
        }
    }

    public interface SetDataListener {
        void onContinueClick(String strToAdd);
    }
}
