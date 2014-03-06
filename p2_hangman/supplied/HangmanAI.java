import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class HangmanAI
{
  private String filename = "dictionary.data";

  private ArrayList<Character> invalid_letters = new ArrayList();
  private ArrayList<String> dictionary = new ArrayList(180000);
  private ArrayList<String> possible_words = new ArrayList();

  public HangmanAI() {
    System.out.print("Running HangingCheater Constructor...initializing dictionary...");
    File localFile = null;
    Scanner localScanner = null;
    try
    {
      localFile = new File(filename);
      localScanner = new Scanner(localFile);
    } catch (FileNotFoundException localFileNotFoundException) {
      System.err.println("Error: Couldn't find " + filename);
      System.exit(1);
    }

    while (localScanner.hasNext()) {
      dictionary.add(localScanner.next());
    }

    System.out.println("..done!  Dictionary contains " + dictionary.size() + " elements");
  }

  public char makeGuess(String paramString1, String paramString2)
  {
    String str1 = paramString1;

    for (int i = 0; i < paramString2.length(); i++) {
      invalid_letters.add(Character.valueOf(paramString2.charAt(i)));
    }

    for (i = 0; i < paramString1.length(); i++) {
      invalid_letters.add(Character.valueOf(paramString1.charAt(i)));
    }

    for (Object localObject1 = dictionary.iterator(); ((Iterator)localObject1).hasNext(); ) { String str2 = (String)((Iterator)localObject1).next();

      if (str2.length() != str1.length()) {
        continue;
      }
      k = 1;

      for (int m = 0; m < str1.length(); m++)
      {
        if ((str1.charAt(m) != '-') && (str2.charAt(m) != str1.charAt(m)))
        {
          k = 0;
          break;
        }
        if ((str1.charAt(m) != '-') || (!invalid_letters.contains(Character.valueOf(str2.charAt(m)))))
          continue;
        k = 0;
        break;
      }

      if (k != 0) {
        possible_words.add(str2);
      }

    }

    localObject1 = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    int j = -1;
    int k = 32;

    for (int i2 : localObject1) {
      int i3 = 0;
      for (String str3 : possible_words) {
        for (int i4 = 0; i4 < str3.length(); i4++) {
          if ((str1.charAt(i4) == '-') && (str3.charAt(i4) == i2)) {
            i3++;
          }
        }
      }
      if ((i3 > 0) && 
        (i3 <= j)) continue;
      j = i3;
      k = i2;
    }

    return k;
  }
}

/* Location:           D:\Vincent Lee\Desktop\New folder (2)\CSCI1302 Soft Development\Assignments\P2 Hangman\
 * Qualified Name:     HangmanAI
 * JD-Core Version:    0.6.0
 */