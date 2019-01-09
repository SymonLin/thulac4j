package io.github.yizhiru.thulac4j.util;

import io.github.yizhiru.thulac4j.common.POSEnum;
import io.github.yizhiru.thulac4j.term.TokenItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author linjian
 * @date 2019/1/9
 */
public final class TokenUtils {

    private final static Pattern FILTER_CHAR_PATTERN = Pattern.compile("\\d\\./m|「|」");

    private final static List<String> SIZE_POS = Arrays.asList(POSEnum.M.getKey(), POSEnum.Q.getKey());

    private final static List<String> CONNECTOR = Arrays.asList("：", ":", "*");

    public static List<TokenItem> merge(List<TokenItem> tokenItemList) {
        List<TokenItem> list = new ArrayList<>();
        int length = tokenItemList.size();
        int flag;
        for (int i = 0; i < length; i++) {
            TokenItem item = tokenItemList.get(i);
            // 过滤序号及特殊字符（类似「1.」）
            Matcher matcher = FILTER_CHAR_PATTERN.matcher(item.toString());
            if (matcher.find()) {
                continue;
            }
            // 合并年龄
            flag = mergeAge(tokenItemList, i, list);
            if (flag != i) {
                i = flag;
                continue;
            }
            // 合并剂量
            flag = mergeDose(tokenItemList, i, list);
            if (flag != i) {
                i = flag;
                continue;
            }
            // 合并时间
            flag = mergeDateTime(tokenItemList, i, list);
            if (flag != i) {
                i = flag;
                continue;
            }
            // 合并尺寸大小
            flag = mergeSize(tokenItemList, i, list);
            if (flag != i) {
                i = flag;
                continue;
            }
            list.add(item);
        }
        return list;
    }

    private static int mergeAge(List<TokenItem> tokenItemList, int i, List<TokenItem> list) {
        TokenItem item = tokenItemList.get(i);
        if (!POSEnum.M.getKey().equals(item.pos)) {
            return i;
        }
        int prev = i - 1;
        int next = i + 1;
        // 匹配「年龄xx」模式
        if (prev >= 0 && "年龄".equals(tokenItemList.get(prev).word)) {
            list.add(new TokenItem(item.word, POSEnum.AGE.getKey()));
            return i;
        }
        // 匹配「x岁x个月」模式
        String year = "";
        String month = "";
        if (next <= tokenItemList.size() && "岁".equals(tokenItemList.get(next).word)) {
            year = item.word + tokenItemList.get(next).word;
            i++;
        }
        if (next + 2 <= tokenItemList.size()
                && POSEnum.M.getKey().equals(tokenItemList.get(next + 1).pos)
                && "个月".equals(tokenItemList.get(next + 2).word)) {
            month = tokenItemList.get(next + 1).word + tokenItemList.get(next + 2).word;
            i += 2;
        }
        if (!year.isEmpty()) {
            list.add(new TokenItem(year + month, POSEnum.AGE.getKey()));
            return i;
        }
        return i;
    }

    private static int mergeDose(List<TokenItem> tokenItemList, int i, List<TokenItem> list) {
        TokenItem item = tokenItemList.get(i);
        if (!POSEnum.M.getKey().equals(item.pos)) {
            return i;
        }
        int next = i + 1;
        // 匹配「m+dose」模式，如5/m,ml/dose
        if (next <= tokenItemList.size() && POSEnum.DOSE.getKey().equals(tokenItemList.get(next).pos)) {
            list.add(new TokenItem(item.word + tokenItemList.get(next).word, POSEnum.DOSE.getKey()));
            i++;
            return i;
        }
        return i;
    }

    private static int mergeDateTime(List<TokenItem> tokenItemList, int i, List<TokenItem> list) {
        TokenItem item = tokenItemList.get(i);
        if (!POSEnum.T.getKey().equals(item.pos) && !POSEnum.M.getKey().equals(item.pos)) {
            return i;
        }
        int next = i + 1;
        // 匹配「t+f」模式，如5月/t,前/f
        if (POSEnum.T.getKey().equals(item.pos)
                && next <= tokenItemList.size()
                && POSEnum.F.getKey().equals(tokenItemList.get(next).pos)) {
            list.add(new TokenItem(item.word + tokenItemList.get(next).word, POSEnum.TI.getKey()));
            i++;
            return i;
        }
        // 匹配「t+t+t+...」模式，如2019年/t,01月/t,09日/t
        StringBuilder dateTime = new StringBuilder();
        while (POSEnum.T.getKey().equals(tokenItemList.get(i).pos)) {
            dateTime.append(tokenItemList.get(i).word);
            i++;
        }
        if (!dateTime.toString().isEmpty()) {
            list.add(new TokenItem(dateTime.toString(), POSEnum.T.getKey()));
            i--;
            return i;
        }
        // 匹配「m+ti」模式，如五/m,天前/ti
        if (POSEnum.M.getKey().equals(item.pos)
                && next <= tokenItemList.size()
                && POSEnum.TI.getKey().equals(tokenItemList.get(next).pos)) {
            list.add(new TokenItem(item.word + tokenItemList.get(next).word, POSEnum.TI.getKey()));
            i++;
            return i;
        }
        return i;
    }

    private static int mergeSize(List<TokenItem> tokenItemList, int i, List<TokenItem> list) {
        TokenItem item = tokenItemList.get(i);
        if (!POSEnum.M.getKey().equals(item.pos)) {
            return i;
        }
        // 匹配「m+q+w...」模式，如5.2/m,mm/q,*/w,3.6/m,mm/q
        StringBuilder size = new StringBuilder();
        for (int j = i + 1; j < tokenItemList.size(); j++) {
            TokenItem nextItem = tokenItemList.get(j);
            if (SIZE_POS.contains(nextItem.pos) || CONNECTOR.contains(nextItem.word)) {
                size.append(nextItem.word);
                i = j;
            } else {
                i = j - 1;
                break;
            }
        }
        if (!size.toString().isEmpty()) {
            list.add(new TokenItem(item.word + size.toString(), POSEnum.SIZE.getKey()));
            return i;
        }
        return i;
    }
}
