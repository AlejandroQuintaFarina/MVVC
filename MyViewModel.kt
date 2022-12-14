package com.example.simondice.viewModel

package com.example.simondice.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import kotlinx.coroutines.*
import java.util.ArrayList
import kotlin.random.Random

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context = getApplication<Application>().applicationContext

    //Ronda de la partida
    private var ronda: Int = 0

    //Record de rondas
    private var record: Int = loadRecord()

    //Variables donde observamos los cambios en la ronda y el record
    var liveRonda = MutableLiveData<Int>()
    var liveRecord = MutableLiveData<Int>()

    //Inicializamos variables cuando las instanciamos
    init {
        liveRonda.value = ronda
        liveRecord.value = record
    }

    //Con estas variables determinamos cuando queremos que el boton cambie de color
    var yellowBlink = MutableLiveData<Boolean>()
    var blueBlink = MutableLiveData<Boolean>()
    var redBlink = MutableLiveData<Boolean>()
    var greenBlink = MutableLiveData<Boolean>()

    //Con esta variable de encender y apagar los botones mientras la secuencia esta activa
    var activateButton = MutableLiveData<Boolean>()

    //Inicializamos variables cuando instanciamos
    init {

        yellowBlink.value = false
        blueBlink.value = false
        redBlink.value = false
        greenBlink.value = false
        activateButton.value = false

    }

    //Delay entre los parpadeos de boton
    private val delay: Long = 500

    //Arraylist de enteros para los colores
    private var cadenaColores: ArrayList<Int> = ArrayList()

    //Contador interno para comprobar secuencia
    private var counter = 0

    //Funcion para que empiece el juego

    fun startGame() {

        startRound()

    }

    //Funcion para que empiece una nueva ronda

    fun startRound() {

        liveRonda.setValue(ronda)

        randomColor()

        showColor()

    }


     //Funcion que genera un numero entero al azar y lo añade a la array "cadenaColores"

    fun randomColor() {

        cadenaColores += Random(System.currentTimeMillis()).nextInt(1, 5)


    }


     //Funcion que comprueba que el boton pulsado por el usuario es el correcto

    fun checkColor(colorIntroducido: Int) {

        //Camino a seguir si se acierta el color
        if (colorIntroducido.equals(cadenaColores[counter])) {


            if (counter != ronda) {

                counter++

            }

            else {

                ronda++
                counter = 0

                startRound()

            }

        }


        //Si se falla en cualquier momento de la secuencia, sale un toast avisando de la derrota y se reincian los valores de la ronda

        else {

            Toast.makeText(context, "Has perdido!", Toast.LENGTH_SHORT).show()

            //Si la ronda alcanzada es mayor que la del record anterior se almacena como el nuevo record y sale un toast de aviso

            if (ronda > record) {

                saveRecord(ronda)

                Toast.makeText(context, "Nuevo Record!", Toast.LENGTH_SHORT).show()

            }

            restartGame()

        }


    }


        //Funcion que reinicia los valores para empezar una nueva partida

    fun restartGame() {

        ronda = 0

        liveRecord.setValue(record)

        cadenaColores.clear()


    }



     //Funcion que inicia la secuencia de los botones del simon dice

    fun showColor() {

        activateButton.setValue(false)

        var jobMuestraColor: Job? = null

        jobMuestraColor = GlobalScope.launch(Dispatchers.Main) {

            for (color: Int in cadenaColores) {

                delay(delay)

                if (color == 1) {

                    yellowBlink.setValue(true)
                    delay(delay)
                    yellowBlink.setValue(false)

                } else if (color == 2) {

                    blueBlink.setValue(true)
                    delay(delay)
                    blueBlink.setValue(false)


                } else if (color == 3) {

                    redBlink.setValue(true)
                    delay(delay)
                    redBlink.setValue(false)


                } else if (color == 4) {

                    greenBlink.setValue(true)
                    delay(delay)
                    greenBlink.setValue(false)

                }

            }

            activateButton.setValue(true)
            Toast.makeText(context, "Repite la secuencia!", Toast.LENGTH_SHORT).show()
        }
        jobMuestraColor

    }



    //Funcion que guarada un entero en SharedPreferences como el nuevo record

    fun saveRecord(record: Int) {
        var pref: SharedPreferences = context.getSharedPreferences("record", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putInt("record", record)
        editor.commit()
        this.record = loadRecord()
    }


     // Funcion que lee el record guardado en SharedPreferences

    fun loadRecord(): Int {
        val pref: SharedPreferences = context.getSharedPreferences("record", Context.MODE_PRIVATE)
        val savedRecord = pref.getInt("record", 0)
        return savedRecord
    }


}