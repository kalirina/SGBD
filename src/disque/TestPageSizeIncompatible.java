package disque;

public class TestPageSizeIncompatible {
	
	public static void TSPI(String dmDir) {
		
		DiskManager dm1 = new DiskManager();
		dm1.Init(dmDir, 4); //page size = 4
		dm1.Save(); 
		
		DiskManager dm2 = new DiskManager();
		boolean except = false;
		try {
			dm2.Init(dmDir, 8); //page size = 8, pas autorisée
		} catch (RuntimeException e){
			except = true;
		}
		
		if (except) {
			System.out.println("Test page size incompatible : success : exception levée!");
		} else {
			System.out.println("Test page size incompatible : ERROR : aucune exception levée !");
		}	
	}
}
