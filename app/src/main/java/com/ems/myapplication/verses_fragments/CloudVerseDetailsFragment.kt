package com.ems.myapplication.verses_fragments

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ems.myapplication.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.verse_details_fragment.*

class CloudVerseDetailsFragment : Fragment(R.layout.verse_details_fragment) {

    private lateinit var mediaPlayer: MediaPlayer
    private val args: CloudVerseDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initializeMediaPlayer()

        verseReader.visibility = TextInputLayout.INVISIBLE
        tafseerType.visibility = TextInputLayout.INVISIBLE
        favBtn.visibility = TextInputLayout.INVISIBLE
        uploadBtn.visibility = TextInputLayout.INVISIBLE

        verseExplanation.text = args.verse.tafseer
        verseTranslation.text = args.verse.translation

        playBtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                playBtn.setBackgroundResource(R.drawable.ic_play)
            } else {
                mediaPlayer.start()
                playBtn.setBackgroundResource(R.drawable.ic_pause)
            }
        }

        mushafBtn.setOnClickListener {
            val toImage = CloudVerseDetailsFragmentDirections
                .actionCloudVerseDetailsFragmentToCloudVerseImageFragment(args.verse)
            findNavController().navigate(toImage)
        }
    }

    private fun initializeMediaPlayer() {
        val attributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(attributes)
            setDataSource(args.verse.audioUrl)
            prepare()
        }
    }

}