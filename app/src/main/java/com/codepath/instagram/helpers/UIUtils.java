package com.codepath.instagram.helpers;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.codepath.instagram.R;

/**
 * Created by araiff on 10/29/15.
 */
public class UIUtils {

    public static SpannableStringBuilder getCommentSpan(Context context, String userName, String comment) {
        ForegroundColorSpan blueForegroundColorSpan = new ForegroundColorSpan(
                context.getResources().getColor(R.color.blue_text));
        SpannableStringBuilder ssb = new SpannableStringBuilder(userName);
        ssb.setSpan(
                blueForegroundColorSpan,
                0,
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (!TextUtils.isEmpty(comment)) {
            ssb.append(" " + comment);
        }
        return ssb;
    }
}
