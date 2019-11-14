package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.helper.ItemTouchHelperAdapter;
import fit.tele.com.telefit.helper.ItemTouchHelperViewHolder;
import fit.tele.com.telefit.helper.OnStartDragListener;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;
import fit.tele.com.telefit.utils.CircleTransform;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private ArrayList<ChompProductBean> list;
    private final OnStartDragListener mDragStartListener;
    private ClickListener clickListener;

    public RecipeListAdapter(Context context, OnStartDragListener dragStartListener, ClickListener clickListener) {
        this.context = context;
        mDragStartListener = dragStartListener;
        this.clickListener = clickListener;
        list = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_list, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        if (list != null && position >= 0 && position < list.size() && list.get(position) != null) {

            if(list.get(position).getDetails() != null && list.get(position).getDetails().getImages() != null &&
                    list.get(position).getDetails().getImages().getFront() != null &&
                    list.get(position).getDetails().getImages().getFront().getSmall() != null && !TextUtils.isEmpty(list.get(position).getDetails().getImages().getFront().getSmall())) {
                Picasso.with(context)
                        .load(list.get(position).getDetails().getImages().getFront().getSmall())
                        .error(R.drawable.empty_food)
                        .placeholder(R.drawable.empty_food)
                        .transform(new CircleTransform())
                        .into(holder.img_recipe);
            }
            else
                holder.img_recipe.setImageResource(R.drawable.empty_food);

            if(list.get(position).getName() != null && !TextUtils.isEmpty(list.get(position).getName()))
                holder.txt_food.setText(Html.fromHtml(list.get(position).getName()));

            if (list.get(position).getDetails().getNutritionLabel() != null && list.get(position).getDetails().getNutritionLabel().getCalories() != null) {
                String strCalories = calculateCalories(list.get(position).getDetails().getNutritionLabel().getCalories().getPerServing(), list.get(position).getServingQty(), list.get(position).getServingQtySecond());
                double convertedCal = Double.parseDouble(strCalories)/4.184;
                if (list.get(position).getServingQty() == 1)
                {
                    if (list.get(position).getServingQtySecond() == 0)
                    {
                        holder.txt_food_calories_serving.setText(String.format("%.2f", convertedCal)+" cals per serving");
                    }
                    else
                        holder.txt_food_calories_serving.setText(String.format("%.2f", convertedCal)+" cals per "+list.get(position).getServingQty()+" and "+list.get(position).getServingQtySecond()+" serving");
                }
                else {
                    if (list.get(position).getServingQtySecond() == 0)
                        holder.txt_food_calories_serving.setText(String.format("%.2f", convertedCal)+" cals per "+list.get(position).getServingQty()+" serving");
                    else
                        holder.txt_food_calories_serving.setText(String.format("%.2f", convertedCal)+" cals per "+list.get(position).getServingQty()+" and "+list.get(position).getServingQtySecond()+" serving");
                }
            }
        }

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickListener != null && list != null && position >= 0 && position < list.size() && list.get(position) != null) {
                    clickListener.onClick(list.get(position));
                }
            }
        });

        // Start a drag whenever the handle view it touched
        holder.ll_details.setOnTouchListener(new View.OnTouchListener() {
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

    public void addAllList(ArrayList<ChompProductBean> data) {
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

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        private TextView txt_food;
        private ImageView img_recipe;
        private TextView txt_food_calories_serving;
        private Button btn_edit;
        private LinearLayout ll_details;

        public ItemViewHolder(View itemView) {
            super(itemView);
            img_recipe = (ImageView) itemView.findViewById(R.id.img_recipe);
            txt_food = (TextView) itemView.findViewById(R.id.txt_food);
            txt_food_calories_serving = (TextView) itemView.findViewById(R.id.txt_food_calories_serving);
            btn_edit = (Button) itemView.findViewById(R.id.btn_edit);
            ll_details = (LinearLayout) itemView.findViewById(R.id.ll_details);
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

    public ArrayList<ChompProductBean> getAllData() {
        return list;
    }

    private String calculateCalories(String strCalories, int intQty, double doubleHalfQty) {
        String calCalories = "";
        double doubleCalories = Double.parseDouble(strCalories);
        if (doubleHalfQty != 0) {
            calCalories = String.format("%.2f", (doubleCalories*intQty+(doubleCalories*doubleHalfQty)));
        }
        else {
            calCalories = String.format("%.2f", (doubleCalories*intQty));
        }

        return calCalories;
    }

    public interface ClickListener {
        void onClick(ChompProductBean chompProductBean);
    }
}
