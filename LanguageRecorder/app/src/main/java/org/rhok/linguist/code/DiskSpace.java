package org.rhok.linguist.code;

import android.os.Environment;
import android.os.StatFs;

import org.rhok.linguist.api.models.Phrase;
import org.rhok.linguist.util.FileUtils;

import java.io.File;
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

    public static boolean createAppDirs() {
        return getAppStorageBasePath().mkdirs() &&
            getPhrasesPath().mkdirs() &&
            getInterviewRecordingsPath().mkdirs();
    }

    private static File getAppStorageBasePath() {
        return new File(Environment.getExternalStorageDirectory(), "local_linguist");
        //switch to this for non-debug, so we don't pollute user's root dir
        //return LinguistApplication.getContextStatic().getExternalFilesDir(null).getAbsolutePath()+"/";
    }

    private static File getPhrasesPath() {
        return new File(getAppStorageBasePath(), "phrases");
    }

    public static File getInterviewsPath() {
        return new File(getAppStorageBasePath(), "interviews");
    }

    private static File getInterviewRecordingsPath() {
        return new File(getInterviewsPath(), "recordings");
    }

    public static File getPhraseAudio(Phrase phrase) {
        if (!phrase.hasAudio()) throw new IllegalArgumentException("phrase has no audio");
        String ext = FileUtils.getExtension(phrase.getAudio());
        return new File(getPhrasesPath(), phrase.getId() + "_audio." + ext);
    }

    public static File getPhraseImage(Phrase phrase) {
        if (!phrase.hasImage()) throw new IllegalArgumentException("phrase has no image");
        String ext = FileUtils.getExtension(phrase.getImage());
        return new File(getPhrasesPath(), phrase.getId() + "_image." + ext);
    }

    public static File getInterviewRecording(String filename) {
        return new File(getInterviewRecordingsPath(), filename);
    }

    /**
     * @deprecated use getInterviewRecording
     */
    @Deprecated
    public static String getAudioFileBasePath() {
        return null;
    }

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
