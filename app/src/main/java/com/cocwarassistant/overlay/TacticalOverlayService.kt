package com.cocwarassistant.overlay

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.app.NotificationCompat
import com.cocwarassistant.MainActivity

class TacticalOverlayService:Service(){
    private var wm:WindowManager?=null;private var view:LinearLayout?=null
    override fun onCreate(){super.onCreate();if(!Settings.canDrawOverlays(this)){stopSelf();return};startForeground(42,notification());wm=getSystemService(WINDOW_SERVICE) as WindowManager;view=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setBackgroundColor(Color.argb(235,20,22,28));val b=Button(context).apply{text="WAR PLAN";setOnClickListener{startActivity(Intent(this@TacticalOverlayService,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))}};addView(b)};val params=WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,PixelFormat.TRANSLUCENT).apply{gravity=Gravity.TOP or Gravity.END;x=16;y=120};wm?.addView(view,params)}
    private fun notification():Notification{val channel="war_assistant_overlay";val nm=getSystemService(NOTIFICATION_SERVICE) as NotificationManager;nm.createNotificationChannel(NotificationChannel(channel,"War Assistant Overlay",NotificationManager.IMPORTANCE_LOW));return NotificationCompat.Builder(this,channel).setContentTitle("War Assistant").setContentText("Tactical companion active").setSmallIcon(android.R.drawable.ic_menu_compass).build()}
    override fun onDestroy(){view?.let{wm?.removeView(it)};super.onDestroy()}
    override fun onBind(intent:Intent?):IBinder?=null
}
