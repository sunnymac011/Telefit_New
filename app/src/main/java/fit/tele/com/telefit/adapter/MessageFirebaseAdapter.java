package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.activity.MessageActivity;
import fit.tele.com.telefit.modelBean.chat.UserModel;
import fit.tele.com.telefit.utils.CircleTransform;

public class MessageFirebaseAdapter extends FirebaseRecyclerAdapter<UserModel, MessageFirebaseAdapter.Header> {
    private Context context;
    private DatabaseReference mFirebaseDatabaseReference;

    public MessageFirebaseAdapter(Context context, DatabaseReference ref) {
        super(UserModel.class, R.layout.item_item_message, MessageFirebaseAdapter.Header.class, ref);
        this.context = context;
    }

    @Override
    public MessageFirebaseAdapter.Header onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_item_message, parent, false);
        return new Header(v);
    }

    @Override
    protected void populateViewHolder(MessageFirebaseAdapter.Header viewHolder, UserModel model, int position) {
        viewHolder.bindData(model);
    }

    public class Header extends RecyclerView.ViewHolder {
        private UserModel model;
        private TextView txtName;
        private ImageView imgProfile;

        Header(View v) {
            super(v);
         //   layout = (ConstraintLayout) v.findViewById(R.id.layout_main);
            txtName = (TextView) v.findViewById(R.id.txt_user_name);
            imgProfile = (ImageView) v.findViewById(R.id.img_profile);
        }

        public void bindData(UserModel bean) {
            this.model = bean;
            if (model != null && model.getPhoto_profile() != null
                    && !TextUtils.isEmpty(model.getPhoto_profile())) {
                Picasso.with(context)
                        .load(model.getPhoto_profile())
                        .placeholder(R.drawable.user_placeholder)
                        .error(R.drawable.user_placeholder)
                        .transform(new CircleTransform())
                        .into(imgProfile);
            }

            if (model != null && model.getName() != null && !TextUtils.isEmpty(model.getName()))
                txtName.setText(model.getName());
            Log.w("Document_check","User Name: "+model.getName());
            try {
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setApiKey("AIzaSyBHW9m9Xbz-fqY3uMBLpmzgu4ymdR89nk8")
                        .setApplicationId("1:323428350483:android:13d70f9ddb40214a")
                        .setDatabaseUrl("https://telfittest.firebaseio.com/")
                        .setStorageBucket("gs://telfittest.appspot.com")
                        .build();
                FirebaseApp secondApp = FirebaseApp.initializeApp(context, options, "second app");
                mFirebaseDatabaseReference = FirebaseDatabase.getInstance(secondApp).getReference();
            }catch (IllegalStateException e){
                e.printStackTrace();
                try {
                    mFirebaseDatabaseReference = FirebaseDatabase.getInstance(FirebaseApp.getInstance("second app")).getReference();
                } catch (IllegalStateException ex){
                    ex.printStackTrace();
                }
            }
            String CHAT_REFERENCE =   model.getUser_id();
            Log.w("Document_check","Data user wise: "+mFirebaseDatabaseReference.child(CHAT_REFERENCE));
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        String message = ds.child("message").getValue(String.class);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            mFirebaseDatabaseReference.child(CHAT_REFERENCE).addListenerForSingleValueEvent(eventListener);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("user_model", model);
                    context.startActivity(intent);
                }
            });
        }
    }


}





