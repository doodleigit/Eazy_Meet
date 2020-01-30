package net.doodlei.android.eazymeet.shareLocation.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.home.model.InviteLocation;
import net.doodlei.android.eazymeet.shareLocation.adapter.ContactPreviewAdapter;
import net.doodlei.android.eazymeet.shareLocation.model.ConfirmContact;
import net.doodlei.android.eazymeet.shareLocation.model.Contact;
import net.doodlei.android.eazymeet.shareLocation.service.InviteCurrentLocationService;
import net.doodlei.android.eazymeet.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsPreviewFragment extends Fragment {

    private Context context;
    private View view;
    private TextView tvTitle, tvTime;
    private ImageView ivBack;
    private RecyclerView recyclerView;
    private Button btnInvite;

    private ContactPreviewAdapter contactPreviewAdapter;

    private InviteCurrentLocationService inviteCurrentLocationService;

    private PreferenceManager preferenceManager;
    private InviteLocation inviteLocation;
    private ArrayList<Contact> contactList;
    private String[] validityTimeTitle;
    private int[] validityTimeInt;
    private String userId;
    private long validityTime;

    public static ContactsPreviewFragment newInstance(ArrayList<Contact> arrayList, InviteLocation inviteLocation) {
        ContactsPreviewFragment fragment = new ContactsPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("contacts", arrayList);
        bundle.putParcelable("location", inviteLocation);
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
        return inflater.inflate(R.layout.contacts_preview_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        initialComponent();
    }

    private void initialComponent() {
        preferenceManager = PreferenceManager.newInstance(context);
        userId = preferenceManager.getID();
        inviteCurrentLocationService = InviteCurrentLocationService.retrofit.create(InviteCurrentLocationService.class);
        contactList = new ArrayList<>();
        validityTimeTitle = getResources().getStringArray(R.array.validity_times);
        validityTimeInt = getResources().getIntArray(R.array.validity_time_int);
        validityTime = validityTimeInt[0] * 60;
        if (getArguments() != null) {
            contactList = getArguments().getParcelableArrayList("contacts");
            inviteLocation = getArguments().getParcelable("location");
        }

        contactPreviewAdapter = new ContactPreviewAdapter(context, contactList);

        tvTitle = view.findViewById(R.id.title);
        tvTime = view.findViewById(R.id.time);
        ivBack = view.findViewById(R.id.back);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnInvite = view.findViewById(R.id.invite);

        // Create the FlexboxLayoutMananger, only flexbox library version 0.3.0 or higher support.
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        // Set flex direction.
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        // Set JustifyContent.
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(flexboxLayoutManager);

        contactPreviewAdapter = new ContactPreviewAdapter(context, contactList);
        recyclerView.setAdapter(contactPreviewAdapter);

        tvTitle.setText(getString(R.string.preview));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity) context).onBackPressed();
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showValidityTimes();
            }
        });

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                HashMap<String, String> map = new HashMap<>();
//                for (int i = 0; i < contactList.size(); i++) {
//                    map.put("name[" + i + "]", contactList.get(i).getName());
//                    map.put("mobile[" + i + "]", contactList.get(i).getNumber());
//                    map.put("country_code[" + i + "]", contactList.get(i).getCountryCode());
//                }

                List<String> mobileList = new ArrayList<>();
                List<String> countryList = new ArrayList<>();
                List<String> nameList = new ArrayList<>();
                for (Contact contact : contactList) {
                    mobileList.add(contact.getPhoneNumbers().get(contact.getSelectNumberPosition()).getNumber());
                    countryList.add(contact.getPhoneNumbers().get(contact.getSelectNumberPosition()).getCountryCode());
                    nameList.add(contact.getName());
                }
                getInviteList(userId, inviteLocation.getLocationName(), inviteLocation.getLatitude(), inviteLocation.getLongitude(), validityTime, mobileList, countryList, nameList);
            }
        });
    }

    private void showValidityTimes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.invitation_validity_time);
        builder.setItems(validityTimeTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                tvTime.setText(validityTimeTitle[which]);
                validityTime = validityTimeInt[which] * 60;
            }
        });
        builder.show();
    }

    private void getInviteList(String userId, String locationName, double latitude, double longitude, long validityTime, List<String> mobileList, List<String> countryList, List<String> nameList) {
        Call<String> call = inviteCurrentLocationService.getInviteWaitingList(userId, locationName, latitude, longitude, validityTime, mobileList, countryList, nameList);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            JSONArray jsonArray = jsonObject.getJSONObject("waiting_list").getJSONArray("reg_user");
                            ArrayList<ConfirmContact> confirmContacts = new ArrayList<>();
                            String invitationIdentityId = jsonObject.getJSONObject("waiting_list").getString("invitation_identity_id");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i).getJSONObject("user_details");
                                ConfirmContact confirmContact = new Gson().fromJson(object.toString(), ConfirmContact.class);
                                confirmContacts.add(confirmContact);
                            }
                            initialInviteConfirmFragment(confirmContacts, invitationIdentityId);
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

    private void initialInviteConfirmFragment(ArrayList<ConfirmContact> confirmContacts, String invitationId) {
        Fragment fragment = InviteConfirmFragment.newInstance(inviteLocation, confirmContacts, invitationId);
        ((AppCompatActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, "InviteConfirmFragment")
                .addToBackStack("Invite_Confirm_TAG")
                .commit();
    }

}
