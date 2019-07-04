package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.activity.AddFoodActivity;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;
import fit.tele.com.telefit.utils.OnLoadMoreListener;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<ChompProductBean> list;
    private ArrayList<ChompProductBean> listFill = new ArrayList<>();
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;

    public FoodAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_rv_general, parent, false);
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
        listFill.clear();
        notifyDataSetChanged();
    }

    public void addAllList(ArrayList<ChompProductBean> data) {
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
        private TextView txt_list_title,txt_date,txt_list_desc;
        private ImageView img_exercise;

        Header(View v) {
            super(v);
            txt_list_title = (TextView) v.findViewById(R.id.txt_list_title);
            txt_list_desc = (TextView) v.findViewById(R.id.txt_list_desc);
            txt_date = (TextView) v.findViewById(R.id.txt_date);
            txt_date.setVisibility(View.GONE);
            img_exercise = (ImageView) v.findViewById(R.id.img_exercise);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        Intent intent = new Intent(context, AddFoodActivity.class);
                        intent.putExtra("SelectedItems",list.get(pos));
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {

//                if (list.get(pos).getExevideo().getVideoCoverimgUrl() != null && list.get(pos).getExevideo().getVideoCoverimgUrl() != null
//                        && !TextUtils.isEmpty(list.get(pos).getExevideo().getVideoCoverimgUrl())) {
//                    Picasso.with(context)
//                            .load(list.get(pos).getExevideo().getVideoCoverimgUrl())
//                            .error(R.drawable.user_placeholder)
//                            .placeholder(R.drawable.user_placeholder)
//                            .transform(new CircleTransform())
//                            .into(img_exercise);
//                }

                if(list.get(pos).getName() != null && !TextUtils.isEmpty(list.get(pos).getName()))
                    txt_list_title.setText(Html.fromHtml(list.get(pos).getName()));
                if(list.get(pos).getDetails().getNutritionLabel().getCalories().getPerServing() != null && !TextUtils.isEmpty(list.get(pos).getDetails().getNutritionLabel().getCalories().getPerServing()))
                    txt_list_desc.setText(list.get(pos).getDetails().getNutritionLabel().getCalories().getPerServing()+" cals per serving");
            }
        }
    }

    // Filter Class
//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        list.clear();
//        if (charText.length() == 0) {
//            list.clear();
//            list.addAll(listFill);
//        }
//        else
//        {
//            for (ExercisesBean wp : listFill)
//            {
//                if (wp.getExeTitle().toLowerCase(Locale.getDefault()).contains(charText))
//                {
//                    list.add(wp);
//                }
//
//            }
//
//        }
//        notifyDataSetChanged();
//    }
}
