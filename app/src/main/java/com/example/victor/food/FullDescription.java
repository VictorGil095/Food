package com.example.victor.food;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FullDescription extends AppCompatActivity {

    ImageView mImageView;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_full);
        mImageView = (ImageView) findViewById(R.id.image_full);
        mTextView = (TextView) findViewById(R.id.text_full);
        CatalogModel catalogModel = getIntent().getParcelableExtra("catalog");
        Picasso.with(getApplicationContext()).load(catalogModel.getmIcon()).into(mImageView);
        if (!(catalogModel.getMcategoryId() == 25)) {
            mTextView.setText(catalogModel.getmName() + '\n' + catalogModel.getMdescription() + '\n' + "Цена: " + catalogModel.getmPrice() + '\n' + "Вес: " + catalogModel.getmWeight());
        } else
            mTextView.setText(catalogModel.getmName() + '\n' + catalogModel.getMdescription() + '\n' + "Цена: " + catalogModel.getmPrice() + '\n');
    }
}