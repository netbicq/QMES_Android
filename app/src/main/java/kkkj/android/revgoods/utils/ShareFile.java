package kkkj.android.revgoods.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.utils
 * Author: Admin
 * Time: 2019/8/23 13:47
 * Describe: 调用系统自带文件分享
 */
public class ShareFile {

    // 調用系統方法分享文件
    public static void shareFile(Context context, File file) {
        if (null != file && file.exists()) {
            Intent share = new Intent(Intent.ACTION_SEND);
            //指定分享到微信
            //share.setPackage("com.tencent.mm");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            share.setType(getMimeType(file.getAbsolutePath()));//此处可发送多种文件

            context.startActivity(Intent.createChooser(share, "分享文件"));
        } else {
            Toast.makeText(context,"分享文件不存在！",Toast.LENGTH_LONG).show();
        }
    }

    // 根据文件后缀名获得对应的MIME类型。
    private static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }

}
