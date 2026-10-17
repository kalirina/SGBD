package disque;
import java.io.File;

public class DiskManagerTests {
	
	public static void main(String[] args) {
		
		DiskManager dm = new DiskManager();
		String dmDir = "stockage";
		new File(dmDir).mkdirs(); 
		dm.Init(dmDir, 4);
		
		TestEcriturePage.TEP(dm);
		System.out.println();
		
		TestReutilisationPage.TRP(dm);
		System.out.println();
		
		TestPlusieursPages.TPP(dm);
		System.out.println();
		
		TestPageSizeIncompatible.TSPI(dmDir);
	}

}
