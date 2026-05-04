# FLM Daw - FL Studio Mobile-like DAW

A digital audio workstation for Android, inspired by FL Studio Mobile. Built with Kotlin.

## Features

- **Step Sequencer**: 16-step grid for creating patterns
- **Step Pad**: Visual grid for toggling steps on/off
- **Transport Controls**: Play, stop, and record
- **Mixer**: Volume controls per track with mute/solo
- **Synthesizer**: Built-in synth with multiple wave shapes
- **Pattern Management**: Multiple tracks with editable patterns

## Project Structure

```
daw/
├── app/
│   ├── src/main/
│   │   ├── java/com/daw/
│   │   │   ├── MainActivity.kt       # Main entry point
│   │   │   ├── AudioEngine.kt        # Audio playback engine
│   │   │   ├── Synthesizer.kt        # Wave generation
│   │   │   ├── PatternManager.kt     # Pattern data management
│   │   │   ├── Pattern.kt            # Pattern model
│   │   │   ├── Track.kt              # Track model
│   │   │   ├── StepPadView.kt        # Step grid UI
│   │   │   ├── SequencerView.kt      # Sequencer display
│   │   │   ├── MixerView.kt          # Volume sliders
│   │   │   ├── TransportView.kt      # Play/stop/record buttons
│   │   │   └── Extensions.kt         # Utility extensions
│   │   └── res/                      # Layouts and resources
│   └── build.gradle                  # App dependencies
├── build.gradle                      # Project build config
├── settings.gradle                   # Module configuration
└── .github/workflows/android.yml     # CI build pipeline
```

## Building

### GitHub Actions
Push to any branch to trigger the CI build. The APK will be available as an artifact.

### Local Build
Requires Android SDK and JDK 17:
```bash
./gradlew assembleDebug
```

## Wave Shapes
- Sine wave
- Square wave  
- Sawtooth wave
- Triangle wave

## License
MIT