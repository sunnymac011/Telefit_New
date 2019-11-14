package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.CustomFoodBean;
import fit.tele.com.telefit.modelBean.NutritionLabelBean;
import fit.tele.com.telefit.modelBean.SetsRepsBean;

public class AddCustomFoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<NutritionLabelBean> list;
    private ClickListener clickListener;

    public AddCustomFoodAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        list = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nutrition_details, parent, false);
        return new Header(view);
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

    public void addAllList(ArrayList<NutritionLabelBean> data) {
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
        private Button btn_remove,btn_edit;
        private TextView txt_name,txt_measur,txt_per,txt_serving;

        Header(View v) {
            super(v);
            txt_name = (TextView) v.findViewById(R.id.txt_name);
            txt_measur = (TextView) v.findViewById(R.id.txt_measur);
            txt_per = (TextView) v.findViewById(R.id.txt_per);
            txt_serving = (TextView) v.findViewById(R.id.txt_serving);
            btn_remove = (Button) v.findViewById(R.id.btn_remove);
            btn_edit = (Button) v.findViewById(R.id.btn_edit);

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onEditClick(list.get(pos));
                    }
                }
            });

            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onDeleteClick(pos);
                        list.remove(pos);
                        notifyItemRemoved(pos);
                    }
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                if(list.get(pos).getNutritionType() != null && !TextUtils.isEmpty(list.get(pos).getNutritionType()))
                    txt_name.setText(list.get(pos).getNutritionType());
                if(list.get(pos).getMeasurement() != null && !TextUtils.isEmpty(list.get(pos).getMeasurement()))
                    txt_measur.setText(list.get(pos).getMeasurement());
                if(list.get(pos).getPer100g() != null && !TextUtils.isEmpty(list.get(pos).getPer100g()))
                    txt_per.setText(list.get(pos).getPer100g());
                if(list.get(pos).getPerServing() != null && !TextUtils.isEmpty(list.get(pos).getPerServing()))
                    txt_serving.setText(list.get(pos).getPerServing());
            }
        }
    }

    public interface ClickListener {
        void onEditClick(NutritionLabelBean nutritionLabelBean);
        void onDeleteClick(int pos);
    }
}
