package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.FoodCategoryBean;
import fit.tele.com.telefit.modelBean.TrainerBean;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;
import fit.tele.com.telefit.utils.CircleTransform;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FoodCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<FoodCategoryBean> list;
    private ArrayList<FoodCategoryBean> listFill = new ArrayList<>();
    private ClickListener clickListener;

    public FoodCategoryAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        list = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_food_calories_list, parent, false);
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

    public void addAllList(ArrayList<FoodCategoryBean> data) {
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
        private TextView txt_calories;
        private TextView txt_recipe_name;
        private TextView txt_delete;
        private Button btn_add_calorie;
        private Button btn_barcode;
        private SwipeLayout swipeLayout;

        Header(View v) {
            super(v);
            txt_calories = (TextView) v.findViewById(R.id.txt_calories);
            txt_recipe_name = (TextView) v.findViewById(R.id.txt_recipe_name);
            txt_delete = (TextView) v.findViewById(R.id.txt_delete);
            btn_add_calorie = (Button) v.findViewById(R.id.btn_add_calorie);
            btn_barcode = (Button) v.findViewById(R.id.btn_barcode);
            swipeLayout = (SwipeLayout) v.findViewById(R.id.swipe);

            btn_add_calorie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onClick(list.get(pos), false);
                    }
                }
            });

            btn_barcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onClick(list.get(pos), true);
                    }
                }
            });

            txt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeLayout.close();
                    if (clickListener != null)
                        clickListener.onDeletClick(list.get(pos).getId().toString(), list.get(pos));
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {

                if(list.get(pos).getFoodType() != null && !TextUtils.isEmpty(list.get(pos).getFoodType()))
                    txt_recipe_name.setText(list.get(pos).getFoodType());
                if (list.get(pos).getRecipeBeans() != null && list.get(pos).getRecipeBeans().size() > 0) {
                    if(list.get(pos).getRecipeBeans().get(0).getRacipeCalories() != null && !TextUtils.isEmpty(list.get(pos).getRecipeBeans().get(0).getRacipeCalories()))
                    {
                        double convertedCal = Double.parseDouble(list.get(pos).getRecipeBeans().get(0).getRacipeCalories())/4.184;
                        txt_calories.setText(String.format("%.2f", convertedCal));
                    }
                }
                else
                    txt_calories.setText("0");

                if(list.get(pos).getId() != null && !TextUtils.isEmpty(list.get(pos).getId())) {
                    if (Integer.parseInt(list.get(pos).getId())>5) {
                        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

                        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.bottom_wrapper));
                        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, swipeLayout.findViewById(R.id.bottom_wrapper1));
                    }
                }
            }
        }
    }

    public interface ClickListener {
        void onClick(FoodCategoryBean categoryBean, boolean isBarcode);
        void onDeletClick(String foodCatId, FoodCategoryBean categoryBean);
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
            for (FoodCategoryBean wp : listFill)
            {
                if (wp.getFoodType().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    list.add(wp);
                }

            }

        }
        notifyDataSetChanged();
    }
}
