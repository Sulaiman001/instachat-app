package com.app.instachat.activities.managers;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import com.app.instachat.activities.constants.IConstants;
import com.app.instachat.chat.activities.R;
import com.app.instachat.activities.models.AttachmentTypes;
import com.app.instachat.activities.models.Chat;
import com.app.instachat.activities.models.DownloadFileEvent;

import java.io.File;

public class DownloadUtil {

    public void loading(Context context, DownloadFileEvent downloadFileEvent) {
        try {
            final Chat attach = downloadFileEvent.getAttachment();
            final File file = new File(IConstants.SDPATH + getDirectoryPath(attach.getAttachmentType()), IConstants.SLASH + context.getString(R.string.app_name) + IConstants.SLASH + attach.getAttachmentType() + IConstants.SLASH + attach.getAttachmentFileName());
            Utils.sout("Downloading + Loading::: " + file.toString());
            if (file.exists()) {
                Utils.getOpenFileIntent(context, file.toString());
            } else {
                final Screens screens = new Screens(context);
                screens.showToast(R.string.msgDownloadingStarted);
                downloadFile(context, attach.getAttachmentPath(), attach.getAttachmentType(), attach.getAttachmentFileName());
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void downloadFile(Context context, String url, String type, String fileName) {
        final DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setDescription(context.getString(R.string.msgDownloadFile, fileName))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                .setVisibleInDownloadsUi(false)
                .setDestinationInExternalPublicDir(getDirectoryPath(type), IConstants.SLASH + context.getString(R.string.app_name) + IConstants.SLASH + type + IConstants.SLASH + fileName);
        mgr.enqueue(request);
    }

    private String getDirectoryPath(String type) {
        return AttachmentTypes.getDirectoryByType(type);
    }
}
