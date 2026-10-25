package disque;
import java.io.File;
import java.nio.ByteBuffer;


public class DiskManagerTests {
	
	public static void TestEcriturePage(DiskManager dm) throws DiskManagerException {
		
		int x = 67;
			
		IPageId ipid = dm.AllocPage();
		ByteBuffer buffer = ByteBuffer.allocate(4);
		
		buffer.putInt(x);
		dm.WritePage(ipid, buffer);
		
		ByteBuffer bufferRead = ByteBuffer.allocate(4); // nouvel buffer pour eviter de lire la contenu de l'ancien
		dm.ReadPage(ipid, bufferRead);
		int valRead = bufferRead.getInt();
		
		if (valRead==x) {
			System.out.println("Test ecriture page : success !");
		} else {
			System.out.println("Test ecriture page : ERROR : valeur attendu = " + x + ", valeur lue " + valRead);
		}
	}
	
	public static void TestPageSizeIncompatible(String dmDir) throws DiskManagerException {
		
		DiskManager dm1 = new DiskManager();
		dm1.Init(dmDir, 4); //page size = 4
		dm1.Save(); 
		
		DiskManager dm2 = new DiskManager();
		boolean except = false;
		try {
			dm2.Init(dmDir, 8); //page size = 8, pas autorisée
		} catch (DiskManagerException e){
			except = true;
		}
		
		if (except) {
			System.out.println("Test page size incompatible : success : exception levée!");
		} else {
			System.out.println("Test page size incompatible : ERROR : aucune exception levée !");
		}	
	}
	
	public static void TestPlusieursPages(DiskManager dm) throws DiskManagerException {
			
		int a = 112;
		int b = 23;
		
		IPageId ipid1 = dm.AllocPage();
		IPageId ipid2 = dm.AllocPage();
		
		ByteBuffer b1 = ByteBuffer.allocate(4);
		b1.putInt(a);
		dm.WritePage(ipid1, b1);
		
		ByteBuffer b2 = ByteBuffer.allocate(4);
		b2.putInt(b);
		dm.WritePage(ipid2, b2);
		
		ByteBuffer bRead1 = ByteBuffer.allocate(4);
		dm.ReadPage(ipid1, bRead1);
		
		ByteBuffer bRead2 = ByteBuffer.allocate(4);
		dm.ReadPage(ipid2, bRead2);
		
		int val1 = bRead1.getInt();
		int val2 = bRead2.getInt();
		
		if (val1 == a && val2 == b) {
			System.out.println("Test plusieurs pages : success !");
		} else {
			System.out.println("Test plusieurs pages : ERROR : page 1 =" + bRead1.getInt() + ", page 2 = " + bRead2.getInt());
		}
	}
	
	public static void TestReutilisationPage(DiskManager dm) throws DiskManagerException {
			
		IPageId page1 = dm.AllocPage();
		int id1 =((PageId) page1).pageId;
		dm.DeallocPage(page1);
		
		IPageId page2 = dm.AllocPage();
		int id2 = ((PageId) page2).pageId;
		
		if(id1==id2) {
			System.out.println("Test reutilisation page : success !");
		} else {
			System.out.println("Test reutilisation page : ERROR : page1=" + id1 + ", page2=" + id2);
		}
	}
	
	public static void TestBufferNull(DiskManager dm) throws DiskManagerException {

	    IPageId ipid = dm.AllocPage();

	    boolean exceptWrite = false;
	    try {
	        dm.WritePage(ipid, null);
	    } catch (DiskManagerException e) {
	        exceptWrite = true;
	    }

	    boolean exceptRead = false;
	    try {
	        dm.ReadPage(ipid, null);
	    } catch (DiskManagerException e) {
	        exceptRead = true;
	    }

	    if (exceptWrite && exceptRead) {
	        System.out.println("Test buffer null : success !");
	    } else {
	        System.out.println("Test buffer null : ERROR : write=" + exceptWrite + ", read=" + exceptRead);
	    }
	}

	public static void TestBufferTailleIncorrecte(DiskManager dm) throws DiskManagerException {

	    IPageId ipid = dm.AllocPage();
	    ByteBuffer bufferTropPetit = ByteBuffer.allocate(2); // pageSize = 4, donc incorrect

	    boolean exceptWrite = false;
	    try {
	        dm.WritePage(ipid, bufferTropPetit);
	    } catch (DiskManagerException e) {
	        exceptWrite = true;
	    }

	    boolean exceptRead = false;
	    try {
	        dm.ReadPage(ipid, bufferTropPetit);
	    } catch (DiskManagerException e) {
	        exceptRead = true;
	    }

	    if (exceptWrite && exceptRead) {
	        System.out.println("Test buffer taille incorrecte : success !");
	    } else {
	        System.out.println("Test buffer taille incorrecte : ERROR : write=" + exceptWrite + ", read=" + exceptRead);
	    }
	}

	public static void TestOperationPageDesallouee(DiskManager dm) throws DiskManagerException {

	    IPageId ipid = dm.AllocPage();
	    dm.DeallocPage(ipid);

	    ByteBuffer buffer = ByteBuffer.allocate(4);
	    buffer.putInt(1);

	    boolean exceptWrite = false;
	    try {
	        dm.WritePage(ipid, buffer);
	    } catch (DiskManagerException e) {
	        exceptWrite = true;
	    }

	    boolean exceptRead = false;
	    try {
	        dm.ReadPage(ipid, ByteBuffer.allocate(4));
	    } catch (DiskManagerException e) {
	        exceptRead = true;
	    }

	    if (exceptWrite && exceptRead) {
	        System.out.println("Test operation sur page desallouee : success !");
	    } else {
	        System.out.println("Test operation sur page desallouee : ERROR : write=" + exceptWrite + ", read=" + exceptRead);
	    }
	}

	public static void TestSecondDealloc(DiskManager dm) throws DiskManagerException {

	    IPageId ipid = dm.AllocPage();
	    dm.DeallocPage(ipid);

	    boolean except = false;
	    try {
	        dm.DeallocPage(ipid); // deuxieme appel sur la meme page
	    } catch (DiskManagerException e) {
	        except = true;
	    }

	    if (except) {
	        System.out.println("Test second dealloc : success : exception levée!");
	    } else {
	        System.out.println("Test second dealloc : ERROR : aucune exception levée !");
	    }
	}
	
	public static void main(String[] args) throws DiskManagerException {
		
		try {
			
			DiskManager dm = new DiskManager();
			
			String dmDir = "stockage";
			new File(dmDir).mkdirs(); 
			
			dm.Init(dmDir, 4);
			
			TestEcriturePage(dm);
			System.out.println();
			
			TestReutilisationPage(dm);
			System.out.println();
			
			TestPlusieursPages(dm);
			System.out.println();
			
			TestPageSizeIncompatible(dmDir);
			System.out.println();

			TestBufferNull(dm);
			System.out.println();

			TestBufferTailleIncorrecte(dm);
			System.out.println();

			TestOperationPageDesallouee(dm);
			System.out.println();

			TestSecondDealloc(dm);
			
		} catch (DiskManagerException e) {
			System.out.println("Error : " + e.getMessage());
		}
	}
}
