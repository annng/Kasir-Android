package com.artevak.kasirpos.ui.activity.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.common.const.Cons
import com.artevak.kasirpos.common.util.ext.dashIfEmpty
import com.artevak.kasirpos.databinding.ActivityWebMarkDownBinding

class WebMarkDownActivity : BaseActivity(), View.OnClickListener {

    companion object {
        fun generateIntent(context: Context, urlMarkdown: String, title : String): Intent {
            val i = Intent(context, WebMarkDownActivity::class.java)
            i.putExtra(Cons.EXTRA.KEY_URL, urlMarkdown)
            i.putExtra(Cons.EXTRA.KEY_TITLE, title)
            return i
        }
    }

    val binding: ActivityWebMarkDownBinding by lazy {
        ActivityWebMarkDownBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initListener()

        val urlMarkdown = intent.getStringExtra(Cons.EXTRA.KEY_URL)
        binding.tvTitle.text = intent.getStringExtra(Cons.EXTRA.KEY_TITLE).dashIfEmpty()

        binding.markdown.loadFromUrl(urlMarkdown)

    }

    override fun initListener() {
        super.initListener()
        binding.ivBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0){
            binding.ivBack -> onBackPressed()
        }
    }
}