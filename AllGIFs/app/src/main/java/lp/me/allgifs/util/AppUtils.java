package lp.me.allgifs.util;

import java.util.Random;

/**
 * Created by nxtruong on 7/5/2017.
 */

public class AppUtils {
    private final static String TRENDING_LINK_RIFFSY = "http://api.riffsy.com/v1/trending?key=ZRB4H2156Q76&limit=50";
    private final static String MORE_TRENDING_LINK_RIFFSY = "http://api.riffsy.com/v1/trending?key=ZRB4H2156Q76&pos=%s&limit=50";
    private final static String FIRST_LINK_RIFFSY = "http://api.riffsy.com/v1/search?key=ZRB4H2156Q76&limit=50&tag=%s";
    private final static String LOAD_MORE_LINK_RIFFSY = "http://api.riffsy.com/v1/search?key=ZRB4H2156Q76&pos=%s&limit=50&tag=%s";

    public static String getFirstLinkRiffsy(String tag) {
        return String.format(FIRST_LINK_RIFFSY, tag);
    }

    public static String getLoadMoreLinkRiffsy(String next, String tag) {

        return String.format(LOAD_MORE_LINK_RIFFSY, next, tag);
    }

    public static String getTrendingRiffsy() {
        return TRENDING_LINK_RIFFSY;
    }

    public static String getMoreTrendingLink(String next){
        return String.format(MORE_TRENDING_LINK_RIFFSY, next);
    }
    static Random rand = new Random();

    public static boolean isMoreLessShowAd() {
        int value = rand.nextInt(9);
        if (value> 6) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isLessShowAd() {
        int value = rand.nextInt(3);
        if (value == 2) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isShowAdnative() {
        int value = rand.nextInt(2);
        if (value == 1) {
            return true;
        } else {
            return false;
        }
    }

}