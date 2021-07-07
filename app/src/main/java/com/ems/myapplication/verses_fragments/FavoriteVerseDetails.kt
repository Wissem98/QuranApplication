package com.ems.myapplication.verses_fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ems.myapplication.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.verse_details_fragment.*

class FavoriteVerseDetails : Fragment(R.layout.verse_details_fragment) {

    private val args: FavoriteVerseDetailsArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        verseReader.visibility = TextInputLayout.INVISIBLE
        playBtn.visibility = Button.INVISIBLE
        mushafBtn.visibility = Button.INVISIBLE
        favBtn.visibility = ImageButton.INVISIBLE
        tafseerType.visibility = TextInputLayout.INVISIBLE

        verseTranslation.text = args.verse.translation
        verseExplanation.text = args.verse.tafseer

    }

}