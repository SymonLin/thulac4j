package io.github.yizhiru.thulac4j;

import io.github.yizhiru.thulac4j.term.TokenItem;
import io.github.yizhiru.thulac4j.util.LabelerUtils;
import io.github.yizhiru.thulac4j.util.TokenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linjian
 * @date 2019/1/8
 */
public final class SentenceHandler {

    public static List<TokenItem> sentenceTagging(String text) {
        if (null == text || text.isEmpty()) {
            return new ArrayList<>();
        }
        String[] sentenceArr = text.split("。|；|;|!|！");
        List<TokenItem> list = new ArrayList<>();
        for (String sentence : sentenceArr) {
            List<TokenItem> tokenItemList = LabelerUtils.labeler.tagging(
                    sentence.replaceAll("[`~@#$^&=|{}''\\[\\]<>?~@#￥……& ;|{}【】‘；”“’？|]", "")
            );
            list.addAll(TokenUtils.merge(tokenItemList));
        }
        return list;
    }

    public static void main(String[] args) {
        String text = "1、于2018年10月5号，2016年5月4号体检发现甲状腺结节B超报告提示：左侧腺体内见一大小4.0mm*3.0mm低回声结节，边界清。（质地、活动度、压痛不详）建议定期复查。2017年体检发现左侧甲状腺结节（大小、质地、活动度、压痛不详）。2、2018年8月体检B超提示：左侧腺体内见一个低回声结节，大小约6.0mm*5.0mm，（质地、活动度、压痛不详）边界欠清，内可见点状强回声。浙二医生建议穿刺，患者穿刺前行血常规、凝血功能检查，无殊，2018年8月至浙二行穿刺提示：（左侧甲状腺结节FNA）甲状腺乳头状癌。3、2018年8月至邵逸夫医院行细针穿刺细胞学检查，提示（左颈IV区结节针吸）见淋巴细胞，未见明确肿瘤依据。医生建议手术。4.患者采纳，预约手术，北京市";
        System.out.println(sentenceTagging(text));
    }
}
