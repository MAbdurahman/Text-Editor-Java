
package memo_pad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 * The FindAndReplace Class
 * @author:  MAbdurrahman
 * @date:  19 January 2017
 * @version:  1.0.0
 */
public class FindAndReplace extends JDialog implements ActionListener {
    /** Instance Variables */
    private JTextField findTextField, replaceTextField;
    private JCheckBox caseSensitiveCheckBox, matchExactWordCheckBox;
    private JRadioButton searchUpRadioButton, searchDownRadioButton;
    private final JPanel northPanel, centerPanel;
    private JLabel statusInfoLabel;
    private final JFrame owner;
    @SuppressWarnings("FieldMayBeFinal")
    private boolean isFind, isReplace;
    private int wordPosition;
    
    /**
     * FindAndReplace Constructor -
     * @param JFrame 
     * @param Boolean
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public FindAndReplace(JFrame owner, boolean modal) {
        super(owner, true);
        this.owner = owner;
        this.isReplace = modal;
        northPanel = new JPanel();
        centerPanel = new JPanel();
   
        if (isReplace) {
            setTitle("Find And Replace");
            setupFindAndReplacePanel(northPanel);
            
        } else {
            setTitle("Find");
            setupFindPanel(northPanel);
        }
        addCenterComponents(centerPanel);
       // addSouthComponents(southPanel);
        
        addWindowListener(new WindowAdapter() {
            /**
             * windowClosing Method -
             * @param WindowEvent -
             */
            @Override
            public void windowClosing(WindowEvent we) {
                dispose();
                
            }//end of the windowClosing Method
        });//end of the Anonymous WindowAdapter
        
        getContentPane().add(northPanel, BorderLayout.NORTH);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        //getContentPane().add(southPanel, BorderLayout.SOUTH);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocation(500, 300);
        setVisible(true);
        
    }//end of the FindAndReplace Constructor
    
    /** Custom HighlightPainter for findNext and findAll */
    Highlighter.HighlightPainter wordHighlighter = new WordHighlighter(new Color(30, 126, 158));
    
    /**
     * WordHighlighter Class
     */
    class WordHighlighter extends DefaultHighlighter.DefaultHighlightPainter {
        /**
         * WordHighlighter Constructor -
         * @param Color 
         */
        public WordHighlighter(Color highlightColor) {
            super(highlightColor);
            
        }//end of the WordHighlighter Constructor
    }//end of the WordHighlighter Class
    /**
     * setupFindPanel Method -
     * @param JPanel -
     */
    private void setupFindPanel(JPanel panel) {
        GridBagLayout gridBag = new GridBagLayout();
        panel.setLayout(gridBag);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel findWordLabel = new JLabel("Find What: ");
        findWordLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
                
        JButton findNextButton = new JButton("Find Next");
        findNextButton.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        findNextButton.addActionListener(this);
        findNextButton.setEnabled(false);
        
        JButton findAllButton = new JButton("Find All");
        findAllButton.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        findAllButton.addActionListener(this);
        findAllButton.setEnabled(false);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        cancelButton.addActionListener(this);
        cancelButton.setEnabled(true);
        
        findTextField = new JTextField(20);
        findTextField.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        findTextField.addActionListener(this);
        findTextField.addKeyListener(new KeyAdapter() {
            /**
             * keyReleased Method -
             * @param KeyEvent -
             */
            @Override
            public void keyReleased(KeyEvent ke) {
                boolean state = (findTextField.getDocument().getLength() > 0);
                findNextButton.setEnabled(state);
                findAllButton.setEnabled(state);
                isFind = false;
                
            }//end of the keyReleased Method
        });//end of the Anonymous KeyAdapter Class
        
        if (findTextField.getText().length() > 0) {
            findNextButton.setEnabled(true);
            findAllButton.setEnabled(true);
            
        } else {
           gbc.gridx = 0;
           gbc.gridy = 0;
           gridBag.setConstraints(findWordLabel, gbc);
           panel.add(findWordLabel);
           
           gbc.gridx = 1;
           gbc.gridy = 0;
           gridBag.setConstraints(findTextField, gbc);
           panel.add(findTextField);
           
           gbc.gridx = 2;
           gbc.gridy = 0;
           gridBag.setConstraints(findNextButton, gbc);
           panel.add(findNextButton);
           
           gbc.gridx = 2;
           gbc.gridy = 1;
           gridBag.setConstraints(findAllButton, gbc);
           panel.add(findAllButton);
           
           gbc.gridx = 2;
           gbc.gridy = 2;
           gridBag.setConstraints(cancelButton, gbc);
           panel.add(cancelButton);
           
        }
        
    }//end of the setFindPanel Method
    /**
     * setupFindAndReplacePanel Method -
     * @param JPanel
     */
    private void setupFindAndReplacePanel(JPanel panel) {
        GridBagLayout gridBag = new GridBagLayout();
        panel.setLayout(gridBag);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel findWordLabel = new JLabel("Find What: ");
        JLabel replaceWordLabel = new JLabel("Replace With: ");
        
        findWordLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        replaceWordLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        
        JButton findNextButton = new JButton("Find Next");
        JButton replaceNextButton = new JButton("Replace Next");
        JButton replaceAllButton = new JButton("Replace All");
        JButton cancelButton = new JButton("Cancel");
        
        findNextButton.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        replaceNextButton.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        replaceAllButton.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        cancelButton.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        
        
        findNextButton.addActionListener(this);
        replaceNextButton.addActionListener(this);
        replaceAllButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        findNextButton.setEnabled(false);
        replaceNextButton.setEnabled(false);
        replaceAllButton.setEnabled(false);
        
        findTextField = new JTextField(20);
        replaceTextField = new JTextField(20);
        
        findTextField.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        replaceTextField.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        
        findTextField.addKeyListener(new KeyAdapter() {
            /**
             * keyReleased Method
             * @param KeyEvent -
             */
            @Override
            public void keyReleased(KeyEvent ke) {
                boolean state = (findTextField.getDocument().getLength() > 0);
                findNextButton.setEnabled(state);
                isFind = false;
            }//end of the keyReleased Method for the findTextField
        });//end of the Anonymous KeyAdapter for the findTextField
        
        replaceTextField.addKeyListener(new KeyAdapter() {
            /**
             * keyReleased Method -
             * @param KeyEvent -
             */
            @Override
            public void keyReleased(KeyEvent ke) {
                boolean state = (replaceTextField.getDocument().getLength() > 0);
                replaceNextButton.setEnabled(state);
                replaceAllButton.setEnabled(state);
                isFind = false;
                
            }//end of the keyReleased Method for the replaceTextField
        });//end of the Anonymous KeyAdapter for the replaceTextField
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gridBag.setConstraints(findWordLabel, gbc);
        panel.add(findWordLabel);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gridBag.setConstraints(findTextField, gbc);
        panel.add(findTextField);
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        gridBag.setConstraints(findNextButton, gbc);
        panel.add(findNextButton);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gridBag.setConstraints(replaceWordLabel, gbc);
        panel.add(replaceWordLabel);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gridBag.setConstraints(replaceTextField, gbc);
        panel.add(replaceTextField);
        
        gbc.gridx = 2;
        gbc.gridy = 1;
        gridBag.setConstraints(replaceNextButton, gbc);
        panel.add(replaceNextButton);
        
        gbc.gridx = 2;
        gbc.gridy = 2;
        gridBag.setConstraints(replaceAllButton, gbc);
        panel.add(replaceAllButton);
        
        gbc.gridx = 2;
        gbc.gridy = 3;
        gridBag.setConstraints(cancelButton, gbc);
        panel.add(cancelButton);
        
    }//end of the setupFindAndReplacePanel Method
    /**
     * addBottomComponents Method - 
     * @param JPanel
     */
    private void addCenterComponents(JPanel panel) {
        JPanel eastPanel = new JPanel();
        JPanel westPanel = new JPanel();
        /** Set the panel to a GridLayout with 1 row and 2 columns */
        panel.setLayout(new GridLayout(1, 2));
        /** Set the eastPanel to a GridLayout with 2 rows and 1 column */
        eastPanel.setLayout(new GridLayout(2, 1));
        /** Set the westPanel to a GridLayout with 2 rows and 1 column */
        westPanel.setLayout(new GridLayout(2, 1));
        
        caseSensitiveCheckBox = new JCheckBox("Case Sensitive", true);
        matchExactWordCheckBox = new JCheckBox("Match Exact Word", true);
        
        caseSensitiveCheckBox.setFont(new Font("Bookman Old Style", Font.PLAIN, 12));
        matchExactWordCheckBox.setFont(new Font("Bookman Old Style", Font.PLAIN, 12));
        
        searchUpRadioButton = new JRadioButton("Search Up", false);
        searchDownRadioButton = new JRadioButton("Search Down", true);
        
        searchUpRadioButton.setFont(new Font("Bookman Old Style", Font.PLAIN, 12));
        searchDownRadioButton.setFont(new Font("Bookman Old Style", Font.PLAIN, 12));
        
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(searchUpRadioButton);
        radioButtonGroup.add(searchDownRadioButton);
        
        eastPanel.add(caseSensitiveCheckBox);
        eastPanel.add(matchExactWordCheckBox);
        eastPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.
                createMatteBorder(1, 1, 1, 1, Color.black), "Search Options",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Bookman Old Style",
                Font.PLAIN, 12), Color.black));
        
        westPanel.add(searchUpRadioButton);
        westPanel.add(searchDownRadioButton);
        westPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.
                createMatteBorder(1, 1, 1, 1, Color.black), "Search Directions",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Bookman Old Style",
                Font.PLAIN, 12), Color.black));
        
        panel.add(eastPanel);
        panel.add(westPanel);
       
    }//end of the addBottomComponents Method
    /**
     * addSouthComponents Method -
     * @param JPanel 
     */
    private void addSouthComponents(JPanel panel) {
        statusInfoLabel = new JLabel("Status Info: ");
        statusInfoLabel.setHorizontalAlignment(JLabel.LEFT);
        statusInfoLabel.setForeground(Color.blue);
        statusInfoLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
        panel.add(statusInfoLabel);
        
    }//end of the addSouthComponents Method
    /**
     * findAllWord Method -
     * @param JTextComponent -
     * @param Sting -
     */
    protected void findAllWord(JTextComponent textComponent, String wordToFind) {
        removeAllHighlights(textComponent);
        try {
            Highlighter thisHighlighter = textComponent.getHighlighter();
            Document document = textComponent.getDocument();
            String text = document.getText(0, document.getLength());
            
            while ((wordPosition = text.toUpperCase().indexOf(wordToFind.toUpperCase(), 
                    wordPosition)) >= 0) {
                thisHighlighter.addHighlight(wordPosition, (wordPosition + wordToFind.length()),
                        wordHighlighter);
                wordPosition += wordToFind.length();
            }
        } catch (Exception ex) {
            String message = ex.getMessage();
            JOptionPane.showMessageDialog(rootPane, message);
        }
    }//end of the findAllWord Method
    /**
     * findNextWord Method -
     * @param JTextComponent -
     * @String
     */
    protected void findNextWord(JTextComponent textComponent, String wordToFind) {
        removeAllHighlights(textComponent);
        try {
            Highlighter thisHighlighter = textComponent.getHighlighter();
            Document document = textComponent.getDocument();
            String text = document.getText(0, document.getLength());
            //int wordPosition = 0;
            
            if (text != null && wordToFind != null) {
                boolean isEndOfSearch = false;
                int start;
                while (!isEndOfSearch) {
                    
                    start = text.indexOf(wordToFind, wordPosition);
                    System.out.println("start is " +start);
                    
                    if (start != - 1) {
                        isEndOfSearch = true;
                        
                        thisHighlighter.addHighlight(start, (start + wordToFind.length()), wordHighlighter);
                        wordPosition = (start + wordToFind.length());
                        System.out.println("wordPosition is "+wordPosition); 
                        
                    } else {
                        isEndOfSearch = true;
                        String message = "TextPad has completed the search for \"" + wordToFind + "\".";
                        JOptionPane.showMessageDialog(rootPane, message);
                    }
                }
            }
        } catch (BadLocationException | HeadlessException ex) {
            String msg = ex.getMessage();
            JOptionPane.showMessageDialog(rootPane, msg);
        }
       
    }//end of the findNextWord Method
    /**
     * removeHighlights Method -
     * @param JTextComponent -
     */
    protected void removeAllHighlights(JTextComponent textComponent) {
        Highlighter thisHighlighter = textComponent.getHighlighter();
        Highlighter.Highlight[] highlights = thisHighlighter.getHighlights();
        
        for (Highlighter.Highlight theHighlighter : highlights) {
            if (theHighlighter.getPainter()instanceof WordHighlighter) {
                thisHighlighter.removeAllHighlights();
            
            } 
        }
        
    }//end of the removeHighlights Method
    /**
     * replaceNextWord Method -
     * @param JTextComponent
     * @param String
     * @param String
     */
    protected void replaceNextWord(JTextComponent textComponent, String wordToFind,
                                                                  String replaceWord) {
            removeAllHighlights(textComponent);
            boolean replacing = true;
        try {
            Highlighter thisHighlighter = textComponent.getHighlighter();
            Document document = textComponent.getDocument();
            String text = document.getText(0, document.getLength());
            
            if (text != null && wordToFind != null) {
                boolean isEndOfSearch = false;
                int start;
                while (!isEndOfSearch) {
                    
                    start = text.indexOf(wordToFind, wordPosition);
                    System.out.println("start is " +start);
                    
                    if (start != - 1) {
                        isEndOfSearch = true;
   
                        wordPosition = (start + wordToFind.length());
                        System.out.println("wordPosition is "+wordPosition); 
                         if (replacing) {
                          
                            StringBuffer stringBuffer = new StringBuffer(text);
                             System.out.println("at stringBuffer");
                            int difference = (replaceWord.length() - wordToFind.length());
                            int offset = 0;
                         
                                System.out.println("getting to replace");
                                stringBuffer.replace(start, (start  + wordToFind.length()),
                                                                            replaceWord);
                                
                                textComponent.setText(stringBuffer.toString());
                            thisHighlighter.addHighlight(start, (start + wordToFind.length()), wordHighlighter);
                        }
                        
                    } else {
                        isEndOfSearch = true;
                        replacing = false;
                        String message = "TextPad has completed replacing text for \"" + wordToFind + "\".";
                        JOptionPane.showMessageDialog(rootPane, message);
                    }
                }
            }
        } catch (BadLocationException | HeadlessException ex) {
            String msg = ex.getMessage();
            JOptionPane.showMessageDialog(rootPane, msg);
        }
       
    }//end of the replaceNextWord Method
    /**
     * isSearchingDown Method -
     * @param Void
     * @return Boolean
     */
    private boolean isSearchingDown() {
        return (searchDownRadioButton.isSelected());
        
    }//end of the isSearchingDown Method
    /**
     * isMatchExactWordSelected Method -
     * @param Void
     * @return Boolean
     */
    private boolean isMatchExactWordSelected() {
        return (matchExactWordCheckBox.isSelected());
        
    }//end of isExactWordSelected Method
    /**
     * isCaseSensitiveSelected Method -
     * @param Void
     * @return Boolean
     */
    private boolean isCaseSensitiveSelected() {
        return (caseSensitiveCheckBox.isSelected());
        
    }//end of isCaseSensitiveSelected Method
    /**
     * checkForExactWord Method -
     * @param Int -
     * @param String -
     * @param Int -
     * @param Int -
     */
    private boolean checkForExactWord(int wordLength, String text, int add, int caretPosition) {
        int offsetLeft = (caretPosition + add) - 1;
        int offsetRight = (caretPosition + add + wordLength);
        
        if ((offsetLeft < 0) || (offsetRight > text.length())) {
            return true;
            
        }
        return ((!Character.isLetterOrDigit(text.charAt(offsetLeft))) &&
                (!Character.isLetterOrDigit(text.charAt(offsetRight))));
        
    }//end of the checkForExactWord Method
    /**
     * getWord Method -
     * @param Void
     * @return String
     */
    private String getWordToFind() {
        if (isCaseSensitiveSelected()) {
            return findTextField.getText();
            
        }
        return findTextField.getText().toLowerCase();
        
    }//end of the getWordToFind Method
    /**
     * getWordToReplace Method 
     * @param Void
     * @return String
     */
    private String getWordToReplace() {
        if (isCaseSensitiveSelected()) {
            return replaceTextField.getText();
            
        }
        return replaceTextField.getText().toLowerCase();
    }//end of the getWordToReplace Method
    /**
     * getAllText Method -
     * @param Void
     * @return String -
     */
    private String getAllText() {
        if (isCaseSensitiveSelected()) {
            return MemoPad.getTextPane().getText();
            
        }
        return MemoPad.getTextPane().getText().toLowerCase();
        
    }//end of the getAllText Method
    /**
     * replaceAll Method - 
     * @param Void
     */
    private void replaceAll() {
        String findWord = findTextField.getText();
        //String text = TextPad.getTextPane().getText();
        String text = getAllText();
        String replaceWord = replaceTextField.getText();
        @SuppressWarnings("StringBufferMayBeStringBuilder")
        StringBuffer stringBuffer = new StringBuffer(text);
        MemoPad.getTextPane().setCaretPosition(0);
        int difference = (replaceWord.length() - findWord.length());
        int offset = 0;
        int foundWords = 0;
        
        for(int i = 0; i < (text.length() - findWord.length()); i++) {
            String temp = text.substring(i, (i + findWord.length()));
            if ((temp.equals(findWord)) && 
                    (checkForExactWord(findWord.length(), text, 0, i))) {
                foundWords++;
                stringBuffer.replace((i + offset),(i + offset + findWord.length()), replaceWord);
                offset += difference;
            }
        }
        MemoPad.getTextPane().setText(stringBuffer.toString());
        displayEndResult(foundWords, true);
        MemoPad.getTextPane().setCaretPosition(0);
        
    }//end of the replaceAll Method
    
    /**
     * updateProcess Method -
     * @param Void
     */
    private void updateProcess() {
        if (isReplace) {
            statusInfoLabel.setText("Replacing the text \"" + findTextField.getText() + "\"");
            
        } else {
           
            statusInfoLabel.setText("Searching for the text \""+ findTextField.getText() +"\"");
        }
        int caretPosition = MemoPad.getTextPane().getCaretPosition();
        String word = getWordToFind();
        String text = getAllText();
        //caretPosition = findWord(text, word, caretPosition);
        
        if (caretPosition <= 0) {
            //displayEndResult(0, false);
            
        }
    }//end of the updateProcess Method
    /**
     * displayEndResult Method -
     * @param Int -
     * @param Boolean
     */
    private void displayEndResult(int foundWords, boolean isReplaceAll) {
        String message = "";
        if (isReplaceAll) {
            if (foundWords == 0) {
                message = "The text \"" + findTextField.getText() + "\" not found.";
                
            } else if (foundWords == 1) {
                message = "One change was made for the text \"" + 
                        findTextField.getText() + "\"";
                
            } else {
                message = "" + foundWords + " changes were made for the text \"" + 
                        findTextField.getText() + "\"";
            }
            
        } else {
            @SuppressWarnings("UnusedAssignment")
            String string = "";
            if (isSearchingDown()) {
                string = "searching Down ";
            
            } else {
                string = "searching Up ";
                
            }
            if (isFind && !isReplace) {
                message = "End of " + string + " for " + findTextField.getText();
                
            } else if (isFind && isReplace) {
                message = "End of replacing " + findTextField.getText() + " with " 
                        + replaceTextField.getText();
            }
        }
        //statusInfoLabel.setText(message);
        
    }//end of the displayEndResult Method
    /**
     * actionPerformed Method -
     * @param ActionEvent -
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        if (ae.getSource().equals(findTextField) ||
                (ae.getSource().equals(replaceTextField))) {
            validate();
        }
        if (ae.getActionCommand().equals("Cancel")) {
            dispose();
        }
        if (ae.getActionCommand().equals("Find Next")) {
            findNextWord(MemoPad.getTextPane(), getWordToFind());
           
        }
        if (ae.getActionCommand().equals("Find All")) {
            findAllWord(MemoPad.getTextPane(), getWordToFind());
        } 
        if (ae.getActionCommand().equals("Replace Next")) {
            replaceNextWord(MemoPad.getTextPane(), getWordToFind(), getWordToReplace());
        }
        if (ae.getActionCommand().equals("Replace All")) {
            replaceAll();
        }
        
    }//end of the actionPerformed Method
}//end of the FindAndReplace Class
