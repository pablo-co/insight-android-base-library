package edu.mit.lastmite.insight_library.util;

import android.app.Application;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * GRUPO RAIDO CONFIDENTIAL
 * __________________
 *
 * [2015] - [2015] Grupo Raido Incorporated
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains
 * the property of Grupo Raido SAPI de CV and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to Grupo Raido SAPI de CV and its
 * suppliers and may be covered by México and Foreign Patents,
 * patents in process, and are protected by trade secret or
 * copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless
 * prior written permission is obtained from Grupo Raido SAPI
 * de CV.
 */
public class TextSpeaker {

    protected TextToSpeech mTextToSpeech;

    public TextSpeaker(Application application) {
        mTextToSpeech = new TextToSpeech(application, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    mTextToSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    public void say(String text) {
        mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
