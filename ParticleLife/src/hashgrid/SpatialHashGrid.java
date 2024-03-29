package hashgrid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.Math;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import camera.Camera;

//TODO: implementent <ISpatial>
public class SpatialHashGrid<T extends ISpatial> {
	private ArrayList<T> elements;

	private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Set<T>>> buckets;
	private boolean[][] bucketFilled;
//	private Set<T> query;

	public int cols;
	public int rows;
	public double cellSize;
	private double convCoef;

	public SpatialHashGrid(int cols, int rows, double cellSize) {
		this.cols = cols;
		this.rows = rows;
		this.cellSize = cellSize;
		this.convCoef = 1 / cellSize;

		elements = new ArrayList<>();
		
		setupBuckets();
		
//		query = new HashSet<>();
	}

	private void setupBuckets() {
		buckets = new ConcurrentHashMap<>();
		
		for (int col = 0; col < cols; col++) {
			buckets.put(col, new ConcurrentHashMap<>());
			
			for (int row = 0; row < rows; row++) {
				buckets.get(col).put(row, new HashSet<>());

			}
			
		}
		System.out.println(buckets.size() + " " + buckets.get(0).size());
		bucketFilled = new boolean[cols][rows];
	}

	public int getCol(ISpatial value) {
		return getCol(value.getX());
	}

	public int getRow(ISpatial value) {
		return getRow(value.getY());
	}

	public int getCol(double x) {
		int col = Math.floorMod((int) (x * convCoef), cols);
		return col;
	}

	public int getRow(double y) {
		int row = Math.floorMod((int) (y * convCoef), rows);
		return row;
	}

	public int size() {
		return elements.size();
	}

	public ArrayList<T> getElements() {
		return elements;
	}

	public void update() {
		clearBuckets();
		
		putElemementsIntoBuckets();
	}

	private void clearBuckets() {
		for (int col = 0; col < cols; col++) {
			for (int row = 0; row < rows; row++) {
				buckets.get(col).get(row).clear();
			}
		}
	}

	public void putElementIntoBucket(T element) {
		int col = getCol(element);
		int row = getRow(element); 
//		if(!buckets.containsKey(col) || !buckets.get(col).containsKey(row)) {
//            buckets.putIfAbsent(col, new ConcurrentHashMap<>());
//            buckets.get(col).putIfAbsent(row, new HashSet<>());
//        }

        buckets.get(col).get(row).add(element);
		bucketFilled[col][row] = true;
	}

	public void putElemementsIntoBuckets() {
		for (T element : elements) {
			putElementIntoBucket(element);
		}
	}

	public void add(T element) {
		elements.add(element);
		putElementIntoBucket(element);
	}

	public Set<T> getElementsFromBucket(int col, int row) {
		return buckets.get(col).get(row);
	}

	@SuppressWarnings("unchecked")
	public Set<T>[] elementsInRectQuery(double x, double y, double width, double height) {
		Set<T>[] query;

		int unwrappedMinCol = (int) ((x) / cellSize);
		int unwrappedMinRow = (int) ((y) / cellSize);
		int unwrappedMaxCol = (int) ((x + width ) / cellSize);
		int unwrappedMaxRow = (int) ((y + height) / cellSize);

		int queryCols = unwrappedMaxCol - unwrappedMinCol + 1;
		int queryRows = unwrappedMaxRow - unwrappedMinRow + 1;
		
		query = new Set[queryCols*queryRows];

		for (int i = 0; i < queryCols; i++) {
			for (int j = 0; j < queryRows; j++) {
				int unwCol = unwrappedMinCol + i;
				int unwRow = unwrappedMinRow + j;
				int col = Math.floorMod(unwCol, cols);
				int row = Math.floorMod(unwRow, rows);
			
				
				if (buckets.containsKey(col) && buckets.get(col).containsKey(row)) {
                    query[i*queryRows + j] = (buckets.get(col).get(row));
                }
			}
		}

		return query;
	}

	public int[][] bucketIdxInRectQuery(double x, double y, double width, double height) {
		int[][] query;

		int unwrappedMinCol = (int) ((x) / cellSize);
		int unwrappedMinRow = (int) ((y) / cellSize);
		int unwrappedMaxCol = (int) ((x + width ) / cellSize);
		int unwrappedMaxRow = (int) ((y + height) / cellSize);


		int queryCols = unwrappedMaxCol - unwrappedMinCol + 1;
		int queryRows = unwrappedMaxRow - unwrappedMinRow + 1;

		query = new int[queryCols * queryRows][2];

		for (int i = 0; i < queryCols; i++) {
			for (int j = 0; j < queryRows; j++) {
				int unwCol = unwrappedMinCol + i;
				int unwRow = unwrappedMinRow + j;
				query[j + i * (queryRows)][0] = Math.floorMod(unwCol, cols);
				query[j + i * (queryRows)][1] = Math.floorMod(unwRow, rows);
			}
		}

		return query;
	}

	public void draw(Graphics2D g2, Camera c) {
		for (int col = 0; col < cols; col++) {
			for (int row = 0; row < rows; row++) {
				
				int width = c.zoomElongation(cellSize);
				int screenX = c.getFrameX(col * cellSize);
				int screenY = c.getFrameY(row * cellSize);
				
				g2.setColor(Color.YELLOW);
				g2.drawRect(screenX, screenY,width,width);
			}
		}

	}

	public void highlightCell(Graphics2D g2, Camera c, int col, int row) {
		int width = c.zoomElongation(cellSize);
		int screenX = c.getFrameX(col * cellSize);
		int screenY = c.getFrameY(row * cellSize);
		
		g2.setColor(new Color(50,50,150));
		g2.drawRect(screenX, screenY,width,width);
	}
}
