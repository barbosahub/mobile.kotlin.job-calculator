package com.android.calculator_hours_worked

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.calculator_hours_worked.databinding.ActivityMainBinding
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mLocale = Locale("pt", "BR")
    private var context = this@MainActivity

    var clique = 0

    lateinit var mViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initComponents()

        binding.imageVer.setOnClickListener { exibeSalario() }
        binding.imageDelete.setOnClickListener { }
    }

    private fun initComponents() {
        mViewModel = ViewModelProvider(context).get(MainViewModel::class.java)
        exibeResultado(false)
        binding.apply {

            mViewModel.mMinutos.observe(context, Observer { valor ->
                txtMinutos.text = valor
            })
            mViewModel.mHoras.observe(context, Observer { valor ->
                txtHoras.text = valor
                txtResultado.text = "O produto informado equivale a $valor horas de trabalho."
            })
            mViewModel.mDias.observe(context, Observer { valor ->
                txtDias.text = valor
            })
            mViewModel.mSemanas.observe(context, Observer { valor ->
                txtSemanas.text = valor
            })
            mViewModel.mMeses.observe(context, Observer { valor ->
                txtMeses.text = valor
            })
            mViewModel.mAnos.observe(context, Observer { valor ->
                txtAnos.text = valor
            })


            edtValorSalario.addTextChangedListener(MoneyTextWatcher(edtValorSalario, mLocale))
            edtProduto.addTextChangedListener(MoneyTextWatcher(edtProduto, mLocale))
            edtProduto.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModelProdutos(s.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun viewModelProdutos(string: String) {
        val salario = binding.edtValorSalario.text.toString().replace("R$", "").replace(".", "")
            .replace(",", "")
        val produto = string.replace("R$", "").replace(".", "").replace(",", "")

        if (validaCampos(produto, salario)) {
            mViewModel.produto(
                produto,
                salario
            )
        }
    }

    private fun exibeSalario() {
        if (clique == 0) {
            clique++
            binding.imageVer.setImageResource(R.drawable.round_visibility_off_24)
            binding.edtValorSalario.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            clique--
            binding.imageVer.setImageResource(R.drawable.round_visibility_24)
            binding.edtValorSalario.transformationMethod = HideReturnsTransformationMethod.getInstance()

        }
    }

    private fun exibeResultado(result: Boolean) {
        binding.apply {
            if (result) {
                layoutEmpty.isVisible = false
                layoutResultado.isVisible = true
                layoutDetalhes.isVisible = true
            } else {
                layoutEmpty.isVisible = true
                layoutResultado.isVisible = false
                layoutDetalhes.isVisible = false
            }
        }
    }

    private fun validaCampos(produto: String, salario: String): Boolean {
        if (produto.toDouble() > 0 && salario.toDouble() > 0) {
            exibeResultado(true)
            return true
        }
        exibeResultado(false)
        return false
    }

    class MoneyTextWatcher : TextWatcher {
        private val editTextWeakReference: WeakReference<EditText?>?
        private val locale: Locale?

        constructor(editText: EditText?, locale: Locale?) {
            editTextWeakReference = WeakReference<EditText?>(editText)
            this.locale = locale ?: Locale.getDefault()
        }

        constructor(editText: EditText?) {
            editTextWeakReference = WeakReference<EditText?>(editText)
            locale = Locale.getDefault()
        }

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {

        }

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
        }

        override fun afterTextChanged(editable: Editable?) {
            val editText: EditText = editTextWeakReference?.get() ?: return
            editText.removeTextChangedListener(this)
            val parsed: BigDecimal? = parseToBigDecimal(editable.toString(), locale)
            val formatted: String = NumberFormat.getCurrencyInstance(locale).format(parsed)
            editText.setText(formatted)
            editText.setSelection(formatted.length)
            editText.addTextChangedListener(this)
        }

        private fun parseToBigDecimal(value: String?, locale: Locale?): BigDecimal? {
            val replaceable = java.lang.String.format(
                "[%s,.\\s]", NumberFormat.getCurrencyInstance(locale).currency.symbol
            )
            val cleanString = value!!.replace(replaceable.toRegex(), "")
            return BigDecimal(cleanString).setScale(
                2, BigDecimal.ROUND_FLOOR
            ).divide(
                BigDecimal(100), BigDecimal.ROUND_FLOOR
            )
        }
    }
}