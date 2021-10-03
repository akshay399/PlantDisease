package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ResultActivity extends AppCompatActivity {

    private TextView cause, cure, crop, disease, main;
    private ImageView cropImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);
        main = findViewById(R.id.main);
//        cure = findViewById(R.id.cure_txt);
        crop = findViewById(R.id.crop_name_id);
//        cause = findViewById(R.id.cause_txt);
        cropImage = findViewById(R.id.cropImage_id);
        disease = findViewById(R.id.disease_id);
        String cropName = getIntent().getStringExtra("crop");
        String diseaseName = getIntent().getStringExtra("disease");
        String cropCause = getIntent().getStringExtra("cause");
        String cropCure = getIntent().getStringExtra("cure");
        String imageUrl = getIntent().getStringExtra("image");
        Uri fileUri = Uri.parse(imageUrl);
        crop.setText(cropName);
        String html = "<u><b>Cure:</b></u> " + cropCure + "<br><br>" + "<u><b>Cause:</b></u> " + cropCause;
//        cure.setText("<strong>Cure:</strong> " + cropCure);
        disease.setText(diseaseName);
//        cause.setText("Cause:" + cropCause);
        cropImage.setImageURI(fileUri);

        main.setText(Html.fromHtml(html));

    }
}