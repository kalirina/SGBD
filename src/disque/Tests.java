package disque;
import java.nio.ByteBuffer;

public class Tests {
	public static void main(String[] args) {
		DiskManager dm = new DiskManager();
		dm.Init("/home/irkalini/eclipse-workspace/BDDA/src/disque", 4);
		
		IPageId page = dm.AllocPage();
		System.out.println(page);
		
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(1234);
		
		dm.WritePage(page, buffer);
		
		ByteBuffer buffer2 = ByteBuffer.allocate(4);
		
		dm.ReadPage(page, buffer2);
		
		System.out.println(buffer2.getInt());
		
		// reutilisation des pages
		dm.DeallocPage(page);
		
		dm.Save();
	}
}
