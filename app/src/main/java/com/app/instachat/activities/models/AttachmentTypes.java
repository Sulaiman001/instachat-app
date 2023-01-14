package com.app.instachat.activities.models;

import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;

import com.app.instachat.activities.constants.IConstants;
import com.app.instachat.activities.managers.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AttachmentTypes {

    public static final int IMAGE = 0;
    public static final int VIDEO = 1;
    public static final int CONTACT = 2;
    public static final int AUDIO = 3;
    public static final int LOCATION = 4;
    public static final int DOCUMENT = 5;
    public static final int RECORDING = 6;
    public static final int NONE_TEXT = 7;
    public static final int NONE_TYPING = 8;

    @IntDef({IMAGE, VIDEO, CONTACT, AUDIO, LOCATION, DOCUMENT, NONE_TEXT, NONE_TYPING, RECORDING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AttachmentType {
    }

    public static String getTypeName(@AttachmentType int attachmentType) {
        switch (attachmentType) {
            case IMAGE:
                return IConstants.TYPE_IMAGE;
            case AUDIO:
                return IConstants.TYPE_AUDIO;
            case VIDEO:
                return IConstants.TYPE_VIDEO;
            case CONTACT:
                return IConstants.TYPE_CONTACT;
            case DOCUMENT:
                return IConstants.TYPE_DOCUMENT;
            case LOCATION:
                return IConstants.TYPE_LOCATION;
            case RECORDING:
                return IConstants.TYPE_RECORDING;
            case NONE_TEXT:
                return "none_text";
            case NONE_TYPING:
                return "none_typing";
            default:
                return "none";
        }
    }

    public static String getTypeName(String attachmentType) {
        switch (attachmentType) {
            case IConstants.TYPE_IMAGE:
                return IConstants.TYPE_IMAGE;
            case IConstants.TYPE_AUDIO:
                return IConstants.TYPE_AUDIO;
            case IConstants.TYPE_VIDEO:
                return IConstants.TYPE_VIDEO;
            case IConstants.TYPE_CONTACT:
                return IConstants.TYPE_CONTACT;
            case IConstants.TYPE_DOCUMENT:
                return IConstants.TYPE_DOCUMENT;
            case IConstants.TYPE_LOCATION:
                return IConstants.TYPE_LOCATION;
            case IConstants.TYPE_RECORDING:
                return IConstants.TYPE_RECORDING;
            default:
                return "none";
        }
    }

    public static String getDirectoryByType(String type) {
        switch (type) {
            case IConstants.TYPE_AUDIO:
            case IConstants.TYPE_RECORDING:
                return Utils.getMusicFolder();
            case IConstants.TYPE_VIDEO:
                return Utils.getMoviesFolder();
            case IConstants.TYPE_DOCUMENT:
            case IConstants.TYPE_CONTACT:
                return Utils.getDownloadFolder();
        }
        return Utils.getDownloadFolder();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Uri getTargetUri(String type) {
        switch (type) {
            case IConstants.TYPE_AUDIO:
            case IConstants.TYPE_RECORDING:
                return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            case IConstants.TYPE_VIDEO:
                return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            case IConstants.TYPE_DOCUMENT:
            case IConstants.TYPE_CONTACT:
                return MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        }
        return MediaStore.Downloads.EXTERNAL_CONTENT_URI;
    }

    public static String getExtension(final String fileExtension, final int attachmentType) {
        switch (attachmentType) {
            case AUDIO:
            case RECORDING:
                if (!fileExtension.endsWith(IConstants.EXT_MP3))
                    return IConstants.EXT_MP3;
        }
        return fileExtension;
    }
}