package word;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Word {
    private String word;
    private Set<String> translations;
    private int count;
    public Word(String word,String translations){
       ValidationUtils.checkNullOrEmpty(word,"Слово не может быть пустым или null");
       ValidationUtils.checkNullOrEmpty(translations,"Перевод не может быть пустым или null");
        this.word = word;
        this.translations = new HashSet<>(Arrays.asList(convertToArray(translations)));
        this.count = 0;
    }
    private String [] convertToArray(String translations){
            return translations.replaceAll("\\s+","").split(",");
    }

    public void addTranslation(String translations){
       ValidationUtils.checkNullOrEmpty(translations,"Перевод не может быть пустым или null");
       this.translations.addAll(Arrays.asList(convertToArray(translations)));
    }
    public void removeTranslation(String translation){
        ValidationUtils.checkNullOrEmpty(translation,"Перевод не может быть пустым или null");
        if(this.translations.remove(translation)) System.out.println("Перевод: "+translation+" удален.");
        else System.out.println("Данный перевод: "+translation+" не найден.");
    }
    private void incrementCount(){
        count++;
    }

    @Override
    public String toString() {
        incrementCount();
        return String.format("Слово %s перевод %s",word,translations);
    }
}
