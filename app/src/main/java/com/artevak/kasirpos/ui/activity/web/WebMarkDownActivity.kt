package com.artevak.kasirpos.ui.activity.web

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityWebMarkDownBinding
import com.mukesh.MarkDown
import java.net.URL

class WebMarkDownActivity : BaseActivity() {
    val binding : ActivityWebMarkDownBinding by lazy {
        ActivityWebMarkDownBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.markdown.apply {
            // Dispose of the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // In Compose world
                MaterialTheme {
                    MarkDown(
                        url = URL("https://raw.githubusercontent.com/mukeshsolanki/MarkdownView-Android/main/README.md"),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}