package com.vivekkaushik.resumify.helper;

import com.vivekkaushik.resumify.R;

public class ResumeImage {

    public static int getImage(String template) {
        switch (template) {
            case "1":
                return R.drawable.template1;
            case "2":
                return R.drawable.template2;
            case "3":
                return R.drawable.template3;
            case "4":
                return R.drawable.template4;
            case "5":
                return R.drawable.template5;
            case "6":
                return R.drawable.template6;
            case "7":
                return R.drawable.template7;
            case "8":
                return R.drawable.template8;
            case "9":
                return R.drawable.template9;
            default:
                return R.drawable.resume;
        }
    }
}
