import java.util.Random;
import java.util.ArrayList;

public class Projecto1{
	public int NWOLFS=30, NSHEEPS=100, SHEEPENERGY=20;
	public int it;

	Cell[][] current = new Cell[51][51];
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
			current[randInt(0, 50)][randInt(0, 50)].addAnimal(
				new Wolf((double)randInt(0, 30)) );
		}
		for(i=0; i<NSHEEPS; i++){
			current[randInt(0, 50)][randInt(0, 50)].addAnimal(
				new Sheep((double)randInt(0, 7)) );
		}

	}
	public boolean wolfActions(Cell curC, Cell nextC){
		double energy;
		for(Wolf wolf: curC.wolfs){
			energy = wolf.energy + curC.sheeps.size() / curC.wolfs.size() - 1;
			if(curC.sheeps.size() > 0){
				System.out.println("Eating, iteration " + it);
			}

			if(energy > 0){
				nextC.addAnimal(new Wolf(energy));
			}else{
				nWolfs--;
				System.out.println("Wolf died, iteration " + it + ", n wolfs " + nWolfs);
			}
		}
		//System.out.println(curC.wolfs.size());
		if(curC.wolfs.size() > 0){
			return false;
		}else{
			return true;
		}
	}

	public void sheepActions(Cell curC, Cell nextC){
		double energy;
		for(Sheep sheep: curC.sheeps){
			energy = sheep.energy + curC.grass / curC.sheeps.size() - 1;
			if(energy > 0){
				nextC.addAnimal(new Sheep(energy));
				nextC.grass = 0;
			}else{
				System.out.println("Sheep died, iteration " + it);
			}
		}
	}

	public void iteration(){
		next = new Cell[51][51];
		
		for(int y=0; y<51; y++){
			for(int x=0; x<51; x++){
				//System.out.print(current[y][x].grass + " ");
				next[y][x] = new Cell(current[y][x].grass);
				
				if(wolfActions(current[y][x], next[y][x]))
					sheepActions(current[y][x], next[y][x]);

				if(next[y][x].grass < 30)
					next[y][x].grass++;
			}
			//System.out.println();
		}
		//System.out.println();

		current = next;
	}

	public Projecto1(){
		init();

		for(it=0; it<40; it++){
			iteration();
		}
	}
	public static void main(String[] args){
		new Projecto1();
	}
}
