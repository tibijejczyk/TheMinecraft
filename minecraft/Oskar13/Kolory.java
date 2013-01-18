package Oskar13;

import java.util.Map;
import java.util.regex.Pattern;


import java.util.HashMap; 

public enum Kolory {
  
    czarny('0', 0x00),

    c_niebieski('1', 0x1),
  
    c_zielony('2', 0x2),

    c_aqua('3', 0x3),
 
    c_czerwony('4', 0x4),
 
   c_fiolet('5', 0x5),

    zloty('6', 0x6),
  
    szary('7', 0x7),
  
    c_szary('8', 0x8),
 
    niebieski('9', 0x9),

    zielony('a', 0xA),

    AQUA('b', 0xB),

    czerwony('c', 0xC),

    jasny_fiolet('d', 0xD),

   zolty('e', 0xE),

   bialy('f', 0xF),

    MAGIC('k', 0x10, true),

    BOLD('l', 0x11, true),

    STRIKETHROUGH('m', 0x12, true),

    UNDERLINE('n', 0x13, true),

    ITALIC('o', 0x14, true),

    oskar('g', 0x14, true),
    
    RESET('r', 0x15);


    public static final char COLOR_CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");

    private final int intCode;
    private final char code;
    private final boolean isFormat;
    private final String toString;
   private final static Map<Integer, Kolory> BY_ID = new HashMap();

    //private final static Map<Character, kolory> BY_CHAR = Maps.newHashMap();
    private final static Map<Character, Kolory> BY_CHAR = new HashMap();
    
    
    private Kolory(char code, int intCode) {
        this(code, intCode, false);
    }

    private Kolory(char code, int intCode, boolean isFormat) {
        this.code = code;
        this.intCode = intCode;
        this.isFormat = isFormat;
        this.toString = new String(new char[] {COLOR_CHAR, code});
    }

 
    public char getChar() {
        return code;
    }

    @Override
    public String toString() {
        return toString;
    }

    public boolean isFormat() {
        return isFormat;
    }

 
    public boolean isColor() {
        return !isFormat && this != RESET;
    }


    public static Kolory getByChar(char code) {
        return BY_CHAR.get(code);
    }



    public static Kolory getByChar(String code) {
        return BY_CHAR.get(code.charAt(0));
    }


    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }

        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
    

	public static Kolory getByCode(final int code) {
		return BY_ID.get(code);
	}


    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
                b[i] = Kolory.COLOR_CHAR;
                b[i+1] = Character.toLowerCase(b[i+1]);
            }
        }
        return new String(b);
    }


    public static String getLastColors(String input) {
        String result = "";
        int lastIndex = -1;
        int length = input.length();

        while ((lastIndex = input.indexOf(COLOR_CHAR, lastIndex + 1)) != -1) {
            if (lastIndex < length - 1) {
                char c = input.charAt(lastIndex + 1);
                Kolory col = getByChar(c);

                if (col != null) {
                    if (col.isColor()) {
                        result = col.toString();
                    } else if (col.isFormat()) {
                        result += col.toString();
                    }
                }
            }
        }

        return result;
    }

    static {
        for (Kolory color : values()) {
            BY_ID.put(color.intCode, color);
            BY_CHAR.put(color.code, color);
        }
    }
}