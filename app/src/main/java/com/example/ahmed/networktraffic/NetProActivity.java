package com.example.ahmed.networktraffic;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NetProActivity extends AppCompatActivity {


    private TextView mIdTextView;
    private TextView mNNameTextView;
    private TextView mSrcIpTextView;
    private TextView mSrcPoTextView;
    private TextView mDesIpTextView;
    private TextView mDesPoTextView;
    private ImageView mProOmageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_pro);
        Connection con = (Connection) getIntent().getSerializableExtra("tag");
        String stringArray[] = getIntent().getExtras().getStringArray("tag2");
        mIdTextView = (TextView) findViewById(R.id.pro_id_tv);
        mNNameTextView = (TextView) findViewById(R.id.pro_name_tv);
        mSrcIpTextView = (TextView) findViewById(R.id.sc_ip_tv);
        mSrcPoTextView = (TextView) findViewById(R.id.sc_port_tv);
        mDesIpTextView = (TextView) findViewById(R.id.des_ip_tv);
        mDesPoTextView = (TextView) findViewById(R.id.des_port_tv);
        mProOmageView= (ImageView) findViewById(R.id.pro_image_view);

        PackageManager pm = getPackageManager();

        try {
            ApplicationInfo ai = null;
            ai = pm.getApplicationInfo( stringArray[0], PackageManager.GET_META_DATA);
            Drawable icon = pm.getApplicationIcon(ai);
            mProOmageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mIdTextView.setText(stringArray[1]);
        mNNameTextView.setText(stringArray[0]);
        mSrcIpTextView.setText(con.src);
        mSrcPoTextView.setText(con.spt);
        mDesIpTextView.setText(con.dst);
        mDesPoTextView.setText(con.dpt);


    }
}
