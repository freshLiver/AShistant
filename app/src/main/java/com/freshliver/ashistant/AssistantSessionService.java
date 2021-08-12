package com.freshliver.ashistant;


import android.os.Build;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.service.voice.VoiceInteractionSessionService;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class AssistantSessionService extends VoiceInteractionSessionService {
    @Override
    public VoiceInteractionSession onNewSession(Bundle bundle) {
        return new AssistantSession(this);
    }
}