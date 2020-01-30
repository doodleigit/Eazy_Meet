package net.doodlei.android.eazymeet.shareLocation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.shareLocation.model.Contact;

import java.util.ArrayList;

public class ContactPreviewAdapter extends RecyclerView.Adapter<ContactPreviewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Contact> arrayList;

    public ContactPreviewAdapter(Context context, ArrayList<Contact> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_preview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(arrayList.get(position).getName());

        if (!arrayList.get(position).getPhoto().isEmpty()) {
            Picasso.with(context).load(arrayList.get(position).getPhoto()).error(R.drawable.default_profile).into(holder.ivContactPhoto);
        } else {
            Picasso.with(context).load(R.drawable.default_profile).into(holder.ivContactPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private ImageView ivContactPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            ivContactPhoto = itemView.findViewById(R.id.contactPhoto);
        }
    }

}
