package disque;
import java.nio.ByteBuffer;

public class TestEcriturePage {
	
	public static void TEP(DiskManager dm) {
		
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
}
