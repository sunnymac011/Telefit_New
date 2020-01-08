package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.CountryBean;
import fit.tele.com.telefit.modelBean.PackageBean;
import fit.tele.com.telefit.utils.Preferences;

public class PackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private int selectedPos = 0;
    private ArrayList<PackageBean> list;
    private ClickListener clickListener;

    public PackageAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_packages, parent, false);
        return new Header(view);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Header) {
            ((Header) holder).bindData(position);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addAllList(ArrayList<PackageBean> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
//            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    private class Header extends RecyclerView.ViewHolder {
        private int pos;
        private TextView txt_validity;
        private TextView txt_price;
        private LinearLayout ll_bg;

        Header(View v) {
            super(v);
            txt_validity = (TextView) v.findViewById(R.id.txt_validity);
            txt_price = (TextView) v.findViewById(R.id.txt_price);
            ll_bg = (LinearLayout) v.findViewById(R.id.ll_bg);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPos = pos;
                    clickListener.onClick(list.get(pos).getId());
                    notifyDataSetChanged();
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                if (selectedPos == pos)
                    ll_bg.setBackgroundResource(R.drawable.gradiant_selected_package_bg);
                else
                    ll_bg.setBackgroundResource(R.drawable.gradiant_package_bg);

                if(list.get(pos).getPrice() != null && !TextUtils.isEmpty(list.get(pos).getPrice()))
                    txt_price.setText("$ "+list.get(pos).getPrice());
                if(list.get(pos).getValidity() != null && !TextUtils.isEmpty(list.get(pos).getValidity()))
                    txt_validity.setText(list.get(pos).getValidity()+" Program >");
            }
        }
    }

    public interface ClickListener {
        void onClick(String packageId);
    }
}
