package com.example.lachlan.myfirstapp.code;

import android.os.Environment;
import android.os.StatFs;

import java.text.DecimalFormat;

/**
 * Created by lachlan on 27/04/2015.
 */
public class DiskSpace {


    public static String totalDiskSpace()
    {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long   total  = ((long)statFs.getBlockCount() * (long)statFs.getBlockSize());
        return bytesToHuman(total);
    }

    public static String totalAvailableDiskSpace()
    {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long   free   = ((long)statFs.getAvailableBlocks() * (long)statFs.getBlockSize());
        return bytesToHuman(free);
    }

    public static String getAudioFileBasePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }

/*    public static String getFilename(int picture) {
        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest" + picture + ".3gp";
        return mFileName;
    }*/

    private static String floatForm (double d)
    {
        return new DecimalFormat("#.##").format(d);
    }

    private static String bytesToHuman (long size)
    {
        long Kb = 1  * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size <  Kb)                 return floatForm(        size     ) + " byte";
        if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " Kb";
        if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " Mb";
        if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " Gb";
        if (size >= Tb && size < Pb)    return floatForm((double)size / Tb) + " Tb";
        if (size >= Pb && size < Eb)    return floatForm((double)size / Pb) + " Pb";
        if (size >= Eb)                 return floatForm((double)size / Eb) + " Eb";

        return "???";
    }

}
