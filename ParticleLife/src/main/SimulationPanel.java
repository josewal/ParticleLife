package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

import camera.Camera;
import enviroment.Enviroment;
import particle.Particle;

public class SimulationPanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	
	Config conf = Config.getInstance();
	
	Thread simThread;

	KeyHandler keyH = new KeyHandler();

	private Enviroment env = new Enviroment();
	
	public Camera camera = new Camera(this, (int) conf.envWidth, (int) conf.envHeight);


	public SimulationPanel() {
		this.setPreferredSize(new Dimension(conf.screenWidth, conf.screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);

		camera.centerOn((int) conf.envWidth / 2, (int) conf.envHeight / 2);
	}
	
	public void setupFromConf() {
		
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
		int drawCount = 0;

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
				System.out.println("FPS:" + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}

	}

	public void update() {
		for(int i = 0; i < conf.UPF; i++) {
			env.update(conf.simStep);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		
//		Particle focus = env.getParticle(0);
//		camera.centerOn(focus.getX(), focus.getY());
				
		env.draw(g2, camera);
//		camera.draw(g2);


		g2.dispose();
	}

	

}
