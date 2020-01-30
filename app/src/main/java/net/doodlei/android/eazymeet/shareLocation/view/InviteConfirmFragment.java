package net.doodlei.android.eazymeet.shareLocation.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.home.model.InviteLocation;
import net.doodlei.android.eazymeet.shareLocation.adapter.InviteUserAdapter;
import net.doodlei.android.eazymeet.shareLocation.model.ConfirmContact;
import net.doodlei.android.eazymeet.utils.SocketIOManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class InviteConfirmFragment extends Fragment {

    private Context context;
    private View view;
    private TextView tvTitle, tvViewOnMap;
    private ImageView ivBack;
    private RecyclerView eazymeetUserRecyclerView, outsideUserRecyclerView;

    private InviteUserAdapter inviteUserAdapter;

    private ArrayList<ConfirmContact> confirmContacts;
    private InviteLocation inviteLocation;
    private String invitationId;

    public static InviteConfirmFragment newInstance(InviteLocation inviteLocation, ArrayList<ConfirmContact> confirmContacts, String invitationId) {
        InviteConfirmFragment fragment = new InviteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("location", inviteLocation);
        bundle.putParcelableArrayList("contacts", confirmContacts);
        bundle.putString("invitation_id", invitationId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.invite_confirm_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        initialComponent();
    }

    private void initialComponent() {
        confirmContacts = new ArrayList<>();
        if (getArguments() != null) {
            confirmContacts = getArguments().getParcelableArrayList("contacts");
            inviteLocation = getArguments().getParcelable("location");
            invitationId = getArguments().getString("invitation_id");
        }
        inviteUserAdapter = new InviteUserAdapter(context, confirmContacts);

        tvTitle = view.findViewById(R.id.title);
        tvViewOnMap = view.findViewById(R.id.viewOnMap);
        ivBack = view.findViewById(R.id.back);
        eazymeetUserRecyclerView = view.findViewById(R.id.eazymeetUserRecyclerView);
        outsideUserRecyclerView = view.findViewById(R.id.outsideUserRecyclerView);

        eazymeetUserRecyclerView.setAdapter(inviteUserAdapter);

        SocketIOManager.newInstance().socket.on(invitationId + "_invite_status", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject jsonObject = new JSONObject(args[0].toString());
                    String userId;
                    int acceptStatus;
                    userId = jsonObject.getString("user_id");
                    acceptStatus = jsonObject.getInt("accept_status");
                    for (int i = 0; i < confirmContacts.size(); i++) {
                        if (confirmContacts.get(i).getId().equals(userId)) {
                            confirmContacts.get(i).setAcceptStatus(acceptStatus);
                            final int finalI = i;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    inviteUserAdapter.notifyItemChanged(finalI);
                                }
                            });
                            break;
                        }
                    }
                } catch (JSONException e) {}
            }
        });

        tvViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMapNavigationActivity();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity) context).finish();
            }
        });
    }

    private void toMapNavigationActivity() {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra("invited_location", inviteLocation);
        intent.putExtra("invitation_id", invitationId);
        startActivity(intent);
    }

}
