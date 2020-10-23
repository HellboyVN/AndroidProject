package hb.me.instagramsave.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Source {
    public static String getURL(String html, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public static ArrayList<String> getArrayURL(String html, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(html);
        ArrayList<String> stringArrayList = new ArrayList();
        while (matcher.find()) {
            stringArrayList.add(matcher.group(1));
        }
        return stringArrayList;
    }
}
