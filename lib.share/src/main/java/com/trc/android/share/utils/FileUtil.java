package com.trc.android.share.utils;

import android.os.Handler;
import android.os.Looper;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author HanTuo on 2017/2/8.
 */

public class FileUtil extends FileProvider {
    public static final String EXTERNAL_CACHE_OPEN = "external-cache-open";
    public static final String CACHE_OPEN = "cache-open";
    static File sShareDir;


    public static void download(final String url, final Map<String, String> headers, final File targetFile, final DownloadListener listener) {
        new Thread(new Runnable() {
            private Handler handler = new Handler(Looper.getMainLooper());
            DownloadListener downloadListener = listener;


            @Override
            public void run() {
                synchronized (targetFile.getPath().intern()) {
                    if (targetFile.exists()) {
                        onSuccess();
                        return;
                    }
                    FileOutputStream fos = null;
                    InputStream is;
                    HttpURLConnection connection = null;
                    boolean isDownloadSuccessful = false;
                    File tmpCacheFile = new File(targetFile.getParentFile(), targetFile.getName() + ".tmp");
                    try {
                        if (tmpCacheFile.exists()) tmpCacheFile.delete();
                        URL u = new URL(url);
                        connection = (HttpURLConnection) u.openConnection();
                        if (null != headers && !headers.isEmpty()) {
                            for (Map.Entry<String, String> entry : headers.entrySet()) {
                                connection.setRequestProperty(entry.getKey(), entry.getValue());
                            }
                        }
                        connection.connect();
                        int responseCode = connection.getResponseCode();
                        if (responseCode > 300 && responseCode < 400) {
                            String location = connection.getHeaderField("Location");
                            if (null != location) {
                                download(location, targetFile, downloadListener);
                                return;
                            }
                        }
                        if (responseCode == HttpURLConnection.HTTP_OK || responseCode < 400) {
                            fos = new FileOutputStream(tmpCacheFile, false);
                            is = connection.getInputStream();
                            byte[] buffer = new byte[20480];
                            int n = 0;
                            long sum = 0;
                            final ProDownloadListener[] proDownloadListener = new ProDownloadListener[1];
                            if (downloadListener instanceof ProDownloadListener) {
                                proDownloadListener[0] = (ProDownloadListener) downloadListener;
                            }
                            final long total = getLong(connection.getHeaderField("content-length"));
                            do {
                                fos.write(buffer, 0, n);
                                n = is.read(buffer);
                                sum += n;
                                if (null != proDownloadListener[0]) {
                                    handler.removeCallbacks(null);
                                    final long percentage = sum * 100 / connection.getContentLength();
                                    onProgress(proDownloadListener, total, (int) percentage);
                                }
                            } while (n != -1);
                            fos.flush();
                            isDownloadSuccessful = true;
                        } else {
                            onFail();
                        }
                    } catch (Throwable e) {
                        onFail();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {

                            }
                        }
                        if (null != connection) {
                            connection.disconnect();
                        }
                        if (isDownloadSuccessful) {
                            if (tmpCacheFile.renameTo(targetFile)) {
                                onSuccess();
                            } else {
                                tmpCacheFile.delete();
                                onFail();
                            }
                        }
                    }
                }
            }

            private void onFail() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            downloadListener.onFail();
                        } catch (Exception e) {

                        }
                    }
                });
            }

            private void onSuccess() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            downloadListener.onSuccess();
                        } catch (Exception e) {

                        }
                    }
                });
            }

            private void onProgress(final ProDownloadListener[] proDownloadListener, final long total, final int percentage) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            proDownloadListener[0].onProgress(percentage, total);
                        } catch (Exception e) {

                        }
                    }
                });
            }
        }).start();
    }

    private static long getLong(String longStr) {
        try {
            return Long.parseLong(longStr);
        } catch (Throwable e) {
            //ExceptionManager.handle(e);
        }
        return -1;
    }


    public static void download(final String url, final File targetFile, final DownloadListener listener) {
        download(url, null, targetFile, listener);
    }

    public interface DownloadListener {

        void onSuccess();

        void onFail();
    }

    public interface ProDownloadListener extends DownloadListener {
        void onProgress(int progress, long total);
    }

    public static long getFileSize(File file) {
        try {
            if (file.isFile()) {
                return file.length();
            } else {
                long size = 0;
                File[] files = file.listFiles();
                for (File f : files) {
                    size += getFileSize(f);
                }
                return size;
            }
        } catch (Exception e) {
            return 0;
        }
    }
}
