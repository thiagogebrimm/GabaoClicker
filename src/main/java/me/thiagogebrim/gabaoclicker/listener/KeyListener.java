package me.thiagogebrim.gabaoclicker.listener;

import java.util.Arrays;
import java.util.List;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import me.thiagogebrim.gabaoclicker.AutoClicker;

public class KeyListener implements NativeKeyListener {

	@Override
	public void nativeKeyPressed(NativeKeyEvent event) {
		List<String> modifiers1 = Arrays.asList(NativeKeyEvent.getModifiersText(event.getModifiers()).split("\\+"));
		List<String> modifiers2 = Arrays.asList(AutoClicker.toggleKey[1].split("\\+"));

		if (NativeKeyEvent.getKeyText(event.getKeyCode()).equals(AutoClicker.toggleKey[0])
				&& modifiers1.containsAll(modifiers2) && !AutoClicker.gui.focused) {
			AutoClicker.toggle();
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent event) {
		// NO-OP
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent event) {
		// NO-OP
	}
}
