import java.io.Reader;
import java.util.Scanner;
import java.util.HashMap;
import java.util.regex.Pattern;

/* * * * * * * * * * *
 *                   *
 * Lexer             *
 *                   *
 * * * * * * * * * * *
 *
 * A simple Lexical Analyzer which produces
 * a stream of tokens for a hypothetical 
 * programming language.
 * 
 *
 * calls to nextToken return syntactic categories
 * of a provided lexeme (as string) 
 *
 */
public class Lexer {
  // syntactic categories
  public static enum Category {
    EQ     ("=="),
    GTEQ   (">="),
    LTEQ   ("<="),
    GT     (">"),
    LT     ("<"),
    ARROW  ("->"),
    MAPS   ("=>"),
    PLUS   ("+"),
    MINUS  ("-"),
    STAR   ("*"),
    SLASH  ("/"),
    ASSIGN ("="),
    LPAR   ("("),
    RPAR   (")"),
    SEMI   (";"),
    COMMA  (","),
    IF     ("if"),
    ELSE   ("else"),
    ELIF   ("elif"),
    WHILE  ("while"),
    FOR    ("for"),
    TYPE   ("type"),
    IDENT  (null),
    NUMERAL(null),
    EOF    (null),
    ERROR  (null);

    final private String lexeme;

    Category(String s) { lexeme = s; }
  }

  public String lastLexeme;
  
  // maps tokens to corresponding syntactic categories
  private static HashMap<String, Category>
      tokenMap = new HashMap<String, Category>();
  
  static {
    for (Category c : Category.values())
      tokenMap.put(c.lexeme, c);
  }

  // input source
  private Scanner input;

  // pattern matches next token or erroneous char/eof
  // groups 1-3 are whitespace, identifiers, numerals
  private static final Pattern tokenPat
      = Pattern.compile(  "(\\s+|#.*)"
                        + "|>=|<=|-->|if|def|else|fi|while"
                        + "|([a-zA-Z][a-zA-Z0-9]*|(\\d+)"
                        + "|.");

  public Lexer(Reader reader) {
    input = new Scanner(reader);
  }

  // call repeatedly
  public Category nextToken() {
    if (input.findWithinHorizon(tokenPat, 0) == null)
      return Category.EOF;

    else {
      lastLexeme = input.match().group(0);

      if (input.match().start(1) != -1)
        return nextToken();

      else if (input.match().start(2) != -1)
        return Category.IDENT;

      else if (input.match().start(3) != -1)
        return Category.NUMERAL;
      
      Category result = tokenMap.get(lastLexeme);

      if (result == null)
        return Category.ERROR;
      else
        return result;
    }
  }
}
