package io.github.yizhiru.thulac4j.common;

/**
 * @author linjian
 * @date 2019/1/8
 */
public enum LexiconEnum {
    /**
     * 词典枚举
     */
    DISEASE("疾病", "ill", "disease.dict"),
    DRUG("药品", "drug", "drug.dict"),
    HOSPITAL("医院", "hs", "hospital.dict"),
    INSPECTION("检查检验", "ex", "inspection.dict"),
    OPERATION("手术", "op", "operation.dict"),
    PERIOD("时刻", "ti", "period.dict"),
    DOSE("剂量", "dose", "dose.dict"),
    ADVICE("医嘱", "advice", "advice.dict"),
    SYMPTOM("症状", "sym", "symptom.dict"),
    ;

    private String name;

    private String pos;

    private String file;

    LexiconEnum(String name, String pos, String file) {
        this.name = name;
        this.pos = pos;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public String getPos() {
        return pos;
    }

    public String getFile() {
        return file;
    }
}
