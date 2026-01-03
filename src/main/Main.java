package main;

import word.Dictionary;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Dictionary dictionary;
    private static boolean exit = false;

    public static void main(String[] args){
        Map<Integer,Runnable> commands = new HashMap<>();
        try(Scanner scanner = new Scanner(System.in)) {
            loadCommands(commands,scanner);
            while (!exit) {
                menu();
                int key = getIntInput(scanner);
                if(commands.containsKey(key)){
                    commands.get(key).run();
                }else System.out.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
        System.out.println("До свидания!");
    }
    private static void menu(){
        final String MENU= """
                Меню:
                1. Создать пустой словарь
                2. Загрузить словарь
                3. Отобразить слово и его перевод
                4. Добавить слово и его перевод
                5. Заменить слово
                6. Удалить слово
                7. Добавить перевод к слову
                8. Заменить перевод
                9. Удалить перевод
                10. Топ популярных слов
                11. Топ не популярных слов
                12. Сохранить словарь
                13. Сохранить как
                14. Выход
                Выберите пункт:""";
        System.out.println(MENU);
    }
    private static void loadCommands(Map<Integer,Runnable>commands,Scanner scanner){
        commands.put(1,()->{
            dictionary = new Dictionary();
            System.out.println("Пустой словарь создан.");
        });
        commands.put(2,()->{
            scanner.nextLine();
            System.out.println("Введите название файла:");
            dictionary = new Dictionary(scanner.nextLine());
        });
        commands.put(3,()->{
            if(checkDictionary()){
                scanner.nextLine();
                System.out.println("Введите слово:");
                dictionary.print(scanner.nextLine());
            }
        });
        commands.put(4,()->{
            if(checkDictionary()){
                scanner.nextLine();
                System.out.println("Введите слово:");
                String word = scanner.nextLine();
                System.out.println("Введите перевод:");
                String translation = scanner.nextLine();
                if(dictionary.addWordToDictionary(word,translation))
                    System.out.println("Слово и его перевод добавлены в словарь");
            }
        });
        commands.put(5,()->{
            if(checkDictionary()){
                scanner.nextLine();
                System.out.println("Введите слово которое надо заменить:");
                String oldWord = scanner.nextLine();
                System.out.println("Введите слово для замены:");
                String newWord = scanner.nextLine();
                if(dictionary.replaceWord(oldWord,newWord))
                    System.out.printf("Произведена замена слова %s на слово %s\n",oldWord,newWord);
                else System.out.println(oldWord+" нет в словаре.");
            }
        });
        commands.put(6,()->{
            if(checkDictionary()){
                scanner.nextLine();
                System.out.println("Введите слово которое надо удалить");
                String word = scanner.nextLine();
                System.out.println(dictionary.removeWord(word) ? "Слово "+word+" удалено." : word+" нет в словаре");
            }
        });
        commands.put(7,()->{
            if(checkDictionary()){
                scanner.nextLine();
                System.out.println("Введите слово:");
                String word = scanner.nextLine();
                System.out.println("Введите перевод:");
                String translation = scanner.nextLine();
                System.out.println(dictionary.addTranslations(word,translation) ? "Перевод "+translation+" добавлен."
                                                                                : word+" нет в словаре.");
            }
        });
        commands.put(8,()->{
            if(checkDictionary()){
                scanner.nextLine();
                System.out.println("Введите слово:");
                String word = scanner.nextLine();
                System.out.println("Введите перевод коротый необходимо заменить :");
                String oldTranslation = scanner.nextLine();
                System.out.println(("Введите перевод для замены:"));
                String newTranslation = scanner.nextLine();
                switch (dictionary.replaceTranslation(word,oldTranslation,newTranslation)){
                    case 1 -> System.out.println("Перевод \""+oldTranslation+"\" заменен на \""+newTranslation+"\"");
                    case 0 -> System.out.println("Данный перевод \""+oldTranslation+"\" не найден");
                    case -1 -> System.out.println("\""+word+"\" нет в словаре.");
                }
            }
        });
        commands.put(9,()->{
            if(checkDictionary()){
                scanner.nextLine();
                System.out.println("Введите слово:");
                String word = scanner.nextLine();
                System.out.println("Введите перевод коротый необходимо удалить:");
                String translation = scanner.nextLine();
                switch (dictionary.removeTranslation(word,translation)){
                    case 1 -> System.out.println("Перевод \""+translation+"\" удален.");
                    case 0 -> System.out.println("Данный перевод \""+translation+"\" не найден");
                    case -1 -> System.out.println("\""+word+"\" нет в словаре.");
                }
            }
        });
        commands.put(10,()->{
            if(checkDictionary()){
                scanner.nextLine();
                System.out.println("Введите количество слов для вывода:");
                dictionary.showTopWord(getIntInput(scanner),true);
            }
        });
        commands.put(11,()->{
            if(checkDictionary()){
                scanner.nextLine();
                System.out.println("Введите количество слов для вывода:");
                dictionary.showTopWord(getIntInput(scanner),false);
            }
        });
        commands.put(12,()->{
            if(checkDictionary()){
                scanner.nextLine();
                if(dictionary.save()) System.out.println("Словарь сохранен.");
                else {
                    System.out.println("Введите имя файла:");
                    if(dictionary.saveAs(scanner.nextLine()))
                        System.out.println("Словарь сохранен в файл " +dictionary.getFilename() );
                }
            }
        });
        commands.put(13,()->{
           if(checkDictionary()){
               scanner.nextLine();
               System.out.println("Введите имя файла:");
               if(dictionary.saveAs(scanner.nextLine()))
                   System.out.println("Словарь сохранен в файл " +dictionary.getFilename() );
           }
        });
        commands.put(14,()->{
            exit=true;
        });
    }
    private static int getIntInput(Scanner scanner){
        while (true){
            try{
                return scanner.nextInt();
            }catch (InputMismatchException e){
                System.out.println("Некорректный ввод. Пожалуйста введите число.");
                scanner.next();
            }
        }
    }
    private static boolean checkDictionary(){
        if(dictionary==null){
            System.out.println("Словарь не создан. Создайте его или загрузите.");
            return false;
        }
        return true;
    }

}
