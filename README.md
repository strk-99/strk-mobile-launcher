# Hi Strk — Offline Voice Launcher

Project skeleton for a Jarvis-style Android launcher with an offline voice
agent, built for a Samsung F15 5G (8GB RAM, Android 14 / One UI 6.1).

## Non-negotiable design constraints (baked into this skeleton)

1. **Fully offline** — no cloud STT/NLU/TTS, no network calls in the voice
   pipeline at all. Everything in `voice/` must work with airplane mode on.
2. **RAM discipline** — the wake-word engine (Porcupine) is the only thing
   that stays resident while voice is toggled ON (~15-30MB). The STT model
   (Vosk, ~250-350MB active) is lazy-loaded per command and released
   immediately after — see `voice/stt/SpeechToTextEngine.kt`.
3. **Toggle-controlled, not always-on-by-default** — wake word only runs
   when the user flips the switch in Settings. Never starts at boot.
   See `voice/wakeword/WakeWordManager.kt` + `settings/SettingsActivity.kt`.
4. **Never touches banking/payment/auth apps beyond launching them** — hard
   enforced in code via `actions/SecurityDenylist.kt`, checked centrally in
   `actions/ActionExecutor.kt` before any action beyond a plain app launch.
5. **Rule-based NLU, not an on-device LLM** — a flexible LLM would cost
   1-2GB RAM active, too risky alongside other apps on an 8GB device.
   Instead: `voice/nlu/IntentParser.kt` (pattern table) +
   `voice/nlu/FuzzyMatcher.kt` (handles phrasing variance like
   "call mom" / "phone mom" / "dial mom" without a model).

## Wake word: "Hi Strk"

Custom keyword, generated via Picovoice Console (console.picovoice.ai) —
free tier, one-time online step to train the `.ppn` model file, then runs
100% offline on-device forever after. Drop the generated file at:

```
app/src/main/assets/models/hi_strk.ppn
```

If "Hi Strk" underperforms in testing (short/unusual phrases can have a
higher miss rate), try "Hi Strike" or "Hey Strk" as alternates — cheap to
regenerate and swap.

## STT model setup

Download Vosk's small English model (~50MB) from
https://alphacephei.com/vosk/models — the `vosk-model-small-en-us-0.15`
variant — and unzip into:

```
app/src/main/assets/models/vosk-model-small-en-us/
```

Not committed to git directly given the file size; consider Git LFS or a
`.gitignore` + setup-script approach if version controlling this repo.

## Build order (recommended)

1. `launcher/` — get the app drawer + home screen replacement visually
   working first (zero permissions beyond QUERY_ALL_PACKAGES)
2. `actions/AlarmAction.kt` + `actions/AppLaunchAction.kt` — zero/low
   permission wins, prove the dispatch pattern end-to-end
3. `voice/tts/VoiceResponder.kt` — trivial, get the assistant "talking" early
   for easier debugging of everything downstream
4. `voice/wakeword/` — get "Hi Strk" reliably detected, toggle wired up
5. `voice/stt/` — Vosk integration, the riskiest RAM-wise piece; profile
   memory carefully here before moving on
6. `voice/nlu/` — expand the command table as each action module lands
7. `actions/CallAction.kt`, `SettingsAction.kt`, remaining actions
8. `data/` — command history, mostly for your own debugging visibility
9. UI polish pass (dark theme, HUD-style mic animation) — see the
   `frontend-design` skill for visual direction once functionally solid

## What's deliberately NOT in this skeleton (see AndroidManifest.xml comments)

- No `BIND_ACCESSIBILITY_SERVICE` — not needed for v1's deep-link-only
  actions, and avoiding this permission class entirely is the strongest
  guarantee against ever accidentally automating something sensitive.
- No `READ_SMS` — Play-sensitive, requires default-SMS-handler status.
- No `MANAGE_EXTERNAL_STORAGE` — MediaStore covers v1's file needs.
- No boot-time auto-start for the wake-word service.

## Every TODO in this codebase

Every stub file has `TODO()` calls or `// TODO:` comments describing
exactly what needs implementing and why, tied back to the constraints
above. Hand each module to Claude Code one at a time, in the build order
above, referencing this README for context.
