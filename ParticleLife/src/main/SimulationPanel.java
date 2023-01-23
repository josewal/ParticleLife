package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import camera.Camera;
import enviroment.Enviroment;

public class SimulationPanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	Config conf = Config.getInstance();

	Thread simThread;
	int drawCount;

	public UI ui = new UI(this);
	public KeyHandler keyH = new KeyHandler(this);
	public SimState state = new SimState(this);
	public Camera camera = new Camera();;

	private Enviroment env = new Enviroment();

	public SimulationPanel() {
		this.setPreferredSize(new Dimension(conf.screenWidth, conf.screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		camera.focusOn(conf.envWidth / 2, conf.envHeight / 2);
		state.setToRun();
	}

	public void setupEnviroment() {
		env = new Enviroment();
	}

	public void startSimThread() {
		simThread = new Thread(this);
		simThread.start();
	}

	@Override
	public void run() {
		double drawInterval = 1000000000 / conf.FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		drawCount = 0;

		while (simThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}

			if (timer >= 1000000000) {
//				System.out.println("FPS:" + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}

	}

	public void update() {
		if (state.isRun()) {
			for (int i = 0; i < conf.UPF; i++) {
				env.update(conf.simStep);
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));

//		Particle focus = env.getParticle(0);
//		camera.centerOn(focus.getX(), focus.getY());

		env.draw(g2, camera);
		
		if (conf.drawCamera) {
			camera.draw(g2);
		}
		
		ui.draw(g2);

		g2.dispose();
	}

}
