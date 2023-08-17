package me.thiagogebrim.gabaoclicker.listener;

import java.awt.event.MouseEvent;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

import me.thiagogebrim.gabaoclicker.AutoClicker;

public class MouseListener implements NativeMouseListener {
	private boolean isLeftClickActive, isRightClickActive;
	private long lastClickTimestamp = 0;

	@Override
	public void nativeMousePressed(NativeMouseEvent event) {
		int clickedButton = event.getButton();

		if (clickedButton == AutoClicker.toggleMouseButton && !AutoClicker.gui.focused) {
			AutoClicker.toggle();
		}

		if (AutoClicker.toggled && !AutoClicker.skipNext) {
			if (clickedButton == MouseEvent.BUTTON1) {
				isLeftClickActive = true;
			} else if (clickedButton == MouseEvent.BUTTON2) {
				isRightClickActive = true;
			}

			// Verifique se há clique esquerdo e direito
			if (isLeftClickActive && isRightClickActive) {
				AutoClicker.blockHit = true;
			}

			if (clickedButton == AutoClicker.button) {
				AutoClicker.mousePos = event.getPoint();
				AutoClicker.activated = true;
				AutoClicker.lastTime = System.currentTimeMillis();
			}
		}

		// Calcula os cliquer por segundo (CPS)
		if (clickedButton == AutoClicker.button) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastClickTimestamp > 1000 && lastClickTimestamp != 0) {
				lastClickTimestamp = 0;
			}

			if (lastClickTimestamp == 0) {
				lastClickTimestamp = currentTime;
			} else if (currentTime != lastClickTimestamp) {
				int cps = Math.round(1000.0f / (currentTime - lastClickTimestamp));
				AutoClicker.gui.cpsNumber.setText((cps < 10) ? "0" + cps : String.valueOf(cps));
				lastClickTimestamp = 0;
			}
		}
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent event) {
		int releasedButton = event.getButton();

		if (!AutoClicker.skipNext) {
			if (releasedButton == AutoClicker.button) {
				isLeftClickActive = false;
				AutoClicker.activated = false;
			} else if (releasedButton == ((AutoClicker.button == 1) ? 2 : 1)) {
				isRightClickActive = false;
				AutoClicker.blockHit = false;
			}
		} else {
			AutoClicker.skipNext = releasedButton == AutoClicker.button && AutoClicker.blockHit;
		}
	}
}