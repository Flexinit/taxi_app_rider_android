package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.models.ToolbarItems;

import java.util.ArrayList;

public class ToolbarRcAdapter extends RecyclerView.Adapter<ToolbarRcAdapter.Holder> {

    private ArrayList<ToolbarItems> mList;
    private Context context;
    private IAdapterItemClickListener iAdapterItemClickListener;

    public ToolbarRcAdapter(ArrayList<ToolbarItems> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.navbar_item, viewGroup, false);
        return new Holder(view);
    }

    public void setiAdapterItemClickListener(IAdapterItemClickListener iAdapterItemClickListener) {
        this.iAdapterItemClickListener = iAdapterItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull final ToolbarRcAdapter.Holder holder, int i) {
        holder.setIsRecyclable(false);
        final ToolbarItems list = mList.get(i);

        holder.text.setText(list.getTitle());
        holder.image.setImageResource(list.getImgRefrence());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView text;

        Holder(@NonNull final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.menu_image);
            text = itemView.findViewById(R.id.menu_text);
        }

        @Override
        public void onClick(View v) {
            if (iAdapterItemClickListener != null)
                iAdapterItemClickListener.onAdapterItemClick(v, getAdapterPosition());
        }
    }

}
