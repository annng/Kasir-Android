package com.artevak.kasirpos.widget

import android.content.Context
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.artevak.kasirpos.R
import com.artevak.kasirpos.util.ext.hide
import com.artevak.kasirpos.util.ext.show

class CustomEditText(context: Context, val attrs: AttributeSet) : RelativeLayout(context, attrs) {
    val mView: View
    private lateinit var tvTitle: TextView
    private lateinit var tvPrefix: TextView
    private lateinit var tvInfo: TextView
    private lateinit var editText: EditText
    private lateinit var ivSuffixIcon: ImageView
    var isShowPassword = true

    init {
        val inflater = LayoutInflater.from(context)
        mView = inflater.inflate(R.layout.widget_custom_edit_text, this)
        init()
    }


    private fun init() {
        tvTitle = mView.findViewById(R.id.tvTitle)
        tvPrefix = mView.findViewById(R.id.tvPrefix)
        tvInfo = mView.findViewById(R.id.tvInfo)
        editText = mView.findViewById(R.id.editText)
        ivSuffixIcon = mView.findViewById(R.id.ivSuffixIcon)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomEditText,
            0, 0
        ).apply {
            getString(R.styleable.CustomEditText_titleEditText)?.let { strTitle ->
                title = strTitle
            }
            getString(R.styleable.CustomEditText_infoEditText)?.let { strInfo ->
                info = strInfo
            }
            getString(R.styleable.CustomEditText_prefixText)?.let { strText ->
                tvPrefix.show()
                tvPrefix.text = strText
            }
            getString(R.styleable.CustomEditText_hintEditText)?.let { strText ->
                editText.hint = strText
            }
            getString(R.styleable.CustomEditText_valueEditText)?.let { strText ->
                text = strText
            }
            getBoolean(R.styleable.CustomEditText_isEnableEditText, true).let {
                isEnable = it
            }

            getDrawable(R.styleable.CustomEditText_suffixIcon).let {
                ivSuffixIcon.setImageDrawable(it)
            }

            getInt(R.styleable.CustomEditText_inputType, 1).let {
                when (it) {
                    1 -> editText.inputType = InputType.TYPE_CLASS_TEXT
                    2 -> editText.inputType =
                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                    3 -> editText.inputType =
                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_PHONE
                    4 -> {
                        editText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        isInputTypePassword()
                    }
                    5 -> {
                        editText.inputType = InputType.TYPE_CLASS_TEXT
                        editText.maxLines = 5
                    }
                    6 -> editText.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                    7 -> {
                        editText.isFocusable = false
                        editText.isLongClickable = false
                        editText.isClickable = false
                        isEnable = false
                    }
                }
            }
        }
    }

    fun onClickSuffixIcon(listener: () -> Unit) {
        ivSuffixIcon.setOnClickListener {
            listener()
        }
    }

    fun isInputTypePassword() {
        editText.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        editText.transformationMethod = PasswordTransformationMethod.getInstance()
        ivSuffixIcon.show()

        ivSuffixIcon.setOnClickListener {
            if (editText.inputType == InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS) {
                if (isShowPassword) {
                    editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    ivSuffixIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_show
                        )
                    )
                    isShowPassword = false
                } else {
                    editText.transformationMethod = PasswordTransformationMethod.getInstance()
                    ivSuffixIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_hide
                        )
                    )
                    isShowPassword = true
                }
            }
        }
    }

    val view: EditText
        get() {
            return editText
        }

    var isEnable: Boolean
        get() {
            return editText.isEnabled
        }
        set(value) {
            editText.isEnabled = value
        }

    var title: String
        get() {
            return tvTitle.text.toString()
        }
        set(value) {
            if (value.isEmpty())
                tvTitle.hide()
            else {
                tvTitle.show()
                tvTitle.text = value
            }
        }

    var info: String
        get() {
            return tvInfo.text.toString()
        }
        set(value) {
            if (value.isEmpty()) {
                tvInfo.setTextColor(ContextCompat.getColor(context, R.color.grey_800))
                tvInfo.hide()
            } else {
                tvInfo.show()
                tvInfo.text = value
            }
        }

    fun setInfoError(strMessage: String) {
        tvInfo.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
        info = strMessage
    }

    var prefix: String
        get() {
            return tvPrefix.text.toString()
        }
        set(value) {
            if (value.isEmpty())
                tvPrefix.hide()
            else {
                tvPrefix.show()
                tvPrefix.text = value
            }
        }

    var text: String
        get() {
            return editText.text.toString()
        }
        set(value) {
            editText.setText(value)
        }

}