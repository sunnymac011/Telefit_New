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
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;

public class RecipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<ChompProductBean> list;

    public RecipeListAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recipe_list, parent, false);
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

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
//            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    private class Header extends RecyclerView.ViewHolder {
        private int pos;
        private TextView txt_food;
        private TextView txt_food_calories_serving;

        Header(View v) {
            super(v);
            txt_food = (TextView) v.findViewById(R.id.txt_food);
            txt_food_calories_serving = (TextView) v.findViewById(R.id.txt_food_calories_serving);
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                if(list.get(pos).getName() != null && !TextUtils.isEmpty(list.get(pos).getName()))
                    txt_food.setText(list.get(pos).getName());

                String strCalories = calculateCalories(list.get(pos).getDetails().getNutritionLabel().getCalories().getPerServing(), list.get(pos).getServingQty(), list.get(pos).getServingQtySecond());
                if (list.get(pos).getServingQty() == 1)
                {
                    if (list.get(pos).getServingQtySecond() == 0)
                    {
                        txt_food_calories_serving.setText(strCalories+" cals serving");
                    }
                    else
                        txt_food_calories_serving.setText(strCalories+" cals per "+list.get(pos).getServingQtySecond()+" serving");
                }
                else {
                    if (list.get(pos).getServingQtySecond() == 0)
                        txt_food_calories_serving.setText(strCalories+" cals per "+list.get(pos).getServingQty()+" serving");
                    else
                        txt_food_calories_serving.setText(strCalories+" cals per "+list.get(pos).getServingQty()+" and "+list.get(pos).getServingQtySecond()+" serving");
                }
            }
        }
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
}
