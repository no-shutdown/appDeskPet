package com.xl.pet;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationView;
import com.xl.pet.databinding.ActivityMainBinding;
import com.xl.pet.flowWindow.FloatWindowService;
import com.xl.pet.utils.DatabaseHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    //开发阶段先暂时去掉不必要功能
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建数据库实例
        DatabaseHelper.createDatabase(this);
        //创建UI界面
        createUI();
//        finish();
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //请求Overlay权限
//        requestOverlayPermissionIfNeed();
//        //请求usageStats权限
//        requestUsageStatsPermissionIfNeed();
//        //创建UI界面
//        createUI();
//        //如果没有启动浮窗服务则启动悬浮窗服务
//        boolean isServiceRunning = false;
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (FloatWindowService.class.getName().equals(service.service.getClassName())) {
//                isServiceRunning = true;
//                break;
//            }
//        }
//        if (!isServiceRunning) {
//            startFloatWindowService();
//        }
//        //创建数据库实例
//        DatabaseHelper.createDatabase(this);
////        finish();
//    }

    private void createUI() {
        com.xl.pet.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_forest_time, R.id.navigation_forest, R.id.navigation_menstruation)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //添加左侧导航监听，设置toolBar和手机状态栏颜色
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            int primaryColor;
            if (R.id.navigation_forest_time == navDestination.getId()) {
                primaryColor = ContextCompat.getColor(this, R.color.colorPrimaryBright);
            } else {
                primaryColor = ContextCompat.getColor(this, R.color.colorPrimary);
            }
            binding.appBarMain.toolbar.setBackgroundColor(primaryColor);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(primaryColor);
        });
    }

    private void startFloatWindowService() {
        Intent serviceIntent = new Intent(MainActivity.this, FloatWindowService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void requestOverlayPermissionIfNeed() {
        //如果api大于23 startActivityForResult
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            } else {
                //TODO do something you need
            }
        }
    }

    private void requestUsageStatsPermissionIfNeed() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        boolean hasUsageStatsPermission = mode == AppOpsManager.MODE_ALLOWED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasUsageStatsPermission) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_abort) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("关于我")  // 设置对话框标题
                    .setMessage("该app系个人开发，仅供学习，不作为任何商业用途。部分界面参考自\"专注森林\"。如有侵权，联系必删。\n\n敬礼！( •̀ ω •́ )y");
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
