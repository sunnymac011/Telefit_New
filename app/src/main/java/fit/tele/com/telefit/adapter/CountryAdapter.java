package fit.tele.com.telefit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.modelBean.CountryBean;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.SubArray;
import fit.tele.com.telefit.utils.Preferences;

public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<CountryBean> list;
    private Preferences preferences;

    public CountryAdapter(Context context, Preferences preferences) {
        this.context = context;
        this.preferences = preferences;
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_country, parent, false);
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

    public void addAllList(ArrayList<CountryBean> data) {
        list.addAll(data);
        notifyDataSetChanged();
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
        private TextView txt_country;
        private ImageView img_selected;

        Header(View v) {
            super(v);
            txt_country = (TextView) v.findViewById(R.id.txt_country);
            img_selected = (ImageView) v.findViewById(R.id.img_selected);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(pos).getNicename().equalsIgnoreCase(preferences.getCountyPref()))
                        preferences.cleanCountydata();
                    else
                        preferences.saveCountyData(list.get(pos).getNicename());
                    notifyDataSetChanged();
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                if(list.get(pos).getNicename() != null && !TextUtils.isEmpty(list.get(pos).getNicename()))
                {
                    txt_country.setText(list.get(pos).getNicename());

                    if (list.get(pos).getNicename().equalsIgnoreCase(preferences.getCountyPref()))
                        img_selected.setVisibility(View.VISIBLE);
                    else
                        img_selected.setVisibility(View.GONE);
                }
            }
        }
    }
}
