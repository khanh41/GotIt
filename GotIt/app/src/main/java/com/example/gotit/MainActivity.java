package com.example.gotit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    ImageView imgInput;
    TessBaseAPI mTess;
    Button button;
    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            prepareData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mTess = new TessBaseAPI();
        mTess.init(String.valueOf(getFilesDir()),"vie");
        imgInput = findViewById(R.id.img_input);
        Bitmap input = BitmapFactory.decodeResource(getResources(),R.drawable.image_test);
        imgInput.setImageBitmap(input);
        tvResult = findViewById(R.id.result);
        button = findViewById(R.id.btnRecognize);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTess.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.image_test));
                String result = mTess.getUTF8Text();
                tvResult.setText(result);
            }
        });

    }
    public void prepareData() throws IOException {
        //copy model from assets to public folder
        File dir = new File(getFilesDir()+"/tessdata");
        if(!dir.exists()){
            dir.mkdir();
        }
        File trainedData = new File(getFilesDir()+"/tessdata/vie.trainedData");
        if(!trainedData.exists()){
            AssetManager asset = getAssets();
            InputStream is = asset.open("tessdata/vie.traineddata");
            OutputStream os =new FileOutputStream(getFilesDir()+"/tessdata/vie.traineddata");
            byte[] buffer = new byte[1024];
            int read;
            while((read=is.read(buffer))!=-1){
                os.write(buffer,0,read);
            }
            is.close(); os.flush(); os.close();
        }
    }
}
