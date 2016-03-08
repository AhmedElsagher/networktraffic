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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends ListActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get running processes
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
        if (runningProcesses != null && runningProcesses.size() > 0) {
            // Set data to the list adapter
            setListAdapter(new ListAdapter(this, getInstalledAppsWithSpecificPermission()));
        } else {
            // In case there are no processes running (not a chance :))
            Toast.makeText(getApplicationContext(), "No application is running", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        long send = 0;
        long recived = 0;
        // Get UID of the selected process
        int uid = 0;

        ApplicationInfo app = (ApplicationInfo) getListAdapter().getItem(position);
        uid = app.uid;


        // Get traffic data
        recived = TrafficStats.getUidRxBytes(uid);
        send = TrafficStats.getUidTxBytes(uid);

        // Display data
        Toast.makeText(getApplicationContext(), "UID " + uid + " details...\n send: " + send / 1000 + "kB" + " \n recived: " + recived / 1000 + "kB", Toast.LENGTH_LONG).show();
    }


    public class ListAdapter extends ArrayAdapter<ApplicationInfo> {
        // List context
        private final Context context;
        // List values
        private final ArrayList<ApplicationInfo> values;

        public ListAdapter(Context context, ArrayList<ApplicationInfo> values) {
            super(context, R.layout.activity_main, values);
            this.context = context;
            this.values = values;
        }


        /**
         * Constructing list element view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.activity_main, parent, false);


            PackageManager pm = context.getPackageManager();

            Drawable icon = pm.getApplicationIcon(values.get(position));
            String name = pm.getApplicationLabel(values.get(position)).toString();


            TextView appName = (TextView) rowView.findViewById(R.id.appNameText);
            ImageView appIcon = (ImageView) rowView.findViewById(R.id.detailsIco);
            appIcon.setImageDrawable(icon);
            appName.setText(name);


            return rowView;
        }
    }

    private ArrayList<ApplicationInfo> getInstalledAppsWithSpecificPermission() {

        ArrayList<String> appsPkgName = new ArrayList<String>();

        PackageManager pm = this.getPackageManager();

        List<PackageInfo> appNamelist = pm.getInstalledPackages(0);
        Iterator<PackageInfo> it = appNamelist.iterator();

        ArrayList<ApplicationInfo> results = new ArrayList<>();
        while (it.hasNext()) {
            PackageInfo pk = (PackageInfo) it.next();
            if ((pk.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {

                continue;
            }
            if (PackageManager.PERMISSION_GRANTED == pm
                    .checkPermission(Manifest.permission.INTERNET,
                            pk.packageName))
                results.add(pk.applicationInfo);
        }


        return results;
    }
}
