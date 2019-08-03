package com.flamingo;

import static org.junit.Assert.assertTrue;

import com.flamingo.util.CompareUtils;
import org.junit.Test;

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
        Map<String,String> result = compareUtils.getCompareTexts("12345", "13245567");
        System.out.println("<div style='width:300px; height:30px'>" + result.get("original") + "</div>");
        System.out.println("<div style='width:300px; height:30px; float:left'>" + result.get("target") + "</div>");


        float similarity = compareUtils.getSimiliratyDegree("12345","123555");

        System.out.println(similarity);
    }
}
