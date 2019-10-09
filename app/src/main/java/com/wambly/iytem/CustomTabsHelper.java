package com.wambly.iytem;

import android.app.Activity;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

class CustomTabsHelper {

    @SuppressWarnings("SameParameterValue")
    static void chromeTab(Activity activity, String url){
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setStartAnimations(activity,R.anim.slide_in_right , R.anim.slide_out_left);
        intentBuilder.setExitAnimations(activity, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        intentBuilder.setToolbarColor(activity.getResources().getColor(R.color.toolbarBg));
        intentBuilder.addDefaultShareMenuItem();
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }
}
