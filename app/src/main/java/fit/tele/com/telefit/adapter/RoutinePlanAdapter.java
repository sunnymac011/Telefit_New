package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.RoutinePlanBean;


public class RoutinePlanAdapter extends RecyclerSwipeAdapter<RoutinePlanAdapter.SimpleViewHolder> {
    private ArrayList<RoutinePlanBean> list;
    private ClickListener clickListener;

    public RoutinePlanAdapter(Context context, RecyclerView recyclerView, ClickListener clickListener) {
        this.clickListener = clickListener;
        list = new ArrayList<>();
    }

    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addAllList(ArrayList<RoutinePlanBean> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine_plan, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        if (list != null && position >= 0 && position < list.size() && list.get(position) != null) {
            final RoutinePlanBean item = list.get(position);
            viewHolder.toBinding(position, item);
            mItemManger.bindView(viewHolder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        private SwipeLayout swipeLayout;
        private TextView txt_plan_name,txt_total_exercises,txt_date,txt_week_day,txt_delete;
        private LinearLayout ll_text_details;
        private Button btn_edit;
        int position;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            txt_plan_name = (TextView) itemView.findViewById(R.id.txt_plan_name);
            txt_total_exercises = (TextView) itemView.findViewById(R.id.txt_total_exercises);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_week_day = (TextView) itemView.findViewById(R.id.txt_week_day);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            ll_text_details = (LinearLayout) itemView.findViewById(R.id.ll_text_details);
            btn_edit = (Button) itemView.findViewById(R.id.btn_edit);
        }

        public void toBinding(int pos, RoutinePlanBean item) {
            position = pos;

            if (item != null) {
                if(item.getRoutineName() != null && !TextUtils.isEmpty(item.getRoutineName()))
                    txt_plan_name.setText(item.getRoutineName());
                if(item.getRoutineName() != null && !TextUtils.isEmpty(item.getRoutineName()))
                    txt_total_exercises.setText(item.getTotalExe());
                if(item.getCreatedAt() != null && !TextUtils.isEmpty(item.getCreatedAt())) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = format.parse(item.getPlaneDate());

                        String finalDate = (String) DateFormat.format("MMM dd",   date);
                        txt_date.setText(finalDate);
                    }catch (Exception e) {

                    }
                }

                if (item.getDayFlag().equalsIgnoreCase("1")) {
                    txt_week_day.setText("Mon");
                    txt_week_day.setBackgroundResource(R.drawable.purple_circle);
                }
                else if (item.getDayFlag().equalsIgnoreCase("2")) {
                    txt_week_day.setText("Tue");
                    txt_week_day.setBackgroundResource(R.drawable.light_blue_circle);
                }
                else if (item.getDayFlag().equalsIgnoreCase("3")) {
                    txt_week_day.setText("Wed");
                    txt_week_day.setBackgroundResource(R.drawable.yellow_circle);
                }
                else if (item.getDayFlag().equalsIgnoreCase("4")) {
                    txt_week_day.setText("Thu");
                    txt_week_day.setBackgroundResource(R.drawable.light_gray_circle);
                }
                else if (item.getDayFlag().equalsIgnoreCase("5")) {
                    txt_week_day.setText("Fri");
                    txt_week_day.setBackgroundResource(R.drawable.green_circle);
                }
                else if (item.getDayFlag().equalsIgnoreCase("6")) {
                    txt_week_day.setText("Sat");
                    txt_week_day.setBackgroundResource(R.drawable.red_circle);
                }
                else if (item.getDayFlag().equalsIgnoreCase("7")) {
                    txt_week_day.setText("Sun");
                    txt_week_day.setBackgroundResource(R.drawable.blue_circle);
                }
            }
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.bottom_wrapper));
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, swipeLayout.findViewById(R.id.bottom_wrapper1));
            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {
                }

                @Override
                public void onOpen(SwipeLayout layout) {
                }

                @Override
                public void onStartClose(SwipeLayout layout) {
                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                }
            });

            txt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null) {
                        clickListener.onDelete(item.getId());
                        swipeLayout.close();
                    }
                }
            });

            ll_text_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && position >= 0 && position < list.size() && list.get(position) != null) {
                        clickListener.onClick(list.get(position).getId());
                        swipeLayout.close();
                    }
                }
            });

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onLongClick(list.get(pos).getId());
                    }
                }
            });
        }
    }

    public interface ClickListener {
        void onClick(String plan_id);
        void onLongClick(String plan_id);
        void onDelete(String plan_id);
    }
}
