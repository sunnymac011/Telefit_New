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
import android.widget.TextView;

import fit.tele.com.telefit.R;

public class SharePostDialog extends Dialog implements View.OnClickListener {
    private TextView txt_share_fb,txt_share_instagram,txt_share_twiter,txt_share_snapchat;
    private Button btn_close;
    private SetDataListener setDataListener;
    private Context context;
    private String strHint;
    Boolean is_snapchat=true,is_facebook=true,is_instagram=true,is_twitter=true,is_fried=true,is_trainer=true;

    public SharePostDialog(@NonNull Context context, SetDataListener setDataListener) {
        super(context);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    public SharePostDialog(@NonNull Context context, @StyleRes int themeResId, SetDataListener setDataListener) {
        super(context, themeResId);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    public SharePostDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, SetDataListener setDataListener) {
        super(context, cancelable, cancelListener);
        this.setDataListener = setDataListener;
        this.context = context;
    }

    public SharePostDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, SetDataListener setDataListener,
                           Boolean is_facebook,Boolean is_instagram,Boolean is_twitter,Boolean is_snapchat) {
        super(context, cancelable, cancelListener);
        this.setDataListener = setDataListener;
        this.context = context;
        this.is_facebook = is_facebook;
        this.is_instagram = is_instagram;
        this.is_twitter = is_twitter;
        this.is_snapchat = is_snapchat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.dialog_share, null);
        setContentView(contentView);

        final Window window = getWindow();
        if(window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        txt_share_fb = (TextView) contentView.findViewById(R.id.txt_share_fb);
        txt_share_instagram = (TextView) contentView.findViewById(R.id.txt_share_instagram);
        txt_share_twiter = (TextView) contentView.findViewById(R.id.txt_share_twiter);
        txt_share_snapchat = (TextView) contentView.findViewById(R.id.txt_share_snapchat);

        if (is_facebook)
            txt_share_fb.setVisibility(View.VISIBLE);
        else
            txt_share_fb.setVisibility(View.GONE);
        if (is_instagram)
            txt_share_instagram.setVisibility(View.VISIBLE);
        else
            txt_share_instagram.setVisibility(View.GONE);
        if (is_twitter)
            txt_share_twiter.setVisibility(View.VISIBLE);
        else
            txt_share_twiter.setVisibility(View.GONE);
        if (is_snapchat)
            txt_share_snapchat.setVisibility(View.VISIBLE);
        else
            txt_share_snapchat.setVisibility(View.GONE);

        btn_close = (Button) contentView.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
        txt_share_fb.setOnClickListener(this);
        txt_share_instagram.setOnClickListener(this);
        txt_share_twiter.setOnClickListener(this);
        txt_share_snapchat.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                    dismiss();
                setDataListener.onContinueClick();

                break;
            case R.id.txt_share_fb:
                setDataListener.shareOnFacebook();
                break;

                case R.id.txt_share_instagram:
                    setDataListener.shareOnInstagram();
                break;

                case R.id.txt_share_snapchat:
                    setDataListener.shareOnSnapChat();
                break;

                case R.id.txt_share_twiter:
                    setDataListener.shareOnTwitter();
                break;

        }
    }

    public interface SetDataListener {
        void onContinueClick();
        void shareOnFacebook();
        void shareOnInstagram();
        void shareOnSnapChat();
        void shareOnTwitter();

    }
}
