package net.doodlei.android.eazymeet.shareLocation.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.home.model.InviteLocation;

public class InviteToCurrentActivity extends AppCompatActivity {

    private InviteLocation inviteLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_to_current);

        inviteLocation = getIntent().getParcelableExtra("location");

        initialFragment();
    }

    private void initialFragment() {
        Fragment fragment = ContactsFragment.newInstance(inviteLocation);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, "ContactsFragment")
                .commit();
    }

}
