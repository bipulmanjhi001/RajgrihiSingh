package in.rajgrihisingh.signature;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.gcacace.signaturepad.views.SignaturePad;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import in.rajgrihisingh.R;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;

public class CaptureSignature extends Activity {

    SignaturePad mSignaturePad;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Button mClearButton;
    private Button mSaveButton;
    String signature_type;
    String getPaths;
    Dialog dialog;
    String vendor_name,item_type_id,quantity,vr_no,token,sign;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    Bitmap newBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_receiver);

        Intent intent = getIntent();
        signature_type = intent.getStringExtra("sign_name");

        if(signature_type.equals("issuer_sign")){
            sign="issuer_sign";
        }else if(signature_type.equals("receiver_sign")){
            sign="receiver_sign";
        }

        vendor_name = intent.getStringExtra("vendor_name");
        item_type_id = intent.getStringExtra("item_type_id");
        quantity = intent.getStringExtra("quantity");
        vr_no=intent.getStringExtra("vr_no");

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token=sp.getString("keyid", "");

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }
            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }
            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    ShowImage();
                } else {
                    Toast.makeText(CaptureSignature.this, "Unable to catch the signature", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CaptureSignature.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {

        newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();

    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {

        boolean result = false;
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateToStr = signature_type+ "-" +format.format(today);
        System.out.println(dateToStr);
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("%s.jpg", dateToStr));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
            getPaths=photo.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    private void scanMediaFile(File photo) {

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        CaptureSignature.this.sendBroadcast(mediaScanIntent);

    }

    public static void verifyStoragePermissions(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    private void ShowImage(){

            Bitmap bmp = BitmapFactory.decodeFile(getPaths);
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog);
            ImageButton close = (ImageButton) dialog.findViewById(R.id.btnClose);
            ImageView view=(ImageView)dialog.findViewById(R.id.sign_load);
            view.setImageBitmap(bmp);
            Button buy = (Button) dialog.findViewById(R.id.btnBuy);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    mSignaturePad.clear();
                    mSaveButton.setEnabled(false);
                    mClearButton.setEnabled(false);
                    Toast.makeText(CaptureSignature.this, "Do again your Signature.", Toast.LENGTH_SHORT).show();
                }
            });
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadImage();
                    dialog.dismiss();
                    onBackPressed();
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

    }

    private void uploadImage(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADD_ISSUE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                String image = getStringImage(newBitmap);
                params.put("vendor_name", vendor_name);
                params.put("item_type_id", item_type_id);
                params.put("quantity", quantity);
                params.put("issue_vr_no", vr_no);
                params.put("token", token);
                params.put(sign,image);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}