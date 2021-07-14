package com.sandappsefur.transport.profile;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.Inflater;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.profile.usertable;

public class FragmentDriverDetails extends Fragment {

    EditText iduser, userLogin, fname, mobile, details, vno, shtcount, available, users, bookstatus, vehicleno, nic, status, fee, imagedefault, imageurl, occupation, company, uaddress, pickuploc, droploc, sheetcount, availableu, bookedstatus;

    View view;

    String userChoosenTask;
    int REQUEST_CAMERA = 101;
    int SELECT_FILE = 102;
    Bitmap bm;
    String encodedbm1;
    ImageView imageView, imageView2;

    InputStream is;
    private final int PICK_IMAGE_REQUEST = 71;
    Uri uri;
    FirebaseAuth mAuth;

    ProgressDialog progressDialog1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_driverdetails, container, false);

        imageView = view.findViewById(R.id.imageViewDriverdpfragment);

        progressDialog1 = new ProgressDialog(getContext());
        progressDialog1.setMessage("Please Wailt...");
        progressDialog1.setCancelable(false);
        progressDialog1.setProgressStyle(progressDialog1.STYLE_SPINNER);
        progressDialog1.show();

        viewImage();

        Button btncancel = view.findViewById(R.id.btnCancelUserUpdate);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), profileDriver.class);
                startActivity(i);

            }
        });

        Button btnChangedpDriver = view.findViewById(R.id.btnChangedpDriver);
        btnChangedpDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        Button btnupdate = view.findViewById(R.id.btnUpdateDriverUpdate);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savetoFirebaseandUpdate();

            }
        });

        return view;
    }

//--------------------------------------------------save to firebase-----------------------------

    public void savetoFirebaseandUpdate() {


        FirebaseStorage storage = FirebaseStorage.getInstance("gs://tranz-b8710.appspot.com");
        StorageReference storageFolder = storage.getReference("users");


        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Sending...");
        progressDialog.show();
        String s = "user";
//        String s = UUID.randomUUID().toString();
//        final String value = s + new Date();

        if (uri != null) {
            final String value = s + appSetting.userid;
            StorageReference ref = storageFolder.child("profileimages/" + value);
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getContext(), "Send..", Toast.LENGTH_SHORT).show();
//                imageView.setVisibility(View.INVISIBLE);
//                Toasty.info(getApplicationContext(),"Uploaded",Toasty.LENGTH_SHORT).show();
                    imageView.setImageURI(null);
                    final Uri uploadSessionUri = taskSnapshot.getUploadSessionUri();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //volley wlta connection eka ghla save krnna one
//                        chatclass chatclass = new chatclass(value, editText.getText().toString(), "image", "Visible");
//                        chatcodeUpload(chatclass);

                            try {

                                String nameu = ((EditText) view.findViewById(R.id.nameDriverfrag)).getText().toString();
                                String occupationu = ((EditText) view.findViewById(R.id.occupationDriverfrag)).getText().toString();
                                String pickupu = ((EditText) view.findViewById(R.id.picupDriverfrag)).getText().toString();
                                String endu = ((EditText) view.findViewById(R.id.endplaceDriverfrag)).getText().toString();
                                String companyu = ((EditText) view.findViewById(R.id.companyDriverfrag)).getText().toString();
                                String adresu = ((EditText) view.findViewById(R.id.addressDriverfrag)).getText().toString();
                                String nicu = ((EditText) view.findViewById(R.id.nicDriverfrag)).getText().toString();
                                String mobu = ((EditText) view.findViewById(R.id.mobileDriverfrag)).getText().toString();
                                String dtlu = ((EditText) view.findViewById(R.id.detailsDriverfrag)).getText().toString();
                                String vnodfr = ((EditText) view.findViewById(R.id.vnoDriverfrag)).getText().toString();
                                String shtcntfr = ((EditText) view.findViewById(R.id.shtcountDriverfrag)).getText().toString();
                                String avamemfr = ((EditText) view.findViewById(R.id.avamembersDriverfrag)).getText().toString();

                                usertable u2 = new usertable(appSetting.userid, appSetting.userloginid, nameu, mobu, dtlu, vnodfr, nicu, "1", appSetting.usertypeid, null, value, occupationu, companyu, adresu, pickupu, endu, shtcntfr, avamemfr, null);


                                final Gson gson = new Gson();
                                final String jsonString = gson.toJson(u2);


                                String url = appSetting.volleyUrl + "uprofileimageSave_serv";

                                Response.Listener rl = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
                                        System.out.println(response);
                                        if (response.equals("1")) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                                        } else if (response.equals("2")) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                                        } else if (response.equals("3")) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        } else if (response.equals("4")) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Details Update Success", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                };

                                Response.ErrorListener re = new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        System.out.println(error.getMessage());
                                    }
                                };


                                GSONRequest request = new GSONRequest(1, url, rl, re);
                                request.json = jsonString;
                                VolleySingleton.getVolleySingleton(getContext()).getRequestQueue().add(request);


                            } catch (Exception e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Please Wait.. " + (int) progress + "%");
                }
            });

        } else {
            try {

                String nameu = ((EditText) view.findViewById(R.id.nameDriverfrag)).getText().toString();
                String occupationu = ((EditText) view.findViewById(R.id.occupationDriverfrag)).getText().toString();
                String pickupu = ((EditText) view.findViewById(R.id.picupDriverfrag)).getText().toString();
                String endu = ((EditText) view.findViewById(R.id.endplaceDriverfrag)).getText().toString();
                String companyu = ((EditText) view.findViewById(R.id.companyDriverfrag)).getText().toString();
                String adresu = ((EditText) view.findViewById(R.id.addressDriverfrag)).getText().toString();
                String nicu = ((EditText) view.findViewById(R.id.nicDriverfrag)).getText().toString();
                String mobu = ((EditText) view.findViewById(R.id.mobileDriverfrag)).getText().toString();
                String dtlu = ((EditText) view.findViewById(R.id.detailsDriverfrag)).getText().toString();
                String vnodfr = ((EditText) view.findViewById(R.id.vnoDriverfrag)).getText().toString();
                String shtcntfr = ((EditText) view.findViewById(R.id.shtcountDriverfrag)).getText().toString();
                String avamemfr = ((EditText) view.findViewById(R.id.avamembersDriverfrag)).getText().toString();

                usertable u2 = new usertable(appSetting.userid, appSetting.userloginid, nameu, mobu, dtlu, vnodfr, nicu, "1", appSetting.usertypeid, null, null, occupationu, companyu, adresu, pickupu, endu, shtcntfr, avamemfr, null);


                final Gson gson = new Gson();
                final String jsonString = gson.toJson(u2);


                String url = appSetting.volleyUrl + "uprofileimageSave_serv";

                Response.Listener rl = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                        if (response.equals("1")) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("2")) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("3")) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("4")) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Details Update Success", Toast.LENGTH_SHORT).show();
                        }

                    }
                };

                Response.ErrorListener re = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        System.out.println(error.getMessage());
                    }
                };


                GSONRequest request = new GSONRequest(1, url, rl, re);
                request.json = jsonString;
                VolleySingleton.getVolleySingleton(getContext()).getRequestQueue().add(request);


            } catch (Exception e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }
        }


    }

//--------------------------------------------------save to firebase-----------------------------

//    ---------------------------------------view details------------------------------

    public void viewImage() {

        String firstLetter = String.valueOf("A");
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(1);

        final TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://tranz-b8710.appspot.com/");
        StorageReference storageReference = storage.getReference("users");
        final StorageReference child = storageReference.child("profileimages/");

        try {

            usertable u2 = new usertable(appSetting.userid, appSetting.userloginid, appSetting.usertypeid, "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");

            final Gson gson = new Gson();
            final String jsonString = gson.toJson(u2);

            String url = appSetting.volleyUrl + "uprofileload_serv";

            Response.Listener rl = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                    if (response.equals("1")) {
                        Toast.makeText(getContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    } else {

                        if (response != null) {
                            final usertable utbl = gson.fromJson(response, usertable.class);

                            if (utbl.getImageurl() != null) {

                                if (utbl.getImageurl().equals("user" + appSetting.userid)) {
                                    Thread t = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            child.child(utbl.getImageurl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    System.out.println(">>>");
                                                    // Picasso.with(context).load(uri).into(viewHolder.imageView);
                                                    Picasso.with(getContext()).load(uri).placeholder(drawable).fit().centerCrop().into(imageView);

                                                }
                                            });
                                        }
                                    });
                                    t.start();
                                }
                            }

                            fname = view.findViewById(R.id.nameDriverfrag);
                            fname.setText(utbl.getFname());
                            ((EditText) view.findViewById(R.id.vnoDriverfrag)).setText(utbl.getVehicleno());
                            ((EditText) view.findViewById(R.id.shtcountDriverfrag)).setText(utbl.getSheetcount());
                            ((EditText) view.findViewById(R.id.avamembersDriverfrag)).setText(utbl.getAvailableu());
//                            ((EditText) view.findViewById(R.id.vehiclestatusDriverfrag)).setText(utbl.getVehicleno());
                            occupation = view.findViewById(R.id.occupationDriverfrag);
                            occupation.setText(utbl.getOccupation());
                            pickuploc = view.findViewById(R.id.picupDriverfrag);
                            pickuploc.setText(utbl.getPickuploc());
                            droploc = view.findViewById(R.id.endplaceDriverfrag);
                            droploc.setText(utbl.getDroploc());
                            company = view.findViewById(R.id.companyDriverfrag);
                            company.setText(utbl.getCompany());
                            uaddress = view.findViewById(R.id.addressDriverfrag);
                            uaddress.setText(utbl.getUaddress());
                            nic = view.findViewById(R.id.nicDriverfrag);
                            nic.setText(utbl.getNic());
                            mobile = view.findViewById(R.id.mobileDriverfrag);
                            mobile.setText(utbl.getMobile());
                            details = view.findViewById(R.id.detailsDriverfrag);
                            details.setText(utbl.getDetails());
                            progressDialog1.dismiss();

                        }


                    }

                }
            };

            Response.ErrorListener re = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog1.dismiss();
                    System.out.println(error.getMessage());
                }
            };


            GSONRequest request = new GSONRequest(1, url, rl, re);
            request.json = jsonString;
            VolleySingleton.getVolleySingleton(getContext()).getRequestQueue().add(request);


        } catch (Exception e) {
            progressDialog1.dismiss();
            e.printStackTrace();
        }
    }

//    ---------------------------------------view details------------------------------

    //    ------------------------------------------------------------------
    private void selectImage() {
//        Toast.makeText(getContext(), "select imageeeeeeeeeeeeeeeeeeeee", Toast.LENGTH_SHORT).show();
//        System.out.println("select imageeeeeeeeeeeeeeeeeeeee");
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = FragmentDriverDetails.Utility.checkPermission(getContext());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    private void cameraIntent() {
//        Toast.makeText(getContext(), "cameraaaaaaaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
//        System.out.println("cameraaaaaaaaaaaaaaaaaaaa");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    private void galleryIntent() {
//        Toast.makeText(getContext(), "galaryyyyyyyyyyyyyyyyyy", Toast.LENGTH_SHORT).show();
//        System.out.println("galaryyyyyyyyyyyyyyyyyy");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case FragmentDriverDetails.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Toast.makeText(getContext(), "onActivityResultttttttttttt", Toast.LENGTH_SHORT).show();

        if (resultCode == Activity.RESULT_OK) {
            System.out.println("result code okkkkkkkkkkk");
//            Toast.makeText(getContext(), "result code okkkkkkkkkkk", Toast.LENGTH_SHORT).show();
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }else{
//            Toast.makeText(getContext(), "reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", Toast.LENGTH_SHORT).show();
//            System.out.println(resultCode+"reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        }
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        System.out.println("lll");
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            uri = data.getData();
//            System.out.println("filepath=" + uri);
//            try {
//                //inputStream = getContentResolver().openInputStream(uri);
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                imageView.setImageBitmap(bitmap);//pennanna one thna
//            } catch (Exception e) {
//                System.out.println(">>>>>>>>>.");
//                e.printStackTrace();
//            }
//        }
//    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
//        Toast.makeText(getContext(), "onSelectFromGalleryResultreeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", Toast.LENGTH_SHORT).show();
        bm = null;
        if (data != null) {
            try {
                uri = data.getData();
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                System.out.println("---------------------------filepath=" + uri);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ((ImageView) view.findViewById(R.id.imageViewDriverdpfragment)).setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
//        Toast.makeText(getContext(), "onCaptureImageResultttttttttttttttt", Toast.LENGTH_SHORT).show();
        bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] byteArray = bytes.toByteArray();
        encodedbm1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null) {
//            System.out.println(data.getData()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..");
            try {
                String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bm, "Title", null);
                uri = Uri.parse(path);

//                uri = getImageUri(getContext(), bm);
////                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                System.out.println("---------------------------filepath=" + uri);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ((ImageView) view.findViewById(R.id.imageViewDriverdpfragment)).setImageBitmap(bm);
    }


//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//
//        final String name = ((EditText) view.findViewById(R.id.idusername)).getText().toString();
//        final String vno = ((EditText) view.findViewById(R.id.idpassword)).getText().toString();
//
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }


//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        FragmentDriverDetails fragment2 = new FragmentDriverDetails();
//
//        FragmentManager manager = getFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.linearlayoutdriverprofile, fragment2, "f2");
//        transaction.commit();
//        super.onCreate(savedInstanceState);
//    }

//    @Override
//    public void onPause() {
//        FragmentManager manager = getFragmentManager();
//        List<Fragment> list = manager.getFragments();
//        FragmentTransaction transaction = manager.beginTransaction();
//
//        for (Fragment fragment : list) {
//            transaction.remove(fragment);
//            transaction.commit();
//        }
//
//        super.onPause();
//
//    }
}
