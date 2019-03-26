package mt.innovation.screenshotshareimage;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.view.FlutterView;
import android.widget.Toast;

/** ScreenshotShareImagePlugin */
public class ScreenshotShareImagePlugin implements MethodCallHandler {

  private static final String TAG = "ScreenshotShareImagePlugin";

  private Registrar registrar;
  private Activity activity;
  private FlutterView flutterView;
  private MethodChannel channel;
  private final int WRITE_ACCESS_REQUEST_ID = 12;

  public ScreenshotShareImagePlugin(Registrar registrar, Activity activity, FlutterView flutterView, MethodChannel channel) {
    this.registrar = registrar;
    this.activity = activity;
    this.flutterView = flutterView;
    this.channel = channel;
    this.channel.setMethodCallHandler(this);
    setRequestPermissionListener();
  }

  private void setRequestPermissionListener() {

    registrar.addRequestPermissionsResultListener(new PluginRegistry.RequestPermissionsResultListener() {
      @Override
      public boolean onRequestPermissionsResult(int i, String[] strings, int[] ints) {
        if (i == WRITE_ACCESS_REQUEST_ID) {

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
              takeScreenshot("screenshot");
              return true;
            } else {

              activity.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
              return false;
            }
          }
        }
        return false;
      }
    });
  }

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "screenshot_share_image");
    channel.setMethodCallHandler(new ScreenshotShareImagePlugin(registrar, registrar.activity(), registrar.view(), channel));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("takeScreenshotAndShare")) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        activity.requestPermissions(new String[]{ android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_ACCESS_REQUEST_ID);
      } else {
        takeScreenshot("screenshot");
      }

    } else {
      result.notImplemented();
    }
  }

  private void takeScreenshot(String fileName) {
    try {
      // image naming and path  to include sd card  appending name you choose for file
      String mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + fileName + ".jpg";
      Bitmap bitmap = flutterView.getBitmap();

      File imageFile = new File(mPath);

      FileOutputStream outputStream = new FileOutputStream(imageFile);
      int quality = 100;
      bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
      outputStream.flush();
      outputStream.close();

      openScreenshot(imageFile);

    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  private void openScreenshot(File imageFile) {
    StrictMode.VmPolicy.Builder builder = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
      builder = new StrictMode.VmPolicy.Builder();
      StrictMode.setVmPolicy(builder.build());
    }

    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    Uri uri = Uri.fromFile(imageFile);
    intent.setDataAndType(uri, "image/*");
    activity.startActivity(intent);
  }
}
