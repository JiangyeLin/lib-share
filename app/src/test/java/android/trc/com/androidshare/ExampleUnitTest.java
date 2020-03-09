package android.trc.com.androidshare;

import android.net.Uri;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String url1 = "http://www.baidu.com/skk";
        System.out.println(Uri.parse(url1));

    }
}