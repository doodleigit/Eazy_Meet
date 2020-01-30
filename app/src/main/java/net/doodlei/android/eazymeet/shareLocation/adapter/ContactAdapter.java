package net.doodlei.android.eazymeet.shareLocation.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.shareLocation.model.Contact;
import net.doodlei.android.eazymeet.shareLocation.service.ContactSelectListener;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Contact> arrayList;
    private ContactSelectListener contactSelectListener;

    public ContactAdapter(Context context, ArrayList<Contact> arrayList, ContactSelectListener contactSelectListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.contactSelectListener = contactSelectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String phoneNumber = arrayList.get(position).getPhoneNumbers().get(0).getCountryCode() + arrayList.get(position).getPhoneNumbers().get(0).getNumber();
        holder.tvName.setText(arrayList.get(position).getName());
        holder.tvNumber.setText(phoneNumber);

        if (arrayList.get(position).getPhoneNumbers().size() > 1) {
            holder.tvNumber.setClickable(true);
            holder.tvNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_24dp, 0);
        } else {
            holder.tvNumber.setClickable(false);
            holder.tvNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        if (!arrayList.get(position).getPhoto().isEmpty()) {
            Picasso.with(context).load(arrayList.get(position).getPhoto()).error(R.drawable.default_profile).into(holder.ivPhoto);
        } else {
            Picasso.with(context).load(R.drawable.default_profile).into(holder.ivPhoto);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if (check) {
                    arrayList.get(position).setCheck(true);
                    contactSelectListener.onContactCheck(arrayList.get(position));
                } else {
                    arrayList.get(position).setCheck(false);
                    contactSelectListener.onContactUncheck(arrayList.get(position));
                }
            }
        });

        holder.tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showValidityTimes(position, holder.tvNumber);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvNumber;
        ImageView ivPhoto;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            ivPhoto = itemView.findViewById(R.id.photo);
            tvNumber = itemView.findViewById(R.id.number);
            checkBox = itemView.findViewById(R.id.check);
        }
    }

    private void showValidityTimes(final int position, final TextView textView) {
        final String[] numbers = new String[arrayList.get(position).getPhoneNumbers().size()];
        for (int i = 0; i < arrayList.get(position).getPhoneNumbers().size(); i++) {
            numbers[i] = arrayList.get(position).getPhoneNumbers().get(i).getCountryCode() + arrayList.get(position).getPhoneNumbers().get(i).getNumber();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(numbers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                textView.setText(numbers[which]);
                arrayList.get(position).setSelectNumberPosition(which);
            }
        });
        builder.show();
    }

}
