package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.helper.ItemTouchHelperAdapter;
import fit.tele.com.telefit.helper.ItemTouchHelperViewHolder;
import fit.tele.com.telefit.helper.OnStartDragListener;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.ExxDetial;
import fit.tele.com.telefit.modelBean.RoutinePlanDetailsBean;
import fit.tele.com.telefit.utils.CircleTransform;

public class ExercisesUpdateDragNDropAdapter extends RecyclerView.Adapter<ExercisesUpdateDragNDropAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private ArrayList<ExxDetial> list;
    private final OnStartDragListener mDragStartListener;
    private ClickListener clickListener;

    public ExercisesUpdateDragNDropAdapter(Context context, OnStartDragListener dragStartListener,ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        mDragStartListener = dragStartListener;
        list = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine_exercises, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        if (list != null && position >= 0 && position < list.size() && list.get(position) != null) {

            if(list.get(position).getExes().getExeImageUrl() != null && !TextUtils.isEmpty(list.get(position).getExes().getExeImageUrl())) {
                Picasso.with(context)
                        .load(list.get(position).getExes().getExeImageUrl())
                        .error(R.drawable.user_placeholder)
                        .placeholder(R.drawable.user_placeholder)
                        .transform(new CircleTransform())
                        .into(holder.img_exercise);
            }

            if(list.get(position).getExes().getExeTitle() != null && !TextUtils.isEmpty(list.get(position).getExes().getExeTitle()))
                holder.txt_exercise.setText(list.get(position).getExes().getExeTitle());
        }

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickListener != null && list != null && position >= 0 && position < list.size() && list.get(position) != null) {
                    clickListener.onClick(list.get(position).getExes().getId(),list.get(position));
                }
            }
        });

        // Start a drag whenever the handle view it touched
        holder.txt_exercise.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addAllList(ArrayList<ExxDetial> data) {
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

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        private TextView txt_exercise;
        private ImageView img_exercise;
        private Button btn_edit;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txt_exercise = (TextView) itemView.findViewById(R.id.txt_exercise);
            img_exercise = (ImageView) itemView.findViewById(R.id.img_exercise);
            btn_edit = (Button) itemView.findViewById(R.id.btn_edit);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public ArrayList<ExxDetial> getAllData() {
        return list;
    }

    public interface ClickListener {
        void onClick(String exe_id, ExxDetial exxDetial);
    }
}
