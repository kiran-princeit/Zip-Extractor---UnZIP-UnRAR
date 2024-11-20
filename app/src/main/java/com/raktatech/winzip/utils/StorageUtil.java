package com.raktatech.winzip.utils;

import android.os.Environment;
import android.os.StatFs;

public class StorageUtil {


    public static double getTotalInternalStorageGB() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return (double) (totalBlocks * blockSize) / (1024 * 1024 * 1024); // Convert to GB
    }

    public static double getAvailableInternalStorageGB() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return (double) (availableBlocks * blockSize) / (1024 * 1024 * 1024); // Convert to GB
    }

    public static double getUsedInternalStorageGB() {
        double totalGB = getTotalInternalStorageGB();
        double availableGB = getAvailableInternalStorageGB();
        return totalGB - availableGB; // Used storage
    }

    // Method to get total internal storage in bytes
    public static long getTotalInternalStorage() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize; // Total storage in bytes
    }

    // Method to get available internal storage in bytes
    public static long getAvailableInternalStorage() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks * blockSize; // Available storage in bytes
    }

    // Method to calculate the used storage percentage
    public static double getUsedStoragePercentage() {
        long totalStorage = getTotalInternalStorage();
        long availableStorage = getAvailableInternalStorage();
        long usedStorage = totalStorage - availableStorage;

        // Calculate percentage
        return ((double) usedStorage / totalStorage) * 100;
    }
}
