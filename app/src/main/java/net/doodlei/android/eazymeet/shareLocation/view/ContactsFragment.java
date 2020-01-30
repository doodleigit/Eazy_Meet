package net.doodlei.android.eazymeet.shareLocation.view;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.home.model.InviteLocation;
import net.doodlei.android.eazymeet.shareLocation.adapter.ContactAdapter;
import net.doodlei.android.eazymeet.shareLocation.model.Contact;
import net.doodlei.android.eazymeet.shareLocation.model.PhoneNumber;
import net.doodlei.android.eazymeet.shareLocation.service.ContactSelectListener;

import java.util.ArrayList;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

public class ContactsFragment extends Fragment {

    private Context context;

    private View view;
    private RecyclerView recyclerView;
    private TextView tvTitle;
    private ImageView ivBack;
    private FloatingActionButton fbDone;

    private ContactAdapter contactAdapter;

    private InviteLocation inviteLocation;
    private ArrayList<Contact> contactList;
    private PhoneNumberUtil phoneNumberUtil;
    public static final int REQUEST_READ_CONTACTS = 79;

    public static ContactsFragment newInstance(InviteLocation inviteLocation) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle bundle = new Bundle();
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
        return inflater.inflate(R.layout.contacts_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        initialComponent();

    }

    private void initialComponent() {
        if (getArguments() != null) {
            inviteLocation = getArguments().getParcelable("location");
        }
        contactList = new ArrayList<>();
        phoneNumberUtil = PhoneNumberUtil.createInstance(context);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTitle = view.findViewById(R.id.title);
        ivBack = view.findViewById(R.id.back);
        fbDone = view.findViewById(R.id.done);
        fbDone.hide();

        ContactSelectListener contactSelectListener = new ContactSelectListener() {
            @Override
            public void onContactCheck(Contact contact) {
                fbDone.show();
            }

            @Override
            public void onContactUncheck(Contact contact) {
                boolean status = false;
                for (Contact con : contactList) {
                    if (con.isCheck()) {
                        status = true;
                        break;
                    }
                }
                if (status) {
                    fbDone.show();
                } else {
                    fbDone.hide();
                }
            }
        };

        contactAdapter = new ContactAdapter(context, contactList, contactSelectListener);
        recyclerView.setAdapter(contactAdapter);
        tvTitle.setText(getString(R.string.contacts));

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            contactList.addAll(getAllContacts());
            contactAdapter.notifyDataSetChanged();
        } else {
            requestPermission();
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity) context).onBackPressed();
            }
        });

        fbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Contact> arrayList = new ArrayList<>();
                for (Contact contact : contactList) {
                    if (contact.isCheck()) {
                        arrayList.add(contact);
                    }
                }
                initialFragment(arrayList);
            }
        });

    }

    private void initialFragment(ArrayList<Contact> arrayList) {
        Fragment fragment = ContactsPreviewFragment.newInstance(arrayList, inviteLocation);
        ((AppCompatActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, "ContactsPreviewFragment")
                .addToBackStack(null)
                .commit();
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(((AppCompatActivity) context), android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(((AppCompatActivity) context), new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(((AppCompatActivity) context), android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(((AppCompatActivity) context), new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contactList.addAll(getAllContacts());
                    contactAdapter.notifyDataSetChanged();
                } else {
                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> nameList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                String photo = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.PHOTO_URI));
                String contactPhoto = photo != null ? photo : "";
//                nameList.add(name);
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    if (pCur != null) {
                        ArrayList<PhoneNumber> phoneNumbers = new ArrayList<>();
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String[] numberDetails = getCountryIsoCode(phoneNo);
                            phoneNumbers.add(new PhoneNumber(numberDetails[0], numberDetails[1]));
//                            nameList.add(new Contact(name, contactPhoto, numberDetails[0], numberDetails[1]));
                        }
                        if (phoneNumbers.size() > 0)
                            nameList.add(new Contact(name, contactPhoto, phoneNumbers));
                        pCur.close();
                    }
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return nameList;
    }

    private String[] getCountryIsoCode(String number) {
        String[] phoneInfo = new String[2];

        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneNumberUtil.parse(number, null);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        if (phoneNumber == null) {
            phoneInfo[0] = "880";
            phoneInfo[1] = formatNumber(number);
            return phoneInfo;
        }

        phoneInfo[0] = String.valueOf(phoneNumber.getCountryCode());
        phoneInfo[1] = String.valueOf(phoneNumber.getNationalNumber());

        return phoneInfo;
    }

    private String formatNumber(String number) {
        String formattedNumber = number.replaceAll("-", "").replaceAll(" ", "");
        long convertNumber = Long.parseLong(formattedNumber);
        return String.valueOf(convertNumber);
    }

}
