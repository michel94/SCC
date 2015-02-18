import java.util.Random;
import java.util.ArrayList;

public class Projecto1{
	//public int NWOLFS=2, NSHEEPS=5, SHEEPENERGY=20, WIDTH=6, HEIGHT=6;
	public int NWOLFS=30, NSHEEPS=100, SHEEPENERGY=7, WOLFENERGY=30, WIDTH=51, HEIGHT=51;
	
	public int it;

	Cell[][] current = new Cell[HEIGHT][WIDTH];
	Cell[][] next;
	public int nSheeps=NSHEEPS, nWolfs = NWOLFS;
	
	public Random random = new Random();

	public int randInt(int start, int end){
		return random.nextInt(end-start+1) + start;
	}

	public void init(){
		int y, x, i;
		for(y=0; y<current.length; y++){
			for(x=0; x<current[0].length; x++){
				current[y][x] = new Cell( randInt(0,1)*30 );
			}
		}
		for(i=0; i<NWOLFS; i++){
			current[randInt(0, HEIGHT-1)][randInt(0, WIDTH-1)].addAnimal(
				new Wolf((double)randInt(0, 30)) );
		}
		for(i=0; i<NSHEEPS; i++){
			current[randInt(0, HEIGHT-1)][randInt(0, WIDTH-1)].addAnimal(
				new Sheep((double)randInt(0, 7)) );
		}

	}

	public Cell getNextCell(int x, int y){
		int nx=x, ny=y;

		while(nx == x && ny == y){
			if(x > 0 && x < WIDTH-1){
				nx = x + randInt(-1, 1);
			}else if(x > 0){
				nx = x + randInt(-1, 0);
			}else if(x < WIDTH-1){
				nx = x + randInt(0, 1);
			}
			if(y > 0 && y < HEIGHT-1){
				ny = y + randInt(-1, 1);
			}else if(y > 0){
				ny = y + randInt(-1, 0);
			}else if(y < HEIGHT-1){
				ny = y + randInt(0, 1);
			}
			
		}
		return next[ny][nx];

	}

	public boolean wolfActions(Cell curC, int x, int y){
		double energy;
		Cell nextC;
		for(Wolf wolf: curC.wolfs){
			energy = wolf.energy + (curC.sheeps.size() / (float)curC.wolfs.size())*20 - 1;
			if(curC.sheeps.size() > 0){
				System.out.println("Wolf eating, iteration " + it);
			}
			if(energy > 0){
				if(randInt(0, 100) < 4){
					nextC = getNextCell(x, y);
					Cell nextC2 = getNextCell(x, y);
					nextC.addAnimal(new Wolf(energy/2));
					nextC2.addAnimal(new Wolf(energy/2));
					//System.out.println("reproduction x:" + x + " y:" + y);
					nWolfs++;
				}else{
					nextC = getNextCell(x, y);
					nextC.addAnimal(new Wolf(energy));
				}

			}else{
				nWolfs--;
				//System.out.println("Wolf died, iteration " + it + ", n wolfs " + nWolfs);
			}
		}
		if(curC.wolfs.size() > 0){
			return false;
		}else{
			return true;
		}
	}

	public void sheepActions(Cell curC, int x, int y){
		double energy;
		Cell nextC = null;
		for(Sheep sheep: curC.sheeps){
			energy = sheep.energy - 1;
			if(curC.grass == 30){
				//System.out.println("Sheep eating, iteration " + it);
				energy += 4 / curC.sheeps.size();
				if(energy > 7)
					energy = 7;
				next[y][x].grass = 0;
			}
			
			if(energy > 0){
				if(randInt(0, 100) < 4){
					nextC = getNextCell(x, y);
					Cell nextC2 = getNextCell(x, y);
					nextC.addAnimal(new Sheep(energy/2));
					nextC2.addAnimal(new Sheep(energy/2));
					//System.out.println("reproduction x:" + x + " y:" + y);
					nSheeps++;
				}else{
					nextC = getNextCell(x, y);
					nextC.addAnimal(new Sheep(energy));
				}
			}else{
				nSheeps--;
				//System.out.println("Sheep died, iteration " + it);
			}
		}
	}

	public void iteration(){
		next = new Cell[HEIGHT][WIDTH];
		
		for(int y=0; y<HEIGHT; y++)
			for(int x=0; x<WIDTH; x++)
				next[y][x] = new Cell(current[y][x].grass);

		for(int y=0; y<HEIGHT; y++){
			for(int x=0; x<WIDTH; x++){
				//System.out.print(current[y][x].grass + " ");
				
				if(wolfActions(current[y][x], x, y))
					sheepActions(current[y][x], x, y);

				if(next[y][x].grass < 30)
					next[y][x].grass++;
			}
			//System.out.println();
		}
		
		/*for(int y=0; y<HEIGHT; y++){
			for(int x=0; x<WIDTH; x++){
				System.out.print(current[y][x].wolfs.size() + "W" + current[y][x].sheeps.size() + "S-" + current[y][x].grass + "\t");
			}
			System.out.println();
		}
		System.out.println();*/
		
		//System.out.println("nWolfs: " + nWolfs + " nSheeps: " + nSheeps);

		current = next;
	}

	public Projecto1(){
		init();

		for(it=0; it<5000; it++){
			iteration();
		}
		System.out.println(nWolfs + " " + nSheeps);
	}
	public static void main(String[] args){
		new Projecto1();
	}
}
