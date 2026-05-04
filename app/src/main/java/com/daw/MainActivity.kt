package com.daw

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.daw.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sequencerView: SequencerView
    private lateinit var stepPadView: StepPadView
    private lateinit var mixerView: MixerView
    private lateinit var transportView: TransportView
    private lateinit var pianoRollView: PianoRollView
    private lateinit var effectsRackView: EffectsRackView
    
    private lateinit var audioEngine: AudioEngine
    private lateinit var patternManager: PatternManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        audioEngine = AudioEngine(this)
        patternManager = PatternManager()
        
        setupViews()
        setupButtons()
    }
    
    private fun setupViews() {
        sequencerView = binding.sequencerView
        sequencerView.setPatternManager(patternManager)
        
        stepPadView = binding.stepPadView
        stepPadView.setPatternManager(patternManager)
        stepPadView.setOnPadClickListener { trackIndex, stepIndex, active ->
            patternManager.setStep(trackIndex, stepIndex, active)
        }
        
        mixerView = binding.mixerView
        mixerView.setPatternManager(patternManager)
        
        transportView = binding.transportView
        transportView.setOnTransportListener(object : TransportView.TransportListener {
            override fun onPlay() {
                audioEngine.playPattern(patternManager)
            }
            
            override fun onStop() {
                audioEngine.stop()
            }
            
            override fun onRecord() {
                audioEngine.startRecording()
            }
        })
        
        pianoRollView = binding.pianoRollView
        pianoRollView.setPatternManager(patternManager)
        
        effectsRackView = binding.effectsRackView
    }
    
    private fun setupButtons() {
        binding.btnPlay.setOnClickListener {
            audioEngine.playPattern(patternManager)
        }
        
        binding.btnStop.setOnClickListener {
            audioEngine.stop()
        }
        
        binding.btnAddTrack.setOnClickListener {
            patternManager.addTrack()
            sequencerView.invalidate()
            stepPadView.invalidate()
            mixerView.invalidate()
        }
        
        binding.btnClear.setOnClickListener {
            for (trackIdx in 0 until 4) {
                for (stepIdx in 0 until 16) {
                    patternManager.setStep(trackIdx, stepIdx, false)
                }
            }
            sequencerView.invalidate()
            stepPadView.invalidate()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        audioEngine.release()
    }
}