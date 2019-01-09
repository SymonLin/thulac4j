package io.github.yizhiru.thulac4j.common;

/**
 * @author linjian
 * @date 2019/1/9
 */
public enum POSEnum {
    /**
     * 词性枚举
     */
    N("n", "名词"),
    NP("np", "人名"),
    NS("ns", "地名"),
    NI("ni", "机构名"),
    NZ("nz", "其它专名"),
    M("m", "数词"),
    Q("q", "量词"),
    T("t", "时间词"),
    F("f", "方位词"),
    S("s", "处所词"),
    V("v", "动词"),
    A("a", "形容词"),
    D("d", "副词"),
    H("h", "前接成分"),
    K("k", "后接成分"),
    I("i", "习语"),
    J("j", "简称"),
    R("r", "代词"),
    C("c", "连词"),
    P("p", "介词"),
    U("u", "助词"),
    E("e", "叹词"),
    O("o", "拟声词"),
    G("g", "语素"),
    W("w", "标点"),
    X("x", "其它"),
    AGE("age", "年龄"),
    DOSE("dose", "剂量"),
    TI("ti", "时间点"),
    SIZE("size", "尺寸大小"),
    DISEASE("ill", "疾病"),
    DRUG("drug", "药品"),
    HOSPITAL("hs", "医院"),
    INSPECTION("ex", "检查检验"),
    OPERATION("op", "手术"),
    PERIOD("ti", "时刻"),
    ADVICE("advice", "医嘱"),
    SYM("sym", "症状"),
    ;

    private String key;

    private String desc;

    POSEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }
}
