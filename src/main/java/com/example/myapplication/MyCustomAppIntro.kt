package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.example.eventbus.service.ResponseEngine
import androidx.fragment.app.Fragment
import com.example.dto.MyResponse
import com.example.iot.App
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.*


class MyCustomAppIntro : AppIntro2(), IMqttMessageListener {
    var usernameState:String="";
    var lampState :String=""
    var Temp:String=""
    var hum:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras

        val username = bundle?.getString("username")
        Temp= bundle?.getString("temp").toString()
        hum= bundle?.getString("hum").toString()
        lampState= bundle?.getString("state").toString()
        usernameState=username.toString();
        // Make sure you don't call setContentView!
        isIndicatorEnabled = true
        setTransformer(AppIntroPageTransformerType.Fade)
        setTransformer(AppIntroPageTransformerType.Zoom)
        setTransformer(AppIntroPageTransformerType.Flow)
        setTransformer(AppIntroPageTransformerType.SlideOver)
        setTransformer(AppIntroPageTransformerType.Depth)
// You can customize your parallax parameters in the constructors.

        setTransformer(AppIntroPageTransformerType.Parallax(
                titleParallaxFactor = 1.0,
                imageParallaxFactor = -1.0,
                descriptionParallaxFactor = 2.0
        ))
        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(AppIntroFragment.newInstance(
                title = "欢迎使用，"+username.toString()+"\n当前是台灯状态展示",
                description = "台灯状态："+lampState,
                titleColor = Color.YELLOW,
                descriptionColor = Color.RED,
                backgroundDrawable = R.drawable.qn

        )
        )
        addSlide(AppIntroFragment.newInstance(
                title = "欢迎使用，"+username.toString()+"\n当前是温度状态展示",
                description = "当前温度： "+Temp+"\n当前湿度： "+hum,
                titleColor = Color.YELLOW,
                descriptionColor = Color.RED,
                backgroundDrawable = R.drawable.qn
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        intent(usernameState)
    }
    fun intent(username:String){
        var intent = Intent(this@MyCustomAppIntro,MainLamp::class.java)
        var bundle = Bundle()
        bundle.putString("username",username)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun intent1(username:String){
        var intent = Intent(this@MyCustomAppIntro,MainTem::class.java)
        var bundle = Bundle()
        bundle.putString("username",username)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        intent1(usernameState)
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        TODO("Not yet implemented")
    }

}
