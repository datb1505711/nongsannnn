package com.example.nongsan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class DangKy extends AppCompatActivity {
    private Button btnAcceptRegister;
    private EditText edtHoten,edtEmail,edtSdt,edtPassword,edtDiaChi;
    private RadioButton radNguoiBan,radNguoiMua;
    private ImageView imgAvatar;
    private String accountType;
    private Uri filePath;

    private final static int SELECT_PHOTO = 200;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageRef = firebaseStorage.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        addControls();

        validateRadioGroup();
        imgAvatarOnclick();
        btnRegisterOnclick();
    }

    private void imgAvatarOnclick() {
        imgAvatar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pickingImage = new Intent(Intent.ACTION_PICK);
                        pickingImage.setType("image/*");
                        startActivityForResult(pickingImage,SELECT_PHOTO);
                    }
                }
        );
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SELECT_PHOTO && resultCode==RESULT_OK && data!=null) {
            try {
                filePath = data.getData();
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imgAvatar.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void validateRadioGroup() {
        radNguoiMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radNguoiMua.setChecked(true);
                radNguoiBan.setChecked(false);
                accountType = "Nguoi Mua";
            }
        });

        radNguoiBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radNguoiMua.setChecked(false);
                radNguoiBan.setChecked(true);
                accountType = "Nguoi Ban";

            }
        });


    }

    private void btnRegisterOnclick() {



        btnAcceptRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final User user = new User(
                        edtHoten.getText().toString(),edtEmail.getText().toString(),
                        edtPassword.getText().toString(),edtSdt.getText().toString(),
                        edtDiaChi.getText().toString(),accountType);

                firebaseFirestore
                        .collection("Users")
                        .document()
                        .set(user);

            }
        });
    }

    private void addControls() {
            accountType = "Nguoi Ban";
            btnAcceptRegister = findViewById(R.id.btnAcceptRegister);
            edtHoten = findViewById(R.id.edtHoTen);
            edtEmail  =findViewById(R.id.edtEmail);
            edtPassword = findViewById(R.id.edtPassword);
            edtSdt = findViewById(R.id.edtSoDienThoai);
            edtDiaChi = findViewById(R.id.edtDiaChi);
            radNguoiBan = findViewById(R.id.radNguoiBan);
            radNguoiMua  = findViewById(R.id.radNguoiMua);
            imgAvatar = findViewById(R.id.imgAvatar);

    }
}
