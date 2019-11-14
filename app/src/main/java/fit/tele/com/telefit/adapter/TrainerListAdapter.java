package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
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
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.SubOptionsBean;
import fit.tele.com.telefit.modelBean.TrainerBean;
import fit.tele.com.telefit.utils.CircleTransform;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class TrainerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<TrainerBean> list;
    private ArrayList<TrainerBean> listFill = new ArrayList<>();
    private ClickListener clickListener;
    private String strBold = "";

    public TrainerListAdapter(Context context, RecyclerView recyclerView, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        list = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_trainer_list, parent, false);
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

    public void addAllList(ArrayList<TrainerBean> data) {
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
        private TextView txt_trainer;
        private TextView txt_company;
        private TextView txt_bio;
        private ImageView img_trainer;
        private MaterialRatingBar ratingbar_trainer;

        Header(View v) {
            super(v);
            txt_trainer = (TextView) v.findViewById(R.id.txt_trainer);
            txt_company = (TextView) v.findViewById(R.id.txt_company);
            txt_bio = (TextView) v.findViewById(R.id.txt_bio);
            img_trainer = (ImageView) v.findViewById(R.id.img_trainer);
            ratingbar_trainer = (MaterialRatingBar) v.findViewById(R.id.ratingbar_trainer);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null && list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        clickListener.onClick(list.get(pos));
                    }
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {

                if(list.get(pos).getProfilePic() != null && !TextUtils.isEmpty(list.get(pos).getProfilePic()) && list.get(pos).getProfilePic() != "") {
                    Picasso.with(context)
                            .load(list.get(pos).getProfilePic())
                            .error(R.drawable.user_placeholder)
                            .placeholder(R.drawable.user_placeholder)
                            .transform(new CircleTransform())
                            .into(img_trainer);
                }
                else
                    img_trainer.setImageResource(R.drawable.user_placeholder);

                if(list.get(pos).getName() != null && !TextUtils.isEmpty(list.get(pos).getName()))
                    txt_trainer.setText(list.get(pos).getName()+" "+list.get(pos).getlName());
                if(list.get(pos).getCompanyName() != null && !TextUtils.isEmpty(list.get(pos).getCompanyName()))
                {
                    strBold = "<b>" + list.get(pos).getCompanyName()+"</b>, "+list.get(pos).getCity()+", "+list.get(pos).getState();
                    txt_company.setText(Html.fromHtml(strBold));
                }
                if(list.get(pos).getDescription() != null && !TextUtils.isEmpty(list.get(pos).getDescription()))
                    txt_bio.setText(list.get(pos).getDescription());
            }
        }
    }

    public interface ClickListener {
        void onClick(TrainerBean trainerBean);
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
            for (TrainerBean wp : listFill)
            {
                if ((wp.getName()!=null?wp.getName():"").toLowerCase(Locale.getDefault()).contains(charText) || (wp.getlName()!=null?wp.getlName():"").toLowerCase(Locale.getDefault()).contains(charText) ||
                        (wp.getCompanyName()!=null?wp.getCompanyName():"").toLowerCase(Locale.getDefault()).contains(charText) || (wp.getCity()!=null?wp.getCity():"").toLowerCase(Locale.getDefault()).contains(charText)
                        || (wp.getState()!=null?wp.getState():"").toLowerCase(Locale.getDefault()).contains(charText) || (wp.getDescription()!=null?wp.getDescription():"").toLowerCase(Locale.getDefault()).contains(charText))
                {
                    list.add(wp);
                }

            }

        }
        notifyDataSetChanged();
    }
}
