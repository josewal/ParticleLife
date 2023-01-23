package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	private SimulationPanel sp;

	public boolean upPressed, downPressed, rightPressed, leftPressed;

	public KeyHandler(SimulationPanel sp) {
		this.sp = sp;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		anyState(code);
		
		if(sp.state.optionsOpen()) {
			optionsState(code);
		}
		else if (sp.state.isRun()) {
			playState(code);
		}
		else if(sp.state.isPause()) {
			pauseState(code);
		}
		
		
	}

	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) {
			upPressed = true;
		} else if (code == KeyEvent.VK_S) {
			downPressed = true;
		} else if (code == KeyEvent.VK_A) {
			leftPressed = true;
		} else if (code == KeyEvent.VK_D) {
			rightPressed = true;
		}
	}

	public void anyState(int code) {
		if (code == KeyEvent.VK_W) {
			upPressed = true;
		}
		if (code == KeyEvent.VK_S) {
			downPressed = true;
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = true;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = true;
		}
	}

	public void optionsState(int code) {		
		if (code == KeyEvent.VK_ESCAPE) {
			sp.state.closeOptions();
		}
	}

	public void pauseState(int code) {
		if (code == KeyEvent.VK_ESCAPE) {
			sp.state.openOptions();
		}
		
		if (code == KeyEvent.VK_P) {
			sp.state.setToRun();
		}
	}

	public void playState(int code) {
		if (code == KeyEvent.VK_ESCAPE) {
			sp.state.openOptions();
		}
		
		if (code == KeyEvent.VK_P) {
			sp.state.setToPause();
		}
	}

}
