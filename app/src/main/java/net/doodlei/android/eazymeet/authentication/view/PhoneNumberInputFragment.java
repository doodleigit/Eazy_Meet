package net.doodlei.android.eazymeet.authentication.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.authentication.model.CountryInfo;
import net.doodlei.android.eazymeet.authentication.service.AuthenticateService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneNumberInputFragment extends Fragment {

    private Context context;
    private View view;
    private ImageView ivBack, ivMenu;
    private TextView tvCountryCode;
    private EditText etMobile;
    private LinearLayout next;

    private AuthenticateService authenticateService;

    private List<CountryInfo> countryInfoList;
    private CountryInfo countryInfo = null;

    public static PhoneNumberInputFragment newInstance() {
        PhoneNumberInputFragment fragment = new PhoneNumberInputFragment();

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.phone_number_input_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        initialComponent();
        getAllCountryInfo();
    }

    private void initialComponent() {
        ivBack = view.findViewById(R.id.back);
        ivMenu = view.findViewById(R.id.menu);
        tvCountryCode = view.findViewById(R.id.countryCode);
        etMobile = view.findViewById(R.id.mobile);
        next = view.findViewById(R.id.next);
        ivBack.setVisibility(View.GONE);

        authenticateService = AuthenticateService.retrofit.create(AuthenticateService.class);
        countryInfoList = new ArrayList<>();

        tvCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countryInfoList.size() == 0) {
                    getAllCountryInfo();
                } else {
                    showAllCountryCode();
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity) context).onBackPressed();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countryInfo != null && mobileNumberValidation(etMobile)) {
                    sendOtpRequest(etMobile.getText().toString(), countryInfo.getCountryId(), countryInfo.getCountryPhoneCode());
                }
            }
        });
    }

    private void setData() {
        if (countryInfoList.size() > 0) {
            countryInfo = countryInfoList.get(0);
            tvCountryCode.setText(getString(R.string.format_country_code, countryInfoList.get(0).getCountryIsoCode2(), countryInfoList.get(0).getCountryPhoneCode()));
        }
    }

    private void showAllCountryCode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.select_country);

        String[] countyCodes = new String[countryInfoList.size()];
        for (int i = 0; i < countryInfoList.size(); i++) {
            countyCodes[i] = getString(R.string.format_country_code, countryInfoList.get(i).getCountryIsoCode2(), countryInfoList.get(i).getCountryPhoneCode());
        }
        builder.setItems(countyCodes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                countryInfo = countryInfoList.get(which);
                tvCountryCode.setText(getString(R.string.format_country_code, countryInfoList.get(which).getCountryIsoCode2(), countryInfoList.get(which).getCountryPhoneCode()));
            }
        });
        builder.show();
    }

    private boolean mobileNumberValidation(EditText etMobile) {
        if (etMobile.getText().toString().isEmpty()) {
            etMobile.setError(getString(R.string.field_is_empty));
        } else if (etMobile.getText().toString().length() < 4) {
            etMobile.setError(getString(R.string.please_enter_a_valid_phone_number));
        } else {
            return true;
        }
        return false;
    }

    private void getAllCountryInfo() {
        Call<String> call = authenticateService.getCountry();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            countryInfoList.clear();
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                CountryInfo countryInfo = new Gson().fromJson(object.toString(), CountryInfo.class);
                                countryInfoList.add(countryInfo);
                            }
                            setData();
                        }
                    } catch (JSONException ignored) {
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void sendOtpRequest(String phoneNumber, final String countryId, final String countryCode) {
        Call<String> call = authenticateService.sendOtp(phoneNumber, countryCode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            String phoneNumber = jsonObject.getString("phone_num");
                            String otpCode = jsonObject.getString("otp_code");
                            initialOtpVerifyFragment(phoneNumber, countryId, countryCode, otpCode);
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

    private void initialOtpVerifyFragment(String phoneNumber, String countryId, String countryCode, String otpCode) {
        Fragment fragment = VerifyPhoneNumberFragment.newInstance(phoneNumber, countryId, countryCode, otpCode);
        ((AppCompatActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, "VerifyPhoneNumberFragment")
                .addToBackStack("Phone_Verify_TAG")
                .commit();
    }

}
