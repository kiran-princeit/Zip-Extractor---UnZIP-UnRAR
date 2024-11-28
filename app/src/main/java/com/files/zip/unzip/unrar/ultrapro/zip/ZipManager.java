package com.files.zip.unzip.unrar.ultrapro.zip;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.Zip4jUtil;

public final class ZipManager {
    private static final int WHAT_FINISH = 101;
    private static final int WHAT_PROGRESS = 102;
    private static final int WHAT_START = 100;
    public static boolean fileIsBig = true;
    public static boolean isZipType = false;
    /* access modifiers changed from: private */
    public static Handler mUIHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case 100:
                        ((IZipCallback) message.obj).onStartIZip();
                        ZipLog.debug("onStart.");
                        return;
                    case 101:
                        ((IZipCallback) message.obj).onFinish(true);
                        ZipLog.debug("onFinish: success=true");
                        return;
                    case 102:
                        ((IZipCallback) message.obj).onProgress(message.arg1);
                        ZipLog.debug("onProgress: percentDone=" + message.arg1);
                        return;
                    default:
                        return;
                }
            }
        }
    };

    private ZipManager() {
    }

    public static void debug(boolean z) {
        ZipLog.config(z);
    }

    public static void zip(String str, String str2, IZipCallback iZipCallback) {
        zip(str, str2, "", iZipCallback);
    }

    public static void zip(String str, String str2, String str3, IZipCallback iZipCallback) {
        isZipType = true;
        if (Zip4jUtil.isStringNotNullAndNotEmpty(str) && Zip4jUtil.isStringNotNullAndNotEmpty(str2)) {
            ZipLog.debug("zip: targetPath=" + str + " , destinationFilePath=" + str2 + " , password=" + str3);
            try {
                ZipParameters zipParameters = new ZipParameters();
                zipParameters.setCompressionMethod(8);
                zipParameters.setCompressionLevel(5);
                if (str3.length() > 0) {
                    zipParameters.setEncryptFiles(true);
                    zipParameters.setEncryptionMethod(99);
                    zipParameters.setAesKeyStrength(3);
                    zipParameters.setPassword(str3);
                }
                ZipFile zipFile = new ZipFile(str2);
                zipFile.setRunInThread(true);
                File file = new File(str);
                if (file.isDirectory()) {
                    zipFile.addFolder(file, zipParameters);
                } else {
                    zipFile.addFile(file, zipParameters);
                }
                timerMsg(iZipCallback, zipFile);
            } catch (Exception e) {
                if (iZipCallback != null) {
                    iZipCallback.onFinish(false);
                }
                ZipLog.debug("zip: Exception=" + e.getMessage());
            }
        } else if (iZipCallback != null) {
            iZipCallback.onFinish(false);
        }
    }

    public static void zip(ArrayList<File> arrayList, String str, String str2, IZipCallback iZipCallback) {
        isZipType = true;
        if (arrayList != null && arrayList.size() != 0 && Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
            ZipLog.debug("zip: list=" + arrayList.toString() + " , destinationFilePath=" + str + " , password=" + str2);
            try {
                ZipParameters zipParameters = new ZipParameters();
                zipParameters.setCompressionMethod(8);
                zipParameters.setCompressionLevel(5);
                if (str2.length() > 0) {
                    zipParameters.setEncryptFiles(true);
                    zipParameters.setEncryptionMethod(99);
                    zipParameters.setAesKeyStrength(3);
                    zipParameters.setPassword(str2);
                }
                ZipFile zipFile = new ZipFile(str);
                zipFile.setRunInThread(true);
                zipFile.addFiles(arrayList, zipParameters);
                timerMsg(iZipCallback, zipFile);
            } catch (Exception e) {
                if (iZipCallback != null) {
                    iZipCallback.onFinish(false);
                }
                ZipLog.debug("zip: Exception=" + e.getMessage());
            }
        } else if (iZipCallback != null) {
            iZipCallback.onFinish(false);
        }
    }

    public static void zip(ArrayList<File> arrayList, String str, boolean z, IZipCallback iZipCallback) {
        zip(arrayList, str, "", iZipCallback);
    }

    public static void unzip(String str, String str2, IZipCallback iZipCallback) {
        unzip(str, str2, "", iZipCallback);
    }

    public static void unzip(String str, String str2, String str3, IZipCallback iZipCallback) {
        isZipType = false;
        if (new File(str).length() >= 10485760) {
            fileIsBig = true;
        } else {
            fileIsBig = false;
        }
        if (Zip4jUtil.isStringNotNullAndNotEmpty(str) && Zip4jUtil.isStringNotNullAndNotEmpty(str2)) {
            ZipLog.debug("unzip: targetZipFilePath=" + str + " , destinationFolderPath=" + str2 + " , password=" + str3);
            try {
                ZipFile zipFile = new ZipFile(str);
                if (zipFile.isEncrypted() && Zip4jUtil.isStringNotNullAndNotEmpty(str3)) {
                    zipFile.setPassword(str3);
                }
                zipFile.setRunInThread(true);
                zipFile.extractAll(str2);
                timerMsg(iZipCallback, zipFile);
            } catch (Exception e) {
                ZipLog.debug("unzip: Exception=" + e.getMessage());
                if (iZipCallback != null) {
                    iZipCallback.onFinish(false);
                }
            }
        } else if (iZipCallback != null) {
            iZipCallback.onFinish(false);
        }
    }

    public static boolean checkFileIsProtected(String str) {
        try {
            if (new ZipFile(str).isEncrypted()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            ZipLog.debug("unzip: Exception=" + e.getMessage());
            return false;
        }
    }

    public static boolean setCommandForCustom(String str, String str2) {
        try {
            new ZipFile(str).setComment(str2);
            return true;
        } catch (Exception e) {
            ZipLog.debug("unzip: Exception=" + e.getMessage());
            return false;
        }
    }

    public static boolean checkFilePassword(String str, String str2) {
        try {
            ZipFile zipFile = new ZipFile(str);
            if (!zipFile.isEncrypted() || !str2.equalsIgnoreCase(zipFile.getComment())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            ZipLog.debug("unzip: Exception=" + e.getMessage());
        }
        return false;
    }
    private static void timerMsg(final IZipCallback iZipCallback, ZipFile zipFile) {
        if (iZipCallback != null) {
            ZipManager.mUIHandler.obtainMessage(100, iZipCallback).sendToTarget();
            final ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
            final Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                public void run() {
                    ZipManager.mUIHandler.obtainMessage(102, progressMonitor.getPercentDone(), 0, iZipCallback).sendToTarget();

                    if (ZipManager.isZipType) {
                        if (progressMonitor.getResult() == ProgressMonitor.RESULT_SUCCESS) { // Assuming 0 is RESULT_SUCCESS
                            ZipManager.mUIHandler.obtainMessage(101, iZipCallback).sendToTarget();
                            cancel();
                            timer.purge();
                        }
                    } else if (ZipManager.fileIsBig) {
                        if (progressMonitor.getPercentDone() > 90 && progressMonitor.getResult() == ProgressMonitor.RESULT_SUCCESS) {
                            ZipManager.mUIHandler.obtainMessage(101, iZipCallback).sendToTarget();
                            cancel();
                            timer.purge();
                        }
                    } else if (progressMonitor.getResult() == ProgressMonitor.RESULT_SUCCESS) {
                        ZipManager.mUIHandler.obtainMessage(101, iZipCallback).sendToTarget();
                        cancel();
                        timer.purge();
                    }
                }
            }, 0, 300);
        }
    }

}
