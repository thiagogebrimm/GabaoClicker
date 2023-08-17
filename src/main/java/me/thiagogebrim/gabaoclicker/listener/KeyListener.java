package me.thiagogebrim.gabaoclicker.listener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import me.thiagogebrim.gabaoclicker.AutoClicker;

public class KeyListener implements NativeKeyListener {

    // Cache dos modificadores de teclas para evitar a criação repetida de objetos
    private final Set<String> cachedToggleKeyModifiers = new HashSet<>(
            Arrays.asList(AutoClicker.toggleKey[1].split("\\+"))
    );

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        String currentKeyText = NativeKeyEvent.getKeyText(event.getKeyCode());
        Set<String> currentKeyModifiers = new HashSet<>(
                Arrays.asList(NativeKeyEvent.getModifiersText(event.getModifiers()).split("\\+"))
        );

        if (currentKeyText.equals(AutoClicker.toggleKey[0])
                && currentKeyModifiers.containsAll(cachedToggleKeyModifiers)
                && !AutoClicker.gui.focused) {
            AutoClicker.toggle();
        }
    }
}