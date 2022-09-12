package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SectionedRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater mLayoutInflater;
    private int mSectionResourceId,mTextResourceId;
    private RecyclerView.Adapter mBaseAdapter;
    private Context  mContext;

    private List<Sections> mSectionList=new ArrayList<>();
    public SectionedRecyclerViewAdapter(Context context,int sectionResourceId,int textResourceId,RecyclerView.Adapter baseAdapter,List<Sections> sectionsList)
    {
        this.mContext=context;
        this.mSectionResourceId=sectionResourceId;
        this.mTextResourceId=textResourceId;
        this.mBaseAdapter=baseAdapter;
        this.mSectionList=sectionsList;

        mBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            }
        });

    }

    public class SectionviewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public SectionviewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(mTextResourceId);
        }
    }


    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < mSectionList.size(); i++) {
            if (mSectionList.get(i).firstPosition > position) {
                break;
            }
            ++offset;
        }
        return position + offset;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return RecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < mSectionList.size(); i++) {
            if (mSectionList.get(i).sectionedPosition > sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSectionList.get(position) != null;
    }
















    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SectionviewHolder(LayoutInflater.from(mContext).inflate(mSectionResourceId,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        //((SectionviewHolder)viewHolder).title.setText(mSectionList.get(i).title);

        if (isSectionHeaderPosition(i)) {
            ((SectionviewHolder)viewHolder).title.setText(mSectionList.get(i).title);
        }else{
            mBaseAdapter.onBindViewHolder(viewHolder,sectionedPositionToPosition(i));
        }
    }

    @Override
    public int getItemCount() {
        return mSectionList.size();
    }


    public static class Sections
    {
        int firstPosition;
        int sectionedPosition;
        String title;

        public Sections(int firstPosition, String title) {
            this.firstPosition = firstPosition;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
