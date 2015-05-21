import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class IOStation extends Station{
	MainModel model;
	Station ioStation;

	public IOStation(MainModel model){
		super(model, "IOStation", 1);
		this.model = model;
		
	}
	
}
