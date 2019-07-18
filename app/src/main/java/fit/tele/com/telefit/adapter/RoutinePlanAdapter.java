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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.RoutinePlanBean;
import fit.tele.com.telefit.utils.CircleTransform;
import fit.tele.com.telefit.utils.OnLoadMoreListener;

public class RoutinePlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<RoutinePlanBean> list;
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;
    private ClickListener clickListener;

    public RoutinePlanAdapter(Context context, RecyclerView recyclerView, ClickListener clickListener) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_routine_plan, parent, false);
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

    public void addAllList(ArrayList<RoutinePlanBean> data) {
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
        private TextView txt_plan_name,txt_total_exercises,txt_date,txt_week_day;

        Header(View v) {
            super(v);
            txt_plan_name = (TextView) v.findViewById(R.id.txt_plan_name);
            txt_total_exercises = (TextView) v.findViewById(R.id.txt_total_exercises);
            txt_date = (TextView) v.findViewById(R.id.txt_date);
            txt_week_day = (TextView) v.findViewById(R.id.txt_week_day);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onClick(list.get(pos).getId());
                    }
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                if(list.get(pos).getRoutineName() != null && !TextUtils.isEmpty(list.get(pos).getRoutineName()))
                    txt_plan_name.setText(list.get(pos).getRoutineName());
                if(list.get(pos).getRoutineName() != null && !TextUtils.isEmpty(list.get(pos).getRoutineName()))
                    txt_total_exercises.setText(list.get(pos).getTotalExe());
                if(list.get(pos).getCreatedAt() != null && !TextUtils.isEmpty(list.get(pos).getCreatedAt())) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = format.parse(list.get(pos).getPlaneDate());

                        String finalDate = (String) DateFormat.format("MMM dd",   date);
                        txt_date.setText(finalDate);
                    }catch (Exception e) {

                    }
                }

                if (list.get(pos).getDayFlag().equalsIgnoreCase("1")) {
                    txt_week_day.setText("Mon");
                    txt_week_day.setBackgroundResource(R.drawable.purple_circle);
                }
                else if (list.get(pos).getDayFlag().equalsIgnoreCase("2")) {
                    txt_week_day.setText("Tue");
                    txt_week_day.setBackgroundResource(R.drawable.light_blue_circle);
                }
                else if (list.get(pos).getDayFlag().equalsIgnoreCase("3")) {
                    txt_week_day.setText("Wed");
                    txt_week_day.setBackgroundResource(R.drawable.yellow_circle);
                }
                else if (list.get(pos).getDayFlag().equalsIgnoreCase("4")) {
                    txt_week_day.setText("Thu");
                    txt_week_day.setBackgroundResource(R.drawable.light_gray_circle);
                }
                else if (list.get(pos).getDayFlag().equalsIgnoreCase("5")) {
                    txt_week_day.setText("Fri");
                    txt_week_day.setBackgroundResource(R.drawable.green_circle);
                }
                else if (list.get(pos).getDayFlag().equalsIgnoreCase("6")) {
                    txt_week_day.setText("Sat");
                    txt_week_day.setBackgroundResource(R.drawable.red_circle);
                }
                else if (list.get(pos).getDayFlag().equalsIgnoreCase("7")) {
                    txt_week_day.setText("Sun");
                    txt_week_day.setBackgroundResource(R.drawable.blue_circle);
                }
            }
        }
    }

    public interface ClickListener {
        void onClick(String plan_id);
    }
}
