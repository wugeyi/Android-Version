package com.monash.survivalguide.entities;

import android.content.Context;

import com.monash.survivalguide.R;
import com.monash.survivalguide.activities.DemoActivity;

public enum Demo {

  SMART_INDICATOR(R.string.demo_title_smart_indicator, R.layout.demo_smart_indicator);

  public final int titleResId;
  public final int layoutResId;

  Demo(int titleResId, int layoutResId) {
    this.titleResId = titleResId;
    this.layoutResId = layoutResId;
  }

  public static int[] tab10() {
    return new int[] {
        R.string.demo_tab_1,
        R.string.demo_tab_2,
        R.string.demo_tab_3,
        R.string.demo_tab_4,
        R.string.demo_tab_5,
        R.string.demo_tab_6,
        R.string.demo_tab_7,
        R.string.demo_tab_8
    };
  }


  public void startActivity(Context context) {
    DemoActivity.startActivity(context, this);
  }
//    public void setup(final SmartTabLayout layout) {
//        //Do nothing.
//    }

  public int[] tabs() {
    return tab10();
  }

}
