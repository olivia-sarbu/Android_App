package com.example.myapplication;
import static com.example.myapplication.Bills.billModelList;

import android.app.NotificationChannel ;
import android.app.NotificationManager ;
import android.app.Service ;
import android.content.Intent ;
import android.os.Handler ;
import android.os.IBinder ;
import android.util.Log ;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer ;
import java.util.TimerTask ;
public class NotificationService extends Service {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    Timer timer ;
    TimerTask timerTask ;
    String TAG = "Timers" ;
    int Your_X_SECS = 5 ;
    @Override
    public IBinder onBind (Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand (Intent intent , int flags , int startId) {
        Log. e ( TAG , "onStartCommand" ) ;
        super .onStartCommand(intent , flags , startId) ;
        startTimer() ;
        return START_STICKY ;
    }
    @Override
    public void onCreate () {
        Log. e ( TAG , "onCreate" ) ;
    }
    @Override
    public void onDestroy () {
        Log. e ( TAG , "onDestroy" ) ;
        stopTimerTask() ;
        super .onDestroy() ;
    }
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler() ;
    public void startTimer () {
        timer = new Timer() ;
        initializeTimerTask() ;
        timer.schedule( timerTask , 5000 , 24*60*60*1000) ;
    }
    public void stopTimerTask () {
        if ( timer != null ) {
            timer .cancel() ;
            timer = null;
        }
    }

    int notificationHour = 20;
    int notificationMinute = 36;

    public void initializeTimerTask () {

        Date currentDate = new Date();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String formattedDate = sdf.format(currentDate);

        timerTask = new TimerTask() {
            public void run () {
                handler .post( new Runnable() {
                    public void run () {
                        Calendar calendar = Calendar.getInstance();
                        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = calendar.get(Calendar.MINUTE);

                        if (currentHour > notificationHour ||
                                (currentHour == notificationHour && currentMinute >= notificationMinute)) {
                            calendar.add(Calendar.DAY_OF_YEAR, 1); // Schedule for the next day
                        }
                        calendar.set(Calendar.HOUR_OF_DAY, notificationHour);
                        calendar.set(Calendar.MINUTE, notificationMinute);
                        calendar.set(Calendar.SECOND, 0);

                        //long delay = calendar.getTimeInMillis() - System.currentTimeMillis();


                        for (BillModel bill : billModelList) {
                            String billDate = bill.getData_notificare();
                            if (billDate != null) {
                                if (billDate.equals(formattedDate)) {
                                    createNotification();
                                }
                            }
                        }
                    }
                }) ;
            }
        } ;
    }

    private void createNotification () {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext() , default_notification_channel_id ) ;
        mBuilder.setContentTitle( "Reminder factura" ) ;
        mBuilder.setContentText( "Termenul limita pentru factura se apropie!" ) ;
        mBuilder.setTicker( "Termenul limita pentru factura se apropie!" ) ;
        mBuilder.setSmallIcon(R.drawable.baseline_notifications_24) ;
        mBuilder.setAutoCancel( true ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int )System.currentTimeMillis () , mBuilder.build()) ;
    }
}