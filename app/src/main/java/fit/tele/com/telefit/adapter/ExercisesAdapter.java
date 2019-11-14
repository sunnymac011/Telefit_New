package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import fit.tele.com.telefit.activity.ExerciseDetailsActivity;
import fit.tele.com.telefit.modelBean.ExercisesBean;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.SubOptionsBean;
import fit.tele.com.telefit.utils.CircleTransform;
import fit.tele.com.telefit.utils.OnLoadMoreListener;

public class ExercisesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<ExercisesListBean> list;
    private ArrayList<ExercisesListBean> listFill = new ArrayList<>();
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;
    private ClickListener clickListener;

    public ExercisesAdapter(Context context, RecyclerView recyclerView, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        list = new ArrayList<>();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(linearLayoutManager != null) {
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_exercises, parent, false);
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

    public void setLoaded() {
        isLoading = false;
    }

    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addAllList(ArrayList<ExercisesListBean> data) {
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
//            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    private class Header extends RecyclerView.ViewHolder {
        private int pos;
        private TextView txt_exercise;
        private ImageView img_exercise;

        Header(View v) {
            super(v);
            txt_exercise = (TextView) v.findViewById(R.id.txt_exercise);
            img_exercise = (ImageView) v.findViewById(R.id.img_exercise);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onClick(list.get(pos).getId(),list.get(pos), true);
                    }
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onClick(list.get(pos).getId(),list.get(pos), false);
                    }
                    return true;
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {

                if(list.get(pos).getExeImageUrl() != null && !TextUtils.isEmpty(list.get(pos).getExeImageUrl())) {
                    Picasso.with(context)
                            .load(list.get(pos).getExeImageUrl())
                            .error(R.drawable.user_placeholder)
                            .placeholder(R.drawable.user_placeholder)
                            .transform(new CircleTransform())
                            .into(img_exercise);
                }

                if(list.get(pos).getExeTitle() != null && !TextUtils.isEmpty(list.get(pos).getExeTitle()))
                    txt_exercise.setText(list.get(pos).getExeTitle());
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
            for (ExercisesListBean wp : listFill)
            {
                if (wp.getExeTitle().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    list.add(wp);
                }

            }

        }
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(String exe_id, ExercisesListBean exercisesListBean, boolean isLongClick);
    }
}
