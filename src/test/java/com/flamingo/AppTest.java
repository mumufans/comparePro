package com.flamingo;

import static org.junit.Assert.assertTrue;

import com.flamingo.util.CompareUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
        CompareUtils compareUtils = new CompareUtils();
        List<String> original = new ArrayList<>();
        List<String> target = new ArrayList<>();
        original.add("123456eert");
        original.add("wedqweq1232342");
        original.add("12345");
        original.add("abcde");

        target.add("123456wweer");
        target.add("wedqweq1232342");
        target.add("12");
        Map<String,String> result = compareUtils.getCompareTexts(original, target);
        System.out.println("<div style='width: 300px;float: left;border: 1px solid;'>" + result.get("original") + "</div>");
        System.out.println("<div style='width: 300px;float: left;border: 1px solid;'>" + result.get("target") + "</div>");


        float similarity = compareUtils.getSimiliratyDegree("12345","123555");

        System.out.println(similarity);
    }
}
