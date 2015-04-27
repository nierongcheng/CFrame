package com.codi.frame.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日志工具类 该类用于控制logcat控制台输出、一般方式文件输出开关， 以及定制独立文件输出
 * <p/>
 * <pre>
 * 独立的日志文件的组织方式：
 * 		此时 FILE_DIR = "/log/backup_restore/"  FILE_NAME = "backup_restore" FILE_SUFFIX = ".log"
 *
 *  	/log/backup_restore/backup_restore_error.log                    (用于记录ERROR以上级别日志)
 *  	/log/backup_restore/2012-08-26/backup_restore_2012-08-26.log    (用于记录全级别日志)
 *  	/log/backup_restore/2012-08-28/backup_restore_2012-08-28.log     (用于记录全级别日志)
 * </pre>
 *
 */
public class LogUtil {

    public static final String NEW_LINE = "\r\n";
    /**
     * 开关用于控制是否启用Log（包括logcat控制台输出、一般方式文件输出）
     */
    public static boolean sLogEnable = true;
    /**
     * 开关用于控制是否启用Log（包括独立文件输出），需依赖logEnable置为true，此项配置才能起作用
     */
    private static boolean sLogToFileEnable = false;
    /**
     * 是否需要详细的log信息（true，输出额外信息例如：(Error 08-28 13:56:02.724)[Thread-main: AutoBackupActivity.java:319
     * onDestroy()]，false 仅输出相关传入字符串信息）
     */
    private static final boolean EXT_INFO_ENABLE = true;
    /**
     * 应用默认TAG
     */
    public static final String APP_TAG_ACCOUNT = "wallet_account";
    public static final String APP_TAG_PAYMENT = "wallet_main";
    /**
     * 日志启用级别，闭区间，大于等于该级别的日志会输出
     */
    protected static final int LOG_LEVEL = Log.VERBOSE;
    /**
     * 应用日志文件存放目录
     */
    private static final String FILE_DIR = "/Amigo/wallet/";
    /**
     * 应用日志文件名称
     */
    private static final String FILE_NAME = "gsp";
    /**
     * 应用日志文件后缀
     */
    private static final String FILE_SUFFIX = ".log";
    /**
     * 应用日志ERROR文件名称
     */
    private static final String ERROR_FILE_NAME = FILE_NAME + "_error";
    /**
     * 该格式用于规范应用日志文件名称中的时间信息
     */
    private static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd",
            Locale.getDefault());
    /**
     * 该格式用于规范应用日志文件内具体日志条目时间信息
     */
    private static final SimpleDateFormat LOG_TIME_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS",
            Locale.getDefault());

    public static void initLogSwitch(boolean flag) {
        sLogEnable = flag;
        sLogToFileEnable = flag;
        if (sLogToFileEnable) {
            i("初始化日志控制类，日志开关开启");
        }
    }

    public static void i(Object obj) {
        i(APP_TAG_PAYMENT, obj);
    }

    public static void i(String tag, Object obj) {
        if (sLogEnable && LOG_LEVEL <= Log.INFO) {
            log(APP_TAG_PAYMENT + "_" + tag, Log.INFO, obj);
        }
    }

    public static void d(Object obj) {
        d(APP_TAG_PAYMENT, obj);
    }

    public static void d(String tag, Object obj) {
        if (sLogEnable && LOG_LEVEL <= Log.DEBUG) {
            log(APP_TAG_PAYMENT + "_" + tag, Log.DEBUG, obj);
        }
    }

    /**
     * 日志记录的基础方法(参数较多，不建议使用)
     */
    public static void log(int level, String tag, Object... objs) {
        if (sLogEnable && LOG_LEVEL <= level) {
            log(tag, level, objs);
        }
    }

    public static void v(Object obj) {
        v(APP_TAG_PAYMENT, obj);
    }

    public static void v(String tag, Object obj) {
        if (sLogEnable && LOG_LEVEL <= Log.VERBOSE) {
            log(APP_TAG_PAYMENT + "_" + tag, Log.VERBOSE, obj);
        }
    }

    public static void w(Object obj) {
        w(APP_TAG_PAYMENT, obj);
    }

    public static void w(String tag, Object obj) {
        if (sLogEnable && LOG_LEVEL <= Log.WARN) {
            log(APP_TAG_PAYMENT + "_" + tag, Log.WARN, obj);
        }
    }

    public static void e(Object obj) {
        // FIXME:方法重载需改进
        if (obj instanceof Throwable) {
            e((Throwable) obj);
            return;
        }
        e(APP_TAG_PAYMENT, obj);
    }

    public static void e(String tag, Object obj) {
        if (sLogEnable && LOG_LEVEL <= Log.ERROR) {
            log(APP_TAG_PAYMENT + "_" + tag, Log.ERROR, obj);
        }
    }

    public static void e(Throwable ex) {
        if (sLogEnable && LOG_LEVEL <= Log.ERROR) {
            log(APP_TAG_PAYMENT, Log.ERROR, NEW_LINE + Log.getStackTraceString(ex));
        }
    }

    protected static void log(String tag, int level, Object... obj) {
        try {
            String messge = buildMessge(level, obj);
            switch (level) {
                case Log.VERBOSE:
                    Log.v(tag, messge);
                    break;
                case Log.DEBUG:
                    Log.d(tag, messge);
                    break;
                case Log.INFO:
                    Log.i(tag, messge);
                    break;
                case Log.WARN:
                    Log.w(tag, messge);
                    break;
                case Log.ERROR:
                    Log.e(tag, messge);
                    break;
                default:
                    Log.e(tag, "不支持的Log级别" + messge);
            }
            // 判断独立文件输出是否开启
            if (sLogToFileEnable) {
//                String currentTs = CommonUtils.createtFileName();
//                messge = currentTs + messge + " ";
                // 全级别存储到一般的日志文件
                saveToSDCard(messge, LogFileType.Ordinary);
                if (level >= Log.ERROR) {
                    // Error以上级别日志存储到Error日志文件
                    saveToSDCard(messge, LogFileType.ErrorFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 构造日志信息
//     */
//    private static String buildMessge(Object obj, int level) {
//        if (CommonUtils.isNull(obj)) {
//            obj = GNConfig.DEFAULT_VALUE;
//        }
//        String retString = GNConfig.DEFAULT_VALUE;
//        // 是否需要显示详细信息
//        if (EXT_INFO_ENABLE) {
//            String functionInfo = getMethodInfo();
//            if (CommonUtils.isNull(functionInfo)) {
//                functionInfo = GNConfig.DEFAULT_VALUE;
//            }
//            // 构造详细信息（日志级别、时间、方法及线程信息）
//            retString = "(" + getLevelInfo(level) + " " + getTimestamp() + ")" + functionInfo;
//        }
//        return retString + obj;
//    }

    /**
     * 构造日志信息
     */
    private static String buildMessge(int level, Object... objs) {
        String message = "";
        if (objs.length == 1) {
            message = objs[0] == null ? "" : objs[0].toString();
        } else if (objs.length == 2) {
            message = (objs[0] == null ? "" : objs[0].toString())
                    + (objs[1] == null ? "" : objs[1].toString());
        } else if (objs.length == 3) {
            message = (objs[0] == null ? "" : objs[0].toString())
                    + (objs[1] == null ? "" : objs[1].toString())
                    + (objs[2] == null ? "" : objs[2].toString());
        } else {
            StringBuilder sb = new StringBuilder();
            for (Object obj : objs) {
                if (obj != null) {
                    sb.append(obj.toString());
                }
            }
            message = sb.toString();
        }

        // 是否需要显示详细信息
        if (EXT_INFO_ENABLE) {
            String functionInfo = getMethodInfo();
            if (functionInfo == null) {
                functionInfo = "";
            }
            // 构造详细信息（日志级别、时间、方法及线程信息）
            String retString = "(" + getLevelInfo(level) + " " + getTimestamp() + ")" + functionInfo;
//            if (style == Style.Style1) {
//                return retString + "\n" + message;
//            } else if (style == Style.Style2) {
//                return message + "      " + retString;
//            }
            message = retString + "      " + message;
        }
        return message;
    }

    /**
     * 从堆栈中取得必要方法信息等
     */
    private static String getMethodInfo() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(LogUtil.class.getName())) {
                continue;
            }
            /*            return "[Thread-" + Thread.currentThread().getName() + ": " + st.getFileName() + ":"
                                + st.getLineNumber() + " " + st.getMethodName() + "()]";*/
            return "[Thread-" + Thread.currentThread().getName() + ": " + st.getFileName() + ":"
                    + st.getLineNumber() + " " + st.getMethodName() + "()]";
        }
        return null;
    }

    /**
     * 取得日志级别对应的显示字符
     */
    private static String getLevelInfo(int level) {
        switch (level) {
            case Log.VERBOSE:
                return "V";
            case Log.DEBUG:
                return "D";
            case Log.INFO:
                return "I";
            case Log.WARN:
                return "W";
            case Log.ERROR:
                return "E";
            default:
                return "不支持的Log级别";
        }
    }

    /**
     * 获取日志具体时间字符串
     */
    private static synchronized String getTimestamp() {
        return LOG_TIME_FORMAT.format(new Date());
    }

    /**
     * 存储日志到SD卡文件
     */
    private static void saveToSDCard(String content, LogFileType type) throws Exception {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Writer bw = getDestLogWriter(type);
            bw.write(NEW_LINE);
            bw.write(content);
            bw.flush();
            // 此处考虑性能，并没有关闭IO流
        }
    }

    // 应用日志文件
    private static File sTargetFile = null;
    private static Writer sTargetFileWriter = null;
    // 应用日志当文件天时间段
    private static long sTargetFileBeginTs = -1;
    private static long sTargetFileEndTs = -1;
    // 应用日志ERROR文件
    private static File sTargetErrFile = null;
    private static Writer sTargetErrFileWriter = null;

    /**
     * 日志文件类型
     */
    private enum LogFileType {
        /**
         * 一般日志文件(各级别全纪录)
         */
        Ordinary,
        /**
         * ERROR文件（记录ERROR以上级别）
         */
        ErrorFile
    }

    private static synchronized Writer getDestLogWriter(LogFileType type) throws Exception {
        if (type == LogFileType.ErrorFile) {
            if (sTargetErrFileWriter == null) {
                sTargetErrFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        getDestLogFile(LogFileType.ErrorFile), true)));
            }
            return sTargetErrFileWriter;
        } else {
            // 大于targetFileEndTs即视为新的周期（一天），需重新取得Writer
            long currentTs = System.currentTimeMillis();
            if (sTargetErrFileWriter == null || currentTs > sTargetFileEndTs
                    || currentTs < sTargetFileBeginTs) {
                sTargetFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        getDestLogFile(LogFileType.Ordinary), true)));
            }
            return sTargetFileWriter;
        }
    }

    private static synchronized File getDestLogFile(LogFileType type) throws Exception {
        File sdCardDir = Environment.getExternalStorageDirectory();
        String filePath = sdCardDir.getPath() + FILE_DIR;
        if (type == LogFileType.ErrorFile) {
            if (sTargetErrFile == null) {
                // 构造日志ERROR文件路径
                filePath = filePath + ERROR_FILE_NAME + FILE_SUFFIX;
                sTargetErrFile = new File(filePath);
                if (!sTargetErrFile.exists()) {
                    sTargetErrFile.getParentFile().mkdirs();
                    sTargetErrFile.createNewFile();
                }
            }
            return sTargetErrFile;
        } else {
            long currentTs = System.currentTimeMillis();
            if (sTargetFile == null || currentTs > sTargetFileEndTs
                    || currentTs < sTargetFileBeginTs) {
                // 构造日志文件路径
                String subDir = FILE_DATE_FORMAT.format(new Date(currentTs));
                filePath = filePath + subDir + "/" + FILE_NAME + "_" + subDir + FILE_SUFFIX;
                sTargetFile = new File(filePath);
                if (!sTargetFile.exists()) {
                    sTargetFile.getParentFile().mkdirs();
                    sTargetFile.createNewFile();
                    // TODO:此处用简便方式获取后一天起始值，稍后修改
                    sTargetFileBeginTs = new SimpleDateFormat("yyyy-MM-dd").parse(subDir).getTime();
                    sTargetFileEndTs = sTargetFileBeginTs + 24 * 60 * 60 * 1000 - 1;
                }
            }
            return sTargetFile;
        }
    }

}
