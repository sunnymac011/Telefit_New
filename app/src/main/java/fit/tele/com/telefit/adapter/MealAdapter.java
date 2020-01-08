package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.FoodCategoryBean;

public class MealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<FoodCategoryBean> list;
    private ArrayList<FoodCategoryBean> listFill = new ArrayList<>();
    private ClickListener clickListener;

    public MealAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        list = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_meal_list, parent, false);
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
        private TextView txt_recipe_date;
        private TextView txt_week_day;
        private Button btn_add_calorie;
        private Button btn_barcode;
        private SwipeLayout swipeLayout;

        Header(View v) {
            super(v);
            txt_calories = (TextView) v.findViewById(R.id.txt_calories);
            txt_recipe_name = (TextView) v.findViewById(R.id.txt_recipe_name);
            txt_delete = (TextView) v.findViewById(R.id.txt_delete);
            txt_recipe_date = (TextView) v.findViewById(R.id.txt_recipe_date);
            txt_week_day = (TextView) v.findViewById(R.id.txt_week_day);
            btn_add_calorie = (Button) v.findViewById(R.id.btn_add_calorie);
            btn_barcode = (Button) v.findViewById(R.id.btn_barcode);
            swipeLayout = (SwipeLayout) v.findViewById(R.id.swipe);

            txt_calories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onAddClick(list.get(pos));
                    }
                }
            });

            txt_recipe_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onAddClick(list.get(pos));
                    }
                }
            });

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
                        txt_calories.setText(String.format("%.2f", convertedCal)+" Cals");
                    }
                    if(list.get(pos).getRecipeBeans().get(0).getUpdatedAt() != null && !TextUtils.isEmpty(list.get(pos).getRecipeBeans().get(0).getUpdatedAt()))
                    {
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = format.parse(list.get(pos).getRecipeBeans().get(0).getUpdatedAt());

                            String finalDate = (String) DateFormat.format("MM-dd-yyyy",   date);
                            String finalDay = (String) DateFormat.format("EEEE",   date);
                            txt_recipe_date.setText(finalDate);

                            if (finalDay.equalsIgnoreCase("monday")) {
                                txt_week_day.setText("Mon");
                                txt_week_day.setBackgroundResource(R.drawable.purple_circle);
                            }
                            else if (finalDay.equalsIgnoreCase("tuesday")) {
                                txt_week_day.setText("Tue");
                                txt_week_day.setBackgroundResource(R.drawable.light_blue_circle);
                            }
                            else if (finalDay.equalsIgnoreCase("wednesday")) {
                                txt_week_day.setText("Wed");
                                txt_week_day.setBackgroundResource(R.drawable.yellow_circle);
                            }
                            else if (finalDay.equalsIgnoreCase("thursday")) {
                                txt_week_day.setText("Thu");
                                txt_week_day.setBackgroundResource(R.drawable.light_gray_circle);
                            }
                            else if (finalDay.equalsIgnoreCase("friday")) {
                                txt_week_day.setText("Fri");
                                txt_week_day.setBackgroundResource(R.drawable.green_circle);
                            }
                            else if (finalDay.equalsIgnoreCase("saturday")) {
                                txt_week_day.setText("Sat");
                                txt_week_day.setBackgroundResource(R.drawable.red_circle);
                            }
                            else if (finalDay.equalsIgnoreCase("sunday")) {
                                txt_week_day.setText("Sun");
                                txt_week_day.setBackgroundResource(R.drawable.blue_circle);
                            }
                        }catch (Exception e){
                            Log.e("txt_recipe_date",""+e);
                        }
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
        void onAddClick(FoodCategoryBean categoryBean);
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
