package com.wakeup.anrdroidcompanytestapp_loadrss;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static final String URL_CODE = "com.wakeup.androidcompanytestapp_loadrss.URL_CODE";

    EditText etURL;
    Button btnLoadRss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etURL = (EditText) findViewById(R.id.etURL);
        btnLoadRss = (Button) findViewById(R.id.btnLoadRss);
        btnLoadRss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RssActivity.class);
                intent.putExtra(URL_CODE, etURL.getText().toString());
                startActivity(intent);
            }
        });
    }
}
