package me.thiagogebrim.gabaoclicker.gui;

import com.apple.eawt.Application;
import me.thiagogebrim.gabaoclicker.AutoClicker;
import org.pushingpixels.radiance.animation.api.Timeline;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClickerGui {
    private static final Logger logger = LogManager.getLogger(ClickerGui.class);

    private static final int WINDOW_WIDTH = 150;
    private static final int WINDOW_HEIGHT = 125;
    private static final int DROPDOWN_HEIGHT = 100;
    private static final Color LIGHT_GRAY = new Color(60, 70, 73);
    private static final Color DARK_GRAY = new Color(45, 47, 49);
    private static final Color GREEN = new Color(35, 168, 105);
    public boolean focused;

    private JFrame frame;
    private JPanel mainPane;
    private JPanel titleBar;
    private JPanel dropdown;
    private JLabel titleText;
    private JLabel cpsRange;
    public JLabel cpsNumber;
    private JLabel dropdownArrow;
    public JLabel powerButton;
    private JLabel toggleKeyText;
    JTextField minCPSField;
    JTextField maxCPSField;
    private JTextField toggleKeyField;
    private JCheckBox overlayBox;
    private JCheckBox rightClickBox;

    public ClickerGui() {
        initializeComponents();
        setupFrame();
        setupMainPane();
        setupTitleBar();
        setupDropdown();
        setupSettings();
        setupMisc();
    }

    private void initializeComponents() {
        frame = new JFrame("Gabão Clicker");
        mainPane = new JPanel(null);
        titleBar = new JPanel(null);
        dropdown = new JPanel(null);
        titleText = new JLabel("Gabão Clicker");
        cpsRange = new JLabel("Faixa de CPS");
        cpsNumber = new JLabel("00");
        dropdownArrow = new JLabel(new ImageIcon(Objects.requireNonNull(AutoClicker.class.getClassLoader().getResource("assets/arrow_down.png"))));
        powerButton = new JLabel(new ImageIcon(Objects.requireNonNull(AutoClicker.class.getClassLoader().getResource("assets/power_button.png"))));
        toggleKeyText = new JLabel("Botão Alternar");
        minCPSField = new JTextField("8", 3);
        maxCPSField = new JTextField("12", 3);
        toggleKeyField = new JTextField("Mouse 3");
        overlayBox = new JCheckBox("Sobrepor", true);
        rightClickBox = new JCheckBox("Clique direito", false);
    }

    private void setupFrame() {
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocation(50, 50);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 0));
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);

        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            Application.getApplication().setDockIconImage(new ImageIcon(AutoClicker.class.getClassLoader().getResource("assets/GabaoClicker.png")).getImage());
        } else {
            frame.setIconImage(new ImageIcon(Objects.requireNonNull(AutoClicker.class.getClassLoader().getResource("assets/GabaoClicker.png"))).getImage());
        }

        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                frame.requestFocusInWindow();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                frame.requestFocusInWindow();
            }
        });

        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event.getID() == MouseEvent.MOUSE_CLICKED) {
                if (!(event.getSource() instanceof JTextField)) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
    }

    private void setupMainPane() {
        mainPane.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT + DROPDOWN_HEIGHT);
        mainPane.setBackground(LIGHT_GRAY);
        mainPane.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, DARK_GRAY));

        powerButton.setBounds(10, 45, 50, 50);
        powerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                AutoClicker.toggle();
            }
        });

        mainPane.add(powerButton);
        cpsNumber.setBounds(75, 45, 75, 50);
        cpsNumber.setForeground(GREEN);
        mainPane.add(cpsNumber);
    }

    private void setupTitleBar() {
        MouseAdapter dragListener = new MouseAdapter() {
            private int pX, pY;

            @Override
            public void mousePressed(MouseEvent e) {
                pX = e.getX();
                pY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                frame.setLocation(frame.getLocation().x + e.getX() - pX, frame.getLocation().y + e.getY() - pY);
            }
        };

        titleBar.setBounds(0, 0, WINDOW_WIDTH, 30);
        titleBar.setBackground(DARK_GRAY);
        titleBar.addMouseListener(dragListener);
        titleBar.addMouseMotionListener(dragListener);

        titleText.setBounds(0, 0, WINDOW_WIDTH, 30);
        titleText.setHorizontalAlignment(SwingConstants.CENTER);
        titleText.setForeground(Color.WHITE);
        titleBar.add(titleText);
    }

    private void setupDropdown() {
        dropdown.setBounds(0, WINDOW_HEIGHT - 15, WINDOW_WIDTH, 15);
        dropdown.setBackground(DARK_GRAY);

        dropdown.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (frame.getHeight() == WINDOW_HEIGHT) {
                    dropdownArrow.setIcon(new ImageIcon(Objects.requireNonNull(AutoClicker.class.getClassLoader().getResource("assets/arrow_up.png"))));

                    final Timeline dropdownTimeline = Timeline.builder(dropdown).addPropertyToInterpolate("location", dropdown.getLocation(), new Point(0, dropdown.getY() + DROPDOWN_HEIGHT)).setDuration(300).build();
                    dropdownTimeline.play();

                    final Timeline frameTimeline = Timeline.builder(frame).addPropertyToInterpolate("size", frame.getSize(), new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT + DROPDOWN_HEIGHT)).setDuration(300).build();
                    frameTimeline.play();

                    KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                } else if (frame.getHeight() == WINDOW_HEIGHT + DROPDOWN_HEIGHT) {
                    dropdownArrow.setIcon(new ImageIcon(Objects.requireNonNull(AutoClicker.class.getClassLoader().getResource("assets/arrow_down.png"))));

                    final Timeline dropdownTimeline = Timeline.builder(dropdown).addPropertyToInterpolate("location", dropdown.getLocation(), new Point(0, WINDOW_HEIGHT - 15)).setDuration(300).build();
                    dropdownTimeline.play();

                    final Timeline frameTimeline = Timeline.builder(frame).addPropertyToInterpolate("size", frame.getSize(), new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)).setDuration(300).build();
                    frameTimeline.play();

                    KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                }
            }


        });

        dropdownArrow.setBounds(69, 2, 13, 10);
        dropdown.add(dropdownArrow);
    }

    private void setupSettings() {
        cpsRange.setBounds(0, 110, WINDOW_WIDTH, 13);
        cpsRange.setHorizontalAlignment(SwingConstants.CENTER);
        cpsRange.setForeground(Color.WHITE);
        mainPane.add(cpsRange);

        minCPSField.setBounds(25, 128, 40, 25);
        minCPSField.setHorizontalAlignment(SwingConstants.CENTER);
        minCPSField.setBackground(DARK_GRAY);
        minCPSField.setForeground(Color.WHITE);
        minCPSField.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        minCPSField.addActionListener(e -> textFieldSetCPS(true));

        minCPSField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                textFieldSetCPS(true);
            }
        });

        mainPane.add(minCPSField);

        maxCPSField.setBounds(WINDOW_WIDTH - 60, 128, 40, 25);
        maxCPSField.setHorizontalAlignment(SwingConstants.CENTER);
        maxCPSField.setBackground(DARK_GRAY);
        maxCPSField.setForeground(Color.WHITE);
        maxCPSField.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        maxCPSField.addActionListener(e -> textFieldSetCPS(false));

        maxCPSField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                textFieldSetCPS(false);
            }
        });

        mainPane.add(maxCPSField);

        overlayBox.setBounds(5, 163, 67, 16);
        overlayBox.setBackground(LIGHT_GRAY);
        overlayBox.setForeground(Color.WHITE);
        overlayBox.setIcon(new ImageIcon(Objects.requireNonNull(AutoClicker.class.getClassLoader().getResource("assets/checkbox_unchecked.png"))));
        overlayBox.setSelectedIcon(new ImageIcon(Objects.requireNonNull(AutoClicker.class.getClassLoader().getResource("assets/checkbox_checked.png"))));

        overlayBox.addActionListener(e -> frame.setAlwaysOnTop(overlayBox.isSelected()));

        mainPane.add(overlayBox);

        rightClickBox.setBounds(66, 163, 80, 16);
        rightClickBox.setBackground(LIGHT_GRAY);
        rightClickBox.setForeground(Color.WHITE);
        rightClickBox.setIcon(new ImageIcon(Objects.requireNonNull(AutoClicker.class.getClassLoader().getResource("assets/checkbox_unchecked.png"))));
        rightClickBox.setSelectedIcon(new ImageIcon(Objects.requireNonNull(AutoClicker.class.getClassLoader().getResource("assets/checkbox_checked.png"))));

        rightClickBox.addActionListener(e -> AutoClicker.button = (rightClickBox.isSelected()) ? 2 : 1);

        mainPane.add(rightClickBox);

        toggleKeyText.setBounds(11, 180, 66, 25);
        toggleKeyText.setForeground(Color.WHITE);
        mainPane.add(toggleKeyText);

        toggleKeyField.setBounds(WINDOW_WIDTH - 70, 182, 60, 20);
        toggleKeyField.setHorizontalAlignment(SwingConstants.CENTER);
        toggleKeyField.setBackground(DARK_GRAY);
        toggleKeyField.setForeground(Color.WHITE);
        toggleKeyField.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        ((AbstractDocument) toggleKeyField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attributes) throws BadLocationException {
                if (offset == -1 && length == -1) {
                    super.replace(fb, 0, toggleKeyField.getText().length(), text, attributes);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) {
                // NO-OP
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attributes) {
                // NO-OP
            }
        });

        toggleKeyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    if (!InputEvent.getModifiersExText(e.getModifiersEx()).contains(KeyEvent.getKeyText(e.getKeyCode())) && e.getKeyCode() != KeyEvent.VK_CAPS_LOCK) {
                        AutoClicker.toggleKey[0] = KeyEvent.getKeyText(e.getKeyCode());
                        AutoClicker.toggleKey[1] = InputEvent.getModifiersExText(e.getModifiersEx());
                        AutoClicker.toggleMouseButton = -1;
                        ((AbstractDocument) toggleKeyField.getDocument()).replace(-1, -1, getKeyString(e.getKeyCode(), e.getModifiersEx()), null);
                    }
                } catch (BadLocationException ex) {
                    logger.error("An error occurred while processing the key event", ex);
                }
            }
        });

        toggleKeyField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    if (e.getButton() == 2 || e.getButton() > 3) {
                        AutoClicker.toggleMouseButton = (e.getButton() == 2) ? 3 : e.getButton();
                        AutoClicker.toggleKey[0] = "";
                        AutoClicker.toggleKey[1] = "";
                        ((AbstractDocument) toggleKeyField.getDocument()).replace(-1, -1, "Mouse " + ((e.getButton() == 2) ? 3 : e.getButton()), null);
                    }
                } catch (BadLocationException ex) {
                    logger.error("Error while updating the toggleKeyField document", ex);
                }
            }
        });

        mainPane.add(toggleKeyField);
    }

    private void setupMisc() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            InputStream fontFile = AutoClicker.class.getClassLoader().getResourceAsStream("assets/BebasNeue.otf");

            if (fontFile == null) {
                logger.warn("Font file not found in resources.");
                return;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            ge.registerFont(font);
            fontFile.close();

            titleText.setFont(font.deriveFont(Font.PLAIN, 25));
            cpsNumber.setFont(font.deriveFont(Font.PLAIN, 55));
            cpsRange.setFont(font.deriveFont(Font.PLAIN, 18));
            overlayBox.setFont(font.deriveFont(Font.PLAIN, 12));
            rightClickBox.setFont(font.deriveFont(Font.PLAIN, 10));
            toggleKeyText.setFont(font.deriveFont(Font.PLAIN, 11));
            minCPSField.setFont(new Font("arial", Font.PLAIN, 12));
            maxCPSField.setFont(new Font("arial", Font.PLAIN, 12));
            toggleKeyField.setFont(new Font("arial", Font.PLAIN, 12));
        } catch (IOException | FontFormatException e) {
            logger.error("Error while setting up the font", e);
        }

        frame.add(titleBar);
        frame.add(dropdown);
        frame.add(mainPane);
        frame.setVisible(true);
    }

    private void textFieldSetCPS(boolean isMin) {
        JTextField textField = isMin ? minCPSField : maxCPSField;

        if (textField.getText().matches("^\\d+$") && Integer.parseInt(textField.getText()) >= 1 && Integer.parseInt(textField.getText()) <= 999) {
            int cpsFieldVal = Integer.parseInt(textField.getText());

            textField.setText(textField.getText().replaceFirst("^0*", ""));
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();

            if (isMin) {
                AutoClicker.minCPS = cpsFieldVal;
                if (AutoClicker.minCPS > AutoClicker.maxCPS) {
                    AutoClicker.minCPS = AutoClicker.maxCPS;
                    minCPSField.setText(String.valueOf(AutoClicker.minCPS));
                }
            } else {
                AutoClicker.maxCPS = cpsFieldVal;
                if (AutoClicker.maxCPS < AutoClicker.minCPS) {
                    AutoClicker.maxCPS = AutoClicker.minCPS;
                    maxCPSField.setText(String.valueOf(AutoClicker.maxCPS));
                }
            }

        } else {
            textField.setText(String.valueOf(isMin ? AutoClicker.minCPS : AutoClicker.maxCPS));
        }
    }


    private String getKeyString(int keyCode, int modifiers) {
        String modifiersString = InputEvent.getModifiersExText(modifiers).replace("+", "");
        String keyString;

        if (keyCode == 0) {
            keyString = "Invalid Key";
            modifiersString = "";
        } else if (keyCode == 32) {
            keyString = "Space";
        } else {
            keyString = KeyEvent.getKeyText(keyCode);
        }

        return modifiersString + keyString;
    }
}