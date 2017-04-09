package br.com.yahoo.mau_mss.unicodeconverterplugin;

import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.api.java.source.CancellableTask;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * Title: UnicodeConverterPlugin
 * Description: Plugin para converter strings em Unicode
 * Date: Jun 13, 2015, 9:14:00 PM
 *
 * @author Mauricio Soares da Silva (mauricio.soares)
 * @see http://www.utf8-chartable.de/unicode-utf8-table.pl?utf8=dec
 */
@ActionID(
        category = "Tools",
        id = "br.com.yahoo.mau_mss.unicodeconverterplugin.UnicodeConverterPlugin"
)
@ActionRegistration(
        iconBase = "resources/unic_icon.png",
        displayName = "#CTL_UnicodeConverterPlugin"
)
@ActionReferences({
  @ActionReference(path = "Menu/Tools", position = 190),
  @ActionReference(path = "Editors/text/x-java/Popup", position = 1590)
})
@Messages("CTL_UnicodeConverterPlugin=Convert to Unicode")
public class UnicodeConverterPlugin implements ActionListener {
  private final DataObject dataObject;
  private static final Logger logger = Logger.getLogger(UnicodeConverterPlugin.class.getName());
  private static final int MAX_CARACTER_UNIVERSAL = 127;
  private static final long serialVersionUID = 1L;

  public UnicodeConverterPlugin(DataObject context) {
    this.dataObject = context;
  }

  @Override
  public void actionPerformed(ActionEvent ev) {
    if (this.dataObject == null) {
      return;
    }
    final FileObject fileObject = this.dataObject.getPrimaryFile();
    if (fileObject == null) {
      return;
    }
    JavaSource javaSource = JavaSource.forFileObject(fileObject);
    if (javaSource == null) {
      StatusDisplayer.getDefault().setStatusText("It isn't a Java file: " + fileObject.getPath());
      return;
    }
    try {
      javaSource.runUserActionTask(new ConverterTask(), true);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Erro ao converter elemento.", e);
      Exceptions.printStackTrace(e);
    }
  }

  protected String toUnicode(String s) {
    StringBuilder buf = new StringBuilder();
    for (char c : s.toCharArray()) {
      buf.append(toUnicode(c));
    }
    return buf.toString();
  }

  private String toUnicode(char c) {
    StringBuilder buf = new StringBuilder();
    if (c > UnicodeConverterPlugin.MAX_CARACTER_UNIVERSAL) {
      buf.append("\\u");
      buf.append(String.format("%04X", new Object[]{(int) c}).toLowerCase());
    } else {
      buf.append(c);
    }
    return buf.toString();
  }

  private class ConverterTask implements CancellableTask<CompilationController> {

    @Override
    public void cancel() {
    }

    @Override
    public void run(CompilationController compilationController) throws Exception {
      compilationController.toPhase(Phase.ELEMENTS_RESOLVED);
      Document document = compilationController.getDocument();
      if (document == null) {
        return;
      }
      JTextComponent editor = EditorRegistry.lastFocusedComponent();
      String selection = editor.getSelectedText();
      String unicode = toUnicode(selection);
      logger.log(Level.INFO, "Caracter selecionado [{0}] -> unicode [{1}].", new Object[]{selection, unicode});
      editor.replaceSelection(unicode);
    }

  }
}
