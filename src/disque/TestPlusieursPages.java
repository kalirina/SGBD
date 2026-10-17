package disque;
import java.nio.ByteBuffer;

public class TestPlusieursPages {
	
	public static void TPP(DiskManager dm) {
		
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

}
