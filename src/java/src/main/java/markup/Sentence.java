package markup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jld
 */
public class Sentence {

    List<Token> tokens;
    List<String> inBetweenPatterns;

    public Sentence() {
        tokens = new ArrayList<>();
        inBetweenPatterns = new ArrayList<>();
    }

    public void setTokens(String[] line) {
        for (String token : line) {
            //System.out.println(token);
            String[] elements = token.split("/");
            //System.out.println(elements.length);
            //System.out.println(elements[0]+" "+elements[1]+" "+elements[2]+" "+elements[4]+" "+elements[5]);
            Token t = new Token(elements[0], elements[1], elements[2], elements[4], elements[5].equals("null") ? null : Double.parseDouble(elements[5]));
            if (elements.length == 7) {
                t.setDepRel(elements[6].split("\\|"), false);
            }
            if (elements.length == 8) {
                t.setDepRel(elements[7].split("\\|"), true);
            }
            tokens.add(t);
        }
        setInBetweenPatterns();
    }

    public void setInBetweenPatterns() {
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.getLemma().equals("be")) setInBetweenPattern(i);
        }
    }

    public void setInBetweenPattern(int i) {
        String inBetweenPattern = "";
        Token t = tokens.get(i);
        int index = i;
        while (!t.getPos().equals("IN") && index < i+4 && index < tokens.size()) {
            t = tokens.get(index);
            inBetweenPattern += t.getLemma()+" ";
            index++;
        }
        inBetweenPattern = inBetweenPattern.trim();
        if (!inBetweenPatterns.contains(inBetweenPattern)) inBetweenPatterns.add(inBetweenPattern);
    }

    public List<String> getInBetweenPatterns() {
        return inBetweenPatterns;
    }

    public List<Token> getTokens() {
        return tokens;
    }

}
