package fr.odupont.sharebase64;

import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.cordova.CordovaActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;


/**
 * ShareB64 is a PhoneGap plugin
 *  
 * @author odvpont@gmail.com
 * 
 */
public class ShareB64 extends CordovaPlugin {

    /*private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));
    }*/
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        try {

            if (action.equals("share")) {
                if (args.length() != 1) {
                    //return new PluginResult(PluginResult.Status.INVALID_ACTION);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
                    return false;
                }

                // Parse the arguments

                JSONObject obj = args.getJSONObject(0);
                
                if (!obj.has("imageData")) {
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
                    return false;
                }

                String imageData = obj.getString("imageData");
                String message = obj.has("message") ? obj.getString("message") : null;

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
                String imageFileName = "Crocolor_" + timeStamp;
                File albumF = null;
                
                albumF = new File(this.cordova.getActivity().getApplicationContext().getExternalFilesDir(null), "Crocolor");

                if (albumF != null) {
                    if (! albumF.mkdirs()) {
                        if (! albumF.exists()){
                            Log.d("ShareB64", "failed to create directory");
                            return false;
                         }
                    }
                }

                File imageF = File.createTempFile(imageFileName, ".jpg", albumF);
                byte[] b = Base64.decode(imageData.replace("data:image/png;base64,", ""), Base64.DEFAULT);
                if (b.length > 0) {
                    Bitmap mBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    FileOutputStream ostream = new FileOutputStream(imageF);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.flush();
                    ostream.close();

                    Uri fileUri = Uri.fromFile(imageF);

                    shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    shareIntent.setType("image/jpeg");
                }


                if (message != null) {
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, message);
                    shareIntent.putExtra(Intent.EXTRA_TITLE, message);
                }

                ((CordovaActivity) this.cordova.getActivity()).startActivity(shareIntent);

                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
                return true;
                
            }
            //return new PluginResult(PluginResult.Status.INVALID_ACTION);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            String errorMessage = e.getMessage();
            //return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION, errorMessage));
            return false;
        } catch (IOException e) {
            String errorMessage = e.getMessage();
            //return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION, errorMessage));
            return false;
        }
    }
}
