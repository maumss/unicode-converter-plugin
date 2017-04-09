package br.com.yahoo.mau_mss.unicodeconverterplugin;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Title: UnicodeConverterPluginTest
 * Description:
 * Date: Jun 13, 2015, 10:35:37 PM
 *
 * @author Mauricio Soares da Silva (Mau)
 */
public class UnicodeConverterPluginTest {

  public UnicodeConverterPluginTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of actionPerformed method, of class UnicodeConverterPlugin.
   */
  @Test
  public void testToUnicode() {
    System.out.println("toUnicode");
    UnicodeConverterPlugin instance = new UnicodeConverterPlugin(null);
    String text = "A árvore que gosta do inverno é a maçã.";
    String expResult = "A \\u00e1rvore que gosta do inverno \\u00e9 a ma\\u00e7\\u00e3.";
    String result = instance.toUnicode(text);
    assertEquals(expResult, result);
  }

}
