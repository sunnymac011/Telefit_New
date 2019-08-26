package fit.tele.com.telefit.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.interfaces.ClickListenerChatFirebase;
import fit.tele.com.telefit.modelBean.chat.ChatModel;
import fit.tele.com.telefit.utils.CircleTransform;

public class ChatFirebaseAdapter extends FirebaseRecyclerAdapter<ChatModel,ChatFirebaseAdapter.MyChatViewHolder> {
    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;
    private static final int RIGHT_MSG_IMG = 2;
    private static final int LEFT_MSG_IMG = 3;
    private static final int LEFT_MSG_FILE = 4;
    private static final int RIGHT_MSG_FILE = 5;

    private ClickListenerChatFirebase mClickListenerChatFirebase;
    private String nameUser;

    public ChatFirebaseAdapter(DatabaseReference ref, String nameUser, ClickListenerChatFirebase mClickListenerChatFirebase) {
        super(ChatModel.class, R.layout.item_message_left, ChatFirebaseAdapter.MyChatViewHolder.class, ref);
        this.nameUser = nameUser;
        this.mClickListenerChatFirebase = mClickListenerChatFirebase;
        Log.w("UsernameAdapter",""+nameUser);
    }



    @Override
    public MyChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right,parent,false);
            return new MyChatViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left,parent,false);
            return new MyChatViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.w("getItemViewType",""+getItem(position));
        ChatModel model = getItem(position);
       if (model.getUserModel().getName().equals(nameUser)){
            return RIGHT_MSG;
        }else{
            return LEFT_MSG;
        }


    }

    @Override
    protected void populateViewHolder(MyChatViewHolder viewHolder, ChatModel model, int position) {
        viewHolder.setIvUser(model.getUserModel().getPhoto_profile());

        Log.w("firebasemessage","model.getMessage(): "+model.getMessage());

        viewHolder.setTxtMessage(model.getMessage());
        viewHolder.setTvTimestamp(model.getTimeStamp());
        viewHolder.tvIsLocation(View.GONE);

    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTimestamp,tvLocation,txtFile;
        TextView txtMessage;
        ImageView ivUser,ivChatPhoto;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            tvTimestamp = (TextView)itemView.findViewById(R.id.timestamp);
            txtMessage = (TextView)itemView.findViewById(R.id.message);

            ivUser = (ImageView)itemView.findViewById(R.id.ivUserChat);




        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ChatModel model = getItem(position);


        }

        public void setTVFile(String url){
            if (txtFile == null)return;
            txtFile.setOnClickListener(this);
        }

        public void setTxtMessage(String message){
            if (txtMessage == null)
                return;
            txtMessage.setText(message);
        }

        public void setIvUser(String urlPhotoUser){
            if (ivUser == null)
                return;

            if (urlPhotoUser != null && !TextUtils.isEmpty(urlPhotoUser)) {
                Picasso.with(ivUser.getContext())
                        .load(urlPhotoUser)
                        .placeholder(R.drawable.user_placeholder)
                        .error(R.drawable.user_placeholder)
                        .transform(new CircleTransform())
                        .into(ivUser);
            }
        }

        public void setTvTimestamp(String timestamp){
            if (tvTimestamp == null)return;
            tvTimestamp.setText(converteTimestamp(timestamp));
        }

        public void setIvChatPhoto(String url){
            if (ivChatPhoto == null)
                return;

                if(url != null && !TextUtils.isEmpty(url))
                Picasso.with(ivChatPhoto.getContext())
                    .load(url)
                    .into(ivChatPhoto);
            ivChatPhoto.setOnClickListener(this);
        }

        public void tvIsLocation(int visible){
            if (tvLocation == null)return;
            tvLocation.setVisibility(visible);
        }
    }

    private CharSequence converteTimestamp(String mileSegundos){
        TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(Locale.ENGLISH).build();
        return TimeAgo.using(Long.parseLong(mileSegundos), messages);
    }
}
