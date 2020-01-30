package net.doodlei.android.eazymeet.eventList.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.eventList.adapter.InvitationListAdapter;
import net.doodlei.android.eazymeet.eventList.model.Invitation;
import net.doodlei.android.eazymeet.eventList.service.EventListService;
import net.doodlei.android.eazymeet.eventList.service.InvitationClickEventListener;
import net.doodlei.android.eazymeet.utils.PreferenceManager;
import net.doodlei.android.eazymeet.utils.SocketIOManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationListFragment extends Fragment {

    private Context context;

    private View view;
    private TextView tvTitle;
    private ImageView ivBack;
    private RecyclerView recyclerView;

    private InvitationListAdapter invitationListAdapter;

    private EventListService eventListService;
    private InvitationClickEventListener invitationClickEventListener;
    private ArrayList<Invitation> invitations;
    private String userId;
    private LatLng latLng;

    public static InvitationListFragment newInstance(LatLng latLng) {
        InvitationListFragment fragment = new InvitationListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("location", latLng);
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
        return inflater.inflate(R.layout.invitation_list_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        initialComponent();
        getInvitationList();
    }

    private void initialComponent() {
        if (getArguments() != null) {
            latLng = getArguments().getParcelable("location");
        }
        userId = PreferenceManager.newInstance(context).getID();
        invitations = new ArrayList<>();
        eventListService = EventListService.retrofit.create(EventListService.class);

        tvTitle = view.findViewById(R.id.title);
        ivBack = view.findViewById(R.id.back);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTitle.setText(getString(R.string.invitations));

        invitationClickEventListener = new InvitationClickEventListener() {
            @Override
            public void onAcceptClick(String invitationIdentityId, String status) {
                acceptRequest(invitationIdentityId, status);
            }

            @Override
            public void onRejectClick(String invitationIdentityId, String status) {
                acceptRequest(invitationIdentityId, status);
            }

            @Override
            public void onViewOnMapClick(String invitationIdentityId) {
                Intent intent = new Intent(getContext(), InvitationNavigationActivity.class);
                intent.putExtra("invitationId", invitationIdentityId);
                startActivity(intent);
            }
        };

        invitationListAdapter = new InvitationListAdapter(context, invitations, invitationClickEventListener);
        recyclerView.setAdapter(invitationListAdapter);

//        SocketIOManager.newInstance().socket.on("", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//
//            }
//        });
    }

    private void getInvitationList() {
        Call<String> call = eventListService.getInviteList(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String text, id, invitationIdentityId, acceptStatus, type, firstName, lastName;
                                text = object.getString("text");
                                JSONObject obj = object.getJSONObject("data");
                                id = obj.getString("id");
                                invitationIdentityId = obj.getString("invitation_identity_id");
                                acceptStatus = obj.getString("accept_status");
                                type = obj.getString("type");
                                firstName = obj.getString("first_name");
                                lastName = obj.getString("last_name");
                                invitations.add(new Invitation(text, id, invitationIdentityId, acceptStatus, type, firstName, lastName));
                            }
                            invitationListAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, getString(R.string.the_server_not_responding), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void acceptRequest(final String invitationIdentityId, final String invitationStatus) {
        Call<String> call = eventListService.invitationAccept(invitationIdentityId, userId, invitationStatus, latLng.latitude, latLng.longitude);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            for (int i = 0; i < invitations.size(); i++) {
                                if (invitationIdentityId.equals(invitations.get(i).getInvitationIdentityId())) {
                                    invitations.get(i).setAcceptStatus(invitationStatus);
                                    if (invitationStatus.equals("0")) {
                                        invitations.remove(i);
                                        invitationListAdapter.notifyDataSetChanged();
                                    } else {
                                        invitationListAdapter.notifyItemChanged(i);
                                    }
                                    break;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, getString(R.string.the_server_not_responding), Toast.LENGTH_LONG).show();
            }
        });
    }

}
