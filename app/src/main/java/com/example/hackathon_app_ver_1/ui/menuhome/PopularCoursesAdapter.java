package com.example.hackathon_app_ver_1.ui.menuhome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hackathon_app_ver_1.R;


import com.example.hackathon_app_ver_1.databinding.CardPopularCoursesBinding;
import com.example.hackathon_app_ver_1.ui.model.CourseCard;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PopularCoursesAdapter extends RecyclerView.Adapter<PopularCoursesAdapter.ViewHolder> {
    private static ClickListener mClickListener;
    private List<CourseCard> mCoursesList;

    public PopularCoursesAdapter(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setListDataItems(List<CourseCard> listItems) {
        this.mCoursesList = listItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCoursesList == null ? 0 : mCoursesList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_popular_courses, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseCard item = mCoursesList.get(position);
        if (item != null) {
            holder.bind(item);
        }
    }

    public interface ClickListener {
        void onClick(CourseCard view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardPopularCoursesBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardPopularCoursesBinding.bind(itemView);
        }

        void bind(@NonNull CourseCard data) {
            // 이미지와 텍스트를 동적으로 설정
            Glide.with(itemView.getContext())
                    .load(data.getImageCourse()) // drawable resource ID를 통해 이미지를 로드합니다.
                    .apply(new RequestOptions().centerCrop())
                    .into(binding.imvCoursePhoto);

            binding.tvCourseTitle.setText(data.getCourseTitle());

            // 클릭 이벤트 처리
            binding.getRoot().setOnClickListener(v -> mClickListener.onClick(data, getLayoutPosition()));
        }
    }
}
