package top.devonte.note.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import top.devonte.note.R;
import top.devonte.note.receiver.DownloadCompletedReceiver;
import top.devonte.note.service.DownloadService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DownloadCompletedReceiver downloadCompletedReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        downloadCompletedReceiver = new DownloadCompletedReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.BROADCAST_ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(downloadCompletedReceiver, intentFilter);
    }

    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(downloadCompletedReceiver);
        super.onDestroy();
    }

}
