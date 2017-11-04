package com.enjoyf.platform.util;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

//TODO: we need chinese word segment algorithm for better performance
public class WordFilterUtil {

    private static final long EXPIRE_INTERVAL = 1000 * 1800; // 30 mins

    private static List<Pattern> wordPatterns;
    private static Set<String> wordSet;
    private static volatile long lastUpdated;

    private WordFilterUtil() {
    }

    private static synchronized void update() {
//        if (wordPatterns == null || System.currentTimeMillis() - lastUpdated > EXPIRE_INTERVAL) {
//            try {
//                Set<String> forbiddenWords = new HashSet<String>();
//                List<ForbiddenWord> temp = MiscServiceSngl.get().getForbiddenWords(null, true, null);
//                for (ForbiddenWord w : temp) {
//                    forbiddenWords.add(w.getWord());
//                }
//                forbiddenWords = Collections.unmodifiableSet(forbiddenWords);
//                List<Pattern> patterns = new ArrayList<Pattern>();
//                for (String word : forbiddenWords) {
//                    if (word.startsWith("(?i)")) { //automatically add case insensitive hint
//                        patterns.add(Pattern.compile(word));
//                    } else {
//                        patterns.add(Pattern.compile("(?i)" + word));
//                    }
//                }
//                wordPatterns = patterns;
//                wordSet = forbiddenWords;
//            } catch (ServiceException e) {
//                GAlerter.lab("cannot get forbidden words from misc service", e);
//                if (wordPatterns == null) {
//                    wordPatterns = new ArrayList<Pattern>(0);
//                }
//                if (wordSet == null) {
//                    wordSet = new HashSet<String>(0);
//                }
//            }
//            lastUpdated = System.currentTimeMillis();
//        }
    }

    public static Set<String> getForbiddenWords() {
        update();
        return wordSet;
    }

    public static String filter(String destWord) {
        if (StringUtil.isEmpty(destWord)) {
            return destWord;
        }

        update();

        for (Pattern pat : wordPatterns) {
            destWord = pat.matcher(destWord).replaceAll("*");
        }

        return destWord;
    }

    public static boolean isValid(String keyword) {
        if (StringUtil.isEmpty(keyword)) {
            return false;
        }
        int len = keyword.length();
        if (len < 2 || len > 30) {
            return false;
        }

        update();

        for (Pattern pat : wordPatterns) {
            if (pat.matcher(keyword).matches()) {
                return false;
            }
        }

        return true;
    }

}
