package com.example.simondice


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import com.example.simondice.R


import androidx.lifecycle.Observer
import com.example.simondice.viewModel.MyViewModel


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btAmarillo: Button
    private lateinit var btAzul: Button
    private lateinit var btRojo: Button
    private lateinit var btVerde: Button
    private lateinit var btStart: Button
    val miVistaModelo by viewModels<MyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declaracion de los botones junto con el "set" de sus OnClickListener
        btAmarillo = findViewById(R.id.btAmarillo)
        btAmarillo.setOnClickListener(this)

        btAzul = findViewById(R.id.btAzul)
        btAzul.setOnClickListener(this)

        btRojo = findViewById(R.id.btRojo)
        btRojo.setOnClickListener(this)

        btVerde = findViewById(R.id.btVerde)
        btVerde.setOnClickListener(this)

        btStart = findViewById(R.id.btStart)


        btStart.setOnClickListener {

            miVistaModelo.restartGame()

            miVistaModelo.startGame()

        }

        //Observer del cambio de Ronda

        miVistaModelo.liveRonda.observe(
            this,
            Observer(
                fun(ronda: Int) {
                    var tvRonda: TextView = findViewById(R.id.tvRonda)
                    if (ronda == 0) btStart.isClickable = true

                    tvRonda.setText("Ronda: " + ronda)

                }
            )
        )
        //Observer del cambio de record

        miVistaModelo.liveRecord.observe(
            this,
            Observer(
                fun(record: Int) {
                    var tvRecord: TextView = findViewById(R.id.tvRecord)

                    tvRecord.setText("Record: " + record)

                }
            )
        )
        //Observer del parpadeo de los botones de colores

        miVistaModelo.yellowBlink.observe(
            this,
            Observer(
                fun(blink: Boolean) {

                    if (blink) btAmarillo.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    else btAmarillo.setBackgroundColor(Color.parseColor("#BEBB00"))

                }
            )
        )

        miVistaModelo.blueBlink.observe(
            this,
            Observer(
                fun(blink: Boolean) {

                    if (blink) btAzul.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    else btAzul.setBackgroundColor(Color.parseColor("#009FA7"))

                }
            )
        )

        miVistaModelo.redBlink.observe(
            this,
            Observer(
                fun(blink: Boolean) {

                    if (blink) btRojo.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    else btRojo.setBackgroundColor(Color.parseColor("#970000"))

                }
            )
        )

        miVistaModelo.greenBlink.observe(
            this,
            Observer(
                fun(blink: Boolean) {

                    if (blink) btVerde.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    else btVerde.setBackgroundColor(Color.parseColor("#1B9700"))


                }
            )
        )

        //Observer "activateButton" 
        miVistaModelo.activateButton.observe(
            this,
            Observer(
                fun (activated: Boolean) {

                    btVerde.isClickable = activated
                    btRojo.isClickable = activated
                    btAzul.isClickable = activated
                    btAmarillo.isClickable = activated

                }
            )
        )



    }

    //OnClickListener de los botones de colores
    override fun onClick(view: View?) {
        when (view?.id) {

            btAmarillo.id -> {

                miVistaModelo.checkColor(1)

            }
            btAzul.id -> {

                miVistaModelo.checkColor(2)

            }

            btRojo.id -> {

                miVistaModelo.checkColor(3)

            }

            btVerde.id -> {


                miVistaModelo.checkColor(4)

            }


        }


    }


}