package disque;

import java.io.File;

public class BufferManagerTests {
	public static void TestInitialisation(BufferManager bm, DiskManager dm) {
		try {
			bm.init(4, 3, IBufferManager.ReplacementPolicy.LRU, dm);
		}
		catch (DBMSException e) {
			System.out.println("Error : " + e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		try {
			DiskManager dm = new DiskManager();
			String dmDir = "stockage";
			new File(dmDir).mkdirs(); 
			dm.Init(dmDir, 4);
			
			BufferManager bm = new BufferManager();
			
			TestInitialisation(bm, dm);
		}
		catch (DBMSException e) {
			System.out.println("Error : " + e.getMessage());
		}
	}
}
