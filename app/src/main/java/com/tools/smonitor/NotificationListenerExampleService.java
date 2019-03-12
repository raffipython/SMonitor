package com.tools.smonitor;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.security.Policy;
import java.util.Timer;

public class NotificationListenerExampleService extends NotificationListenerService {

    final int[] i = {0};
    Thread vibratethread;

    /*
        These are the package names of the apps. for which we want to
        listen the notifications
     */



    private static final class ApplicationPackageNames {

        public static final String SIGNAL_PACK_NAME = "org.thoughtcrime.securesms";
        //public static final String FACEBOOK_PACK_NAME = "com.facebook.katana";
        //public static final String FACEBOOK_MESSENGER_PACK_NAME = "com.facebook.orca";
        //public static final String WHATSAPP_PACK_NAME = "com.whatsapp";
        //public static final String INSTAGRAM_PACK_NAME = "com.instagram.android";
    }

    /*
        These are the return codes we use in the method which intercepts
        the notifications, to decide whether we should do something or not
     */
    public static final class InterceptedNotificationCode {
        //public static final int FACEBOOK_CODE = 1;
        //public static final int WHATSAPP_CODE = 2;
        //public static final int INSTAGRAM_CODE = 3;
        public static final int SIGNAL_CODE = 10;
        public static final int OTHER_NOTIFICATIONS_CODE = 4; // We ignore all notification with code == 4
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        int notificationCode = matchNotificationCode(sbn);

        if (notificationCode != InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {
            Intent intent = new Intent("com.tools.smonitor"); // com.github.chagall.notificationlistenerexample
            intent.putExtra("Notification Code", notificationCode);
            sendBroadcast(intent);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        int notificationCode = matchNotificationCode(sbn);

        if (notificationCode != InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {

            StatusBarNotification[] activeNotifications = this.getActiveNotifications();

            if (activeNotifications != null && activeNotifications.length > 0) {
                for (int i = 0; i < activeNotifications.length; i++) {
                    if (notificationCode == matchNotificationCode(activeNotifications[i])) {
                        Intent intent = new Intent("com.tools.smonitor"); // com.github.chagall.notificationlistenerexample
                        intent.putExtra("Notification Code", notificationCode);
                        sendBroadcast(intent);
                        break;
                    }
                }
            }
        }
    }

    private int matchNotificationCode(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();

        if (packageName.equals(ApplicationPackageNames.SIGNAL_PACK_NAME)) {

            String msg = sbn.getNotification().extras.toString();
            Log.e("EXTRAS", msg);
            //Log.e("SPLITTED", msg.split("android.text", 1)[0]);

            if (sbn.getNotification().extras.getString("android.text").contains("ALERT")) {
                Log.e("ALERT", "ALERT ALERT ALERT");
            }


            return (InterceptedNotificationCode.SIGNAL_CODE);
        }
        if (sbn.getNotification().extras.getString("android.text") != null) {
            //Log.e("OTHER CONTENT", sbn.getNotification().extras.getString("android.text"));
            if (sbn.getNotification().extras.getString("android.text").contains("alert")) {
                //Log.e("ALERT", "ALERT ALERT ALERT");
                FlashLight();

            }
        }
        return (InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE);
    }


    private void FlashLight() {
        // Flash method
        Log.e("FLASHLIGHT", "1 0 1 0 1 0 1 0");
        on_viberate();
    }

    private void on_viberate() {
        vibratethread = new Vibratethread();
        vibratethread.start();
    }

    class Vibratethread extends Thread {
        final Vibrator vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        public Vibratethread() {
            super();
        }

        public void run() {
            vibrate.vibrate(3000);
            try {
                Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            vibrate.vibrate(3000);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}