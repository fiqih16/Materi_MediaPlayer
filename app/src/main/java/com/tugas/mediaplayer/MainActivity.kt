package com.tugas.mediaplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPutar: MediaPlayer
    private var waktuTotal: Int = 0

    @Suppress("RedundantSamConstructor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPutar = MediaPlayer.create(this, R.raw.musik2)
        mediaPutar.isLooping = true
        mediaPutar.setVolume(0.5f, 0.5f)
        waktuTotal = mediaPutar.duration

        suaraSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        val jumlahVolume = progress / 100.0f
                        mediaPutar.setVolume(jumlahVolume, jumlahVolume)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }
        )

        posisiSeekBarLagu.max = waktuTotal
        posisiSeekBarLagu.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mediaPutar.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }
        )

        Thread(Runnable {
            while (mediaPutar != null) {
                try {
                    val pesan = Message()
                    pesan.what = mediaPutar.currentPosition
                    penanganan.sendMessage(pesan)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()

    }

    private var penanganan = @SuppressLint("HandlearLeak")
    object : Handler() {
        @SuppressLint("SetTextI18n")
        override fun handleMessage(pesan: Message) {
            super.handleMessage(pesan)
            val posisiSaatIni = pesan.what
            posisiSeekBarLagu.progress = posisiSaatIni

            val perkiraanWaktu = buatLabelWaktu(posisiSaatIni)
            labelPerkiraanWaktu.text = perkiraanWaktu

            val pengingatWaktu =
                buatLabelWaktu(waktuTotal - posisiSaatIni)
            labelPengigatWaktu.text = "-$pengingatWaktu"
        }
    }

    fun buatLabelWaktu(waktu: Int): String {
        var labelWaktu =""
        val menit = waktu / 1000 / 60
        val detik = waktu / 1000 % 60

        labelWaktu = "$menit: "
        if (detik < 10) labelWaktu += "0"
        labelWaktu += detik
        return labelWaktu
    }
  fun playBtnClick(v: View){
      if (mediaPutar.isPlaying){
          mediaPutar.pause()
          Btn_PlayPause.setBackgroundResource(R.drawable.play)
      } else {
          mediaPutar.start()
          Btn_PlayPause.setBackgroundResource(R.drawable.pause)
      }
  }
}
