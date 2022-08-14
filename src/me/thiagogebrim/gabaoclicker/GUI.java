package me.thiagogebrim.gabaoclicker;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUI extends JFrame implements ActionListener, NativeKeyListener {
    String projectVersion = "1.0.0";
    private boolean running = false;
    private Thread autoClicker = null;
    private boolean setting = false;

    private int VC_KP_SUBTRACT;
    private long lastTime = System.currentTimeMillis();

    private JPanel panContent;
    private JTextField txtDelay;
    @SuppressWarnings("rawtypes")
    private JComboBox drpClickType;
    @SuppressWarnings("rawtypes")
    private JComboBox drpClickAmt;
    private JButton btnStart;
    private JButton btnStop;
    private JTextField txtStartDelay;
    @SuppressWarnings("FieldCanBeLocal")
    private JPanel panHotkey;
    private JButton btnHotkey;

    public GUI() {
        setTitle("GabãoClicker V" + projectVersion);
        setLocation(150, 150);
        setSize(450, 320);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panContent);
        pack();
        setVisible(true);
        btnHotkey.addActionListener(this);
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);
        //noinspection unchecked
        drpClickAmt.addItem("Clique Unico");
        //noinspection unchecked
        drpClickAmt.addItem("Clique Duplo");
        //noinspection unchecked
        drpClickAmt.addItem("Clique Triplo");
        //noinspection unchecked
        drpClickType.addItem("Botão Esquerdo");
        //noinspection unchecked
        drpClickType.addItem("Botão do Meio");
        //noinspection unchecked
        drpClickType.addItem("Botão Direito");
        btnHotkey.setText(NativeKeyEvent.getKeyText(VC_KP_SUBTRACT));
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            Handler[] handlers = Logger.getLogger("").getHandlers();
            for (Handler handler : handlers) {
                handler.setLevel(Level.OFF);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHotkey) {
            setting = true;
            btnHotkey.setText("Configurando... aperte uma tecla");
            btnHotkey.setEnabled(false);
        }
        if (e.getSource() == btnStart) {
            startAutoClicker();
        }
        if (e.getSource() == btnStop) {
            stopAutoClicker();
        }
    }

    private void startAutoClicker() {
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        running = true;
        autoClicker = autoClicker(Integer.parseInt(txtStartDelay.getText()), Integer.parseInt(txtDelay.getText()), Integer.parseInt(Objects.requireNonNull(drpClickType.getSelectedItem()).toString().split(" ")[2]), (drpClickAmt.getSelectedIndex() + 1));
        autoClicker.start();
    }

    private void stopAutoClicker() {
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        running = false;
        //noinspection deprecation
        autoClicker.stop();
        autoClicker = null;
    }

    private Thread autoClicker(final int startDelay, final int delay, final int clickType, final int clickAmt) {
        return new Thread(() -> {
            try {
                Robot r = new Robot();
                r.delay(startDelay);
                while (true) {
                    if (running) {
                        for (int x = 0; x <= clickAmt; x++) {
                            r.mousePress(clickType);
                            r.delay(10);
                            r.mouseRelease(clickType);
                        }
                        r.delay(delay);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        if (System.currentTimeMillis() - lastTime > 500) {
            if (setting) {
                VC_KP_SUBTRACT = nativeKeyEvent.getKeyCode();
                btnHotkey.setEnabled(true);
                btnHotkey.setText(NativeKeyEvent.getKeyText(VC_KP_SUBTRACT));
                setting = false;
                System.out.println(VC_KP_SUBTRACT + nativeKeyEvent.getKeyCode());
            } else if (nativeKeyEvent.getKeyCode() == VC_KP_SUBTRACT) {
                if (!running) {
                    startAutoClicker();
                } else {
                    stopAutoClicker();
                }
            }
            lastTime = System.currentTimeMillis();
        }
    }

    @Override public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }

    @Override public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

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
        panContent = new JPanel();
        panContent.setSize(450, 320);
        panContent.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        JPanel panTiming = new JPanel();
        panTiming.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panContent.add(panTiming, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panTiming.setBorder(BorderFactory.createTitledBorder(null, "Controle de CPS", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
        txtDelay = new JTextField();
        txtDelay.setText("200");
        panTiming.add(txtDelay, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, -1), null, 0, false));
        JLabel lblDelay = new JLabel();
        lblDelay.setText("Delay (ms)");
        panTiming.add(lblDelay, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtStartDelay = new JTextField();
        txtStartDelay.setText("250");
        panTiming.add(txtStartDelay, new GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, -1), null, 0, false));
        JLabel lblStartDelay = new JLabel();
        lblStartDelay.setText("Start Delay (ms)");
        panTiming.add(lblStartDelay, new GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        JPanel panClicking = new JPanel();
        panClicking.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panContent.add(panClicking, new GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panClicking.setBorder(BorderFactory.createTitledBorder(null, "Opções de Clique", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
        drpClickType = new JComboBox();
        panClicking.add(drpClickType, new GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        drpClickAmt = new JComboBox();
        panClicking.add(drpClickAmt, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        JPanel panRun = new JPanel();
        panRun.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panContent.add(panRun, new GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panRun.setBorder(BorderFactory.createTitledBorder(null, "Iniciar/Parar", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
        btnStart = new JButton();
        btnStart.setText("Iniciar");
        panRun.add(btnStart, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnStop = new JButton();
        btnStop.setEnabled(false);
        btnStop.setText("Parar");
        panRun.add(btnStop, new GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panHotkey = new JPanel();
        panHotkey.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panContent.add(panHotkey, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panHotkey.setBorder(BorderFactory.createTitledBorder(null, "Botão Iniciar/Parar", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
        btnHotkey = new JButton();
        btnHotkey.setText("[KEYBIND]");
        panHotkey.add(btnHotkey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panContent;
    }
}