/**
 * cordova Share Base 64
 * Copyright (c) Olivier Dupont
 *
 */
 (function(cordova){
    var ShareB64 = function() {

    };

    ShareB64.prototype.ACTION_SEND = "android.intent.action.SEND";
    ShareB64.prototype.ACTION_VIEW= "android.intent.action.VIEW";
    ShareB64.prototype.EXTRA_TEXT = "android.intent.extra.TEXT";
    ShareB64.prototype.EXTRA_SUBJECT = "android.intent.extra.SUBJECT";
    ShareB64.prototype.EXTRA_STREAM = "android.intent.extra.STREAM";
    ShareB64.prototype.EXTRA_EMAIL = "android.intent.extra.EMAIL";
    ShareB64.prototype.ACTION_CALL = "android.intent.action.CALL";
    ShareB64.prototype.ACTION_SENDTO = "android.intent.action.SENDTO";

    ShareB64.prototype.share = function(params, success, fail) {
        return cordova.exec(function(args) {
            success(args);
        }, function(args) {
            fail(args);
        }, 'ShareB64', 'share', [params]);        
    };

    window.shareb64 = new ShareB64();
    
    // backwards compatibility
    window.plugins = window.plugins || {};
    window.plugins.shareb64 = window.shareb64;
})(window.PhoneGap || window.Cordova || window.cordova);
