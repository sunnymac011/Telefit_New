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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.ConferenceBean;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.utils.CircleTransform;
import fit.tele.com.telefit.utils.OnLoadMoreListener;

public class ConferenceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<ConferenceBean> list;
    private ClickListener clickListener;

    public ConferenceAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_conference, parent, false);
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

    public void addAllList(ArrayList<ConferenceBean> data) {
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
        private TextView txt_trainer_name, txt_conference_date, txt_status;
        private ImageView img_trainer;

        Header(View v) {
            super(v);
            txt_trainer_name = (TextView) v.findViewById(R.id.txt_trainer_name);
            img_trainer = (ImageView) v.findViewById(R.id.img_trainer);
            txt_conference_date = (TextView) v.findViewById(R.id.txt_conference_date);
            txt_status = (TextView) v.findViewById(R.id.txt_status);
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {

                if(list.get(pos).getAvatar() != null && !TextUtils.isEmpty(list.get(pos).getAvatar())) {
                    Picasso.with(context)
                            .load(list.get(pos).getAvatar())
                            .error(R.drawable.user_placeholder)
                            .placeholder(R.drawable.user_placeholder)
                            .transform(new CircleTransform())
                            .into(img_trainer);
                }

                if(list.get(pos).getName() != null && !TextUtils.isEmpty(list.get(pos).getName()))
                    txt_trainer_name.setText(list.get(pos).getName()+" "+list.get(pos).getlName());
                if(list.get(pos).getDate() != null && !TextUtils.isEmpty(list.get(pos).getDate()))
                    txt_conference_date.setText(getDateFormatted(list.get(pos).getDate())+" "+list.get(pos).getTime());
                if(list.get(pos).getIsAccept() != null && !TextUtils.isEmpty(list.get(pos).getIsAccept())) {
                    if (list.get(pos).getIsAccept().equalsIgnoreCase("0"))
                    {
                        txt_status.setText("Pending");
                        txt_status.setTextColor(context.getResources().getColor(R.color.yellow));
                    }
                    if (list.get(pos).getIsAccept().equalsIgnoreCase("1"))
                    {
                        txt_status.setText("Active");
                        txt_status.setTextColor(context.getResources().getColor(R.color.light_blue_text));
                    }
                    if (list.get(pos).getIsAccept().equalsIgnoreCase("2"))
                    {
                        txt_status.setText("Denied");
                        txt_status.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }
        }
    }

    public interface ClickListener {
        void onClick(String exe_id);
    }

    public String getDateFormatted(String dateNew){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            date = format.parse(dateNew);
            String finalDate = (String) DateFormat.format("MM/dd/yy",   date);
            return finalDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }
}
