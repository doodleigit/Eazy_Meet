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
import net.doodlei.android.eazymeet.shareLocation.model.ConfirmContact;

import java.util.ArrayList;

public class InviteUserAdapter extends RecyclerView.Adapter<InviteUserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ConfirmContact> arrayList;

    public InviteUserAdapter(Context context, ArrayList<ConfirmContact> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_user_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = arrayList.get(position).getFirstName() + " " + arrayList.get(position).getLastName();
        String number = arrayList.get(position).getCountryPhoneCode() + arrayList.get(position).getMobile();
        String photo = arrayList.get(position).getUserImage();
        holder.tvName.setText(name);
        holder.tvNumber.setText(number);
        if (arrayList.get(position).getAcceptStatus() == 0) {
            holder.tvStatus.setText(context.getString(R.string.rejected));
        } else if (arrayList.get(position).getAcceptStatus() == 1) {
            holder.tvStatus.setText(context.getString(R.string.accepted));
        } else {
            holder.tvStatus.setText(context.getString(R.string.waiting));
        }

        if (photo != null && !photo.isEmpty()) {
            Picasso.with(context).load(photo).error(R.drawable.default_profile).into(holder.ivPhoto);
        } else {
            Picasso.with(context).load(R.drawable.default_profile).into(holder.ivPhoto);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvNumber, tvStatus;
        ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvNumber = itemView.findViewById(R.id.number);
            tvStatus = itemView.findViewById(R.id.status);
            ivPhoto = itemView.findViewById(R.id.photo);
        }
    }

}
