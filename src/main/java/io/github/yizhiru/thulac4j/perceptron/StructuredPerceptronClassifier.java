package io.github.yizhiru.thulac4j.perceptron;

import io.github.yizhiru.thulac4j.term.AnnotatedTerms;
import io.github.yizhiru.thulac4j.term.POC;


public final class StructuredPerceptronClassifier {

    /**
     * Null previous label.
     */
    private static final int NULL_PREVIOUS_LABEL = -5;

    /**
     * Initial score.
     */
    private static final int INITIAL_SCORE = 0;

    /**
     * Initial previous label.
     */
    private static final int INITIAL_PREVIOUS_LABEL = -1;

    /**
     * SP 模型.
     */
    private StructuredPerceptronModel model;


    public StructuredPerceptronClassifier(StructuredPerceptronModel model) {
        this.model = model;
    }

    /**
     * 解码路径节点
     */
    private static class PathNode {
        /**
         * Score.
         */
        private int score;

        /**
         * Previous Label.
         */
        private int previousLabel;

        public PathNode() {
            score = INITIAL_SCORE;
            previousLabel = NULL_PREVIOUS_LABEL;
        }

        @Override
        public String toString() {
            return score + ", " + previousLabel;
        }
    }

    /**
     * 结构感知器分类，采用Viterbi算法解码
     *
     * @param annotatedTerms     规则处理后的句子Label 类
     * @param previousTransition 前向转移label
     * @return 最优路径对应的label索引值
     */
    public int[] classify(
            AnnotatedTerms annotatedTerms,
            int[][] previousTransition) {
        int len = annotatedTerms.getAnnotatedLength();
        // 最优路径对应的label
        int[] bestPath = new int[len];
        int labelSize = model.labelSize;
        int optimalLastScore = Integer.MIN_VALUE;
        int optimalLastLabel = 2;
        PathNode node;
        // 记录在位置i时类别为y的最优路径
        // [current index][current Label] -> PathNode(score, previousLabel)
        PathNode[][] pathTabular = new PathNode[len][];
        for (int i = 0; i < len; i++) {
            pathTabular[i] = new PathNode[labelSize];
            for (int j = 0; j < labelSize; j++) {
                pathTabular[i][j] = new PathNode();
            }
        }

        char[] chars = annotatedTerms.appendBoundaryAround();
        POC[] pocs = annotatedTerms.getPocs();

        // DP求解
        for (int i = 0; i < len; i++) {
            int[] labelIndices = model.allowTabular[pocs[i].ordinal()];
            int[] weights = model.evaluateCharWeights(
                    chars[i],
                    chars[i + 1],
                    chars[i + 2],
                    chars[i + 3],
                    chars[i + 4],
                    labelIndices);
            for (int labelIndex : labelIndices) {
                node = pathTabular[i][labelIndex];
                if (i == 0) {
                    node.previousLabel = INITIAL_PREVIOUS_LABEL;
                } else {
                    int[] preLabels = previousTransition[labelIndex];
                    for (int pre : preLabels) {
                        if (pathTabular[i - 1][pre].previousLabel == NULL_PREVIOUS_LABEL) {
                            continue;
                        }
                        int score = pathTabular[i - 1][pre].score
                                + model.llWeights[pre * model.labelSize + labelIndex];
                        if (node.previousLabel == NULL_PREVIOUS_LABEL || score > node.score) {
                            node.score = score;
                            node.previousLabel = pre;
                        }
                    }
                }
                node.score += weights[labelIndex];
                if (i == len - 1 && optimalLastScore < node.score) {
                    optimalLastScore = node.score;
                    optimalLastLabel = labelIndex;
                }
            }
        }
        // 尾节点的最优label
        node = pathTabular[len - 1][optimalLastLabel];
        bestPath[len - 1] = optimalLastLabel;
        // 回溯最优路径，保留label到数组
        for (int i = len - 2; i >= 0; i--) {
            bestPath[i] = node.previousLabel;
            node = pathTabular[i][node.previousLabel];
        }
        return bestPath;
    }

    /**
     * 得到所有label.
     */
    public String[] getLabelValues() {
        return model.labelValues;
    }
}
