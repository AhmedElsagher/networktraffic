package com.example.ahmed.networktraffic;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView proListView;
    ArrayList<ActivityManager.RunningAppProcessInfo> runningProcesses;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        proListView = (ListView) findViewById(R.id.listView);
        // Get running processes
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        runningProcesses = (ArrayList<ActivityManager.RunningAppProcessInfo>) manager.getRunningAppProcesses();
        if (runningProcesses != null && runningProcesses.size() > 0) {
            ProListAdapter proListAdapter = new ProListAdapter(this, runningProcesses);
            proListView.setAdapter(proListAdapter);
            proListView.setOnItemClickListener(this);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ActivityManager.RunningAppProcessInfo pro = (ActivityManager.RunningAppProcessInfo) parent.getItemAtPosition(position);
        String b = String.valueOf(pro.pid);
        BufferedReader in = null;//   /proc/Puid/net/tcp
        try {
            in = new BufferedReader(new FileReader("/proc/" + pro.uid + "/net/tcp"));
            String line;
            ArrayList<String> lines = new ArrayList<>();
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            if (lines.size() > 0) {
                 Connection connection = new Connection(lines.get(lines.size() - 1));
                Intent intent = new Intent(this, NetProActivity.class);
                intent.putExtra("tag", connection);
                intent.putExtra("tag2", new String[]{pro.processName,pro.pid+""});
                startActivity(intent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class ProListAdapter extends ArrayAdapter<ActivityManager.RunningAppProcessInfo> {
        private final Context context;
        private final ArrayList<ActivityManager.RunningAppProcessInfo> values;

        public ProListAdapter(Context context, ArrayList<ActivityManager.RunningAppProcessInfo> values) {
            super(context, R.layout.custom_layout, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.custom_layout, parent, false);
            TextView proName = (TextView) rowView.findViewById(R.id.appNameText);
            TextView proRx = (TextView) rowView.findViewById(R.id.rx_tv);
            TextView proTx = (TextView) rowView.findViewById(R.id.tx_tv);
            ImageView appIcon = (ImageView) rowView.findViewById(R.id.detailsIco);
            ActivityManager.RunningAppProcessInfo processInfo=values.get(position);
            long rx= TrafficStats.getUidRxBytes(processInfo.uid);
            long tx= TrafficStats.getUidTxBytes(processInfo.uid);

            PackageManager pm = context.getPackageManager();
            try {
                ApplicationInfo ai = pm.getApplicationInfo(processInfo.processName, PackageManager.GET_META_DATA);
                Drawable icon = pm.getApplicationIcon(ai);
                appIcon.setImageDrawable(icon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            proName.setText(values.get(position).processName);
            proRx.setText("Rx: "+rx );
            proTx.setText("Tx: "+tx);

            return rowView;
        }
    }
}
