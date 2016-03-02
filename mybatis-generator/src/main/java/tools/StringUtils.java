package tools;

/**
 * Created by Beeant on 2016/2/13.
 */
public class StringUtils extends org.springframework.util.StringUtils {
    public static String lowerString(String string, int start, int end){
        if(start != 0){
            return string.substring(0,start).concat(string.substring(start,end).toLowerCase()).concat(string.substring(end));
        }
        return string.substring(start,end).toLowerCase().concat(string.substring(end));
    }

    public static String upperString(String string, int start, int end){
        if(start != 0){
            return string.substring(0,start).concat(string.substring(start,end).toUpperCase()).concat(string.substring(end));
        }
        return string.substring(start,end).toUpperCase().concat(string.substring(end));
    }
}
