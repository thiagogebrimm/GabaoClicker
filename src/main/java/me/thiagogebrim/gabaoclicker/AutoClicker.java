package me.thiagogebrim.gabaoclicker;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.util.Objects;
import java.util.Random;

import me.thiagogebrim.gabaoclicker.listener.DeletePrefetchFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.ImageIcon;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import me.thiagogebrim.gabaoclicker.gui.ClickerGui;
import me.thiagogebrim.gabaoclicker.listener.KeyListener;
import me.thiagogebrim.gabaoclicker.listener.MouseListener;

public class AutoClicker {

    public static Robot robot;
    public static Point mousePos;
    public static ClickerGui gui = new ClickerGui();

    public static boolean toggled = false;
    public static boolean activated = false;
    public static boolean skipNext = false;
    public static boolean blockHit = false;

    private static int delay = -1;
    public static long lastTime = 0;
    public static int minCPS = 8;
    public static int maxCPS = 12;
    public static int button = 1;

    public static String[] toggleKey = {"", ""};
    public static int toggleMouseButton = 3;
    private static final Logger logger = LogManager.getLogger(AutoClicker.class);


    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
            robot = new Robot();
            GlobalScreen.addNativeMouseListener(new MouseListener());
            GlobalScreen.addNativeKeyListener(new KeyListener());
            DeletePrefetchFile.executeDeletePrefetchFile();
        } catch (NativeHookException | AWTException e) {
            logger.error("An error occurred: ", e);
        }

        try {
            while (true) {
                Thread.sleep(1);
                Random random = new Random();
                if (delay == -1)
                    delay = random.nextInt((1003 / minCPS) - (1004 / maxCPS) + 1) + (1004 / maxCPS);

                if (activated && toggled && !gui.focused) {
                    if (System.currentTimeMillis() - lastTime >= delay) {
                        click();
                        lastTime = System.currentTimeMillis();
                        delay = random.nextInt((1003 / minCPS) - (1004 / maxCPS) + 1) + (1004 / maxCPS);
                    }
                }
            }
        } catch (InterruptedException e) {
            logger.error("An error occurred: ", e);
        }
    }

    private static void click() {
        skipNext = true;
        robot.mousePress((button == 1) ? 16 : 4);
        robot.mouseRelease((button == 1) ? 16 : 4);

        if (blockHit) {
            robot.mousePress((button == 1) ? 4 : 16);
            robot.mouseRelease((button == 1) ? 4 : 16);
        }
    }

    public static void toggle() {
        if (AutoClicker.toggled) {
            AutoClicker.toggled = false;
            AutoClicker.gui.powerButton
                    .setIcon(new ImageIcon(Objects.requireNonNull(AutoClicker.class.getClassLoader().getResource("assets/power_button.png"))));
        } else {
            AutoClicker.toggled = true;
            AutoClicker.gui.powerButton.setIcon(
                    new ImageIcon(Objects.requireNonNull(AutoClicker.class.getClassLoader().getResource("assets/power_button_on.png"))));
        }

        AutoClicker.activated = false;
        AutoClicker.skipNext = false;
        AutoClicker.blockHit = false;
    }
}
