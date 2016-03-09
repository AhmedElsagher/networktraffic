package com.example.ahmed.networktraffic;

import android.app.ActivityManager;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by ahmed on 08/03/16.
 */
public class Connection implements Serializable {
    //if u want state u can get by get specific field from the line
    final String states[] = {"ESTBLSH", "SYNSENT", "SYNRECV", "FWAIT1", "FWAIT2", "TMEWAIT",
            "CLOSED", "CLSWAIT", "LASTACK", "LISTEN", "CLOSING", "UNKNOWN"
    };

    String src;
    String spt;
    String dst;
    String dpt;
     String line;

    public Connection(  String line) {
         this.line = line;
        String[] fields = line.split("\\s+", 10);

        String srcA[] = fields[1].split(":", 2);
        String dstA[] = fields[2].split(":", 2);


        this.src = getAddress(srcA[0]);
        this.spt = String.valueOf(getInt16(srcA[1]));

        this.dst = getAddress(dstA[0]);
        this.dpt = String.valueOf(getInt16(dstA[1]));
    }
//the reamining methods to transform from /proc/pid/net/tcp hexa nums to decimal nums

    public static String getAddress(final String hexa) {
        try {
            final long v = Long.parseLong(hexa, 16);
            final long adr = (v >>> 24) | (v << 24) |
                    ((v << 8) & 0x00FF0000) | ((v >> 8) & 0x0000FF00);
            return ((adr >> 24) & 0xff) + "." + ((adr >> 16) & 0xff) + "." + ((adr >> 8) & 0xff) + "." + (adr & 0xff);
        } catch (Exception e) {
            Log.w("NetworkLog", e.toString(), e);
            return "-1.-1.-1.-1";
        }
    }

    public static String getAddress6(final String hexa) {
        try {
            final String ip4[] = hexa.split("0000000000000000FFFF0000");

            if (ip4.length == 2) {
                final long v = Long.parseLong(ip4[1], 16);
                final long adr = (v >>> 24) | (v << 24) |
                        ((v << 8) & 0x00FF0000) | ((v >> 8) & 0x0000FF00);
                return ((adr >> 24) & 0xff) + "." + ((adr >> 16) & 0xff) + "." + ((adr >> 8) & 0xff) + "." + (adr & 0xff);
            } else {
                return "-2.-2.-2.-2";
            }
        } catch (Exception e) {
            Log.w("NetworkLog", e.toString(), e);
            return "-1.-1.-1.-1";
        }
    }

    public static int getInt16(final String hexa) {
        try {
            return Integer.parseInt(hexa, 16);
        } catch (Exception e) {
            Log.w("NetworkLog", e.toString(), e);
            return -1;
        }
    }
}

