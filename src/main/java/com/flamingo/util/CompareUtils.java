package com.flamingo.util;

import com.flamingo.domain.EditDistanceInfo;

import java.util.*;

public class CompareUtils {

    /**
     * 比对两行文字并返回比对后的文本，不同的字符标红
     * @param originalLine
     * @param targetLine
     * @return
     */
    public Map<String, String> getCompareTexts(String originalLine, String targetLine){
        Map<String, String> result = new HashMap<String, String>(2);
//        if(originalLine == null && targetLine == null){
//            result.put("original", "");
//            result.put("target","");
//            return result;
//        } else if (originalLine == null){
//            result.put("original", "");
//            result.put("target",targetLine);
//            return result;
//        } else if (targetLine == null){
//            result.put("original", originalLine);
//            result.put("target","");
//            return result;
//        } else if(originalLine.equals(targetLine)){
//            result.put("original", originalLine);
//            result.put("target",targetLine);
//            return result;
//        }
        int d[][] = compare(originalLine, targetLine);
        //获得编辑距离的编辑路径链表
        List<Integer> path = getEditPath(d);
        int a = 0;
        int b = 0;
        StringBuilder originalText = new StringBuilder();
        StringBuilder targetText = new StringBuilder();
        boolean originalFlag = false;
        boolean targetFlag = false;
        for(int m : path){
            if(m == 0){
                if(!targetFlag){
                    targetText.append("<span style='color:red'>");
                    targetFlag = true;
                }
                targetText.append(targetLine.charAt(b));
                b++;
            }
            if(m == 1){
                if(!originalFlag){
                    originalText.append("<span style='color:red'>");
                    originalFlag = true;
                }
                originalText.append(originalLine.charAt(a));
                a++;
            }
            if(m == 2){
                if(targetLine.charAt(b) == originalLine.charAt(a)){
                    if(targetFlag){
                        targetText.append("</span>");
                        targetFlag = false;
                    }
                    if(originalFlag){
                        originalText.append("</span>");
                        originalFlag = false;
                    }
                } else {
                    if(!targetFlag){
                        targetText.append("<span style='color:red'>");
                        targetFlag = true;
                    }
                    if(!originalFlag){
                        originalText.append("<span style='color:red'>");
                        originalFlag = true;
                    }
                }
                targetText.append(targetLine.charAt(b));
                b++;
                originalText.append(originalLine.charAt(a));
                a++;
            }
        }
        if(targetFlag){
            targetText.append("</span>");
        }
        if(originalFlag){
            originalText.append("</span>");
        }
        result.put("original", originalText.toString());
        result.put("target", targetText.toString());
        return result;

    }

    /**
     * 比对两行文字并返回编辑距离矩阵
     * @param originalLine
     * @param targetLine
     * @return
     */
    public int[][] compare(String originalLine, String targetLine){
        int d[][];
        int m = originalLine.length();
        int n = targetLine.length();
        //初始化第一行和第一列
        d = initializeCompareArray(m, n);
        //逐行逐列计算编辑距离
        int temp;
        for(int i = 1; i <= m; i++){
            char a = originalLine.charAt(i - 1);
            for(int j = 1; j <= n; j++){
                char b = targetLine.charAt(j - 1);
                if(a == b){
                    temp = 0;
                } else {
                    temp = 1;
                }
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d;
    }

    /**
     * 获取两个字符串的相似度
     * @param originalLine
     * @param targetLine
     * @return
     */
    public float getSimiliratyDegree(String originalLine, String targetLine){
        int m = originalLine.length();
        int n = targetLine.length();
        int length = compare(originalLine, targetLine)[m][n];
        return 1 - (float)length / Math.max(m,n);
    }

    public Map<String, String> getCompareTexts(List<String> originalList, List<String> targetList){
        Map<String, String> result = new HashMap<>();
        EditDistanceInfo[][] d = compareForEdit(originalList, targetList);
        //获得编辑距离的编辑路径链表
        List<Integer> path = getEditPath(d);
        int a = 0;
        int b = 0;
        StringBuilder originalText = new StringBuilder();
        StringBuilder targetText = new StringBuilder();
        String style = "<style type='text/css'>tr{height:20px;}.edited{background-color:aqua;}.deleted{background-color:salmon}</style><table>";
        originalText.append(style);
        targetText.append(style);
        for(int m : path){
            if(m == 0){
                targetText.append("<tr class='edited'><td>");
                targetText.append(targetList.get(b));
                targetText.append("</td></tr>");
                b++;
                originalText.append("<tr class='edited'><td></td></tr>");
            }

            if(m == 1){
                originalText.append("<tr class='deleted'><td>");
                originalText.append(originalList.get(a));
                originalText.append("</td></tr>");
                a++;
                targetText.append("<tr class='deleted'><td></td></tr>");
            }
            if(m == 2){
                EditDistanceInfo e = d[a + 1][b + 1];
                if(e.isTheSame()){
                    originalText.append("<tr class='normal'><td>");
                    originalText.append(originalList.get(a));
                    originalText.append("</td></tr>");
                    targetText.append("<tr class='normal'><td>");
                    targetText.append(targetList.get(b));
                    targetText.append("</td></tr>");
                } else {
                    Map<String,String> compareTexts = getCompareTexts(originalList.get(a), targetList.get(b));
                    originalText.append("<tr class='edited'><td>");
                    originalText.append(compareTexts.get("original"));
                    originalText.append("</td></tr>");
                    targetText.append("<tr class='edited'><td>");
                    targetText.append(compareTexts.get("target"));
                    targetText.append("</td></tr>");
                }
                a++;
                b++;
            }
        }
        originalText.append("</table>");
        targetText.append("</table>");
        result.put("original", originalText.toString());
        result.put("target", targetText.toString());

        return result;
    }

    /**
     * 比较两个字符串list，并返回编辑距离矩阵
     * @param originalList
     * @param targetList
     * @return
     */
    public int[][] compare(List<String> originalList, List<String> targetList){
        int d[][];
        int m = originalList.size();
        int n = targetList.size();
        //初始化第一行第一列
        d = initializeCompareArray(m, n);
        //逐行计算编辑距离
        int temp;
        for(int i = 1; i <= m; i++){
            String a = originalList.get(i - 1);
            for(int j = 1; j <= n; j++){
                String b = targetList.get(j - 1);
                if(a != null && a.equals(b)){
                    temp = 0;
                } else {
                    temp = 1;
                }
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d;
    }

    /**
     * 获取用于对比编辑的编辑距离矩阵，相似度大于0.4认定为编辑
     * @param originalList
     * @param targetList
     * @return
     */
    public EditDistanceInfo[][] compareForEdit(List<String> originalList, List<String> targetList){
        EditDistanceInfo d[][];
        int m = originalList.size();
        int n = targetList.size();
        //初始化第一行第一列
        d = new EditDistanceInfo[m + 1][n + 1];
        for(int i = 0; i <= m; i++){
            EditDistanceInfo e = new EditDistanceInfo(false);
            e.setEditDistance(i);
            d[i][0] = e;
        }
        for(int j = 0; j <= n; j++){
            EditDistanceInfo e = new EditDistanceInfo(false);
            e.setEditDistance(j);
            d[0][j] = e;
        }
        //逐行计算编辑距离
        int temp = 0;
        for(int i = 1; i <= m; i++){
            String a = originalList.get(i - 1);
            for(int j = 1; j <= n; j++){
                String b = targetList.get(j - 1);
                boolean ifTheSame = false;
                if(a != null && a.equals(b)){
                    temp = 0;
                    ifTheSame = true;
                }
                else if(getSimiliratyDegree(a, b) > 0.4){
                    temp = 0;
                }
                else {
                    temp = 1;
                }
                int editDistance  = min(d[i - 1][j].getEditDistance() + 1, d[i][j - 1].getEditDistance() + 1, d[i - 1][j - 1].getEditDistance() + temp);
                EditDistanceInfo e = new EditDistanceInfo(false);
                e.setEditDistance(editDistance);
                e.setTheSame(ifTheSame);
                d[i][j] = e;
            }
        }
        return d;
    }

    /**
     * 初始化编辑距离数组
     * @param m
     * @param n
     * @return
     */
    private int[][] initializeCompareArray(int m, int n){
        int[][] d = new int[m + 1][n + 1];
        for(int i = 0; i <= m; i++){
            d[i][0] = i;
        }
        for(int j = 0; j <= n; j++){
            d[0][j] = j;
        }
        return d;
    }
    /**
     * 比较三个整数并返回最小值
     */
    private int min(int a, int b, int c){
        return a > b ? (b > c ? c : b) : (a > c ? c : a);
    }

    /**
     * 根据编辑距离矩阵获取编辑路径--字符串比较
     * @param d
     * @return
     */
    private List<Integer> getEditPath(int[][] d){
        int i = d.length - 1;
        int j = d[0].length - 1;
        //定义获得编辑距离的编辑路径链表
        List<Integer> path = new LinkedList<Integer>();
        /**
         * 编辑路径选取逻辑：左 > 上 > 左上
         * 0 - 左
         * 1 - 上
         * 2 - 左上
         * 从后至前找寻路径，然后反转
         */
        while(i != 0 || j != 0){
            if(j != 0 && (d[i][j - 1] + 1) == d[i][j]){
                path.add(0);
                j--;
            } else if(i != 0 && (d[i - 1][j] + 1) == d[i][j]) {
                path.add(1);
                i--;
            } else {
                path.add(2);
                i--;
                j--;
            }
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * 根据编辑距离矩阵获取编辑路径--段落比较
     * @param d
     * @return
     */
    public List<Integer> getEditPath(EditDistanceInfo[][] d){
        int i = d.length - 1;
        int j = d[0].length - 1;
        //定义获得编辑距离的编辑路径链表
        List<Integer> path = new LinkedList<Integer>();
        /**
         * 编辑路径选取逻辑：左 > 上 > 左上
         * 0 - 左
         * 1 - 上
         * 2 - 左上
         * 从后至前找寻路径，然后反转
         */
        while(i != 0 || j != 0){
            if(j != 0 && (d[i][j - 1].getEditDistance() + 1) == d[i][j].getEditDistance()){
                path.add(0);
                j--;
            } else if(i != 0 && (d[i - 1][j].getEditDistance() + 1) == d[i][j].getEditDistance()) {
                path.add(1);
                i--;
            } else {
                path.add(2);
                i--;
                j--;
            }
        }
        Collections.reverse(path);
        return path;
    }
}
