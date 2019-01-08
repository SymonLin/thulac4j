package io.github.yizhiru.thulac4j.util;

import io.github.yizhiru.thulac4j.POSTagger;
import io.github.yizhiru.thulac4j.common.LexiconEnum;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linjian
 * @date 2019/1/8
 */
public class LabelerUtils {

    private static final String POS_TAGGING_MODEL_BIN_PATH = "/models/model_c_model.bin";
    private static final String POS_TAGGING_DAT_BIN_PATH = "/models/model_c_dat.bin";
    private static final String EXTRA_DICT_PATH = "/dictionary/";

    public static POSTagger labeler;

    static {
        labeler = init();
    }

    private static POSTagger init() {
        POSTagger pos = new POSTagger(LabelerUtils.class.getResourceAsStream(POS_TAGGING_MODEL_BIN_PATH),
                LabelerUtils.class.getResourceAsStream(POS_TAGGING_DAT_BIN_PATH));
        for (LexiconEnum lexiconEnum : LexiconEnum.values()) {
            List<String> wordList = loadDictionary(lexiconEnum.getFile());
            pos.addCustomWords(wordList, lexiconEnum.getPos());
        }
        return pos;
    }

    private static List<String> loadDictionary(String file) {
        InputStream inputStream = LabelerUtils.class.getResourceAsStream(EXTRA_DICT_PATH + file);
        List<String> wordList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String s;
            while ((s = reader.readLine()) != null) {
                wordList.add(s);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wordList;
    }
}
