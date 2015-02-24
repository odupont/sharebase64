package fr.odupont.sharebase64;

import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.CordovaActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.PluginResult;

/**
 * ShareB64 is a PhoneGap plugin
 *  
 * @author odvpont@gmail.com
 * 
 */
public class ShareB64 extends CordovaPlugin {

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
                File albumF = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Crocolor");

                if (albumF != null) {
                    if (! albumF.mkdirs()) {
                        if (! albumF.exists()){
                            Log.d("ShareB64", "failed to create directory");
                            return;
                         }
                    }
                }

                /*File imageF = File.createTempFile(imageFileName, ".jpg", albumF);
                byte[] b = Base64.decode(imageData, Base64.DEFAULT);
                if (b.length > 0) {
                    Bitmap mBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    FileOutputStream ostream = new FileOutputStream(imageF);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.flush();
                    ostream.close();

                    Uri fileUri = Uri.fromFile(imageF);

                    shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    shareIntent.setType("image/jpeg");
                }*/


                if (message != null) {
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, message);
                    shareIntent.putExtra(Intent.EXTRA_TITLE, message);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                }

                ((CordovaActivity) this.cordova.getActivity()).startActivity(Intent.createChooser(shareIntent));*/

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
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION,errorMessage));
            return false;
        }
    }
}
