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
import fit.tele.com.telefit.modelBean.ExxDetial;

public class GoalCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int checkedPosition = 0;
    private Context context;
    private ArrayList<String> list;
    private ClickListener clickListener;

    public GoalCategoryAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        list = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_exercise_type, parent, false);
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

    public void addAllList(ArrayList<String> data) {
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

    public void changeSelection(int pos) {
        checkedPosition = pos;
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
        private TextView txt_mechanics_tab;
        private View view_mechanics;

        Header(View v) {
            super(v);
            txt_mechanics_tab = (TextView) v.findViewById(R.id.txt_mechanics_tab);
            view_mechanics = (View) v.findViewById(R.id.view_mechanics);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        checkedPosition = pos;
                        notifyDataSetChanged();
                        if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null)
                            clickListener.onClick(pos,list.get(pos));
                    }
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                if (checkedPosition == pos) {
                    txt_mechanics_tab.setTextColor(context.getResources().getColor(R.color.white));
                    view_mechanics.setVisibility(View.VISIBLE);
                }
                else {
                    txt_mechanics_tab.setTextColor(context.getResources().getColor(R.color.light_gray));
                    view_mechanics.setVisibility(View.GONE);
                }

                if(list.get(pos) != null && !TextUtils.isEmpty(list.get(pos)))
                    txt_mechanics_tab.setText(list.get(pos));
            }
        }
    }

    public interface ClickListener {
        void onClick(int pos,String goalName);
    }
}
