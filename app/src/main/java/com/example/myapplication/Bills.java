package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.myapplication.databinding.BillsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Bills extends AppCompatActivity {

    private static final String CHANNEL_ID = "my_channel_id";
    private static final int REQUEST_CODE = 0;
    BillsBinding binding;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    static ArrayList<BillModel> billModelList;
    BillAdapter billAdapter;
    int notificationId = 0;
    private static final long IDLE_PERIOD = 24 * 60 * 60 * 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BillsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        createNotificationChannel();

        billModelList = new ArrayList<>();
        binding.billsRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.billsRecycler.setHasFixedSize(true);

        binding.addNewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Bills.this, BillCategory.class));
                } catch (Exception e) {

                }
            }
        });

        binding.refreshPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Bills.this, Bills.class));
                    finish();
                } catch (Exception e) {

                }
            }
        });

        getData();
        //NotifyFunc();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    Bills.CHANNEL_ID,
                    "My Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void getData() {
        String id = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("Facturi")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        BillModel model = new BillModel(
                                doc.getString("id"),
                                doc.getString("categorie_factura"),
                                doc.getString("suma_plata"),
                                doc.getString("termen_limita"),
                                doc.getString("data_notificare"));

                        billModelList.add(model);
                    }

                    if (billAdapter == null) {
                        billAdapter = new BillAdapter(Bills.this, billModelList);
                        binding.billsRecycler.setAdapter(billAdapter);

                    } else {
                        billAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                });
    }

    /*void NotifyFunc() {
        Date currentDate = new Date();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String formattedDate = sdf.format(currentDate);

        for (BillModel bill : billModelList) {
            String billDate = bill.getData_notificare();
            if (billDate != null) {
                if (billDate.equals(formattedDate)) {
                    sendNotification( "Reminder factura", "Termenul limita pentru factura se apropie!");
                }
            }
        }
    }*/

    /*
    @SuppressLint("MissingPermission")
    void sendNotification(String bill_reminder, String s) {

        Intent notificationIntent = new Intent(this, Bills.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(bill_reminder)
                .setContentText(s)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId(Bills.CHANNEL_ID);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(notificationId, builder.build());
        notificationId++;
    }*/

    protected void onStop () {
        super .onStop() ;
        startService( new Intent( this, NotificationService. class )) ;
    }
}



