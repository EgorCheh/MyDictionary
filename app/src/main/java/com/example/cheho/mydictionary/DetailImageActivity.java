package com.example.cheho.mydictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cheho.myapplication.R;
import com.squareup.picasso.Picasso;

public class DetailImageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView detailImageView;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);

        intent = getIntent();
        Button butYes=findViewById(R.id.butYesDetalActiv);
        Button butNo=findViewById(R.id.butNotDetalActiv);
        butNo.setOnClickListener(this);
        butYes.setOnClickListener(this);

        detailImageView = findViewById(R.id.imViewDetalImage);
        int h=1794/3;
        Picasso.get()
                .load(intent.getExtras().getString("URL"))
                .resize(1080,1794-h)
                .into(detailImageView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.butYesDetalActiv:
                setResult(RESULT_OK, intent);
                finish();

                break;
            case R.id.butNotDetalActiv:
                super.onBackPressed();
                break;
            default:
                break;
        }
    }
}