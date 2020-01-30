package net.doodlei.android.eazymeet.authentication.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.chaos.view.PinView;
import com.google.gson.Gson;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.authentication.service.AuthenticateService;
import net.doodlei.android.eazymeet.home.model.User;
import net.doodlei.android.eazymeet.home.view.HomeActivity;
import net.doodlei.android.eazymeet.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhoneNumberFragment extends Fragment {

    private Context context;
    private View view;
    private ImageView ivBack;
    private TextView tvPhoneNumber;
    private PinView firstPinView;
    private LinearLayout verifyLayout;

    private AuthenticateService authenticateService;

    private String phoneNumber, countryId, countryCode, otpCode;

    public static VerifyPhoneNumberFragment newInstance(String phoneNumber, String countryId, String countryCode, String otpCode) {
        VerifyPhoneNumberFragment fragment = new VerifyPhoneNumberFragment();
        Bundle bundle = new Bundle();
        bundle.putString("phoneNumber", phoneNumber);
        bundle.putString("countryId", countryId);
        bundle.putString("countryCode", countryCode);
        bundle.putString("otpCode", otpCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phoneNumber = getArguments().getString("phoneNumber");
            countryId = getArguments().getString("countryId");
            countryCode = getArguments().getString("countryCode");
            otpCode = getArguments().getString("otpCode");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.verify_phone_number_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        ivBack = view.findViewById(R.id.back);
        tvPhoneNumber = view.findViewById(R.id.phoneNumber);
        firstPinView = view.findViewById(R.id.firstPinView);
        verifyLayout = view.findViewById(R.id.verifyLayout);

        initialComponent();
        setData();
    }

    private void initialComponent() {
        authenticateService = AuthenticateService.retrofit.create(AuthenticateService.class);

        firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals(otpCode)) {
                    getUserDetailsByPhone(phoneNumber, countryId);
                }
            }
        });

        verifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstPinView.getText().toString().equals(otpCode)) {
                    getUserDetailsByPhone(phoneNumber, countryId);
                } else {
                    Toast.makeText(context, getString(R.string.otp_does_not_match), Toast.LENGTH_LONG).show();
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity) context).onBackPressed();
            }
        });
    }

    private void setData() {
        tvPhoneNumber.setText(countryCode + phoneNumber);
        firstPinView.setText(otpCode);
    }

    private void getUserDetailsByPhone(String phoneNumber, String countryId) {
        Call<String> call = authenticateService.getUserDetails(phoneNumber, countryId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            boolean isRegistered = jsonObject.getBoolean("is_registered");
                            if (isRegistered) {
                                JSONObject object = jsonObject.getJSONObject("data");
                                User user = new Gson().fromJson(object.toString(), User.class);
                                toHomeActivity(user);
                            } else {
                                String userId = jsonObject.getString("user_id");
                                initialPersonalInformationInputFragment(userId);
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.there_is_an_issue_with_otp), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException ignored) {
                        Toast.makeText(context, getString(R.string.there_is_an_issue_with_otp), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, getString(R.string.the_server_not_responding), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void toHomeActivity(User user) {
        PreferenceManager preferenceManager = PreferenceManager.newInstance(context);
        preferenceManager.setUserData(user);
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("user", (Parcelable) user);
        startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    private void initialPersonalInformationInputFragment(String userId) {
        Fragment fragment = PersonalInformationInputFragment.newInstance(userId);
        ((AppCompatActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, "PersonalInformationInputFragment")
                .addToBackStack("Personal_Info_TAG")
                .commit();
    }

}
