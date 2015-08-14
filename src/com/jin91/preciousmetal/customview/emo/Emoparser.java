package com.jin91.preciousmetal.customview.emo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.jin91.preciousmetal.R;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Emoparser {
    private Context context;
    private String[] emoList;
    private Pattern mPattern;
    public static HashMap<String, Integer> phraseIdMap;
    private static HashMap<Integer, String> idPhraseMap;
    private static Emoparser instance = null;
    private final int DEFAULT_SMILEY_TEXTS = R.array.face_array;
    private final int[] DEFAULT_EMO_RES_IDS = { R.drawable.face1, R.drawable.face2, R.drawable.face3, R.drawable.face4, R.drawable.face5, R.drawable.face6, R.drawable.face7, R.drawable.face8,
            R.drawable.face9, R.drawable.face10, R.drawable.face11, R.drawable.face12, R.drawable.face13, R.drawable.face14, R.drawable.face15, R.drawable.face16, R.drawable.face17,
            R.drawable.face18, R.drawable.face19, R.drawable.face20, R.drawable.face21, R.drawable.face22, R.drawable.face23, R.drawable.face24, R.drawable.face25, R.drawable.face26,
            R.drawable.face27, R.drawable.face28, R.drawable.face29, R.drawable.face30, R.drawable.face31, R.drawable.face32, R.drawable.face33, R.drawable.face34, R.drawable.face35,
            R.drawable.face36, R.drawable.face37, R.drawable.face38, R.drawable.face39, R.drawable.face40, R.drawable.face41, R.drawable.face42, R.drawable.face43, R.drawable.face44,
            R.drawable.face45, R.drawable.face46, R.drawable.face47, R.drawable.face48, R.drawable.face49, R.drawable.face50, R.drawable.face51, R.drawable.face52, R.drawable.face53,
            R.drawable.face54, R.drawable.face55, R.drawable.face56, R.drawable.face57, R.drawable.face58, R.drawable.face59, R.drawable.face60, R.drawable.face61, R.drawable.face62,
            R.drawable.face63, R.drawable.face64, R.drawable.face65, R.drawable.face66, R.drawable.face67, R.drawable.face68, R.drawable.face69, R.drawable.face70,R.drawable.face71,
            R.drawable.face72, R.drawable.face73, R.drawable.face74, R.drawable.face75, R.drawable.face76, R.drawable.face77, R.drawable.face78, R.drawable.face79,R.drawable.face80,
            R.drawable.face81, R.drawable.face82, R.drawable.face83, R.drawable.face84, R.drawable.face85, R.drawable.face86, R.drawable.face87, R.drawable.face88,R.drawable.face89,
            R.drawable.face90, R.drawable.face91, R.drawable.face92, R.drawable.face93, R.drawable.face94, R.drawable.face95, R.drawable.face96, R.drawable.face97,R.drawable.face98,
            R.drawable.face99, R.drawable.face100, R.drawable.face101, R.drawable.face102, R.drawable.face103, R.drawable.face104, R.drawable.face105, R.drawable.face106,R.drawable.face107,
            R.drawable.face108, R.drawable.face109, R.drawable.face110, R.drawable.face111, R.drawable.face112, R.drawable.face113, R.drawable.face114, R.drawable.face115,R.drawable.face116,R.drawable.face117};

    public int[] getResIdList() {
        return DEFAULT_EMO_RES_IDS;
    }

    public static synchronized Emoparser getInstance(Context cxt) {
        if (null == instance && null != cxt) {
            instance = new Emoparser(cxt);
        }
        return instance;
    }

    private Emoparser(Context cxt) {
        context = cxt;
        emoList = context.getResources().getStringArray(DEFAULT_SMILEY_TEXTS);
        buildMap();
        mPattern = buildPattern();
    }

    private void buildMap() {
        if (DEFAULT_EMO_RES_IDS.length != emoList.length) {
            throw new IllegalStateException("Smiley resource ID/text mismatch");
        }

        phraseIdMap = new HashMap<String, Integer>(emoList.length);
        idPhraseMap = new HashMap<Integer, String>(emoList.length);
        for (int i = 0; i < emoList.length; i++) {
            phraseIdMap.put(emoList[i], DEFAULT_EMO_RES_IDS[i]);
            idPhraseMap.put(DEFAULT_EMO_RES_IDS[i], emoList[i]);
        }
    }

    public HashMap<String, Integer> getPhraseIdMap() {
        return phraseIdMap;
    }

    public HashMap<Integer, String> getIdPhraseMap() {
        return idPhraseMap;
    }

    private Pattern buildPattern() {
        StringBuilder patternString = new StringBuilder(emoList.length * 3);
        patternString.append('(');
        for (String s : emoList) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");

        return Pattern.compile(patternString.toString());
    }

    public CharSequence emoCharsequence(CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = phraseIdMap.get(matcher.group());
            Drawable drawable = context.getResources().getDrawable(resId);
            int size = (int) (EmoCommonUtil.getElementSzie(context) * 0.8);
            drawable.setBounds(0, 0, size, size);
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            builder.setSpan(imageSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

}
