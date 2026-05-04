# FLM Daw - FL Studio Mobile-like DAW

A digital audio workstation for Android, inspired by FL Studio Mobile. Built with Kotlin.

## Features

- **Step Sequencer**: 16-step grid for creating patterns with visual feedback
- **Step Pad**: Interactive grid for toggling beats on/off per track
- **Piano Roll**: Note editor for detailed pattern editing
- **Transport Controls**: Play, stop, and record buttons
- **Mixer**: Volume sliders per track with mute/solo controls
- **Effects Rack**: Visual effects chain (Delay, Reverb, Filter, Distortion, Compressor)
- **Synthesizer**: Built-in synth with multiple wave shapes (sine, square, sawtooth, triangle)
- **Drum Sounds**: Kicks, snares, hi-hats, and claps for beat making
- **Pattern Management**: Multiple tracks with editable patterns and step sequencing

## Project Structure

```
daw/
├── app/
│   ├── src/main/
│   │   ├── java/com/daw/
│   │   │   ├── MainActivity.kt       # Main entry point
│   │   │   ├── AudioEngine.kt        # Real-time audio engine
│   │   │   ├── Synthesizer.kt        # Wave generation & drum sounds
│   │   │   ├── PatternManager.kt     # Pattern data and step tracking
│   │   │   ├── Pattern.kt            # Pattern model
│   │   │   ├── Track.kt              # Track model with wave type
│   │   │   ├── StepPadView.kt        # Interactive step grid
│   │   │   ├── SequencerView.kt      # Step display
│   │   │   ├── PianoRollView.kt      # Note editor
│   │   │   ├── MixerView.kt          # Volume sliders
│   │   │   ├── TransportView.kt      # Play/stop/record
│   │   │   └── EffectsRackView.kt    # Effects chain
│   │   └── res/                      # Layouts and resources
│   └── build.gradle                  # App dependencies
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

## Drum Sounds
- Kick drum (deep bass with pitch envelope)
- Snare drum (noise with decay)
- Hi-hat (high-passed noise)
- Clap (short noise burst)

## License
MIT