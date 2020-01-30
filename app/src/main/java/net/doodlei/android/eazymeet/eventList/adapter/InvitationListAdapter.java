package net.doodlei.android.eazymeet.eventList.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.eventList.model.Invitation;
import net.doodlei.android.eazymeet.eventList.service.InvitationClickEventListener;

import java.util.ArrayList;

public class InvitationListAdapter extends RecyclerView.Adapter<InvitationListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Invitation> arrayList;
    private InvitationClickEventListener invitationClickEventListener;

    public InvitationListAdapter(Context context, ArrayList<Invitation> arrayList, InvitationClickEventListener invitationClickEventListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.invitationClickEventListener = invitationClickEventListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String name = arrayList.get(position).getFirstName() + " " + arrayList.get(position).getLastName();
        holder.tvFullName.setText(name);
        holder.tvInvitationText.setText(arrayList.get(position).getText());

        if (arrayList.get(position).getAcceptStatus().equals("1")) {
            holder.acceptLayout.setVisibility(View.GONE);
            holder.tvViewOnMap.setVisibility(View.VISIBLE);
        } else {
            holder.acceptLayout.setVisibility(View.VISIBLE);
            holder.tvViewOnMap.setVisibility(View.GONE);
        }

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitationClickEventListener.onAcceptClick(arrayList.get(position).getInvitationIdentityId(), "1");
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitationClickEventListener.onAcceptClick(arrayList.get(position).getInvitationIdentityId(), "0");
            }
        });

        holder.tvViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitationClickEventListener.onViewOnMapClick(arrayList.get(position).getInvitationIdentityId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout acceptLayout;
        ImageView ivUserImage;
        TextView tvFullName, tvInvitationText, tvViewOnMap;
        Button btnAccept, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            acceptLayout = itemView.findViewById(R.id.acceptLayout);
            ivUserImage = itemView.findViewById(R.id.userImage);
            tvFullName = itemView.findViewById(R.id.fullName);
            tvInvitationText = itemView.findViewById(R.id.invitationText);
            tvViewOnMap = itemView.findViewById(R.id.viewOnMap);
            btnAccept = itemView.findViewById(R.id.accept);
            btnReject = itemView.findViewById(R.id.reject);
        }
    }

}
