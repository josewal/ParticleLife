package main;

public class SimState {
	/*
	 *  0 = PAUSE
	 *  1 = RUN
	 */
	
	SimulationPanel sp;
	Config conf = Config.getInstance();
	
	public int state = 0;

	private final int PAUSE = 0;
	private final int RUN = 1;

	private boolean optionsOpen = false;
	
	public SimState(SimulationPanel sp) {
		this.sp = sp;
	}
	
	public void setToRun() {
		state = RUN;
	}
	
	public void setToPause() {
		state = PAUSE;
	}
	
	public void openOptions() {
		optionsOpen = true;
		sp.ui.setCameraToOptionsScaling();
	}
	
	public void closeOptions() {
		optionsOpen = false;
		sp.ui.setCameraToStandardScaling();
	}
	
	public void toggleOptions() {
		if(optionsOpen) {
			closeOptions();
		}else {
			openOptions();
		}
	}
	
	public void runPauseToggle() {
		if (isRun()) {
			setToPause();
		}else if(isPause()) {
			setToRun();
		}
	}
	
	public boolean isPause() {
		return state == PAUSE;
	}
	
	public boolean isRun() {
		return state == RUN;
	}
	
	public boolean optionsOpen() {
		return optionsOpen;
	}
	
}
