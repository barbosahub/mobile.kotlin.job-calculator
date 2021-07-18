package com.android.calculator_hours_worked

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var mMinutos = MutableLiveData<String>().apply { value = produto.toString() }
    var mHoras = MutableLiveData<String>().apply { value = produto.toString() }
    var mDias = MutableLiveData<String>().apply { value = produto.toString() }
    var mSemanas = MutableLiveData<String>().apply { value = produto.toString() }
    var mMeses = MutableLiveData<String>().apply { value = produto.toString() }
    var mAnos = MutableLiveData<String>().apply { value = produto.toString() }

    private var produto: Int = 0

    private fun setmMinuto(i: String?) {
        mMinutos.value = i.toString()
    }
    private fun setmHoras(i: String?) {
        mHoras.value = i.toString()
    }
    private fun setmDias(i: String?) {
        mDias.value = i.toString()
    }
    private fun setmSemanas(i: String?) {
        mSemanas.value = i.toString()
    }
    private fun setmMeses(i: String?) {
        mMeses.value = i.toString()
    }
    private fun setmAnos(i: String?) {
        mAnos.value = i.toString()
    }

    private fun validaProduto(produto: Double, salario: Double) {

        val diasTrabalhados = 5
        val horasTrabalhadas = 8


        val salarioporsemana = salario.div(diasTrabalhados)
        val salariopordia = salarioporsemana.div(diasTrabalhados)
        val salarioporhora = salariopordia.div(horasTrabalhadas)


        val minutos = produto.div(salarioporhora) * 60
        val horas = produto.div(salarioporhora)
        val dias = horas.div(horasTrabalhadas)
        val semanas = dias.div(7)
        val meses = semanas.div(4)
        val anos = meses.div(12)

        if (produto > 0) {
            setmMinuto(withSuffix(minutos.toLong()))
            setmHoras(withSuffix(horas.toLong()))
            setmDias(withSuffix(dias.toLong()))
            setmSemanas(withSuffix(semanas.toLong()))
            setmMeses(withSuffix(meses.toLong()))
            setmAnos( "%.1f".format(anos))
        }


    }

    private fun withSuffix(count: Long): String? {
        if (count < 1000) return "" + count
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        return String.format(
            "%.1f %c",
            count / Math.pow(1000.0, exp.toDouble()),
            "mMBTPE"[exp - 1]
        )
    }

    fun produto(produto: String, salario: String) {
        validaProduto(produto.toDouble() / 100, salario.toDouble() / 100)
    }
}


