package disque;

public class PageId implements IPageId{
	public int pageId;
	
	public PageId(int pageId) {
		this.pageId = pageId;
	}
	@Override
	public String toString() {
	    return String.valueOf(pageId);
	}
}
