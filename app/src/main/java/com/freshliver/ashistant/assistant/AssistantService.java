package com.freshliver.ashistant.assistant;


import android.os.Build;
import android.service.voice.VoiceInteractionService;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class AssistantService extends VoiceInteractionService {}
