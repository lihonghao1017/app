package com.jin91.preciousmetal.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.zip.ZipFile;


public class FileUtils {
    private static final String FD_FLASH = "-ext";

    public String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static boolean createFileDir(File file) {
        try {
            if (file.exists()) {
                return true;
            } else {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
            }
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断SD卡是否存在
     *
     * @return
     * @author hanyp
     * @Created 2013-4-22
     */
    public static boolean isSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建文件夹
     * derectory为想用存储的完整自定义路径如(/aa/bb/cc.apk)
     * 此方法可以判断是存储在sdk下还是data/data/包名
     *
     * @param context
     * @param derectory
     * @return
     * @author hanyp
     * @Created 2014-4-12
     */
    public static String getFailPath(Context context, String derectory) {
        if (TextUtils.isEmpty(derectory)) {
            return "";
        }

        if (derectory.startsWith("/")) {
            derectory = derectory.substring(derectory.indexOf("/") + 1);
        }

        String path = null;
        if (avaiableMedia()) {
            if (FlashMemory()) {
                path = Environment.getExternalStorageDirectory().getPath() + FD_FLASH + "/" + derectory;
            } else {
                path = Environment.getExternalStorageDirectory().getPath() + "/" + derectory;
            }
        } else {
            path = context.getCacheDir().getPath() + "/" + derectory;
        }
        File mFile = new File(path);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        return path;
    }

    /**
     *
     * @param context
     * @param derectory
     * @return
     */

    // dir为路径如（/aa/bb）url为下载地址
    public static String getFailPath(Context context, String dir, String url) {
        String filename = (url.substring((url.lastIndexOf("/") + 1)));
        if (!dir.startsWith("/", dir.length() - 1)) {
            dir = dir + "/";
        }
        return getFailPath(context, dir + filename);
    }

    /**
     * 判断文件可以大小
     *
     * @param path
     * @return
     */
    public static long getAvaiableSize(String path) {
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSize();
        long blockCount = stat.getBlockCount();
        return blockCount * blockSize;
    }

    /**
     * 获取文件大小
     *
     * @param path
     * @return
     */
    public static long getDirSize(String path) {
        File file = new File(path);
        return file.length();
    }

    /**
     *
     * @return
     */
    private static boolean avaiableMedia() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return
     */
    private static boolean FlashMemory() {
        String path = Environment.getExternalStorageDirectory().getPath() + FD_FLASH;
        File file = new File(path);
        if (file.exists()) {
            if (getAvaiableSize(path) == 0) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    // 获取包名
    public static String getPackageName(Context ctx, String localPath) {
        File file = new File(localPath);
        if (!file.exists()) {
            return null;
        }

        try {
            new ZipFile(new File(localPath), ZipFile.OPEN_READ);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        PackageManager pm = ctx.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(localPath, PackageManager.GET_ACTIVITIES);
        if (info == null) {
            return null;
        } else {
            return info.packageName;
        }
    }

    // 只创建文件
    public static boolean createFile(File file) {
        try {
            if (file.exists()) {
                return true;
            } else {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }

            if (!file.exists()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 创建文件并写入流
    public static boolean createFile(File file, byte[] buffer) {
        if (createFile(file)) {
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(file);
                fos.write(buffer);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    // 创建文件并写入流
    public static boolean createFile(File file, InputStream inputStream) {
        if (createFile(file)) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                byte buffer[] = new byte[1024];
                while (true) {
                    int stream = inputStream.read(buffer);
                    if (stream == -1) {
                        break;
                    }
                    fos.write(buffer, 0, stream);
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    //	//可以监听流的读取更新进度条
    //	public static boolean createFile(File file, InputStream inputStream,
    //			DownLoadFileInterface downLoadFileInterface, int TotalCount) {
    //		if (createFile(file)) {
    //			FileOutputStream fos = null;
    //			try {
    //				fos = new FileOutputStream(file);
    //				byte buffer[] = new byte[1024];
    //				int count = 0;
    //				int downloadSize = 0;
    //				long totalLength = Long.parseLong(TotalCount + "");
    //				while (true) {
    //					int stream = inputStream.read(buffer);
    //					count = stream;
    //					if (stream == -1) {
    //						break;
    //					}
    //					fos.write(buffer, 0, stream);
    //					int progressSize = (int) ((downloadSize * 0.1)
    //							/ (totalLength * 0.1) * 100);
    //					if (progressSize >= 100) {
    //						progressSize = 100;
    //					}
    //					downloadSize += count;
    //					downLoadFileInterface.OnInputStream(progressSize + "");
    //				}
    //				fos.flush();
    //				fos.close();
    //			} catch (Exception e) {
    //				e.printStackTrace();
    //				return false;
    //			}
    //		}
    //		return true;
    //	}

    // 传文件路径判断文件是否存在
    public static boolean exists(String imagePath) {
        if (imagePath != null && !imagePath.equals("")) {
            File file = new File(imagePath);
            return exists(file);
        }
        return false;

    }

    // 传文件判断是否存在
    public static boolean exists(File file) {
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static byte[] streamToBytes(FileInputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (Exception e) {
        } catch (OutOfMemoryError e2) {
            System.gc();
        }
        return os.toByteArray();
    }

    /**
     * 获取手机外部可用空间大小
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (isSdcard()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 删除指定文件
     *
     * @param path
     * @return
     */
    public static boolean delFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    /**
     * 删除指定文件
     *
     * @param path
     * @return
     */
    public static boolean delFile(File file) {
        if (exists(file)) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹
     *
     * @param path
     * @return
     */
    public static void deleteAllFile(String path) {

        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                deleteAllFile(path + "/" + tempList[i]);
                deleteFolder(path + "/" + tempList[i]);
            }
        }
    }

    public static void deleteFolder(String folderPath) {
        try {
            deleteAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 默认 JPEG格式保存
     * @param context
     * @param bm
     * @return
     * @throws java.io.IOException
     * @author hanyp
     * @Created 2014年12月31日
     */
    public static File saveFile(Context context, Bitmap bm) throws IOException {
        return saveFile(context, 1, bm);
    }

    /**
     * @param context
     * @param type  1.Bitmap.CompressFormat.JPEG
     *              2.Bitmap.CompressFormat.PNG
     * @param bm
     * @author hanyp
     * @Created 2014年12月31日
     */
    public static File saveFile(Context context, int type, Bitmap bm) throws IOException {
        String fileName = "";
        if (type == 1) {
            fileName = "doodle" + System.currentTimeMillis() + ".jpg";
        } else {
            fileName = "doodle" + System.currentTimeMillis() + ".png";
        }
        return saveFile(context, type, bm, fileName);
    }

    /**
     * @param context
     * @param type  1.Bitmap.CompressFormat.JPEG
     *              2.Bitmap.CompressFormat.PNG
     * @param bm
     * @param fileName
     * @return
     * @throws java.io.IOException
     * @author hanyp
     * @Created 2015年1月4日
     */
    public static File saveFile(Context context, int type, Bitmap bm, String fileName) throws IOException {
        String path = getFailPath(context, "doodle/");
        return saveFile(type, bm, fileName, path);
    }

    /**
     * 保存bitmap资源到指定路径下
     * @param type 1.Bitmap.CompressFormat.JPEG  2.Bitmap.CompressFormat.PNG
     * @param bm
     * @param fileName
     * @param imgPath
     * @throws java.io.IOException
     * @author hanyp
     * @Created 2013-4-22
     */
    public static File saveFile(int type, Bitmap bm, String fileName, String imgPath) {
        File result = null;
        File advDir = new File(imgPath);
        if (!advDir.exists()) {
            advDir.mkdirs();
        }
        if (bm != null) {
            BufferedOutputStream bos = null;
            try {
                result = new File(imgPath + "/" + fileName);
                bos = new BufferedOutputStream(new FileOutputStream(result));
                if (type == 1) {
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                } else if (type == 2) {
                    bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result = null;
            } finally {
                if (bos != null) {
                    try {
                        bos.flush();
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        result = null;
                    }
                }
            }
        }
        return result;
    }

    /**文件重命名
     * @param path 文件目录
     * @param oldname  原来的文件名
     * @param newname 新文件名
     */
    public static void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (!oldfile.exists()) {
                return;//重命名文件不存在
            }
            if (newfile.exists()) {
//                Log.i("文件重命名", newname + "已经存在！");
            } else {
                oldfile.renameTo(newfile);
            }
        } else {
//            Logger.i("文件重命名", "新文件名和旧文件名相同...");
        }
    }

    public static String getFileName(String url) {
        String fileName = "";
        if (!TextUtils.isEmpty(url)) {
            fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        }
        return fileName;
    }

    /**

     * 使用文件通道的方式复制文件

     * @param s
     *            源文件
     * @param t
     *            复制到的新文件
     */

    public static void fileChannelCopy(FileInputStream fi, File t) {
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}