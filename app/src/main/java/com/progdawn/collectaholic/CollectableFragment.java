package com.progdawn.collectaholic;

import android.support.v4.app.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import static android.widget.CompoundButton.*;

/**
 * Created by Dawn Myers on 5/7/2017.
 */

public class CollectableFragment extends Fragment {

    private static final String ARG_COLLECTABLE_ID = "collectable_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;

    private Collectable mCollectable;
    private File mPhotoFile;
    private EditText mNameField;
    private Button mDateButton;
    private Button mShareButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    public static CollectableFragment newInstance(UUID collectableId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COLLECTABLE_ID, collectableId);
        CollectableFragment fragment = new CollectableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID collectableId = (UUID)getArguments().getSerializable(ARG_COLLECTABLE_ID);
        mCollectable = Collection.get(getActivity()).getCollectable(collectableId);
        mPhotoFile = Collection.get(getActivity()).getPhotoFile(mCollectable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCollectable.setDate(date);
            updateDate();
        }else if (requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.progdawn.android.collectaholic.fileprovider", mPhotoFile);

            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCollectable.getDate().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_collectable, container, false);

        mNameField = (EditText)v.findViewById(R.id.collectable_name);
        mNameField.setText(mCollectable.getName());
        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCollectable.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.collectable_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCollectable.getDate());
                dialog.setTargetFragment(CollectableFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });


        mShareButton = (Button)v.findViewById(R.id.collectable_share);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCollectableShare());
                i = Intent.createChooser(i, getString(R.string.send_share));
                startActivity(i);
            }
        });


        PackageManager packageManager = getActivity().getPackageManager();
        mPhotoButton = (ImageButton)v.findViewById(R.id.collectable_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.progdawn.android.collectaholic.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for(ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView)v.findViewById(R.id.collectable_photo);
        updatePhotoView();

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();

        Collection.get(getActivity())
                .updateCollectable(mCollectable);
    }

    private String getCollectableShare(){

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCollectable.getDate()).toString();


        String report = getString(R.string.collectable_share, mCollectable.getName(), dateString);

        return report;
    }

    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
