package word;

import java.io.*;
import java.util.*;

public class Dictionary {
    private Map<String, Word> dictionary;
    private  String filename ;

    public Dictionary() {
       this.dictionary = new HashMap<>();
       this.filename = null;
    }
    public Dictionary(String filename){
        ValidationUtils.checkNullOrEmpty(filename,"Имя файла не может быть пустым или null");
        this.filename = filename+".map";
        this.dictionary = loadDictionaryFromFile();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private Map<String,Word> loadDictionaryFromFile(){
        if(!new File(this.filename).exists()) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.filename))){
            Object object = ois.readObject();
            if(object instanceof Map) return (Map<String, Word>) object;
            else System.out.println("Ошибка: содержимое файла не является ожидаемым типом данных. " +
                                    "Загружается пустая коллекция.");
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: "+ e.getMessage());
        }catch (ClassNotFoundException e){
            System.out.println("Объект в файле не найден или его класс неизвестен: "+e.getMessage());
        }catch (ClassCastException e){
            System.out.println("Некорректный формат данных в файле: "+e.getMessage());
        }
        return new HashMap<>();
    }
    public boolean save(){
       // if(this.filename==null) throw new IllegalStateException("Не задано имя файла");
        if(this.filename==null) return false;
        saveToFile(this.filename);
        return true;
    }
    public boolean saveAs(String filename){
        ValidationUtils.checkNullOrEmpty(filename,"Имя файла не может быть пустым или null");
        if(dictionary==null) return false;
        return saveToFile(filename);
    }
    private boolean saveToFile(String path){
        try(ObjectOutputStream oop = new ObjectOutputStream(new FileOutputStream(path))){
            oop.writeObject(this.dictionary);
            setFilename(path+".map");
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean addWordToDictionary(String word, String translations){
        ValidationUtils.checkNullOrEmpty(word,"Слово не может быть пустым или null");
        ValidationUtils.checkNullOrEmpty(translations,"Перевод не может быть пустым или null");
        if(!dictionary.containsKey(word)){
            dictionary.put(word, new Word(word,translations));
            return true;
        }else System.out.println("Слово - "+word+" уже есть в словаре.");
        return false;
    }
    public boolean removeWord(String word){
        ValidationUtils.checkNullOrEmpty(word,"Слово не может быть пустым или null");
        //System.out.println(dictionary.remove(word)!=null ? "Слово "+word+" удалено." : word+"нет в словаре");
        return dictionary.remove(word)!=null;
    }
    public boolean replaceWord(String oldWord,String newWord){
        ValidationUtils.checkNullOrEmpty(oldWord,"Слово не может быть пустым или null");
        ValidationUtils.checkNullOrEmpty(newWord,"Слово не может быть пустым или null");
        if(dictionary.containsKey(oldWord)){
            dictionary.put(newWord,dictionary.remove(oldWord));
            dictionary.get(newWord).setWord(newWord);
            return true;
        }
        return false;
    }
    public boolean addTranslations(String word,String translations){
        ValidationUtils.checkNullOrEmpty(word,"Слово не может быть пустым или null");
        ValidationUtils.checkNullOrEmpty(translations,"Перевод не может быть пустым или null");
        if(dictionary.containsKey(word)) {
            dictionary.get(word).addTranslations(translations);
            return true;
        }
        return false;
    }
    public boolean removeTranslation(String word,String translation){
        ValidationUtils.checkNullOrEmpty(word,"Слово не может быть пустым или null");
        ValidationUtils.checkNullOrEmpty(translation,"Перевод не может быть пустым или null");
        if(dictionary.containsKey(word)){
            dictionary.get(word).removeTranslation(translation);
            return true;
        }
        return false;
    }
    public boolean replaceTranslation(String word,String oldTranslation,String newTranslation){
        ValidationUtils.checkNullOrEmpty(word,"Слово не может быть пустым или null");
        ValidationUtils.checkNullOrEmpty(oldTranslation,"Перевод не может быть пустым или null");
        ValidationUtils.checkNullOrEmpty(newTranslation,"Перевод не может быть пустым или null");
        if(dictionary.containsKey(word)){
            dictionary.get(word).removeTranslation(oldTranslation);
            dictionary.get(word).addTranslations(newTranslation);
            return true;
        }
        return false;
    }
    public void print(String word){
        ValidationUtils.checkNullOrEmpty(word,"Слово не может быть пустым или null");
        if(!dictionary.containsKey(word)){
            System.out.printf("Слова %s нет в словаре.",word);
            return;
        }
        if(dictionary.get(word).getTranslations().isEmpty()){
            System.out.printf("Слово %s не содержит перевода, добавьте перевод\n",word);
        }else System.out.println(dictionary.get(word).toString());
    }
    public void showTopWord(int limit,boolean mostPopular){
       if(dictionary.isEmpty()){
           System.out.println("Словарь пуст");
           return;
       }
       if(dictionary.size()<limit){
           System.out.println("Словарь содержит всего "+limit+" слов");
           limit=dictionary.size();
       }
        if(mostPopular) {
           System.out.printf("Топ %s самых популярных слов:\n",limit);
           dictionary.values().stream()
                   .sorted((a, b) -> Integer.compare(b.getCount(), a.getCount()))
                   .limit(limit)
                   .forEach(object -> System.out.println(object.getWord() + " (обращений: " + object.getCount() + ")"));
       }else {
           System.out.printf("Ton %s самых не популярныйх слов:\n",limit);
           dictionary.values().stream()
                   .sorted(Comparator.comparingInt(Word::getCount))
                   .limit(limit)
                   .forEach( object ->System.out.println(object.getWord() + " (обращений: " + object.getCount()+ ")"));
       }

    }

}
