package com.alohagoha.developerslife.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alohagoha.developerslife.DevelopersLifeApp
import com.alohagoha.developerslife.R
import com.alohagoha.developerslife.databinding.FragmentGifCardBinding
import com.alohagoha.developerslife.model.entities.Gif
import com.alohagoha.developerslife.model.entities.GifCategory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class GifCardFragment : Fragment(R.layout.fragment_gif_card) {

    //private lateinit var gifsRepository: GifsRepository
    private lateinit var category: String

    @Suppress("UNCHECKED_CAST")
    private val viewModel: GifCardViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return GifCardViewModel((requireContext().applicationContext as DevelopersLifeApp).gifsRepository) as T
            }
        }
    }

    private var _binding: FragmentGifCardBinding? = null
    private val binding: FragmentGifCardBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentGifCardBinding.bind(view)
        category = arguments?.getString(EXTRA_CATEGORY).orEmpty()
        viewModel.fetchStartGif(category)
        initListeners()
        viewModel.currentGif.observe(viewLifecycleOwner, ::renderUi)
        viewModel.isFirstGif.observe(viewLifecycleOwner, ::changeAccessButton)
    }

    @FlowPreview
    fun initListeners() {
        binding.gifCard.prevBtn.clicks().debounce(300).onEach { viewModel.fetchPrevGif(category) }
            .launchIn(lifecycleScope)
        binding.gifCard.nextBtn.clicks().debounce(300).onEach { viewModel.fetchNextGif(category) }
            .launchIn(lifecycleScope)
        binding.errorPlaceholder.actionErrorBtn.clicks().debounce(300)
            .onEach { viewModel.fetchStartGif(category) }.launchIn(lifecycleScope)
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
                renderError(getString(R.string.empty_request_error_description))
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
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.gifCard.progressBar.isVisible = false
                    binding.errorPlaceholder.root.isVisible = true
                    binding.gifCard.root.isVisible = false
                    renderError(getString(R.string.network_error_description))
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
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
        fun newInstance(category: GifCategory): GifCardFragment {
            val args = Bundle().apply { putString(EXTRA_CATEGORY, category.key) }
            val fragment = GifCardFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
