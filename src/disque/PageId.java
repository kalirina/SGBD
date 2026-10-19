package disque;

public class PageId implements IPageId {
	public int pageId;
	
	public PageId(int pageId) {
		this.pageId = pageId;
	}
	@Override
	public String toString() {
	    return String.valueOf(pageId);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (!(obj instanceof PageId)) {
			return false;
		}
		PageId other = (PageId) obj;
		return this.pageId == other.pageId;
	}
	@Override
    public int hashCode() {
		return Integer.hashCode(pageId);
	}
}
