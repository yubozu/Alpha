package cn.ac.ict.alpha.Utils;

/**
 * Created by yxd on 18/11/2016.
 */

public class BaseResult {
    int type;
    String resultPath;
    float score;
    public BaseResult(String resultPath, float score, int type) {
        this.resultPath = resultPath;
        this.score = score;
        this.type = type;
    }

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
