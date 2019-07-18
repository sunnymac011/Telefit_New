package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.CreatePostBean;
import fit.tele.com.telefit.utils.CircleTransform;
import fit.tele.com.telefit.utils.OnLoadMoreListener;

public class ActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_ITEM_RIGHT =2;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<CreatePostBean> list;
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;
    ActivitiesListner listener;
    private ArrayList<CreatePostBean> listFill = new ArrayList<>();
    private int userId;

    public ActivityAdapter(Context context,int userId, RecyclerView recyclerView, ActivitiesListner listener) {
        this.context = context;
        list = new ArrayList<>();
        this.listener = listener;
        this.userId = userId;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_rv_activity, parent, false);
            return new Header(view);
        } else if (viewType == VIEW_TYPE_ITEM_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_rv_activity_right, parent, false);
            return new Header(view);
        }
//        else if (viewType == VIEW_TYPE_LOADING) {
//            View view = LayoutInflater.from(context).inflate(R.layout.item_progress, parent, false);
//            return new LoadingViewHolder(view);
//        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
       // return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;

        if(list.get(position).getUser_id()==userId){
            return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM_RIGHT;
        }else {
            return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Header)
            ((Header) holder).bindData(position);
        else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
//            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void clearAll() {
        list.clear();
        listFill.clear();
        notifyDataSetChanged();
    }

    public void addAllList(ArrayList<CreatePostBean> data) {
        list.addAll(data);
        listFill.addAll(data);
        notifyDataSetChanged();
    }

    public void addProgress() {
        list.add(null);
        notifyItemInserted(list.size() - 1);
    }

    public void removeProgress() {
        if (list.size() > 0 && list.get(list.size() - 1) == null) {
            list.remove(list.size() - 1);
            notifyItemRemoved(list.size());
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    private class Header extends RecyclerView.ViewHolder {
        int position;
        private TextView txt_customer_name, txt_date,txt_list_desc;
        private ImageView img_user;

        Header(View v) {
            super(v);
            txt_customer_name = (TextView) v.findViewById(R.id.txt_list_title);
            txt_list_desc = (TextView) v.findViewById(R.id.txt_list_desc);
            txt_date = (TextView) v.findViewById(R.id.txt_date);
            img_user = (ImageView) v.findViewById(R.id.img_user);
        }

        public void bindData(int pos) {
            position = pos;
            if (list != null && position >= 0 && position < list.size()) {
                if (list.get(position) != null) {
                    if (list.get(position).getName() != null && !TextUtils.isEmpty(list.get(position).getName())) {
                        txt_customer_name.setText(list.get(position).getName() + " " + list.get(position).getlName());
                    } else {
                        txt_customer_name.setText("");
                    }
                    if (list.get(position).getPost_desc() != null && !TextUtils.isEmpty(list.get(position).getPost_desc())) {
                        txt_list_desc.setText(list.get(position).getPost_desc());
                    } else {
                        txt_list_desc.setText("");
                    }
                    if (list.get(position).getCreatedAt() != null && !TextUtils.isEmpty(list.get(position).getCreatedAt())) {
                        txt_date.setText(getDateFormatted(list.get(position).getCreatedAt()));
                    } else {
                        txt_date.setText("");
                    }

                    if (list.get(pos).getProfilePic() != null && !list.get(pos).getProfilePic().equalsIgnoreCase("")
                            && !TextUtils.isEmpty(list.get(pos).getProfilePic())) {
                        Picasso.with(context)
                                .load(list.get(pos).getProfilePic())
                                .error(R.drawable.user_placeholder)
                                .placeholder(R.drawable.user_placeholder)
                                .transform(new CircleTransform())
                                .into(img_user);
                    }



                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if (listener != null)
                                listener.onClick(50002, list.get(position));
                            return false;
                        }
                    });

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (listener != null)
                                listener.onClick(50001, list.get(position));
                        }
                    });
                }




            }
        }

    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.clear();
            list.addAll(listFill);
        }
        else
        {
            for (CreatePostBean wp : listFill)
            {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText) || wp.getlName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    list.add(wp);
                }

            }

        }
        notifyDataSetChanged();
    }


    public interface ActivitiesListner {
        void onClick(int id, CreatePostBean bean);
    }

    public String getDateFormatted(String dateNew){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = null;
            date = format.parse(dateNew);
            String finalDate = (String) DateFormat.format("hh:mm aa",   date);
            return finalDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }
}
