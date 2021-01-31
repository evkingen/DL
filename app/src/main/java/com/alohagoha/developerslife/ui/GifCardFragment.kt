package com.alohagoha.developerslife.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alohagoha.developerslife.DevelopersLifeApp
import com.alohagoha.developerslife.R
import com.alohagoha.developerslife.databinding.FragmentGifCardBinding
import com.alohagoha.developerslife.model.data.GifsRepository
import com.alohagoha.developerslife.model.entities.Gif
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class GifCardFragment() : Fragment(R.layout.fragment_gif_card) {

    private lateinit var gifsRepository: GifsRepository

    @Suppress("UNCHECKED_CAST")
    private val viewModel: GifCardViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return GifCardViewModel(gifsRepository) as T
            }
        }
    }

    private var _binding: FragmentGifCardBinding? = null
    private val binding: FragmentGifCardBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gifsRepository = GifsRepository((requireContext().applicationContext as DevelopersLifeApp).apiService)
        _binding = FragmentGifCardBinding.bind(view)
        Log.d("TAG", this.toString())
        val category = arguments?.getString(EXTRA_CATEGORY).orEmpty()
        viewModel.fetchStartGif(category)
        binding.gifCard.prevBtn.setOnClickListener { viewModel.fetchPrevGif(category) }
        binding.gifCard.nextBtn.setOnClickListener { viewModel.fetchNextGif(category) }
        binding.errorPlaceholder.actionErrorBtn.setOnClickListener { viewModel.fetchStartGif(category) }
        viewModel.currentGif.observe(viewLifecycleOwner, ::renderUi)
        viewModel.isFirstGif.observe(viewLifecycleOwner, ::changeAccessButton)
    }

    private fun changeAccessButton(isFirst: Boolean) {
        binding.gifCard.prevBtn.isEnabled = !isFirst
    }

    private fun renderUi(currentGif: GifResult) {
        when (currentGif) {
            Loading -> {
                binding.errorPlaceholder.root.isVisible = false
                binding.gifCard.root.isVisible = true
                binding.gifCard.progressBar.isVisible = true
            }
            ErrorResult -> {
                binding.errorPlaceholder.root.isVisible = true
                binding.gifCard.root.isVisible = false
                renderError(getString(R.string.network_error_description))
            }
            is ValidResult -> {
                binding.errorPlaceholder.root.isVisible = false
                binding.gifCard.root.isVisible = true
                binding.gifCard.progressBar.isVisible = true
                renderGif(currentGif.gif)
            }
            EmptyResult -> {
                binding.errorPlaceholder.root.isVisible = true
                binding.gifCard.root.isVisible = false
                renderError("По данному запросу не найдено новых gif")
            }
        }
    }

    private fun renderError(errorMessage: String) {
        binding.errorPlaceholder.descriptionErrorIv.text = errorMessage
    }

    private fun renderGif(currentGif: Gif) {
        binding.gifCard.descriptionTv.text = currentGif.description
        Glide.with(this).load(currentGif.gifUrl)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        binding.gifCard.progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        binding.gifCard.progressBar.isVisible = false
                        return false
                    }
                })
                .centerCrop().into(binding.gifCard.gifIv)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val EXTRA_CATEGORY = "GifCardFragment:category"
        fun newInstance(category: String): GifCardFragment {
            val args = Bundle().apply { putString(EXTRA_CATEGORY, category) }
            val fragment = GifCardFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
