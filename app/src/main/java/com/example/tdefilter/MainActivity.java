package com.example.tdefilter;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 22;
    Button btnPicture;
    Button btnGray;
    ImageView imageView;
    Bitmap originalPhoto;
    Bitmap currentPhoto;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPicture = findViewById(R.id.btncamera_id);
        btnGray = findViewById(R.id.btngray_id);
        imageView = findViewById(R.id.imageview1);

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CODE);
            }
        });

        btnGray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (originalPhoto != null) {
                    Bitmap grayPhoto = applyGrayFilter(originalPhoto);
                    imageView.setImageBitmap(grayPhoto);
                    currentPhoto = grayPhoto;
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            originalPhoto = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(originalPhoto);
            currentPhoto = originalPhoto;
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Bitmap applyGrayFilter(Bitmap sourceBitmap) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);

        Bitmap filteredBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(filteredBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);

        return filteredBitmap;
    }
}
