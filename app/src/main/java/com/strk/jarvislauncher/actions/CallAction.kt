package com.strk.jarvislauncher.actions

import android.content.Context

object CallAction {
    fun execute(context: Context, spokenContactName: String?) {
        TODO(
            "1. Query ContactsContract for spokenContactName (fuzzy match against " +
            "contact display names, same pattern as AppLaunchAction)\n" +
            "2. Intent(Intent.ACTION_CALL, Uri.parse(\"tel:\" + number)) — requires " +
            "CALL_PHONE permission, request contextually on first use\n" +
            "3. If no match / ambiguous match, fall back to ACTION_DIAL " +
            "(opens dialer pre-filled, no permission needed) rather than guessing"
        )
    }
}
