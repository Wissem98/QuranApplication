package com.ems.myapplication.verses_fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ems.myapplication.R
import com.ems.myapplication.util.Constants
import kotlinx.android.synthetic.main.moushaf_fragment.*

class CloudVerseImageFragment : Fragment(R.layout.moushaf_fragment) {

    private val args: CloudVerseImageFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadImage()

    }

    private fun loadImage() {
        val url = "${Constants.IMAGES_URL}${args.verse.sourahId}/${args.verse.verseIdentifier}"
        Glide.with(requireContext()).load(url).fitCenter().into(moushafImg)
    }

}