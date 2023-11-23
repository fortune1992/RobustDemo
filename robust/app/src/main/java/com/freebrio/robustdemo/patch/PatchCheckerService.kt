package com.bikan.reading.patch

import android.app.IntentService
import android.content.Intent

class PatchCheckerService : IntentService("PatchChecker") {

    override fun onHandleIntent(intent: Intent) {
        PatchManager.requestPatch()
    }
}
