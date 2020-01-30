package net.doodlei.android.eazymeet.eventList.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import net.doodlei.android.eazymeet.R;

public class EventListActivity extends AppCompatActivity {

    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        latLng = getIntent().getParcelableExtra("location");
        initialFragment();
    }

    private void initialFragment() {
        Fragment fragment = InvitationListFragment.newInstance(latLng);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, "InvitationListFragment")
                .commit();
    }

}
