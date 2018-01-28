package com.github.network.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by zlove on 2018/1/22.
 */

public class NetworkUtils {

    public interface NetworkTypeInterceptor {
        NetworkType getNetworkType();
    }

    public enum CompressType {
        NONE(0),
        GZIP(1),
        DEFLATER(2);

        CompressType(int ni) {
            nativeInt = ni;
        }
        final int nativeInt;
    }

    public enum NetworkType {
        NONE(0),
        MOBILE(1),
        MOBILE_2G(2),
        MOBILE_3G(3),
        WIFI(4),
        MOBILE_4G(5);

        NetworkType(int ni) {
            nativeInt = ni;
        }

        public int getValue() {
            return nativeInt;
        }

        final int nativeInt;
    }

    private static final boolean DEBUG_MOBILE = false;

    private static NetworkTypeInterceptor sNetworkTypeInterceptor;

    public static void setNetworkTypeInterceptor(NetworkTypeInterceptor networkTypeInterceptor) {
        NetworkUtils.sNetworkTypeInterceptor = networkTypeInterceptor;
    }

    public static boolean is2G(Context context) {
        NetworkType nt = getNetworkType(context);
        return nt == NetworkType.MOBILE || nt == NetworkType.MOBILE_2G;
    }

    public static boolean isWifi(Context context) {
        try {
            ConnectivityManager manager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info == null || !info.isAvailable()) {
                return false;
            }
            if (DEBUG_MOBILE) {
                return false;
            } else {
                if (sNetworkTypeInterceptor != null && sNetworkTypeInterceptor.getNetworkType() != NetworkType.NONE) {
                    return sNetworkTypeInterceptor.getNetworkType() == NetworkType.WIFI;
                }
                return (ConnectivityManager.TYPE_WIFI == info.getType());
            }
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    /** detect network available or not */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager manager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    /** get network type. */
    public static NetworkType getNetworkType(Context context) {
        if (sNetworkTypeInterceptor != null && sNetworkTypeInterceptor.getNetworkType() != NetworkType.NONE) {
            return sNetworkTypeInterceptor.getNetworkType();
        }
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info == null || !info.isAvailable()) {
                return NetworkType.NONE;
            }
            int type = info.getType();
            if (ConnectivityManager.TYPE_WIFI == type) {
                if (DEBUG_MOBILE) {
                    return NetworkType.MOBILE;
                } else {
                    return NetworkType.WIFI;
                }
            } else if (ConnectivityManager.TYPE_MOBILE == type) {
                TelephonyManager mgr = (TelephonyManager) context.getSystemService(
                        Context.TELEPHONY_SERVICE);
                switch(mgr.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NetworkType.MOBILE_3G;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NetworkType.MOBILE_4G;
                    default:
                        return NetworkType.MOBILE;
                }
            } else {
                return NetworkType.MOBILE;
            }
        } catch (Throwable e) {
            return NetworkType.MOBILE;
        }
    }

    /** get network access type */
    public static String getNetworkAccessType(Context context) {
        return getNetworkAccessType(getNetworkType(context));
    }

    public static String getNetworkAccessType(NetworkType nt) {
        String access = "";
        try {
            switch (nt) {
                case WIFI:
                    access = "wifi";
                    break;
                case MOBILE_2G:
                    access = "2g";
                    break;
                case MOBILE_3G:
                    access = "3g";
                    break;
                case MOBILE_4G:
                    access = "4g";
                    break;
                case MOBILE:
                    access = "mobile";
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            // ignore
        }
        return access;
    }
}
