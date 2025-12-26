package word;

public final class ValidationUtils {
    private ValidationUtils(){}
    public static void checkNullOrEmpty(String input, String errorMessage){
        if(input==null || input.isEmpty()) throw new IllegalArgumentException(errorMessage);
    }
}
