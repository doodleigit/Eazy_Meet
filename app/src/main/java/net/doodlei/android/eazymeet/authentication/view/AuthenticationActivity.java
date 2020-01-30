package net.doodlei.android.eazymeet.authentication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.home.view.HomeActivity;
import net.doodlei.android.eazymeet.utils.PreferenceManager;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

//        initialFragment();
        checkAuthorization();
    }

    private void checkAuthorization() {
        PreferenceManager preferenceManager = PreferenceManager.newInstance(getApplicationContext());
        if (preferenceManager.getID().isEmpty()) {
            initialFragment();
        } else {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    private void initialFragment() {
        Fragment fragment = PhoneNumberInputFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, "PhoneNumberInputFragment")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag("PersonalInformationInputFragment") != null) {
            // I'm viewing Fragment C
            getSupportFragmentManager().popBackStack("Phone_Verify_TAG",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            super.onBackPressed();
        }
    }
}
