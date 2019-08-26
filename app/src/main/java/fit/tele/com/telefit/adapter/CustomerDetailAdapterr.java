package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.CustomerDetailBean;
import fit.tele.com.telefit.utils.CircleTransform;
import fit.tele.com.telefit.utils.OnLoadMoreListener;
import fit.tele.com.telefit.utils.Preferences;

public class CustomerDetailAdapterr extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<CustomerDetailBean> list;
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;
    CustomerDetailListner listener;
    private ArrayList<CustomerDetailBean> listFill = new ArrayList<>();
    Preferences preferences;

    public CustomerDetailAdapterr(Context context, RecyclerView recyclerView, CustomerDetailListner listener) {
        this.context = context;
        list = new ArrayList<>();
        this.listener = listener;
        preferences = new Preferences(context);

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
            View view = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false);
            return new Header(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_progress, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Header)
            ((Header) holder).bindData(position);
        else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
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
        notifyDataSetChanged();
    }

    public void dataChanged(int position){
        list.get(position).setIs_request(true);
        notifyItemChanged(position);

    }
    public void addAllList(ArrayList<CustomerDetailBean> data) {
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
        private TextView txt_customer_name, txt_add,txt_address;
        private ImageView img_customer;

        Header(View v) {
            super(v);
            txt_customer_name = (TextView) v.findViewById(R.id.txt_customer_name);
            txt_address = (TextView) v.findViewById(R.id.txt_address);
            txt_add = (TextView) v.findViewById(R.id.txt_add);
            img_customer = (ImageView) v.findViewById(R.id.img_customer);
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
                    if (list.get(position).getAddress() != null && !TextUtils.isEmpty(list.get(position).getAddress())) {
                        txt_address.setText(list.get(position).getAddress() );
                    } else {
                        txt_address.setVisibility(View.GONE);
                    }

                    if (list.get(pos).getProfilePic() != null && !list.get(pos).getProfilePic().equalsIgnoreCase("")
                            && !TextUtils.isEmpty(list.get(pos).getProfilePic())) {
                        Picasso.with(context)
                                .load(list.get(pos).getProfilePic())
                                .error(R.drawable.user_placeholder)
                                .placeholder(R.drawable.user_placeholder)
                                .transform(new CircleTransform())
                                .into(img_customer);
                    }

                    if(preferences.getUserDataPref().getId()==list.get(pos).getId() || list.get(pos).getIs_request()) {
                        txt_add.setVisibility(View.INVISIBLE);
                    } else {
                        txt_add.setVisibility(View.VISIBLE);
                    }
                }

                txt_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null)
                            listener.onClick(50001, list.get(position),position);
                    }
                });
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
            for (CustomerDetailBean wp : listFill)
            {
                String fullName =  wp.getName().toLowerCase(Locale.getDefault())+" "+wp.getlName().toLowerCase(Locale.getDefault());
                String fullName1 =  wp.getlName().toLowerCase(Locale.getDefault())+ " "+wp.getName().toLowerCase(Locale.getDefault());
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText) || wp.getlName().toLowerCase(Locale.getDefault()).contains(charText) || fullName.contains(charText) || fullName1.contains(charText))
                {
                    list.add(wp);
                }

            }

        }
        notifyDataSetChanged();
    }


    public interface CustomerDetailListner {
        void onClick(int id,CustomerDetailBean bean,int position);
    }
}
