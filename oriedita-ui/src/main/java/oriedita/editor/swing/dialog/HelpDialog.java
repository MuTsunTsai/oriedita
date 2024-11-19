package oriedita.editor.swing.dialog;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.tinylog.Logger;
import oriedita.editor.FrameProvider;
import oriedita.editor.databinding.ApplicationModel;
import oriedita.editor.tools.ResourceUtil;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class HelpDialog {
    private final FrameProvider frameProvider;
    private final ApplicationModel applicationModel;
    private JPanel contentPane;
    private JTextPane helpLabel;
    private JPanel gifPanel;
    private JTextPane gifLabel;
    private JScrollPane helpScrollPanel;
    private HelpDialogUI helpDialogUI;

    public void setVisible(boolean helpVisible) {
        assert helpDialogUI != null;

        helpDialogUI.setVisible(helpVisible);
    }

    private static class HelpDialogUI extends JDialog {
        public HelpDialogUI(Frame owner, JPanel contentPane, ApplicationModel applicationModel) {
            super(owner, "Help");

            setContentPane(contentPane);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    applicationModel.setHelpVisible(false);
                }
            });

            applicationModel.addPropertyChangeListener(e -> {
                if (e.getPropertyName() == null || e.getPropertyName().equals("helpVisible")) {
                    setVisible(applicationModel.getHelpVisible());
                }
                owner.requestFocus();
            });

            addWindowFocusListener(new WindowAdapter() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    owner.requestFocus();
                }
            });

            setUndecorated(false);

            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

            setDefaultCloseOperation(HIDE_ON_CLOSE);
        }
    }

    @Inject
    public HelpDialog(FrameProvider frameProvider, ApplicationModel applicationModel) {
        this.frameProvider = frameProvider;
        this.applicationModel = applicationModel;
    }

    public void start(Point canvasLocation, Dimension canvasSize) {
        $$$setupUI$$$();
        helpDialogUI = new HelpDialogUI(frameProvider.get(), contentPane, applicationModel);
        helpDialogUI.setPreferredSize(new Dimension(360, 250));

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> applicationModel.setHelpVisible(false), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        helpLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        helpDialogUI.pack();
        helpDialogUI.setLocation(canvasLocation.x + canvasSize.width - helpDialogUI.getSize().width - 10, canvasLocation.y + 10);

        String helpStartupString = ResourceBundle.getBundle("help").getString("a__hajimeni");
        helpLabel.setText(helpStartupString);
        helpScrollPanel.setBorder(new EmptyBorder(0, 1, 0, 0));
    }

    public void setExplanation(String key) {
        String gif = ResourceUtil.getBundleString("gif", key);
        String name = ResourceUtil.getBundleString("name", key);
        String description = ResourceUtil.getBundleString("help", key);

        if (gif != null && !gif.isEmpty()) {
            gifPanel.setVisible(true);
            gifLabel.setText(processPaths("<html><center><img src=\"help-gif/" + gif + ".gif\" width=\"128\"><center></html>"));
        } else {
            gifPanel.setVisible(false);
        }
        if (description != null) {
            helpDialogUI.setTitle(name);
            helpLabel.setText(processPaths(description));
        }
        helpLabel.setCaretPosition(0);
    }

    private String processPaths(String helpText) {
        Pattern p = Pattern.compile("src\\s*=\\s*[\"']([^\"']*)[\"']");
        Matcher matcher = p.matcher(helpText);
        return matcher.replaceAll(result -> {
            String path = result.group(1);
            URL resource = HelpDialog.class.getClassLoader().getResource(path);
            if (resource != null) {
                return "src='" + resource + "'";
            }
            Logger.warn("Failed to find resource " + path);
            return "src='' /> Failed to load " + path + "<br";
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        contentPane.setAutoscrolls(false);
        contentPane.setMinimumSize(new Dimension(200, 250));
        contentPane.setPreferredSize(new Dimension(200, 220));
        helpScrollPanel = new JScrollPane();
        helpScrollPanel.setHorizontalScrollBarPolicy(31);
        helpScrollPanel.setMinimumSize(new Dimension(200, 220));
        helpScrollPanel.setOpaque(true);
        helpScrollPanel.setPreferredSize(new Dimension(200, 220));
        helpScrollPanel.setVerticalScrollBarPolicy(20);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(helpScrollPanel, gbc);
        helpLabel = new JTextPane();
        helpLabel.setAutoscrolls(false);
        helpLabel.setContentType("text/html");
        helpLabel.setFocusable(false);
        helpLabel.setOpaque(true);
        helpScrollPanel.setViewportView(helpLabel);
        gifPanel = new JPanel();
        gifPanel.setLayout(new GridBagLayout());
        gifPanel.setAutoscrolls(true);
        gifPanel.setInheritsPopupMenu(false);
        gifPanel.setMinimumSize(new Dimension(1, 1));
        gifPanel.setOpaque(true);
        gifPanel.setPreferredSize(new Dimension(148, 220));
        gifPanel.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(gifPanel, gbc);
        gifLabel = new JTextPane();
        gifLabel.setBackground(new Color(-1));
        gifLabel.setContentType("text/html");
        gifLabel.setEditable(false);
        gifLabel.setMargin(new Insets(46, 0, 0, 0));
        gifLabel.setOpaque(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gifPanel.add(gifLabel, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
