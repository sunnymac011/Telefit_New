package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.SubArray;
import fit.tele.com.telefit.modelBean.SubOptionsBean;

public class SubCatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<SubArray> list;

    public SubCatAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sub_cat, parent, false);
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

    public void addAllList(ArrayList<SubArray> data) {
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
        private TextView txt_header,txt_header_type;

        Header(View v) {
            super(v);
            txt_header = (TextView) v.findViewById(R.id.txt_header);
            txt_header_type = (TextView) v.findViewById(R.id.txt_header_type);

        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                if(list.get(pos).getSubCatName() != null && !TextUtils.isEmpty(list.get(pos).getSubCatName()))
                    txt_header.setText(list.get(pos).getSubCatName());
                if(list.get(pos).getSubCatOption() != null && !TextUtils.isEmpty(list.get(pos).getSubCatOption()))
                    txt_header_type.setText(list.get(pos).getSubCatOption());
            }
        }
    }
}
