package com.example.hackathon_app_ver_1.ui.menusearch;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.hackathon_app_ver_1.databinding.ItemPagerCardBinding;
import com.example.hackathon_app_ver_1.ui.base.BaseViewHolder;
import com.example.hackathon_app_ver_1.ui.listners.ItemClickListner;
import com.example.hackathon_app_ver_1.ui.model.MatchCourse;

import java.util.List;

public class CourseTopicsViewPager extends RecyclerView.Adapter<BaseViewHolder<MatchCourse>> {
    private List<MatchCourse> mCoursesList;
    private final ItemClickListner<MatchCourse> matchCourseClickListner;

    public CourseTopicsViewPager(ItemClickListner<MatchCourse> listener) {
        this.matchCourseClickListner = listener;
    }

    public void setListDataItems(List<MatchCourse> listItems) {
        this.mCoursesList = listItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder<MatchCourse> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPagerCardBinding itemPagerCardBinding = ItemPagerCardBinding.inflate(inflater, parent, false);
        return new ViewHolder(itemPagerCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<MatchCourse> holder, int position) {
        MatchCourse item = mCoursesList.get(position);
        holder.bind(item);
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.getItemCardBinding().cardViewCourse.setOnClickListener(v -> {
            matchCourseClickListner.onItemClick(item,
                    viewHolder.getItemCardBinding().image);
        });

//        holder.setBind(mCoursesList.get(position));
//
//        holder.binding.cardViewCourse.setOnClickListener(
//                v -> matchCourseClickListener.onScrollPagerItemClick(
//                        mCoursesList.get(holder.getAdapterPosition()),
//                        holder.binding.image));
    }
    @Override
    public int getItemCount() {
        return mCoursesList.size();
    }


    public static class ViewHolder extends BaseViewHolder<MatchCourse> {

        ItemPagerCardBinding itemCardBinding;

        public ViewHolder(@NonNull ItemPagerCardBinding binding) {
            super(binding.getRoot());
            this.itemCardBinding = binding;
        }

        public ItemPagerCardBinding getItemCardBinding() {
            return itemCardBinding;
        }

        @Override
        public void bind(MatchCourse matchCourse) {
            itemCardBinding.tvTitulo.setText(matchCourse.getName());
            itemCardBinding.tvCantidadCursos.setText(matchCourse.getNumberOfCourses());

            Glide.with(itemView.getContext())
                    .load(matchCourse.getImageResource())
                    .transform(new CenterCrop())
                    .into(itemCardBinding.image);
        }
    }
}
