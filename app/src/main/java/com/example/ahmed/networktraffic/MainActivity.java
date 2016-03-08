package com.example.ahmed.networktraffic;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Button filterButtonl;
    private ListView proListView;
    ArrayList<ActivityManager.RunningAppProcessInfo> runningProcesses;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        filterButtonl = (Button) findViewById(R.id.filter_button);
        proListView = (ListView) findViewById(R.id.listView);
        // Get running processes
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        runningProcesses = (ArrayList<ActivityManager.RunningAppProcessInfo>) manager.getRunningAppProcesses();
//        List<ActivityManager.RunningAppProcessInfo> nRunningProcesses = getNetworkProcesses(runningProcesses);

        if (runningProcesses != null && runningProcesses.size() > 0) {
            // Set data to the list adapter
            ProListAdapter proListAdapter = new ProListAdapter(this, runningProcesses);
            proListView.setAdapter(proListAdapter);
            proListView.setOnItemClickListener(this);
        }
    }
    public void selectNetwork(View v){

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            long send = 0;
            long recived = 0;
            // Get UID of the selected process
            int uid = 0;

            ActivityManager.RunningAppProcessInfo pro = (ActivityManager.RunningAppProcessInfo) parent.getItemAtPosition(position);
            uid = pro.uid;


            // Get traffic data
            recived = TrafficStats.getUidRxBytes(uid);
            send = TrafficStats.getUidTxBytes(uid);

            // Display data
            Toast.makeText(getApplicationContext(), "UID " + uid + " details...\n send: " + send / 1000 + "kB" + " \n recived: " + recived / 1000 + "kB", Toast.LENGTH_LONG).show();
        }



        public class ProListAdapter extends ArrayAdapter<ActivityManager.RunningAppProcessInfo> {
        private final Context context;
        private final ArrayList<ActivityManager.RunningAppProcessInfo> values;

        public ProListAdapter(Context context, ArrayList<ActivityManager.RunningAppProcessInfo> values) {
            super(context, R.layout.activity_main, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.activity_main, parent, false);

            TextView appName = (TextView) rowView.findViewById(R.id.appNameText);
            TextView appId = (TextView) rowView.findViewById(R.id.pro_id_text_view);
            ImageView appIcon = (ImageView) rowView.findViewById(R.id.detailsIco);


            PackageManager pm = context.getPackageManager();
            try {
                ApplicationInfo ai = pm.getApplicationInfo(values.get(position).processName, PackageManager.GET_META_DATA);
                Drawable icon = pm.getApplicationIcon(ai);
                 appIcon.setImageDrawable(icon);
                appName.setText(values.get(position).processName);
                appId.setText("id : "+values.get(position).pid);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }



            return rowView;
        }
    }

//    private ArrayList<ApplicationInfo> getInstalledAppsWithSpecificPermission() {
//
//        ArrayList<String> appsPkgName = new ArrayList<String>();
//
//        PackageManager pm = this.getPackageManager();
//
//        List<PackageInfo> appNamelist = pm.getInstalledPackages(0);
//        Iterator<PackageInfo> it = appNamelist.iterator();
//
//        ArrayList<ApplicationInfo> results = new ArrayList<>();
//        while (it.hasNext()) {
//            PackageInfo pk = (PackageInfo) it.next();
//            if ((pk.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
//
//                continue;
//            }
//            if (PackageManager.PERMISSION_GRANTED == pm
//                    .checkPermission(Manifest.permission.INTERNET,
//                            pk.packageName))
//                results.add(pk.applicationInfo);
//        }
//
//
//        return results;
//    }
}
