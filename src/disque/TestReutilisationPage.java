package disque;

public class TestReutilisationPage {
	
	public static void TRP(DiskManager dm) {
		
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
}
