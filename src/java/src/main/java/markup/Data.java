package markup;

import java.util.List;
import java.util.Map;

/**
 * @author jld
 */
public class Data {

    Explanation explanation;
    QA qa;
    int class_val = -1;

    public Data(Explanation e, QA qa) {
        this.explanation = e;
        this.qa = qa;
    }

    public void setClassVal(int v) {
        this.class_val = v;
    }

}
