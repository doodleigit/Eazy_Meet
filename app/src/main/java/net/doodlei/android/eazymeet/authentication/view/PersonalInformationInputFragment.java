package net.doodlei.android.eazymeet.authentication.view;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.authentication.service.AuthenticateService;
import net.doodlei.android.eazymeet.home.model.User;
import net.doodlei.android.eazymeet.home.view.HomeActivity;
import net.doodlei.android.eazymeet.utils.PreferenceManager;
import net.doodlei.android.eazymeet.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class PersonalInformationInputFragment extends Fragment {

    private Context context;
    private View view;
    private RelativeLayout profileImageLayout;
    private LinearLayout done;
    private ImageView ivProfileImage, ivBack, ivMenu;
    private EditText etFirstName, etLastName;

    private AuthenticateService authenticateService;

    private Uri imageUri;
    private String mediaId = "", imageFileName = "";
    private String userId;
    private final int REQUEST_TAKE_CAMERA = 101;
    private final int REQUEST_TAKE_GALLERY_IMAGE = 102;

    public static PersonalInformationInputFragment newInstance(String userId) {
        PersonalInformationInputFragment fragment = new PersonalInformationInputFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
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
            userId = getArguments().getString("userId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.personal_information_input_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        profileImageLayout = view.findViewById(R.id.profile_image_layout);
        done = view.findViewById(R.id.done);
        ivBack = view.findViewById(R.id.back);
        ivMenu = view.findViewById(R.id.menu);
        ivProfileImage = view.findViewById(R.id.profile_image);
        etFirstName = view.findViewById(R.id.first_name);
        etLastName = view.findViewById(R.id.last_name);

        initialComponent();
    }

    private void initialComponent() {
        authenticateService = AuthenticateService.retrofit.create(AuthenticateService.class);

        profileImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageSource(profileImageLayout);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNameFields(etFirstName, etLastName)) {
                    updateUserInfo(userId, etFirstName.getText().toString(), etLastName.getText().toString(), mediaId);
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

    private void selectImageSource(View view) {
        PopupMenu popup = new PopupMenu(context, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.image_source_menu, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.select_picture:
                        checkGalleryPermission();
                        return true;
                    case R.id.capture_picture:
                        checkCameraPermission();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();//showing popup menu
    }

    private void checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_TAKE_GALLERY_IMAGE);
        } else {
            sendImageFromGallery();
        }
    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_TAKE_CAMERA);
        } else {
            sendImageFromCamera();
        }
    }

    public void sendImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = getImageUri();
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_CAMERA);
        }
    }

    public void sendImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_TAKE_GALLERY_IMAGE);
    }

    private Uri getImageUri() {
        Uri m_imgUri = null;
        File m_file;
        try {
            SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
            String m_curentDateandTime = m_sdf.format(new Date());
            String m_imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + m_curentDateandTime + ".jpg";
            m_file = new File(m_imagePath);
            m_imgUri = Uri.fromFile(m_file);
        } catch (Exception ignored) {
        }
        return m_imgUri;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_TAKE_GALLERY_IMAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendImageFromGallery();
                }
                break;
            case REQUEST_TAKE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendImageFromCamera();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_GALLERY_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    getSelectedImagesPath(data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == REQUEST_TAKE_CAMERA) {
            if (resultCode == RESULT_OK) {
                uploadImage();
            }
        }
    }

    private void getSelectedImagesPath(Intent data) throws FileNotFoundException {
        ClipData clipData = data.getClipData();
        if (clipData != null) {
            Uri uri = null;
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                uri = item.getUri();
            }
            imageUri = uri;
            uploadImage();
        } else {
            imageUri = data.getData();
            uploadImage();
        }
    }

    private void uploadImage() {
        String path = Tools.getPath(context, imageUri);
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        Call<String> call = authenticateService.uploadUserPhoto(fileToUpload);
//        progressDialog.setMessage(getString(uploading));
//        progressDialog.show();
        sendImageRequest(call);
    }

    private void sendImageRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            mediaId = jsonObject.getString("media_id");
                            imageFileName = jsonObject.getString("photo_url");
                            Picasso.with(context).load(imageFileName).error(R.drawable.default_profile).placeholder(R.drawable.profile).into(ivProfileImage);
                        } else {
                            Toast.makeText(context, getString(R.string.there_is_an_issue_occurs), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException ignored) {
                        Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.there_is_an_issue_occurs), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, getString(R.string.the_server_not_responding), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUserInfo(String userId, String firstName, String lastName, String mediaId) {
        Call<String> call = authenticateService.updateUserInfo(userId, firstName, lastName, mediaId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            JSONObject object = jsonObject.getJSONObject("data");
                            User user = new Gson().fromJson(object.toString(), User.class);
                            toHomeActivity(user);
                        } else {
                            Toast.makeText(context, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
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

    private void toHomeActivity(User user) {
        PreferenceManager preferenceManager = PreferenceManager.newInstance(context);
        preferenceManager.setUserData(user);
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("user", (Parcelable) user);
        startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    private boolean checkNameFields(EditText etFirstName, EditText etLastName) {
        if (etFirstName.getText().toString().isEmpty()) {
            etFirstName.setError(getString(R.string.field_is_empty));
            return false;
        }
        if (etLastName.getText().toString().isEmpty()) {
            etFirstName.setError(getString(R.string.field_is_empty));
            return false;
        }
        return true;
    }

}
