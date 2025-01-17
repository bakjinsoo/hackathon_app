package com.example.hackathon_app_ver_1.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hackathon_app_ver_1.databinding.ItemShopCardBinding;
import com.example.hackathon_app_ver_1.ui.listners.MatchCourseClickListner;
import com.example.hackathon_app_ver_1.ui.model.MatchCourse;

import java.util.List;

public class MatchesCoursesAdapter extends RecyclerView.Adapter<MatchesCoursesAdapter.ViewHolder>{
    private final List<MatchCourse> mData;
    private final MatchCourseClickListner matchCourseClickListner;

    public MatchesCoursesAdapter(List<MatchCourse> mData, MatchCourseClickListner listener) {
        this.mData = mData;
        this.matchCourseClickListner = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemShopCardBinding itemCardBinding = ItemShopCardBinding.inflate(inflater, parent, false);
        return new MatchesCoursesAdapter.ViewHolder(itemCardBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setBind(mData.get(position));

        holder.itemView.setOnClickListener(v -> matchCourseClickListner.onScrollPagerItemClick(mData.get(holder.getAdapterPosition()), holder.itemCardBinding.image));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemShopCardBinding itemCardBinding;

        public ViewHolder(@NonNull ItemShopCardBinding cardBinding) {
            super(cardBinding.getRoot());
            this.itemCardBinding = cardBinding;

            //this.itemRecyclerMealBinding.
        }

        void setBind(MatchCourse matchCourse) {

            itemCardBinding.tvTitulo.setText(matchCourse.getName());
            itemCardBinding.tvCantidadCursos.setText(matchCourse.getNumberOfCourses());

            Glide.with(itemView.getContext())
                    .load(matchCourse.getImageResource())
//                .transform(new CenterCrop(), new RoundedCorners(24))
//                .transform(new RoundedCorners(40))
                    .into(itemCardBinding.image);
        }
    }
}
