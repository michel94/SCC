import java.util.ArrayList;

public class Cell{
	int grass;
	ArrayList <Wolf> wolfs = new ArrayList<Wolf>();
	ArrayList <Sheep> sheeps = new ArrayList<Sheep>();
	public Cell(int g){
		grass = g;
	}
	public void addAnimal(Animal animal){
		if(animal.typeIs("wolf")){
			wolfs.add((Wolf)animal);
		}else if(animal.typeIs("sheep")){
			sheeps.add((Sheep)animal);
		}
	}
}
