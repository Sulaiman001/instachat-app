package com.app.instachat.activities.fcm;

import static com.app.instachat.activities.constants.IConstants.FCM_BODY;
import static com.app.instachat.activities.constants.IConstants.FCM_GROUPS;
import static com.app.instachat.activities.constants.IConstants.FCM_ICON;
import static com.app.instachat.activities.constants.IConstants.FCM_SENT;
import static com.app.instachat.activities.constants.IConstants.FCM_TITLE;
import static com.app.instachat.activities.constants.IConstants.FCM_TYPE;
import static com.app.instachat.activities.constants.IConstants.FCM_USER;
import static com.app.instachat.activities.constants.IConstants.FCM_USERNAME;
import static com.app.instachat.activities.constants.IConstants.TYPE_IMAGE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.app.instachat.chat.activities.R;
import com.app.instachat.activities.managers.SessionManager;
import com.app.instachat.activities.managers.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(@NotNull String s) {
        super.onNewToken(s);

        Utils.uploadToken(s);
    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (!SessionManager.get().isNotificationOn()) {
            return;
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String sent = remoteMessage.getData().get(FCM_SENT);
            if (!ApplicationLifecycleManager.isAppVisible()) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    assert sent != null;
                    if (sent.equalsIgnoreCase(firebaseUser.getUid())) {
                        sendNotification(remoteMessage);
                    }
                }
            }
        }

    }

    private String strGroups = "";
    private String type = "";
    private String username = "";

    private void sendNotification(RemoteMessage remoteMessage) {
        final String user = remoteMessage.getData().get(FCM_USER);
        final String icon = remoteMessage.getData().get(FCM_ICON);
        final String title = remoteMessage.getData().get(FCM_TITLE);
        final String message = remoteMessage.getData().get(FCM_BODY);
        try {
            type = remoteMessage.getData().get(FCM_TYPE);
            username = remoteMessage.getData().get(FCM_USERNAME);
        } catch (Exception ignored) {
        }
        try {
            strGroups = remoteMessage.getData().get(FCM_GROUPS);
        } catch (Exception ignored) {
        }

        Bitmap bitmap = null;

        String body;
        if (!Utils.isEmpty(type)) {
            if (type.equalsIgnoreCase(TYPE_IMAGE)) {
                bitmap = getBitmapFromURL(message);
                body = String.format(getString(R.string.strPhotoSent), username);
            } else {
                body = username + ": " + message;
            }
        } else {
            body = message;
        }

        final Bundle bundle = new Bundle();

        final String channelId = getString(R.string.default_notification_channel_id);

        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setTicker(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        if (bitmap != null) {
            notificationBuilder.setLargeIcon(bitmap);
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(bitmap));
        }

        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel(channelId, "Chat Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify((int) new Date().getTime(), notificationBuilder.build());
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            final URL url = new URL(strURL);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            final InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            Utils.getErrors(e);
            return null;
        }
    }

}
