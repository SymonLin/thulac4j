package io.github.yizhiru.thulac4j.common;

/**
 * @author linjian
 * @date 2019/1/8
 */
public enum LexiconEnum {
    /**
     * 词典枚举
     */
    disease("疾病", "ill", "disease.dict"),
    drug("药品", "drug", "drug.dict"),
    hospital("医院", "hs", "hospital.dict"),
    inspection("检查检验", "ex", "inspection.dict"),
    operation("手术", "op", "operation.dict"),
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
