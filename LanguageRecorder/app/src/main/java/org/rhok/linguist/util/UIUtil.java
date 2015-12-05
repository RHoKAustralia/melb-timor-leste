package org.rhok.linguist.util;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import org.rhok.linguist.R;
import org.rhok.linguist.application.LinguistApplication;

/**
 * Created by bramleyt on 5/12/2015.
 */
public class UIUtil {
    public static  float density;
    public static int densityDpi;

    static {
        try {
            density = LinguistApplication.getContextStatic().getResources().getDisplayMetrics().density;
            densityDpi = LinguistApplication.getContextStatic().getResources().getDisplayMetrics().densityDpi;
        } catch (Exception e) {
            density = 1;
            densityDpi = DisplayMetrics.DENSITY_DEFAULT;

        }
    }
    /**
     * Convert pixel to DP, based on device's screen density.
     * @param in
     * @return
     */
    public static float unScale(float in){
        return in/density;
    }
    /**
     * Convert DP to pixel, based on device's screen density.
     * @param in
     * @return
     */
    public static float scale(float in){
        return density*in;
    }

    /**
     * Convert DP to integer pixel, based on device's screen density.
     * @param in
     * @return
     */
    public static int scaleLayoutParam(int in){
        return (int) (in*density+0.5f);
    }


    /**
     * Set text1 and text2 from R.string. The second text view is not explicitly hidden if the text is empty.
     * @param root
     * @param text1
     * @param text2
     */
    public static void doHeadingText(View root, int text1, int text2){
        TextView tv1 = (TextView) root.findViewById(R.id.text1);
        if(tv1!=null){
            tv1.setText(text1);
        }

        TextView tv2 = (TextView) root.findViewById(R.id.text2);
        if(tv2!=null){
            if(text2==0) tv2.setVisibility(View.GONE);
            else {
                tv2.setText(text2);
                tv2.setVisibility(View.VISIBLE);
            }
        }
    }
    /**
     * Set text1 and text2. If text2 is null or empty, the second text view is hidden.
     * @param root
     * @param text1
     * @param text2
     */
    public static void doHeadingText(View root, CharSequence text1, CharSequence text2){
        TextView tv1 = (TextView) root.findViewById(R.id.text1);
        if(tv1!=null){
            tv1.setText(text1);
        }

        TextView tv2 = (TextView) root.findViewById(R.id.text2);
        if(tv2!=null){
            if(text2==null || text2.length()==0){
                tv2.setVisibility(View.GONE);
            }
            else{
                tv2.setText(text2);
                tv2.setVisibility(View.VISIBLE);
            }
        }
    }
}
