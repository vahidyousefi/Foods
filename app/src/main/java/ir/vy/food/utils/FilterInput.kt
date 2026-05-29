package ir.vy.food.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

class MaxDigitsFilter(private val maxDigits: Int) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        val currentText = dest.toString().replace(",", "")
        val newText = source.toString().replace(",", "")

        // محاسبه طول نهایی بعد از اضافه شدن کاراکتر جدید
        val newLength = currentText.length - (dend - dstart) + newText.length

        // اگه بیشتر از حد مجاز شد، هیچی برنگردون (کاراکتر وارد نشه)
        return if (newLength > maxDigits) "" else null
    }
}

fun EditText.addThousandSeparator(maxDigits: Int, counterLayout: TextInputLayout? = null) {
    this.filters = arrayOf(MaxDigitsFilter(maxDigits))

    val textHelper = "قیمت را به تومان وارد کنید !"

    this.addTextChangedListener(object : TextWatcher {
        @SuppressLint("DefaultLocale")
        override fun afterTextChanged(s: Editable?) {
            if (s.isNullOrEmpty()) {
                counterLayout?.helperText = "0/$maxDigits    $textHelper"
                counterLayout?.isHelperTextEnabled = true
                counterLayout?.isCounterEnabled = false
                return
            }

            removeTextChangedListener(this)

            val cleanText = s.toString().replace(",", "")
            val number = cleanText.toLongOrNull()

            if (number != null) {
                val formatted = String.format("%,d", number)
                setText(formatted)
                setSelection(formatted.length)
            }

            val length = cleanText.length
            counterLayout?.helperText = "$length/$maxDigits    $textHelper"
            counterLayout?.isHelperTextEnabled = true
            counterLayout?.isCounterEnabled = false

            if (length > maxDigits) {
                counterLayout?.setHelperTextColor(ColorStateList.valueOf(Color.RED))
            } else {
                counterLayout?.setHelperTextColor(ColorStateList.valueOf(Color.GRAY))
            }
            addTextChangedListener(this)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.getNumericText(): String {
    return this.text.toString().replace(",", "")
}
