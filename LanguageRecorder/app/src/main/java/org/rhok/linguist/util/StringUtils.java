package org.rhok.linguist.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bramleyt on 5/12/2015.
 */
public class StringUtils {
    public static final String DATE_STANDARD = "dd/MM/yyyy";

    public static String formatDate(Date date, String pattern) {

        pattern = isNullOrEmpty(pattern, DATE_STANDARD);


        if (date == null) {
            date = new Date();
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern,
                Locale.getDefault());
        return formatter.format(date);

    }
    public static <T> T isNullOrEmpty(T obj, T replace) {

        if (obj == null)
            return replace;
        if ("null".equals(obj.toString()))
            return replace;

        if (obj.toString().length() == 0)
            return replace;

        return obj;

    }
    public static boolean isNullOrEmpty(CharSequence string) {
        return string == null || string.length() == 0;
    }
    public static boolean isAnyNullOrEmpty(String... strings) {
        if(strings==null || strings.length==0) return true;
        boolean isNull=false;
        for(String string : strings){
            if (string==null || string.length()==0){
                isNull=true;
                break;
            }
        }
        return isNull;
    }

    public static boolean isNullOrEmpty(List<?> list){
        return list == null || list.size() == 0;
    }
    public static boolean isNullOrEmpty(int[] list){
        return list == null || list.length == 0;
    }
    public static boolean isNullOrEmpty(Object[] list){
        return list == null || list.length == 0;
    }
    public static boolean isNullOrEmpty(Number number){
        return number==null || number==0 || number==0d || number.equals(BigDecimal.ZERO)
                || (number instanceof BigDecimal && BigDecimal.ZERO.compareTo((BigDecimal) number)==0)
                || number.toString().equals("0");

    }

    public static int getValueOrDefault(Integer in){
        return isNullOrEmpty(in)?0:in;
    }
    public static long getValueOrDefault(Long in){
        return isNullOrEmpty(in)?0L:in;
    }
    public static double getValueOrDefault(Double in){
        return isNullOrEmpty(in)?0d:in;
    }
    public static BigDecimal getValueOrDefault(BigDecimal in){
        return isNullOrEmpty(in)?BigDecimal.ZERO:in;
    }
    public static boolean getValueOrDefault(Boolean in){
        return Boolean.TRUE.equals(in);
    }

    public static String stringArrayToString(int[] in, String token, boolean filterDuplicates){
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<in.length;i++){
            if(filterDuplicates && i>0 && in[i-1]==(in[i])){
                continue;
            }
            builder.append(in[i]);
            if(i<in.length-1){
                builder.append(token);
            }
        }
        String result = builder.toString();
        if (result.endsWith(token)){
            return result.substring(0, result.length()-token.length());
        }
        return result;
    }

    public static String stringArrayToString(Object[] in, String token, boolean filterDuplicates){
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<in.length;i++){
            if(filterDuplicates && i>0 && in[i-1].equals(in[i])){
                continue;
            }
            builder.append(in[i]);
            if(i<in.length-1){
                builder.append(token);
            }
        }
        String result = builder.toString();
        if (result.endsWith(token)){
            return result.substring(0, result.length()-token.length());
        }
        return result;
    }

    public static String stringListToString(List<?> in, String token, boolean filterDuplicates){
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<in.size();i++){
            if(filterDuplicates && i>0 && in.get(i-1).equals(in.get(i))){
                continue;
            }
            builder.append(in.get(i));
            if(i<in.size()-1){
                builder.append(token);
            }
        }
        String result = builder.toString();
        if (result.endsWith(token)){
            return result.substring(0, result.length()-token.length());
        }
        return result;
    }

    /**
     * Appends the character sequence {@code text} and spans {@code what} over the appended part.
     * See {@link android.text.Spanned} for an explanation of what the flags mean.
     * @param text the character sequence to append.
     * @param what the object(s) to be spanned over the appended text.

     * @return this {@code SpannableStringBuilder}.
     */
    public static void appendSpan(SpannableStringBuilder builder, CharSequence text, Object... what) {
        int start = builder.length();
        builder.append(text);
        for(Object obj : what) {
            builder.setSpan(obj, start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }
}
