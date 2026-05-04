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
    
    private lateinit var audioEngine: AudioEngine
    private lateinit var patternManager: PatternManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        audioEngine = AudioEngine(this)
        patternManager = PatternManager()
        
        setupViews()
        setupTransport()
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
                audioEngine.playPattern(patternManager.currentPattern)
            }
            
            override fun onStop() {
                audioEngine.stop()
            }
            
            override fun onRecord() {
                audioEngine.startRecording()
            }
        })
    }
    
    private fun setupTransport() {
        binding.btnAddTrack.setOnClickListener {
            patternManager.addTrack()
            sequencerView.invalidate()
            stepPadView.invalidate()
            mixerView.invalidate()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        audioEngine.release()
    }
}