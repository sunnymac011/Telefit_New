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
import fit.tele.com.telefit.modelBean.SetsRepsBean;
import fit.tele.com.telefit.modelBean.SubOptionsBean;

public class SetsRepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<SetsRepsBean> list;
    private ClickListener clickListener;

    public SetsRepsAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        list = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_setsreps_details, parent, false);
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

    public void addAllList(ArrayList<SetsRepsBean> data) {
        list.addAll(data);
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
//            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    private class Header extends RecyclerView.ViewHolder {
        private int pos;
        private TextView txt_sets,txt_reps,txt_weight,txt_time;

        Header(View v) {
            super(v);
            txt_sets = (TextView) v.findViewById(R.id.txt_sets);
            txt_reps = (TextView) v.findViewById(R.id.txt_reps);
            txt_weight = (TextView) v.findViewById(R.id.txt_weight);
            txt_time = (TextView) v.findViewById(R.id.txt_time);

            txt_reps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onRepsClick(pos);
                    }
                }
            });

            txt_weight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onWeightClick(pos);
                    }
                }
            });

            txt_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onTimeClick(pos);
                    }
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                if(list.get(pos).getSets() != null && !TextUtils.isEmpty(list.get(pos).getSets()))
                    txt_sets.setText(list.get(pos).getSets());
                if(list.get(pos).getReps() != null && !TextUtils.isEmpty(list.get(pos).getReps()))
                    txt_reps.setText(list.get(pos).getReps());
                if(list.get(pos).getWeight() != null && !TextUtils.isEmpty(list.get(pos).getWeight()))
                    txt_weight.setText(list.get(pos).getWeight()+" "+list.get(pos).getWeightType());
                if(list.get(pos).getSrHours() != null && !TextUtils.isEmpty(list.get(pos).getSrHours()) &&
                        list.get(pos).getSrMin() != null && !TextUtils.isEmpty(list.get(pos).getSrMin()) &&
                        list.get(pos).getSrSec() != null && !TextUtils.isEmpty(list.get(pos).getSrSec()))
                    txt_time.setText(list.get(pos).getSrHours()+":"+list.get(pos).getSrMin()+":"+list.get(pos).getSrSec());
            }
        }
    }

    public interface ClickListener {
        void onRepsClick(int pos);
        void onWeightClick(int pos);
        void onTimeClick(int pos);
    }
}
